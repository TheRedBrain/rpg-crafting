package com.github.theredbrain.rpgcrafting.block;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.config.ServerConfig;
import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgcrafting.registry.BlockRegistry;
import com.github.theredbrain.rpgcrafting.registry.Tags;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class CraftingRootBlock extends Block {

	public static final int CRAFTING_TAB_AMOUNT = 4;

	public CraftingRootBlock(Settings settings) {
		super(settings);
	}

	// TODO Block Codecs
	public MapCodec<CraftingRootBlock> getCodec() {
		return null;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}
		switch (RPGCrafting.serverConfig.crafting_root_block_provided_screen) {
			case CRAFTING_TAB_1 ->
					player.openHandledScreen(createCraftingRootBlockScreenHandlerFactory(state, world, pos, 1));
			case CRAFTING_TAB_2 ->
					player.openHandledScreen(createCraftingRootBlockScreenHandlerFactory(state, world, pos, 2));
			case CRAFTING_TAB_3 ->
					player.openHandledScreen(createCraftingRootBlockScreenHandlerFactory(state, world, pos, 3));
			case CRAFTING_TAB_4 ->
					player.openHandledScreen(createCraftingRootBlockScreenHandlerFactory(state, world, pos, 4));
			case CRAFTING_GRID_3X3 -> player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
		}
//        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE); // TODO stats
		return ActionResult.CONSUME;
	}

	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new CraftingScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), Text.translatable("container.crafting"));
	}

	public static NamedScreenHandlerFactory createCraftingRootBlockScreenHandlerFactory(BlockState state, World world, BlockPos pos, int initialTab) {
		int posX = pos.getX();
		int posY = pos.getY();
		int posZ = pos.getZ();

		// using sets so two blocks of the same type only count as one level
		Set<String> craftingTab1LevelProviders = new HashSet<>();
		Set<String> craftingTab2LevelProviders = new HashSet<>();
		Set<String> craftingTab3LevelProviders = new HashSet<>();
		Set<String> craftingTab4LevelProviders = new HashSet<>();

		boolean isStorageTabProviderInReach = false;
		boolean isCraftingTab1ProviderInReach = false;
		boolean isCraftingTab2ProviderInReach = false;
		boolean isCraftingTab3ProviderInReach = false;
		boolean isCraftingTab4ProviderInReach = false;
		boolean isStorageArea0ProviderInReach = false;
		boolean isStorageArea1ProviderInReach = false;
		boolean isStorageArea2ProviderInReach = false;
		boolean isStorageArea3ProviderInReach = false;
		boolean isStorageArea4ProviderInReach = false;
		int[] tabLevels = new int[CRAFTING_TAB_AMOUNT];
		byte tabProvidersInReach = 0;
		byte storageProvidersInReach = 0;
		int crafting_root_block_reach_radius = RPGCrafting.serverConfig.crafting_root_block_reach_radius;

		BlockState blockState;
		if (world != null) {
			for (int i = -crafting_root_block_reach_radius; i <= crafting_root_block_reach_radius; i++) {
				for (int j = -crafting_root_block_reach_radius; j <= crafting_root_block_reach_radius; j++) {
					for (int k = -crafting_root_block_reach_radius; k <= crafting_root_block_reach_radius; k++) {
						blockState = world.getBlockState(new BlockPos(posX + i, posY + j, posZ + k));

						if (blockState.isIn(Tags.PROVIDES_STORAGE_AREA_0)) {
							isStorageArea0ProviderInReach = true;
						}
						if (blockState.isIn(Tags.PROVIDES_STORAGE_AREA_1)) {
							isStorageArea1ProviderInReach = true;
						}
						if (blockState.isIn(Tags.PROVIDES_STORAGE_AREA_2)) {
							isStorageArea2ProviderInReach = true;
						}
						if (blockState.isIn(Tags.PROVIDES_STORAGE_AREA_3)) {
							isStorageArea3ProviderInReach = true;
						}
						if (blockState.isIn(Tags.PROVIDES_STORAGE_AREA_4)) {
							isStorageArea4ProviderInReach = true;
						}

						isStorageTabProviderInReach = isStorageArea0ProviderInReach || isStorageArea1ProviderInReach || isStorageArea2ProviderInReach || isStorageArea3ProviderInReach || isStorageArea4ProviderInReach;

						if (blockState.isOf(BlockRegistry.CRAFTING_TAB_1_PROVIDER_BLOCK) || (RPGCrafting.serverConfig.crafting_root_block_provided_screen == ServerConfig.RootBlockProvidedScreen.CRAFTING_TAB_1 && blockState.isOf(BlockRegistry.CRAFTING_ROOT_BLOCK))) {
							isCraftingTab1ProviderInReach = true;
						}
						if (blockState.isOf(BlockRegistry.CRAFTING_TAB_2_PROVIDER_BLOCK) || (RPGCrafting.serverConfig.crafting_root_block_provided_screen == ServerConfig.RootBlockProvidedScreen.CRAFTING_TAB_2 && blockState.isOf(BlockRegistry.CRAFTING_ROOT_BLOCK))) {
							isCraftingTab2ProviderInReach = true;
						}
						if (blockState.isOf(BlockRegistry.CRAFTING_TAB_3_PROVIDER_BLOCK) || (RPGCrafting.serverConfig.crafting_root_block_provided_screen == ServerConfig.RootBlockProvidedScreen.CRAFTING_TAB_3 && blockState.isOf(BlockRegistry.CRAFTING_ROOT_BLOCK))) {
							isCraftingTab3ProviderInReach = true;
						}
						if (blockState.isOf(BlockRegistry.CRAFTING_TAB_4_PROVIDER_BLOCK) || (RPGCrafting.serverConfig.crafting_root_block_provided_screen == ServerConfig.RootBlockProvidedScreen.CRAFTING_TAB_4 && blockState.isOf(BlockRegistry.CRAFTING_ROOT_BLOCK))) {
							isCraftingTab4ProviderInReach = true;
						}
						if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_1_LEVEL)) {
							craftingTab1LevelProviders.add(blockState.getBlock().getTranslationKey());
						}
						if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_2_LEVEL)) {
							craftingTab2LevelProviders.add(blockState.getBlock().getTranslationKey());
						}
						if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_3_LEVEL)) {
							craftingTab3LevelProviders.add(blockState.getBlock().getTranslationKey());
						}
						if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_4_LEVEL)) {
							craftingTab4LevelProviders.add(blockState.getBlock().getTranslationKey());
						}
					}
				}
			}
		}

		tabLevels[0] = craftingTab1LevelProviders.size();
		tabLevels[1] = craftingTab2LevelProviders.size();
		tabLevels[2] = craftingTab3LevelProviders.size();
		tabLevels[3] = craftingTab4LevelProviders.size();

		tabProvidersInReach = (byte) (isStorageTabProviderInReach ? tabProvidersInReach | 1 << 0 : tabProvidersInReach & ~(1 << 0));
		tabProvidersInReach = (byte) (isCraftingTab1ProviderInReach ? tabProvidersInReach | 1 << 1 : tabProvidersInReach & ~(1 << 1));
		tabProvidersInReach = (byte) (isCraftingTab2ProviderInReach ? tabProvidersInReach | 1 << 2 : tabProvidersInReach & ~(1 << 2));
		tabProvidersInReach = (byte) (isCraftingTab3ProviderInReach ? tabProvidersInReach | 1 << 3 : tabProvidersInReach & ~(1 << 3));
		tabProvidersInReach = (byte) (isCraftingTab4ProviderInReach ? tabProvidersInReach | 1 << 4 : tabProvidersInReach & ~(1 << 4));

		storageProvidersInReach = (byte) (isStorageArea0ProviderInReach ? storageProvidersInReach | 1 << 0 : storageProvidersInReach & ~(1 << 0));
		storageProvidersInReach = (byte) (isStorageArea1ProviderInReach ? storageProvidersInReach | 1 << 1 : storageProvidersInReach & ~(1 << 1));
		storageProvidersInReach = (byte) (isStorageArea2ProviderInReach ? storageProvidersInReach | 1 << 2 : storageProvidersInReach & ~(1 << 2));
		storageProvidersInReach = (byte) (isStorageArea3ProviderInReach ? storageProvidersInReach | 1 << 3 : storageProvidersInReach & ~(1 << 3));
		storageProvidersInReach = (byte) (isStorageArea4ProviderInReach ? storageProvidersInReach | 1 << 4 : storageProvidersInReach & ~(1 << 4));

		byte finalTabProvidersInReach = tabProvidersInReach;
		byte finalStorageProvidersInReach = storageProvidersInReach;
		return new ExtendedScreenHandlerFactory<>() {
			@Override
			public CraftingBenchBlockScreenHandler.CraftingBenchBlockData getScreenOpeningData(ServerPlayerEntity player) {
				return new CraftingBenchBlockScreenHandler.CraftingBenchBlockData(pos, initialTab, finalTabProvidersInReach, finalStorageProvidersInReach, tabLevels);
			}

			@Override
			public Text getDisplayName() {
				return Text.translatable("gui.crafting_bench.title");
			}

			@Nullable
			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
				return new CraftingBenchBlockScreenHandler(syncId, playerInventory, player.getEnderChestInventory(), ((DuckPlayerEntityMixin) player).rpgcrafting$getStashInventory(), pos, initialTab, finalTabProvidersInReach, finalStorageProvidersInReach, tabLevels);
			}
		};
	}
}
