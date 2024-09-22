package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class RecipeRegistry {
	public static void init() {

		Registry.register(Registries.RECIPE_SERIALIZER, RPGCraftingRecipe.Serializer.ID,
				RPGCraftingRecipe.Serializer.INSTANCE);
		Registry.register(Registries.RECIPE_TYPE, RPGCrafting.identifier(RPGCraftingRecipe.Type.ID), RPGCraftingRecipe.Type.INSTANCE);
	}
}
