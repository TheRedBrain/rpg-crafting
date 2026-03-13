package com.github.theredbrain.rpgcrafting.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VanillaRecipeToRPGCraftingRecipeConverter extends FabricRecipeProvider {

	public VanillaRecipeToRPGCraftingRecipeConverter(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public void generate(RecipeExporter recipeExporter) {
		List<RPGCraftingRecipeJsonBuilder> tab0level1Recipes = new ArrayList<>();
		List<RPGCraftingRecipeJsonBuilder> tab1level1Recipes = new ArrayList<>();

		tab0level1Recipes.addAll(getDyeRecipes());
		tab0level1Recipes.addAll(getPlankRecipes());
		tab0level1Recipes.addAll(getBarkRecipes());

		tab1level1Recipes.addAll(getDyeRecipes());
		tab1level1Recipes.addAll(getPlankRecipes());
		tab1level1Recipes.addAll(getBarkRecipes());
		tab1level1Recipes.add(
				RPGCraftingRecipeJsonBuilder.createRPGCrafting(new ItemStack(Items.LOOM))
						.rpgIngredient(ItemTags.WOOL)
						.rpgIngredient(ItemTags.WOOL)
						.rpgIngredient(ItemTags.LOGS)
						.rpgIngredient(ItemTags.LOGS)
						.criterion("has_wool", FabricRecipeProvider.conditionsFromTag(ItemTags.WOOL))
						.criterion("has_logs", FabricRecipeProvider.conditionsFromTag(ItemTags.LOGS))
						.criterion(FabricRecipeProvider.hasItem(Items.LOOM), FabricRecipeProvider.conditionsFromItem(Items.LOOM))
		);

		RPGCraftingRecipeDataGenerationHelper.offerRPGRecipesWithTabAndLevel(recipeExporter, tab0level1Recipes, 0, 1, "rpgcrafting");
		RPGCraftingRecipeDataGenerationHelper.offerRPGRecipesWithTabAndLevel(recipeExporter, tab1level1Recipes, 1, 1, "rpgcrafting");
		// TODO add recipes

	}

	public static List<RPGCraftingRecipeJsonBuilder> getPlankRecipes() {
		List<RPGCraftingRecipeJsonBuilder> plankRecipes = new ArrayList<>();
		plankRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithItemTagCriteria(
								new ItemStack(Blocks.ACACIA_PLANKS, 4),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemTagRecipeInput(ItemTags.ACACIA_LOGS, 1, "has_logs")
								)
						)
						.showNotification(true)
		);
		plankRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithItemTagCriteria(
								new ItemStack(Blocks.BIRCH_PLANKS, 4),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemTagRecipeInput(ItemTags.BIRCH_LOGS, 1, "has_logs")
								)
						)
						.showNotification(true)
		);
		plankRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithItemTagCriteria(
								new ItemStack(Blocks.CRIMSON_PLANKS, 4),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemTagRecipeInput(ItemTags.CRIMSON_STEMS, 1, "has_logs")
								)
						)
						.showNotification(true)
		);
		plankRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithItemTagCriteria(
								new ItemStack(Blocks.DARK_OAK_PLANKS, 4),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemTagRecipeInput(ItemTags.DARK_OAK_LOGS, 1, "has_logs")
								)
						)
						.showNotification(true)
		);
		plankRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithItemTagCriteria(
								new ItemStack(Blocks.JUNGLE_PLANKS, 4),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemTagRecipeInput(ItemTags.JUNGLE_LOGS, 1, "has_logs")
								)
						)
						.showNotification(true)
		);
		plankRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithItemTagCriteria(
								new ItemStack(Blocks.OAK_PLANKS, 4),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemTagRecipeInput(ItemTags.OAK_LOGS, 1, "has_logs")
								)
						)
						.showNotification(true)
		);
		plankRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithItemTagCriteria(
						new ItemStack(Blocks.SPRUCE_PLANKS, 4),
						List.of(
								new RPGCraftingRecipeDataGenerationHelper.ItemTagRecipeInput(ItemTags.SPRUCE_LOGS, 1, "has_logs")
						)
				)
		);
		plankRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithItemTagCriteria(
								new ItemStack(Blocks.WARPED_PLANKS, 4),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemTagRecipeInput(ItemTags.WARPED_STEMS, 1, "has_logs")
								)
						)
						.showNotification(true)
		);
		plankRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithItemTagCriteria(
								new ItemStack(Blocks.MANGROVE_PLANKS, 4),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemTagRecipeInput(ItemTags.MANGROVE_LOGS, 1, "has_logs")
								)
						)
						.showNotification(true)
		);
		return plankRecipes;
	}

	public static List<RPGCraftingRecipeJsonBuilder> getBarkRecipes() {
		List<RPGCraftingRecipeJsonBuilder> barkRecipes = new ArrayList<>();
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.ACACIA_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.ACACIA_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.BIRCH_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.BIRCH_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.DARK_OAK_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.DARK_OAK_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.JUNGLE_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.JUNGLE_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.OAK_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.OAK_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.SPRUCE_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.SPRUCE_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.CRIMSON_HYPHAE, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.CRIMSON_STEM, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.WARPED_HYPHAE, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.WARPED_STEM, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.MANGROVE_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.MANGROVE_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.STRIPPED_ACACIA_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.STRIPPED_ACACIA_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.STRIPPED_BIRCH_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.STRIPPED_BIRCH_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.STRIPPED_DARK_OAK_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.STRIPPED_DARK_OAK_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.STRIPPED_JUNGLE_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.STRIPPED_JUNGLE_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.STRIPPED_OAK_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.STRIPPED_OAK_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.STRIPPED_SPRUCE_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.STRIPPED_SPRUCE_LOG, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.STRIPPED_CRIMSON_HYPHAE, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.STRIPPED_CRIMSON_STEM, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.STRIPPED_WARPED_HYPHAE, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.STRIPPED_WARPED_STEM, 4)
								)
						)
						.showNotification(true)
		);
		barkRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.STRIPPED_MANGROVE_WOOD, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Blocks.STRIPPED_MANGROVE_LOG, 4)
								)
						)
						.showNotification(true)
		);
		return barkRecipes;
	}

	public static List<RPGCraftingRecipeJsonBuilder> getDyeRecipes() {
		List<RPGCraftingRecipeJsonBuilder> dyeRecipes = new ArrayList<>();
		dyeRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.MAGENTA_DYE, 4),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Items.BLUE_DYE, 1),
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Items.RED_DYE, 2),
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Items.WHITE_DYE, 1)
								)
						)
						.showNotification(true)
						.recipePath("magenta_dye_from_blue_red_white_dye")
		);
		dyeRecipes.add(
				RPGCraftingRecipeDataGenerationHelper.createRPGRecipeWithIndividualItemCriteria(
								new ItemStack(Items.MAGENTA_DYE, 3),
								List.of(
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Items.BLUE_DYE, 1),
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Items.RED_DYE, 1),
										new RPGCraftingRecipeDataGenerationHelper.ItemRecipeInput(Items.PINK_DYE, 1)
								)
						)
						.showNotification(true)
						.recipePath("magenta_dye_from_blue_red_pink_dye")
		);
		return dyeRecipes;
	}
}
