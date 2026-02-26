package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import com.github.theredbrain.rpgcrafting.util.RPGCraftingHelper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class CraftFromCraftingBenchPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<CraftFromCraftingBenchPacket> {
	@Override
	public void receive(CraftFromCraftingBenchPacket payload, ServerPlayNetworking.Context context) {

		String recipeIdentifier = payload.recipeIdentifier();

		boolean useStorageInventory = payload.useStorageInventory();

		ServerPlayerEntity player = context.player();

		ScreenHandler screenHandler = player.currentScreenHandler;

		Optional<RecipeEntry<?>> rpgCraftingRecipeEntryOptional = player.getWorld().getRecipeManager().get(Identifier.of(recipeIdentifier));

		if (rpgCraftingRecipeEntryOptional.isPresent() && screenHandler instanceof CraftingBenchBlockScreenHandler craftingBenchBlockScreenHandler) {
			if (rpgCraftingRecipeEntryOptional.get().value() instanceof RPGCraftingRecipe rpgCraftingRecipe) {
				if (rpgCraftingRecipe.matches(craftingBenchBlockScreenHandler.getCraftingInputInventory(((DuckPlayerEntityMixin) craftingBenchBlockScreenHandler.getPlayerInventory().player).rpgcrafting$useStashForCrafting()), context.player().getWorld())) {

					int playerHotbarSize = RPGCrafting.getActiveHotbarSize(player);

					int playerInventorySize = RPGCrafting.getActiveInventorySize(player);

					int stash0InventorySize = useStorageInventory && craftingBenchBlockScreenHandler.isStorageArea0ProviderInReach() ? 6 : 0;

					int stash1InventorySize = useStorageInventory && craftingBenchBlockScreenHandler.isStorageArea1ProviderInReach() ? 8 : 0;

					int stash2InventorySize = useStorageInventory && craftingBenchBlockScreenHandler.isStorageArea2ProviderInReach() ? 12 : 0;

					int stash3InventorySize = useStorageInventory && craftingBenchBlockScreenHandler.isStorageArea3ProviderInReach() ? 14 : 0;

					int stash4InventorySize = useStorageInventory && craftingBenchBlockScreenHandler.isStorageArea4ProviderInReach() ? 21 : 0;

					boolean bl = true;

					for (RPGCraftingRecipe.RPGItemStackIngredient itemStackIngredient : rpgCraftingRecipe.rpgItemStackIngredients) {

						if (!itemStackIngredient.isConsumed()) {
							continue;
						}

						int itemStackIngredientCount = itemStackIngredient.itemStack().getCount();

						// TODO play test which inventory normally contains the most crafting ingredients and should be checked first

						itemStackIngredientCount = RPGCraftingHelper.removeItemStackIngredientFromInventory(
								craftingBenchBlockScreenHandler.getPlayerInventory(),
								playerHotbarSize,
								0,
								itemStackIngredient,
								itemStackIngredientCount
						);
						if (itemStackIngredientCount <= 0) {
							continue;
						}

						itemStackIngredientCount = RPGCraftingHelper.removeItemStackIngredientFromInventory(
								craftingBenchBlockScreenHandler.getPlayerInventory(),
								playerInventorySize,
								9,
								itemStackIngredient,
								itemStackIngredientCount
						);
						if (itemStackIngredientCount <= 0) {
							continue;
						}

						itemStackIngredientCount = RPGCraftingHelper.removeItemStackIngredientFromInventory(
								craftingBenchBlockScreenHandler.getEnderChestInventory(),
								stash0InventorySize,
								0,
								itemStackIngredient,
								itemStackIngredientCount
						);
						if (itemStackIngredientCount <= 0) {
							continue;
						}

						itemStackIngredientCount = RPGCraftingHelper.removeItemStackIngredientFromInventory(
								craftingBenchBlockScreenHandler.getStashInventory(),
								stash1InventorySize,
								0,
								itemStackIngredient,
								itemStackIngredientCount
						);
						if (itemStackIngredientCount <= 0) {
							continue;
						}

						itemStackIngredientCount = RPGCraftingHelper.removeItemStackIngredientFromInventory(
								craftingBenchBlockScreenHandler.getStashInventory(),
								stash2InventorySize,
								8,
								itemStackIngredient,
								itemStackIngredientCount
						);
						if (itemStackIngredientCount <= 0) {
							continue;
						}

						itemStackIngredientCount = RPGCraftingHelper.removeItemStackIngredientFromInventory(
								craftingBenchBlockScreenHandler.getStashInventory(),
								stash3InventorySize,
								20,
								itemStackIngredient,
								itemStackIngredientCount
						);
						if (itemStackIngredientCount <= 0) {
							continue;
						}

						itemStackIngredientCount = RPGCraftingHelper.removeItemStackIngredientFromInventory(
								craftingBenchBlockScreenHandler.getEnderChestInventory(),
								stash4InventorySize,
								6,
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

							// TODO play test which inventory normally contains the most crafting ingredients and should be checked first

							bl1 = RPGCraftingHelper.removeRPGIngredientFromInventory(
									craftingBenchBlockScreenHandler.getPlayerInventory(),
									playerHotbarSize,
									0,
									rpgIngredient
							);
							if (bl1) {
								continue;
							}

							bl1 = RPGCraftingHelper.removeRPGIngredientFromInventory(
									craftingBenchBlockScreenHandler.getPlayerInventory(),
									playerInventorySize,
									9,
									rpgIngredient
							);
							if (bl1) {
								continue;
							}

							bl1 = RPGCraftingHelper.removeRPGIngredientFromInventory(
									craftingBenchBlockScreenHandler.getEnderChestInventory(),
									stash0InventorySize,
									0,
									rpgIngredient
							);
							if (bl1) {
								continue;
							}

							bl1 = RPGCraftingHelper.removeRPGIngredientFromInventory(
									craftingBenchBlockScreenHandler.getStashInventory(),
									stash1InventorySize,
									0,
									rpgIngredient
							);
							if (bl1) {
								continue;
							}

							bl1 = RPGCraftingHelper.removeRPGIngredientFromInventory(
									craftingBenchBlockScreenHandler.getStashInventory(),
									stash2InventorySize,
									8,
									rpgIngredient
							);
							if (bl1) {
								continue;
							}

							bl1 = RPGCraftingHelper.removeRPGIngredientFromInventory(
									craftingBenchBlockScreenHandler.getStashInventory(),
									stash3InventorySize,
									20,
									rpgIngredient
							);
							if (bl1) {
								continue;
							}

							bl1 = RPGCraftingHelper.removeRPGIngredientFromInventory(
									craftingBenchBlockScreenHandler.getEnderChestInventory(),
									stash4InventorySize,
									6,
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
				craftingBenchBlockScreenHandler.populateRecipeLists();
				craftingBenchBlockScreenHandler.setShouldScreenCalculateCraftingStatus(1);
			}
		}
	}
}