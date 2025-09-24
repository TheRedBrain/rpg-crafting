package com.github.theredbrain.rpgcrafting.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class CauldronShapedCraftingTabProviderBlock extends AbstractCraftingTabProviderBlock {
	public static final MapCodec<CauldronShapedCraftingTabProviderBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
		return instance.group(Codec.INT.fieldOf("openedTab").forGetter((block) -> {
			return block.openedTab;
		}), createSettingsCodec()).apply(instance, CauldronShapedCraftingTabProviderBlock::new);
	});
	private static final VoxelShape RAYCAST_SHAPE = createCuboidShape(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
	protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(
			VoxelShapes.fullCube(),
			VoxelShapes.union(
					createCuboidShape(0.0, 0.0, 4.0, 16.0, 3.0, 12.0),
					createCuboidShape(4.0, 0.0, 0.0, 12.0, 3.0, 16.0),
					createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0),
					RAYCAST_SHAPE
			),
			BooleanBiFunction.ONLY_FIRST
	);

	public CauldronShapedCraftingTabProviderBlock(int openedTab, Settings settings) {
		super(openedTab, settings);
	}

	@Override
	protected MapCodec<CauldronShapedCraftingTabProviderBlock> getCodec() {
		return CODEC;
	}

	@Override
	protected boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE_SHAPE;
	}

	@Override
	protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return RAYCAST_SHAPE;
	}
}
