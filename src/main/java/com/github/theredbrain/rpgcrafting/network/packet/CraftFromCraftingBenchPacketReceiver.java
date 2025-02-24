package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
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

					ItemStack itemStack;

					for (ItemStack itemStackIngredient : rpgCraftingRecipe.itemStackIngredients) {

						int j;
						int itemStackIngredientCount = itemStackIngredient.getCount();
						boolean bl = false;

						// TODO play test which inventory normally contains the most crafting ingredients and should be checked first

						for (j = 0; j < playerHotbarSize; j++) {
							if (RPGCraftingRecipe.areItemStacksEqual(itemStackIngredient, craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j))) {
								itemStack = craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount > itemStackIngredientCount) {
									itemStack.setCount(stackCount - itemStackIngredientCount);
									itemStackIngredientCount = 0;
									craftingBenchBlockScreenHandler.getPlayerInventory().setStack(j, itemStack);
								} else {
									itemStackIngredientCount -= stackCount;
									craftingBenchBlockScreenHandler.getPlayerInventory().setStack(j, ItemStack.EMPTY);
								}
								if (itemStackIngredientCount <= 0) {
									bl = true;
									break;
								}
							}
						}
						if (bl) {
							continue;
						}

						for (j = 9; j < playerInventorySize; j++) {
							if (RPGCraftingRecipe.areItemStacksEqual(itemStackIngredient, craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j))) {
								itemStack = craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount > itemStackIngredientCount) {
									itemStack.setCount(stackCount - itemStackIngredientCount);
									itemStackIngredientCount = 0;
									craftingBenchBlockScreenHandler.getPlayerInventory().setStack(j, itemStack);
								} else {
									itemStackIngredientCount -= stackCount;
									craftingBenchBlockScreenHandler.getPlayerInventory().setStack(j, ItemStack.EMPTY);
								}
								if (itemStackIngredientCount <= 0) {
									bl = true;
									break;
								}
							}
						}
						if (bl) {
							continue;
						}

						for (j = 0; j < stash0InventorySize; j++) {
							if (RPGCraftingRecipe.areItemStacksEqual(itemStackIngredient, craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(j))) {
								itemStack = craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount > itemStackIngredientCount) {
									itemStack.setCount(stackCount - itemStackIngredientCount);
									itemStackIngredientCount = 0;
									craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(j, itemStack);
								} else {
									itemStackIngredientCount -= stackCount;
									craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(j, ItemStack.EMPTY);
								}
								if (itemStackIngredientCount <= 0) {
									bl = true;
									break;
								}
							}
						}
						if (bl) {
							continue;
						}

						for (j = 0; j < stash1InventorySize; j++) {
							if (RPGCraftingRecipe.areItemStacksEqual(itemStackIngredient, craftingBenchBlockScreenHandler.getStashInventory().getStack(j))) {
								itemStack = craftingBenchBlockScreenHandler.getStashInventory().getStack(j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount > itemStackIngredientCount) {
									itemStack.setCount(stackCount - itemStackIngredientCount);
									itemStackIngredientCount = 0;
									craftingBenchBlockScreenHandler.getStashInventory().setStack(j, itemStack);
								} else {
									itemStackIngredientCount -= stackCount;
									craftingBenchBlockScreenHandler.getStashInventory().setStack(j, ItemStack.EMPTY);
								}
								if (itemStackIngredientCount <= 0) {
									bl = true;
									break;
								}
							}
						}
						if (bl) {
							continue;
						}

						for (j = 0; j < stash2InventorySize; j++) {
							if (RPGCraftingRecipe.areItemStacksEqual(itemStackIngredient, craftingBenchBlockScreenHandler.getStashInventory().getStack(8 + j))) {
								itemStack = craftingBenchBlockScreenHandler.getStashInventory().getStack(8 + j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount > itemStackIngredientCount) {
									itemStack.setCount(stackCount - itemStackIngredientCount);
									itemStackIngredientCount = 0;
									craftingBenchBlockScreenHandler.getStashInventory().setStack(8 + j, itemStack);
								} else {
									itemStackIngredientCount -= stackCount;
									craftingBenchBlockScreenHandler.getStashInventory().setStack(8 + j, ItemStack.EMPTY);
								}
								if (itemStackIngredientCount <= 0) {
									bl = true;
									break;
								}
							}
						}
						if (bl) {
							continue;
						}

						for (j = 0; j < stash3InventorySize; j++) {
							if (RPGCraftingRecipe.areItemStacksEqual(itemStackIngredient, craftingBenchBlockScreenHandler.getStashInventory().getStack(20 + j))) {
								itemStack = craftingBenchBlockScreenHandler.getStashInventory().getStack(20 + j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount > itemStackIngredientCount) {
									itemStack.setCount(stackCount - itemStackIngredientCount);
									itemStackIngredientCount = 0;
									craftingBenchBlockScreenHandler.getStashInventory().setStack(20 + j, itemStack);
								} else {
									itemStackIngredientCount -= stackCount;
									craftingBenchBlockScreenHandler.getStashInventory().setStack(20 + j, ItemStack.EMPTY);
								}
								if (itemStackIngredientCount <= 0) {
									bl = true;
									break;
								}
							}
						}
						if (bl) {
							continue;
						}

						for (j = 0; j < stash4InventorySize; j++) {
							if (RPGCraftingRecipe.areItemStacksEqual(itemStackIngredient, craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(6 + j))) {
								itemStack = craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(6 + j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount > itemStackIngredientCount) {
									itemStack.setCount(stackCount - itemStackIngredientCount);
									itemStackIngredientCount = 0;
									craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(6 + j, itemStack);
								} else {
									itemStackIngredientCount -= stackCount;
									craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(6 + j, ItemStack.EMPTY);
								}
								if (itemStackIngredientCount <= 0) {
									break;
								}
							}
						}
					}

					for (Ingredient ingredient : rpgCraftingRecipe.ingredients) {

						int j;
						boolean bl = false;
						// TODO play test which inventory normally contains the most crafting ingredients and should be checked first

						for (j = 0; j < playerHotbarSize; j++) {
							if (ingredient.test(craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j))) {
								itemStack = craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount >= 1) {
									itemStack.setCount(stackCount - 1);
									craftingBenchBlockScreenHandler.getPlayerInventory().setStack(j, itemStack);
								} else {
									craftingBenchBlockScreenHandler.getPlayerInventory().setStack(j, ItemStack.EMPTY);
								}
								bl = true;
								break;
							}
						}
						if (bl) {
							continue;
						}

						for (j = 9; j < playerInventorySize; j++) {
							if (ingredient.test(craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j))) {
								itemStack = craftingBenchBlockScreenHandler.getPlayerInventory().getStack(j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount >= 1) {
									itemStack.setCount(stackCount - 1);
									craftingBenchBlockScreenHandler.getPlayerInventory().setStack(j, itemStack);
								} else {
									craftingBenchBlockScreenHandler.getPlayerInventory().setStack(j, ItemStack.EMPTY);
								}
								bl = true;
								break;
							}
						}
						if (bl) {
							continue;
						}

						for (j = 0; j < stash0InventorySize; j++) {
							if (ingredient.test(craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(j))) {
								itemStack = craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount >= 1) {
									itemStack.setCount(stackCount - 1);
									craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(j, itemStack);
								} else {
									craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(j, ItemStack.EMPTY);
								}
								bl = true;
								break;
							}
						}
						if (bl) {
							continue;
						}

						for (j = 0; j < stash1InventorySize; j++) {
							if (ingredient.test(craftingBenchBlockScreenHandler.getStashInventory().getStack(j))) {
								itemStack = craftingBenchBlockScreenHandler.getStashInventory().getStack(j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount >= 1) {
									itemStack.setCount(stackCount - 1);
									craftingBenchBlockScreenHandler.getStashInventory().setStack(j, itemStack);
								} else {
									craftingBenchBlockScreenHandler.getStashInventory().setStack(j, ItemStack.EMPTY);
								}
								bl = true;
								break;
							}
						}
						if (bl) {
							continue;
						}

						for (j = 0; j < stash2InventorySize; j++) {
							if (ingredient.test(craftingBenchBlockScreenHandler.getStashInventory().getStack(8 + j))) {
								itemStack = craftingBenchBlockScreenHandler.getStashInventory().getStack(8 + j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount >= 1) {
									itemStack.setCount(stackCount - 1);
									craftingBenchBlockScreenHandler.getStashInventory().setStack(8 + j, itemStack);
								} else {
									craftingBenchBlockScreenHandler.getStashInventory().setStack(8 + j, ItemStack.EMPTY);
								}
								bl = true;
								break;
							}
						}
						if (bl) {
							continue;
						}

						for (j = 0; j < stash3InventorySize; j++) {
							if (ingredient.test(craftingBenchBlockScreenHandler.getStashInventory().getStack(20 + j))) {
								itemStack = craftingBenchBlockScreenHandler.getStashInventory().getStack(20 + j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount >= 1) {
									itemStack.setCount(stackCount - 1);
									craftingBenchBlockScreenHandler.getStashInventory().setStack(20 + j, itemStack);
								} else {
									craftingBenchBlockScreenHandler.getStashInventory().setStack(20 + j, ItemStack.EMPTY);
								}
								bl = true;
								break;
							}
						}
						if (bl) {
							continue;
						}

						for (j = 0; j < stash4InventorySize; j++) {
							if (ingredient.test(craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(6 + j))) {
								itemStack = craftingBenchBlockScreenHandler.getEnderChestInventory().getStack(6 + j).copy();
								int stackCount = itemStack.getCount();
								if (stackCount >= 1) {
									itemStack.setCount(stackCount - 1);
									craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(6 + j, itemStack);
								} else {
									craftingBenchBlockScreenHandler.getEnderChestInventory().setStack(6 + j, ItemStack.EMPTY);
								}
								break;
							}
						}
					}
					player.getInventory().offerOrDrop(rpgCraftingRecipe.result.copy());
				} else {
					player.sendMessage(Text.translatable("hud.message.not_all_crafting_ingredients_were_found"));
				}
				craftingBenchBlockScreenHandler.populateRecipeLists();
				craftingBenchBlockScreenHandler.setShouldScreenCalculateCraftingStatus(1);
			}
		}
	}
}