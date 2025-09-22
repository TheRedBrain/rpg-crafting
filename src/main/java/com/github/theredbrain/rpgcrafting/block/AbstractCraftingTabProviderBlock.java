package com.github.theredbrain.rpgcrafting.block;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.config.ServerConfig;
import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgcrafting.registry.Tags;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCraftingTabProviderBlock extends Block implements TabProvider {
	public static final int CRAFTING_TAB_AMOUNT = 4;
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
		player.openHandledScreen(createCraftingTabProviderBlockScreenHandlerFactory(state, world, pos, player, this.openedTab));
//		player.sendMessage(Text.translatable("gui.crafting_bench.no_crafting_root_block_nearby"), true);
		return ActionResult.CONSUME;
//        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE); // TODO stats
	}

	private static boolean isTabProviderBlockActive(World world, BlockState blockState) {
		if (blockState.getBlock() instanceof TabProvider tabProvider) {
			return tabProvider.isActive(world, blockState);
		}
		return true;
	}

	public static NamedScreenHandlerFactory createCraftingTabProviderBlockScreenHandlerFactory(BlockState state, World world, BlockPos pos, PlayerEntity player, int initialTab) {
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
		ServerConfig serverConfig = RPGCrafting.SERVER_CONFIG;
		int crafting_root_block_reach_radius = serverConfig.crafting_bench_block_reach_radius.get();

		BlockState blockState;
		if (world != null) {
			boolean stateIsActiveTabProvider = isTabProviderBlockActive(world, state);
			int stateOpenedTab = 0;
			if (state.getBlock() instanceof AbstractCraftingTabProviderBlock abstractCraftingTabProviderBlock) {
				stateOpenedTab = abstractCraftingTabProviderBlock.openedTab;
			}
			for (int i = -crafting_root_block_reach_radius; i <= crafting_root_block_reach_radius; i++) {
				for (int j = -crafting_root_block_reach_radius; j <= crafting_root_block_reach_radius; j++) {
					for (int k = -crafting_root_block_reach_radius; k <= crafting_root_block_reach_radius; k++) {
						blockState = world.getBlockState(new BlockPos(posX + i, posY + j, posZ + k));

						if ((blockState.isIn(Tags.PROVIDES_STORAGE_AREA_0) && isTabProviderBlockActive(world, blockState)) || (state.isIn(Tags.PROVIDES_STORAGE_AREA_0) && stateIsActiveTabProvider)) {
							isStorageArea0ProviderInReach = true;
						}
						if ((blockState.isIn(Tags.PROVIDES_STORAGE_AREA_1) && isTabProviderBlockActive(world, blockState)) || (state.isIn(Tags.PROVIDES_STORAGE_AREA_1) && stateIsActiveTabProvider)) {
							isStorageArea1ProviderInReach = true;
						}
						if ((blockState.isIn(Tags.PROVIDES_STORAGE_AREA_2) && isTabProviderBlockActive(world, blockState)) || (state.isIn(Tags.PROVIDES_STORAGE_AREA_2) && stateIsActiveTabProvider)) {
							isStorageArea2ProviderInReach = true;
						}
						if ((blockState.isIn(Tags.PROVIDES_STORAGE_AREA_3) && isTabProviderBlockActive(world, blockState)) || (state.isIn(Tags.PROVIDES_STORAGE_AREA_3) && stateIsActiveTabProvider)) {
							isStorageArea3ProviderInReach = true;
						}
						if ((blockState.isIn(Tags.PROVIDES_STORAGE_AREA_4) && isTabProviderBlockActive(world, blockState)) || (state.isIn(Tags.PROVIDES_STORAGE_AREA_4) && stateIsActiveTabProvider)) {
							isStorageArea4ProviderInReach = true;
						}

						isStorageTabProviderInReach = isStorageArea0ProviderInReach || isStorageArea1ProviderInReach || isStorageArea2ProviderInReach || isStorageArea3ProviderInReach || isStorageArea4ProviderInReach;

						if ((blockState.getBlock() instanceof AbstractCraftingTabProviderBlock abstractCraftingTabProviderBlock && abstractCraftingTabProviderBlock.openedTab == 1 && isTabProviderBlockActive(world, blockState)) || (stateOpenedTab == 1 && stateIsActiveTabProvider)) {
							isCraftingTab1ProviderInReach = true;
						}
						if ((blockState.getBlock() instanceof AbstractCraftingTabProviderBlock abstractCraftingTabProviderBlock && abstractCraftingTabProviderBlock.openedTab == 2 && isTabProviderBlockActive(world, blockState)) || (stateOpenedTab == 2 && stateIsActiveTabProvider)) {
							isCraftingTab2ProviderInReach = true;
						}
						if ((blockState.getBlock() instanceof AbstractCraftingTabProviderBlock abstractCraftingTabProviderBlock && abstractCraftingTabProviderBlock.openedTab == 3 && isTabProviderBlockActive(world, blockState)) || (stateOpenedTab == 3 && stateIsActiveTabProvider)) {
							isCraftingTab3ProviderInReach = true;
						}
						if ((blockState.getBlock() instanceof AbstractCraftingTabProviderBlock abstractCraftingTabProviderBlock && abstractCraftingTabProviderBlock.openedTab == 4 && isTabProviderBlockActive(world, blockState)) || (stateOpenedTab == 4 && stateIsActiveTabProvider)) {
							isCraftingTab4ProviderInReach = true;
						}
						if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_1_LEVEL) && isTabProviderBlockActive(world, blockState)) {
							craftingTab1LevelProviders.add(blockState.getBlock().getTranslationKey());
						}
						if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_2_LEVEL) && isTabProviderBlockActive(world, blockState)) {
							craftingTab2LevelProviders.add(blockState.getBlock().getTranslationKey());
						}
						if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_3_LEVEL) && isTabProviderBlockActive(world, blockState)) {
							craftingTab3LevelProviders.add(blockState.getBlock().getTranslationKey());
						}
						if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_4_LEVEL) && isTabProviderBlockActive(world, blockState)) {
							craftingTab4LevelProviders.add(blockState.getBlock().getTranslationKey());
						}
					}
				}
			}

			if (state.isIn(Tags.PROVIDES_CRAFTING_TAB_1_LEVEL) && stateIsActiveTabProvider) {
				craftingTab1LevelProviders.add(state.getBlock().getTranslationKey());
			}
			if (state.isIn(Tags.PROVIDES_CRAFTING_TAB_2_LEVEL) && stateIsActiveTabProvider) {
				craftingTab2LevelProviders.add(state.getBlock().getTranslationKey());
			}
			if (state.isIn(Tags.PROVIDES_CRAFTING_TAB_3_LEVEL) && stateIsActiveTabProvider) {
				craftingTab3LevelProviders.add(state.getBlock().getTranslationKey());
			}
			if (state.isIn(Tags.PROVIDES_CRAFTING_TAB_4_LEVEL) && stateIsActiveTabProvider) {
				craftingTab4LevelProviders.add(state.getBlock().getTranslationKey());
			}
		}

		int craftingTab1AttributeLevel = ((DuckPlayerEntityMixin) player).rpgcrafting$getCraftingTab1Level();
		int craftingTab2AttributeLevel = ((DuckPlayerEntityMixin) player).rpgcrafting$getCraftingTab2Level();
		int craftingTab3AttributeLevel = ((DuckPlayerEntityMixin) player).rpgcrafting$getCraftingTab3Level();
		int craftingTab4AttributeLevel = ((DuckPlayerEntityMixin) player).rpgcrafting$getCraftingTab4Level();

		if (serverConfig.crafting_bench_level_calculation.get() == RPGCrafting.CraftingLevelCalculation.ADDITION) {
			tabLevels[0] = craftingTab1LevelProviders.size() + craftingTab1AttributeLevel;
			tabLevels[1] = craftingTab2LevelProviders.size() + craftingTab2AttributeLevel;
			tabLevels[2] = craftingTab3LevelProviders.size() + craftingTab3AttributeLevel;
			tabLevels[3] = craftingTab4LevelProviders.size() + craftingTab4AttributeLevel;
		} else if (serverConfig.crafting_bench_level_calculation.get() == RPGCrafting.CraftingLevelCalculation.BLOCKS_REQUIRED) {
			tabLevels[0] = (int) Math.clamp(craftingTab1AttributeLevel, 0.0, craftingTab1LevelProviders.size());
			tabLevels[1] = (int) Math.clamp(craftingTab2AttributeLevel, 0.0, craftingTab2LevelProviders.size());
			tabLevels[2] = (int) Math.clamp(craftingTab3AttributeLevel, 0.0, craftingTab3LevelProviders.size());
			tabLevels[3] = (int) Math.clamp(craftingTab4AttributeLevel, 0.0, craftingTab4LevelProviders.size());
		} else if (serverConfig.crafting_bench_level_calculation.get() == RPGCrafting.CraftingLevelCalculation.HIGHER_VALUE) {
			tabLevels[0] = Math.max(craftingTab1LevelProviders.size(), craftingTab1AttributeLevel);
			tabLevels[1] = Math.max(craftingTab2LevelProviders.size(), craftingTab2AttributeLevel);
			tabLevels[2] = Math.max(craftingTab3LevelProviders.size(), craftingTab3AttributeLevel);
			tabLevels[3] = Math.max(craftingTab4LevelProviders.size(), craftingTab4AttributeLevel);
		} else {
			tabLevels[0] = 0;
			tabLevels[1] = 0;
			tabLevels[2] = 0;
			tabLevels[3] = 0;
		}

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
				return Text.translatable("gui.crafting_bench.default_title");
			}

			@Nullable
			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
				return new CraftingBenchBlockScreenHandler(syncId, playerInventory, player.getEnderChestInventory(), ((DuckPlayerEntityMixin) player).rpgcrafting$getStashInventory(), pos, initialTab, finalTabProvidersInReach, finalStorageProvidersInReach, tabLevels);
			}
		};
	}

	public static void triggerAdvancementCriterion(ServerPlayerEntity serverPlayerEntity, byte tabProvidersInReach, int[] tabLevels) {
		if ((tabProvidersInReach & 1 << 1) != 0 && tabLevels.length >= 1) {
			RPGCrafting.INTERACTED_WITH_RPG_CRAFTING_STATION.trigger(serverPlayerEntity, 1, tabLevels[0]);
		}
		if ((tabProvidersInReach & 1 << 2) != 0 && tabLevels.length >= 2) {
			RPGCrafting.INTERACTED_WITH_RPG_CRAFTING_STATION.trigger(serverPlayerEntity, 2, tabLevels[1]);
		}
		if ((tabProvidersInReach & 1 << 3) != 0 && tabLevels.length >= 3) {
			RPGCrafting.INTERACTED_WITH_RPG_CRAFTING_STATION.trigger(serverPlayerEntity, 3, tabLevels[2]);
		}
		if ((tabProvidersInReach & 1 << 4) != 0 && tabLevels.length >= 4) {
			RPGCrafting.INTERACTED_WITH_RPG_CRAFTING_STATION.trigger(serverPlayerEntity, 4, tabLevels[3]);
		}
	}

	@Override
	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}
}
