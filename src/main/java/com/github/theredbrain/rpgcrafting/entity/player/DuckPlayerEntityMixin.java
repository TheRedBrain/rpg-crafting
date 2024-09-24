package com.github.theredbrain.rpgcrafting.entity.player;

import com.github.theredbrain.rpgcrafting.inventory.StashInventory;

public interface DuckPlayerEntityMixin {

    int rpgcrafting$getActiveHandCraftingLevel();
    int rpgcrafting$getHandCraftingLevel();
    boolean rpgcrafting$useStashForCrafting();
    void rpgcrafting$setUseStashForCrafting(boolean useStashForCrafting);
    StashInventory rpgcrafting$getStashInventory();
    void rpgcrafting$setStashInventory(StashInventory stashInventory);
}
