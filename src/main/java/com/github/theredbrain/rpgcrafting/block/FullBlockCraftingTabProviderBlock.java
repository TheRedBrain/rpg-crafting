package com.github.theredbrain.rpgcrafting.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public class FullBlockCraftingTabProviderBlock extends AbstractCraftingTabProviderBlock {
	public static final MapCodec<FullBlockCraftingTabProviderBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
		return instance.group(Codec.INT.fieldOf("openedTab").forGetter((block) -> {
			return block.openedTab;
		}), createSettingsCodec()).apply(instance, FullBlockCraftingTabProviderBlock::new);
	});
	public static final DirectionProperty FACING = Properties.FACING;

	public FullBlockCraftingTabProviderBlock(int openedTab, Settings settings) {
		super(openedTab, settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<FullBlockCraftingTabProviderBlock> getCodec() {
		return CODEC;
	}

	@Override
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
	}
}
