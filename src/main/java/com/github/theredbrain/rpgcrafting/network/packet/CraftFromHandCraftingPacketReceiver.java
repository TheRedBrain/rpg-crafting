package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.screen.HandCraftingScreenHandler;
import com.github.theredbrain.rpgcrafting.util.RPGCraftingHelper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class CraftFromHandCraftingPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<CraftFromHandCraftingPacket> {
	@Override
	public void receive(CraftFromHandCraftingPacket payload, ServerPlayNetworking.Context context) {

		String recipeIdentifier = payload.recipeIdentifier();

		ServerPlayerEntity player = context.player();

		ScreenHandler screenHandler = player.currentScreenHandler;

		Optional<RecipeEntry<?>> rpgCraftingRecipeEntryOptional = player.getWorld().getRecipeManager().get(Identifier.of(recipeIdentifier));

		if (rpgCraftingRecipeEntryOptional.isPresent() && screenHandler instanceof HandCraftingScreenHandler handCraftingScreenHandler) {
			if (rpgCraftingRecipeEntryOptional.get().value() instanceof RPGCraftingRecipe rpgCraftingRecipe) {
				if (rpgCraftingRecipe.matches(handCraftingScreenHandler.getCraftingInputInventory(), context.player().getWorld())) {

					int playerHotbarSize = RPGCrafting.getActiveHotbarSize(player);

					int playerInventorySize = RPGCrafting.getActiveInventorySize(player);

					boolean bl = true;

					for (RPGCraftingRecipe.RPGItemStackIngredient itemStackIngredient : rpgCraftingRecipe.rpgItemStackIngredients) {

						if (!itemStackIngredient.isConsumed()) {
							continue;
						}

						int itemStackIngredientCount = itemStackIngredient.itemStack().getCount();

						itemStackIngredientCount = RPGCraftingHelper.removeItemStackIngredientFromInventory(
								handCraftingScreenHandler.getPlayerInventory(),
								playerHotbarSize,
								0,
								itemStackIngredient,
								itemStackIngredientCount
						);
						if (itemStackIngredientCount <= 0) {
							continue;
						}

						itemStackIngredientCount = RPGCraftingHelper.removeItemStackIngredientFromInventory(
								handCraftingScreenHandler.getPlayerInventory(),
								playerInventorySize,
								9,
								itemStackIngredient,
								itemStackIngredientCount
						);
						if (itemStackIngredientCount <= 0) {
							bl = false;
							break;
						}
					}
					if (bl) {

						for (RPGCraftingRecipe.RPGIngredient rpgIngredient : rpgCraftingRecipe.rpgIngredients) {

							if (!rpgIngredient.isConsumed()) {
								continue;
							}

							boolean bl1;

							bl1 = RPGCraftingHelper.removeRPGIngredientFromInventory(
									handCraftingScreenHandler.getPlayerInventory(),
									playerHotbarSize,
									0,
									rpgIngredient
							);
							if (bl1) {
								continue;
							}

							bl1 = RPGCraftingHelper.removeRPGIngredientFromInventory(
									handCraftingScreenHandler.getPlayerInventory(),
									playerInventorySize,
									9,
									rpgIngredient
							);
							if (!bl1) {
								bl = false;
								break;
							}
						}
					}
					if (bl) {
						player.getInventory().offerOrDrop(rpgCraftingRecipe.result.copy());
					}
				} else {
					player.sendMessage(Text.translatable("hud.message.not_all_crafting_ingredients_were_found"));
				}
				handCraftingScreenHandler.populateRecipeLists();
				handCraftingScreenHandler.setShouldScreenCalculateCraftingStatus(1);
			}
		}
	}
}