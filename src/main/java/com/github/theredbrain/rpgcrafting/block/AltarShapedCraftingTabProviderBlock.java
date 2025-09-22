package com.github.theredbrain.rpgcrafting.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class AltarShapedCraftingTabProviderBlock extends AbstractCraftingTabProviderBlock {
	public static final MapCodec<AltarShapedCraftingTabProviderBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
		return instance.group(Codec.INT.fieldOf("openedTab").forGetter((block) -> {
			return block.openedTab;
		}), createSettingsCodec()).apply(instance, AltarShapedCraftingTabProviderBlock::new);
	});
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	private static final VoxelShape TOP_SHAPE = Block.createCuboidShape(1, 12, 1, 15, 16, 15);
	private static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(4, 3, 4, 12, 12, 12);
	private static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(1, 0, 1, 15, 3, 15);
	private static final VoxelShape SHAPE = VoxelShapes.union(TOP_SHAPE, MIDDLE_SHAPE, BOTTOM_SHAPE);

	public AltarShapedCraftingTabProviderBlock(int openedTab, Settings settings) {
		super(openedTab, settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<AltarShapedCraftingTabProviderBlock> getCodec() {
		return CODEC;
	}

	@Override
	protected boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
}
