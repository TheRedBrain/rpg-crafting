package com.github.theredbrain.rpgcrafting.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class RPGCraftingResultSlot extends Slot {
	public RPGCraftingResultSlot(Inventory inventory, int i, int j, int k) {
		super(inventory, i, j, k);
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return false;
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		return false;
	}
}