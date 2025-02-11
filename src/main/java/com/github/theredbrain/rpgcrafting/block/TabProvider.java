package com.github.theredbrain.rpgcrafting.block;

import net.minecraft.block.BlockState;
import net.minecraft.world.World;

public interface TabProvider {
	default boolean isActive(World world, BlockState blockState) {
		return true;
	}
}
