package com.github.theredbrain.rpgcrafting.datagen;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

import java.util.List;

public class RPGCraftingRecipeDataGenerationHelper {

	public static RPGCraftingRecipeJsonBuilder createRPGRecipeWithIndividualItemCriteria(ItemStack output, List<ItemRecipeInput> inputs) {
		RPGCraftingRecipeJsonBuilder builder = RPGCraftingRecipeJsonBuilder.createRPGCrafting(output);

		for (ItemRecipeInput itemRecipeInput : inputs) {
			for (int i = 0; i < itemRecipeInput.count(); i++) {

				builder.rpgIngredient(itemRecipeInput.item());
			}
			builder.criterion(FabricRecipeProvider.hasItem(itemRecipeInput.item()), FabricRecipeProvider.conditionsFromItem(itemRecipeInput.item()));
		}
		return builder;
	}

	public static RPGCraftingRecipeJsonBuilder createRPGRecipeWithItemTagCriteria(ItemStack output, List<ItemTagRecipeInput> inputs) {
		RPGCraftingRecipeJsonBuilder builder = RPGCraftingRecipeJsonBuilder.createRPGCrafting(output);

		for (ItemTagRecipeInput itemTagRecipeInput : inputs) {
			for (int i = 0; i < itemTagRecipeInput.count(); i++) {

				builder.rpgIngredient(itemTagRecipeInput.tagKey());
			}
			builder.criterion(itemTagRecipeInput.name(), FabricRecipeProvider.conditionsFromTag(itemTagRecipeInput.tagKey()));
		}
		return builder;
	}

	public record ItemTagRecipeInput(TagKey<Item> tagKey, int count, String name) {

	}

	public record ItemRecipeInput(ItemConvertible item, int count) {

	}

	public static void offerRPGRecipesWithTabAndLevel(RecipeExporter recipeExporter, List<RPGCraftingRecipeJsonBuilder> builderList, int tab, int level, String recipeNamespace) {
		for (RPGCraftingRecipeJsonBuilder builder : builderList) {
			if (!recipeNamespace.isEmpty()) {
				builder.recipeNamespace(recipeNamespace);
			}
			builder.tab(tab).level(level).offerTo(recipeExporter);
		}
	}

}
