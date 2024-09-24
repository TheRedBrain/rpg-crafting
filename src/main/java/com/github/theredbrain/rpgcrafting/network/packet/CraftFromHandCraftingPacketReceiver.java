package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.screen.HandCraftingScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
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

				int playerHotbarSize = RPGCrafting.getActiveHotbarSize(player);
				int playerInventorySize = RPGCrafting.getActiveInventorySize(player);

				ItemStack itemStack;

				for (Ingredient ingredient : rpgCraftingRecipe.ingredients) {

					int j;

					for (j = 0; j < playerHotbarSize; j++) {
						if (ingredient.test(handCraftingScreenHandler.getPlayerInventory().getStack(j))) {
							itemStack = handCraftingScreenHandler.getPlayerInventory().getStack(j).copy();
							int stackCount = itemStack.getCount();
							if (stackCount >= 1) {
								itemStack.setCount(stackCount - 1);
								handCraftingScreenHandler.getPlayerInventory().setStack(j, itemStack);
							} else {
								handCraftingScreenHandler.getPlayerInventory().setStack(j, ItemStack.EMPTY);
							}
							break;
						}
					}

					for (j = 9; j < playerInventorySize; j++) {
						if (ingredient.test(handCraftingScreenHandler.getPlayerInventory().getStack(j))) {
							itemStack = handCraftingScreenHandler.getPlayerInventory().getStack(j).copy();
							int stackCount = itemStack.getCount();
							if (stackCount >= 1) {
								itemStack.setCount(stackCount - 1);
								handCraftingScreenHandler.getPlayerInventory().setStack(j, itemStack);
							} else {
								handCraftingScreenHandler.getPlayerInventory().setStack(j, ItemStack.EMPTY);
							}
							break;
						}
					}
				}
				player.getInventory().offerOrDrop(rpgCraftingRecipe.result.copy());
				handCraftingScreenHandler.populateRecipeLists();
				handCraftingScreenHandler.setShouldScreenCalculateCraftingStatus(1);
			}
		}
	}
}