package com.github.theredbrain.rpgcrafting.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;

public class RecipeListInputInventory implements Inventory {
	private final ScreenHandler handler;
	private final DefaultedList<ItemStack> stacks;

	public RecipeListInputInventory(int size, ScreenHandler handler) {
		this.handler = handler;
		this.stacks = DefaultedList.ofSize(size, ItemStack.EMPTY);
	}

	@Override
	public int size() {
		return this.stacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemStack : this.stacks) {
			if (!itemStack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return slot >= this.size() ? ItemStack.EMPTY : this.stacks.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.stacks, slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack itemStack = Inventories.splitStack(this.stacks, slot, amount);
		if (!itemStack.isEmpty()) {
			this.handler.onContentChanged(this);
		}

		return itemStack;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.stacks.set(slot, stack);
		this.handler.onContentChanged(this);
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		this.stacks.clear();
	}
}
