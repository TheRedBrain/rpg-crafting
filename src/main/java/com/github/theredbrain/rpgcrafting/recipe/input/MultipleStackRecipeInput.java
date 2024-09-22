package com.github.theredbrain.rpgcrafting.recipe.input;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

import java.util.List;

public record MultipleStackRecipeInput(List<ItemStack> ingredients, int size) implements RecipeInput {
	@Override
	public ItemStack getStackInSlot(int slot) {

		if (slot >= this.ingredients.size() || slot >= this.size) {
			return ItemStack.EMPTY;
		}
		return this.ingredients.get(slot);
	}

	@Override
	public int getSize() {
		return this.size;
	}
}
