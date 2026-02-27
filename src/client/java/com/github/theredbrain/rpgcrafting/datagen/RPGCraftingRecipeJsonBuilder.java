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
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RPGCraftingRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
	private List<RPGCraftingRecipe.RPGItemStackIngredient> rpgItemStackIngredients;
	private final List<RPGCraftingRecipe.RPGIngredient> rpgIngredients;
	private ItemStack result;
	private int level;
	private int tab;
	private String recipeType;
	private boolean showNotification;
	private boolean requiresUnlockAdvancement;
	private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
	private final RPGCraftingRecipe.RecipeFactory recipeFactory;

	public RPGCraftingRecipeJsonBuilder(
			RPGCraftingRecipe.RecipeFactory recipeFactory,
			List<RPGCraftingRecipe.RPGItemStackIngredient> rpgItemStackIngredients,
			List<RPGCraftingRecipe.RPGIngredient> rpgIngredients,
			ItemStack result,
			int level,
			int tab,
			String recipeType,
			boolean showNotification,
			boolean requiresUnlockAdvancement
	) {
		this.recipeFactory = recipeFactory;
		this.rpgItemStackIngredients = rpgItemStackIngredients;
		this.rpgIngredients = rpgIngredients;
		this.result = result;
		this.level = level;
		this.tab = tab;
		this.recipeType = recipeType;
		this.showNotification = showNotification;
		this.requiresUnlockAdvancement = requiresUnlockAdvancement;
	}

	public static RPGCraftingRecipeJsonBuilder createRPGCrafting(ItemStack result) {
		return new RPGCraftingRecipeJsonBuilder(RPGCraftingRecipe::new, new ArrayList<>(), new ArrayList<>(), result, 1, 1, "standard", false, false);
	}

	public static RPGCraftingRecipeJsonBuilder createRPGCrafting(List<RPGCraftingRecipe.RPGItemStackIngredient> rpgItemStackIngredients, List<RPGCraftingRecipe.RPGIngredient> rpgIngredients, ItemStack result, int level, int tab, String recipeType, boolean showNotification, boolean requiresUnlockAdvancement) {
		return new RPGCraftingRecipeJsonBuilder(RPGCraftingRecipe::new, rpgItemStackIngredients, rpgIngredients, result, level, tab, recipeType, showNotification, requiresUnlockAdvancement);
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

	@Override
	public RPGCraftingRecipeJsonBuilder criterion(String string, AdvancementCriterion<?> advancementCriterion) {
		this.criteria.put(string, advancementCriterion);
		this.requiresUnlockAdvancement = true;
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
	public void offerTo(RecipeExporter exporter, Identifier recipeId) {
		this.validate(recipeId);
		AdvancementEntry advancementEntry = null;
		if (this.requiresUnlockAdvancement && !this.criteria.isEmpty()) {
			Advancement.Builder builder = exporter.getAdvancementBuilder()
					.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
					.rewards(AdvancementRewards.Builder.recipe(recipeId))
					.criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
			this.criteria.forEach(builder::criterion);
			advancementEntry = builder.build(recipeId.withPrefixedPath("recipes/rpg_crafting/tab_" + this.tab + "/" + CraftingBenchBlockScreenHandler.RecipeType.byName(this.recipeType).asString() + "/"));
		}
		RPGCraftingRecipe rpgCraftingRecipe = this.recipeFactory
				.create(this.rpgItemStackIngredients, this.rpgIngredients, this.result, this.level, this.tab, this.recipeType, this.showNotification, this.requiresUnlockAdvancement);
		exporter.accept(recipeId, rpgCraftingRecipe, advancementEntry);
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
