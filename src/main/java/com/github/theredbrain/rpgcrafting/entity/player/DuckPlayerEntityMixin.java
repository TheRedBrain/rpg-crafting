package com.github.theredbrain.rpgcrafting.entity.player;

import com.github.theredbrain.rpgcrafting.inventory.StashInventory;

public interface DuckPlayerEntityMixin {

	int rpgcrafting$getHandCraftingLevel();

	int rpgcrafting$getCraftingTab1Level();

	int rpgcrafting$getCraftingTab2Level();

	int rpgcrafting$getCraftingTab3Level();

	int rpgcrafting$getCraftingTab4Level();

	boolean rpgcrafting$useStashForCrafting();

	void rpgcrafting$setUseStashForCrafting(boolean useStashForCrafting);

	StashInventory rpgcrafting$getStashInventory();

	void rpgcrafting$setStashInventory(StashInventory stashInventory);
}
