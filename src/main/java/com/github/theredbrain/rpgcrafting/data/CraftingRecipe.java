package com.github.theredbrain.rpgcrafting.data;

import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import com.github.theredbrain.rpgcrafting.utils.ItemUtils;

import java.util.List;

public final class CraftingRecipe {

    private final List<ItemUtils.VirtualItemStack> ingredients;
    private final ItemUtils.VirtualItemStack result;
    private final String unlockAdvancement;
    private final int tab;
    private final CraftingBenchBlockScreenHandler.RecipeType recipeType;
    private final int level;

    public CraftingRecipe(List<ItemUtils.VirtualItemStack> ingredients, ItemUtils.VirtualItemStack result, String unlockAdvancement, int tab, CraftingBenchBlockScreenHandler.RecipeType recipeType, int level) {
        this.ingredients = ingredients;
        this.result = result;
        this.unlockAdvancement = unlockAdvancement;
        this.tab = tab;
        this.recipeType = recipeType;
        this.level = level;
    }

    public List<ItemUtils.VirtualItemStack> getIngredients() {
        return this.ingredients;
    }

    public ItemUtils.VirtualItemStack getResult() {
        return result;
    }

    public String getUnlockAdvancement() {
        return unlockAdvancement;
    }

    public int getTab() {
        return this.tab;
    }

    public CraftingBenchBlockScreenHandler.RecipeType getRecipeType() {
        return this.recipeType;
    }

    public int getLevel() {
        return this.level;
    }
}
