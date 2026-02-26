package com.github.theredbrain.rpgcrafting.block;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.util.RPGCraftingHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractCraftingTabProviderBlock extends Block implements TabProvider {
	protected final int openedTab;

	public AbstractCraftingTabProviderBlock(int openedTab, Settings settings) {
		super(settings);
		this.openedTab = openedTab;
	}

	@Override
	protected abstract MapCodec<? extends AbstractCraftingTabProviderBlock> getCodec();

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}
		player.openHandledScreen(RPGCraftingHelper.supplyCraftingTabProviderBlockScreenHandlerFactoryFromBlockPos(state, world, pos, player, this.openedTab, RPGCrafting.SERVER_CONFIG.isolate_crafting_tabs_from_blocks.get()));
//		player.sendMessage(Text.translatable("gui.crafting_bench.no_crafting_root_block_nearby"), true);
		return ActionResult.CONSUME;
//        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE); // TODO stats
	}

	@Override
	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}

	public int getOpenedTab() {
		return this.openedTab;
	}

}
