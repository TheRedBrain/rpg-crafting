package com.github.theredbrain.rpgcrafting.datagen;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

import java.util.List;

public class RPGCraftingRecipeDataGenerationHelper {

	public static RPGCraftingRecipeJsonBuilder createRPGRecipeWithCriteria(ItemStack output, List<Pair<Item, Integer>> inputs) {
		RPGCraftingRecipeJsonBuilder builder = RPGCraftingRecipeJsonBuilder.createRPGCrafting(output);

		for (Pair<Item, Integer> pair : inputs) {
			for (int i = 0; i < pair.getRight(); i++) {

				builder.rpgIngredient(pair.getLeft());
			}
			builder.criterion(FabricRecipeProvider.hasItem(pair.getLeft()), FabricRecipeProvider.conditionsFromItem(pair.getLeft()));
		}
		return builder;
	}

	public static void offerRPGRecipesWithTabAndLevel(RecipeExporter recipeExporter, List<RPGCraftingRecipeJsonBuilder> builderList, int tab, int level) {
		for (RPGCraftingRecipeJsonBuilder builder : builderList) {
			builder.tab(tab).level(level).offerTo(recipeExporter);
		}
	}

}
