package com.github.theredbrain.rpgcrafting.datagen;

import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class RPGCraftingRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
	private Optional<RPGCraftingRecipe.RPGItemStackIngredient> upgradedItemStackIngredient;
	private String upgradeType;
	private List<RPGCraftingRecipe.RPGItemStackIngredient> rpgItemStackIngredients;
	private final List<RPGCraftingRecipe.RPGIngredient> rpgIngredients;
	private ItemStack result;
	private int level;
	private int tab;
	private String recipeType;
	private boolean showNotification;
	private boolean requiresUnlockAdvancement;
	private boolean hasTabAndLevelCriterion;
	private String recipeNamespace;
	private String recipePath;
	private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
	private final RPGCraftingRecipe.RecipeFactory recipeFactory;

	public RPGCraftingRecipeJsonBuilder(
			RPGCraftingRecipe.RecipeFactory recipeFactory,
			Optional<RPGCraftingRecipe.RPGItemStackIngredient> upgradedItemStackIngredient,
			String upgradeType,
			List<RPGCraftingRecipe.RPGItemStackIngredient> rpgItemStackIngredients,
			List<RPGCraftingRecipe.RPGIngredient> rpgIngredients,
			ItemStack result,
			int level,
			int tab,
			String recipeType,
			boolean showNotification,
			boolean requiresUnlockAdvancement,
			boolean hasTabAndLevelCriterion,
			String recipePath
	) {
		this.recipeFactory = recipeFactory;
		this.upgradedItemStackIngredient = upgradedItemStackIngredient;
		this.upgradeType = upgradeType;
		this.rpgItemStackIngredients = rpgItemStackIngredients;
		this.rpgIngredients = rpgIngredients;
		this.result = result;
		this.level = level;
		this.tab = tab;
		this.recipeType = recipeType;
		this.showNotification = showNotification;
		this.requiresUnlockAdvancement = requiresUnlockAdvancement;
		this.hasTabAndLevelCriterion = hasTabAndLevelCriterion;
		this.recipePath = recipePath;
	}

	public static RPGCraftingRecipeJsonBuilder createRPGCrafting(ItemStack result) {
		return new RPGCraftingRecipeJsonBuilder(RPGCraftingRecipe::new, Optional.empty(), "", new ArrayList<>(), new ArrayList<>(), result, 1, 1, "standard", false, false, false, "");
	}

	public static RPGCraftingRecipeJsonBuilder createRPGCrafting(Optional<RPGCraftingRecipe.RPGItemStackIngredient> upgradedItemStackIngredient, String upgradeType, List<RPGCraftingRecipe.RPGItemStackIngredient> rpgItemStackIngredients, List<RPGCraftingRecipe.RPGIngredient> rpgIngredients, ItemStack result, int level, int tab, String recipeType, boolean showNotification, boolean requiresUnlockAdvancement, boolean hasTabAndLevelCriterion, String recipeId) {
		return new RPGCraftingRecipeJsonBuilder(RPGCraftingRecipe::new, upgradedItemStackIngredient, upgradeType, rpgItemStackIngredients, rpgIngredients, result, level, tab, recipeType, showNotification, requiresUnlockAdvancement, hasTabAndLevelCriterion, recipeId);
	}

	public RPGCraftingRecipeJsonBuilder upgradedItemStackIngredient(Optional<RPGCraftingRecipe.RPGItemStackIngredient> upgradedItemStackIngredient) {
		this.upgradedItemStackIngredient = upgradedItemStackIngredient;
		return this;
	}

	public RPGCraftingRecipeJsonBuilder upgradeType(RPGCraftingRecipe.UpgradeType upgradeType) {
		this.upgradeType = upgradeType.asString();
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgItemStackIngredient(RPGCraftingRecipe.RPGItemStackIngredient rpgItemStackIngredient) {
		this.rpgItemStackIngredients.add(rpgItemStackIngredient);
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgItemStackIngredient(ItemStack itemStack) {
		this.rpgItemStackIngredients.add(new RPGCraftingRecipe.RPGItemStackIngredient(itemStack, true, true));
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgItemStackIngredient(ItemStack itemStack, boolean completeComponentMatch, boolean isConsumed) {
		this.rpgItemStackIngredients.add(new RPGCraftingRecipe.RPGItemStackIngredient(itemStack, completeComponentMatch, isConsumed));
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgItemStackIngredients(List<RPGCraftingRecipe.RPGItemStackIngredient> rpgItemStackIngredients) {
		this.rpgItemStackIngredients.addAll(rpgItemStackIngredients);
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgIngredient(RPGCraftingRecipe.RPGIngredient rpgIngredient) {
		this.rpgIngredients.add(rpgIngredient);
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgIngredient(Ingredient ingredient) {
		this.rpgIngredients.add(new RPGCraftingRecipe.RPGIngredient(ingredient, true));
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgIngredient(Ingredient ingredient, boolean isConsumed) {
		this.rpgIngredients.add(new RPGCraftingRecipe.RPGIngredient(ingredient, isConsumed));
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgIngredient(TagKey<Item> tag) {
		this.rpgIngredients.add(new RPGCraftingRecipe.RPGIngredient(Ingredient.fromTag(tag), true));
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgIngredient(TagKey<Item> tag, boolean isConsumed) {
		this.rpgIngredients.add(new RPGCraftingRecipe.RPGIngredient(Ingredient.fromTag(tag), isConsumed));
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgIngredient(ItemConvertible itemProvider) {
		this.rpgIngredients.add(new RPGCraftingRecipe.RPGIngredient(Ingredient.ofItems(itemProvider), true));
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgIngredient(ItemConvertible itemProvider, boolean isConsumed) {
		this.rpgIngredients.add(new RPGCraftingRecipe.RPGIngredient(Ingredient.ofItems(itemProvider), isConsumed));
		return this;
	}

	public RPGCraftingRecipeJsonBuilder rpgIngredients(List<RPGCraftingRecipe.RPGIngredient> rpgIngredients) {
		this.rpgIngredients.addAll(rpgIngredients);
		return this;
	}

	public RPGCraftingRecipeJsonBuilder level(int level) {
		this.level = level;
		return this;
	}

	public RPGCraftingRecipeJsonBuilder tab(int tab) {
		this.tab = tab;
		return this;
	}

	public RPGCraftingRecipeJsonBuilder standardRecipeType() {
		this.recipeType = "standard";
		return this;
	}

	public RPGCraftingRecipeJsonBuilder specialRecipeType() {
		this.recipeType = "special";
		return this;
	}

	public RPGCraftingRecipeJsonBuilder showNotification(boolean showNotification) {
		this.showNotification = showNotification;
		return this;
	}

	/**
	 * This feature is currently not implemented, setting this boolean has no effect
	 */
	@Deprecated
	public RPGCraftingRecipeJsonBuilder hasTabAndLevelCriterion(boolean hasTabAndLevelCriterion) {
		this.hasTabAndLevelCriterion = hasTabAndLevelCriterion;
		return this;
	}

	public RPGCraftingRecipeJsonBuilder recipeNamespace(String recipeNamespace) {
		this.recipeNamespace = recipeNamespace;
		return this;
	}

	public RPGCraftingRecipeJsonBuilder recipePath(String recipePath) {
		this.recipePath = recipePath;
		return this;
	}

	@Override
	public RPGCraftingRecipeJsonBuilder criterion(String string, AdvancementCriterion<?> advancementCriterion) {
		this.criteria.put(string, advancementCriterion);
		this.requiresUnlockAdvancement = true;
		return this;
	}

	public RPGCraftingRecipeJsonBuilder requiresUnlockAdvancement(boolean requiresUnlockAdvancement) {
		this.requiresUnlockAdvancement = requiresUnlockAdvancement;
		return this;
	}

	@Override
	public CraftingRecipeJsonBuilder group(@Nullable String group) {
		return null;
	}

	@Override
	public Item getOutputItem() {
		return this.result.getItem();
	}

	@Override
	public void offerTo(RecipeExporter exporter) {
		String recipePath = this.recipePath;
		String recipeNamespace = this.recipeNamespace;
		if (recipePath.isEmpty() && recipeNamespace.isEmpty()) {
			this.offerTo(exporter, getTabLevelRecipeTypePrefixedIdentifier(CraftingRecipeJsonBuilder.getItemId(this.getOutputItem())));
		} else if (recipePath.isEmpty()) {
			this.offerTo(exporter, getTabLevelRecipeTypePrefixedIdentifier(Identifier.of(recipeNamespace, CraftingRecipeJsonBuilder.getItemId(this.getOutputItem()).getPath())));
		} else if (recipeNamespace.isEmpty()) {
			this.offerTo(exporter, recipePath);
		} else {
			this.offerTo(exporter, getTabLevelRecipeTypePrefixedIdentifier(Identifier.of(recipeNamespace, recipePath)));
		}
	}

	@Override
	public void offerTo(RecipeExporter exporter, String recipePath) {
		Identifier identifier = getTabLevelRecipeTypePrefixedIdentifier(CraftingRecipeJsonBuilder.getItemId(this.getOutputItem()));
		Identifier identifier2 = getTabLevelRecipeTypePrefixedIdentifier(Identifier.of(recipePath));
		if (identifier2.equals(identifier)) {
			throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
		} else {
			this.offerTo(exporter, identifier2);
		}
	}

	@Override
	public void offerTo(RecipeExporter exporter, Identifier recipeId) {
		this.validate(recipeId);
		AdvancementEntry advancementEntry = null;
		if (this.requiresUnlockAdvancement && !this.criteria.isEmpty()) {
			Advancement.Builder builder = exporter.getAdvancementBuilder()
					.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
					.rewards(AdvancementRewards.Builder.recipe(recipeId));
//			if (this.hasTabAndLevelCriterion) { // TODO work on this later
//
//				List<String> criteriaList = new ArrayList<>(List.copyOf(this.criteria.keySet()));
////				criteriaList.add("has_the_recipe");
//
//				AdvancementCriterion<InteractWithRPGCraftingStationCriterion.Conditions> interactWithRPGCraftingStationCriterion = InteractWithRPGCraftingStationCriterion.create(this.tab, this.level);
//				builder.criterion("tab_and_level", interactWithRPGCraftingStationCriterion);
//				this.criteria.put("tab_and_level", interactWithRPGCraftingStationCriterion);
//
//				List<String> tabAndLevelList = List.of("has_the_recipe", "tab_and_level");
//
//				List<List<String>> requirements = new ArrayList<>();
//
//				requirements.add(criteriaList);
//				requirements.add(tabAndLevelList);
//				AdvancementRequirements advancementRequirements = new AdvancementRequirements(requirements);
//				RPGCrafting.info("advancementRequirements: " + advancementRequirements);
//				builder.requirements(advancementRequirements);
//			} else {
			builder.criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
			this.criteria.forEach(builder::criterion);
//			}

			advancementEntry = builder.build(recipeId);
		}
		RPGCraftingRecipe rpgCraftingRecipe = this.recipeFactory
				.create(this.upgradedItemStackIngredient, this.upgradeType, this.rpgItemStackIngredients, this.rpgIngredients, this.result, this.level, this.tab, this.recipeType, this.showNotification, this.requiresUnlockAdvancement);
		exporter.accept(recipeId, rpgCraftingRecipe, advancementEntry);
	}

	private Identifier getTabLevelRecipeTypePrefixedIdentifier(Identifier identifier) {
		return identifier.withPrefixedPath("recipes/rpg_crafting/tab_" + this.tab + "/level_" + this.level + "/" + CraftingBenchBlockScreenHandler.RecipeType.byName(this.recipeType).asString() + "/");
	}

	private void validate(Identifier recipeId) {
		if (this.requiresUnlockAdvancement && this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + recipeId);
		}
		if (this.tab < 0 || this.tab > 4) {
			throw new IllegalStateException("Recipe " + recipeId + " has invalid tab");
		}
	}
}
