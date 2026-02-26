package com.github.theredbrain.rpgcrafting.util;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.block.AbstractCraftingTabProviderBlock;
import com.github.theredbrain.rpgcrafting.block.TabProvider;
import com.github.theredbrain.rpgcrafting.config.ServerConfig;
import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.registry.Tags;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class RPGCraftingHelper {
	public static final int CRAFTING_TAB_AMOUNT = 4;

	public static boolean isTabProviderBlockActive(World world, BlockState blockState) {
		if (blockState.getBlock() instanceof TabProvider tabProvider) {
			return tabProvider.isActive(world, blockState);
		}
		return true;
	}

	public static NamedScreenHandlerFactory supplyCraftingTabProviderBlockScreenHandlerFactoryFromBlockPos(BlockState state, World world, BlockPos pos, PlayerEntity player, int initialTab, boolean isolated) {
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
		boolean blockStateIsActiveTabProvider;
		if (world != null) {
			boolean stateIsActiveTabProvider = isTabProviderBlockActive(world, state);

			for (int i = -crafting_root_block_reach_radius; i <= crafting_root_block_reach_radius; i++) {
				for (int j = -crafting_root_block_reach_radius; j <= crafting_root_block_reach_radius; j++) {
					for (int k = -crafting_root_block_reach_radius; k <= crafting_root_block_reach_radius; k++) {
						blockState = world.getBlockState(new BlockPos(posX + i, posY + j, posZ + k));
						blockStateIsActiveTabProvider = isTabProviderBlockActive(world, blockState);

						if (!isolated) {

							if (blockState.getBlock() instanceof AbstractCraftingTabProviderBlock abstractCraftingTabProviderBlock && abstractCraftingTabProviderBlock.getOpenedTab() == 1 && blockStateIsActiveTabProvider) {
								isCraftingTab1ProviderInReach = true;
							}
							if (blockState.getBlock() instanceof AbstractCraftingTabProviderBlock abstractCraftingTabProviderBlock && abstractCraftingTabProviderBlock.getOpenedTab() == 2 && blockStateIsActiveTabProvider) {
								isCraftingTab2ProviderInReach = true;
							}
							if (blockState.getBlock() instanceof AbstractCraftingTabProviderBlock abstractCraftingTabProviderBlock && abstractCraftingTabProviderBlock.getOpenedTab() == 3 && blockStateIsActiveTabProvider) {
								isCraftingTab3ProviderInReach = true;
							}
							if (blockState.getBlock() instanceof AbstractCraftingTabProviderBlock abstractCraftingTabProviderBlock && abstractCraftingTabProviderBlock.getOpenedTab() == 4 && blockStateIsActiveTabProvider) {
								isCraftingTab4ProviderInReach = true;
							}
						}

						if (!isolated || initialTab == -1) {

							if (blockState.isIn(Tags.PROVIDES_STORAGE_AREA_0) && blockStateIsActiveTabProvider) {
								isStorageArea0ProviderInReach = true;
							}
							if (blockState.isIn(Tags.PROVIDES_STORAGE_AREA_1) && blockStateIsActiveTabProvider) {
								isStorageArea1ProviderInReach = true;
							}
							if (blockState.isIn(Tags.PROVIDES_STORAGE_AREA_2) && blockStateIsActiveTabProvider) {
								isStorageArea2ProviderInReach = true;
							}
							if (blockState.isIn(Tags.PROVIDES_STORAGE_AREA_3) && blockStateIsActiveTabProvider) {
								isStorageArea3ProviderInReach = true;
							}
							if (blockState.isIn(Tags.PROVIDES_STORAGE_AREA_4) && blockStateIsActiveTabProvider) {
								isStorageArea4ProviderInReach = true;
							}
						}

						if (!isolated || initialTab == 1) {

							if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_1_LEVEL) && blockStateIsActiveTabProvider) {
								craftingTab1LevelProviders.add(blockState.getBlock().getTranslationKey());
							}
						}

						if (!isolated || initialTab == 2) {

							if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_2_LEVEL) && blockStateIsActiveTabProvider) {
								craftingTab2LevelProviders.add(blockState.getBlock().getTranslationKey());
							}
						}

						if (!isolated || initialTab == 3) {

							if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_3_LEVEL) && blockStateIsActiveTabProvider) {
								craftingTab3LevelProviders.add(blockState.getBlock().getTranslationKey());
							}
						}

						if (!isolated || initialTab == 4) {

							if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_4_LEVEL) && blockStateIsActiveTabProvider) {
								craftingTab4LevelProviders.add(blockState.getBlock().getTranslationKey());
							}
						}
					}
				}
			}

			if (!isolated || initialTab == 1) {

				if (state.isIn(Tags.PROVIDES_CRAFTING_TAB_1_LEVEL) && stateIsActiveTabProvider) {
					craftingTab1LevelProviders.add(state.getBlock().getTranslationKey());
				}
			}

			if (!isolated || initialTab == 2) {

				if (state.isIn(Tags.PROVIDES_CRAFTING_TAB_2_LEVEL) && stateIsActiveTabProvider) {
					craftingTab2LevelProviders.add(state.getBlock().getTranslationKey());
				}
			}

			if (!isolated || initialTab == 3) {

				if (state.isIn(Tags.PROVIDES_CRAFTING_TAB_3_LEVEL) && stateIsActiveTabProvider) {
					craftingTab3LevelProviders.add(state.getBlock().getTranslationKey());
				}
			}

			if (!isolated || initialTab == 4) {

				if (state.isIn(Tags.PROVIDES_CRAFTING_TAB_4_LEVEL) && stateIsActiveTabProvider) {
					craftingTab4LevelProviders.add(state.getBlock().getTranslationKey());
				}
			}

			if (!isolated || initialTab == -1) {

				if (state.isIn(Tags.PROVIDES_STORAGE_AREA_0) && stateIsActiveTabProvider) {
					isStorageArea0ProviderInReach = true;
				}
				if (state.isIn(Tags.PROVIDES_STORAGE_AREA_1) && stateIsActiveTabProvider) {
					isStorageArea1ProviderInReach = true;
				}
				if (state.isIn(Tags.PROVIDES_STORAGE_AREA_2) && stateIsActiveTabProvider) {
					isStorageArea2ProviderInReach = true;
				}
				if (state.isIn(Tags.PROVIDES_STORAGE_AREA_3) && stateIsActiveTabProvider) {
					isStorageArea3ProviderInReach = true;
				}
				if (state.isIn(Tags.PROVIDES_STORAGE_AREA_4) && stateIsActiveTabProvider) {
					isStorageArea4ProviderInReach = true;
				}

				isStorageTabProviderInReach = isStorageArea0ProviderInReach || isStorageArea1ProviderInReach || isStorageArea2ProviderInReach || isStorageArea3ProviderInReach || isStorageArea4ProviderInReach;
			}

			if (initialTab == 1 && stateIsActiveTabProvider) {
				isCraftingTab1ProviderInReach = true;
			}
			if (initialTab == 2 && stateIsActiveTabProvider) {
				isCraftingTab2ProviderInReach = true;
			}
			if (initialTab == 3 && stateIsActiveTabProvider) {
				isCraftingTab3ProviderInReach = true;
			}
			if (initialTab == 4 && stateIsActiveTabProvider) {
				isCraftingTab4ProviderInReach = true;
			}
		}

		int craftingTab1AttributeLevel = ((DuckPlayerEntityMixin) player).rpgcrafting$getCraftingTab1Level();
		int craftingTab2AttributeLevel = ((DuckPlayerEntityMixin) player).rpgcrafting$getCraftingTab2Level();
		int craftingTab3AttributeLevel = ((DuckPlayerEntityMixin) player).rpgcrafting$getCraftingTab3Level();
		int craftingTab4AttributeLevel = ((DuckPlayerEntityMixin) player).rpgcrafting$getCraftingTab4Level();

		if (serverConfig.crafting_bench_level_calculation.get() == RPGCrafting.CraftingLevelCalculation.ADDITION) {
			tabLevels[0] = !isolated || initialTab == 1 ? craftingTab1LevelProviders.size() + craftingTab1AttributeLevel : 0;
			tabLevels[1] = !isolated || initialTab == 2 ? craftingTab2LevelProviders.size() + craftingTab2AttributeLevel : 0;
			tabLevels[2] = !isolated || initialTab == 3 ? craftingTab3LevelProviders.size() + craftingTab3AttributeLevel : 0;
			tabLevels[3] = !isolated || initialTab == 4 ? craftingTab4LevelProviders.size() + craftingTab4AttributeLevel : 0;
		} else if (serverConfig.crafting_bench_level_calculation.get() == RPGCrafting.CraftingLevelCalculation.BLOCKS_REQUIRED) {
			tabLevels[0] = !isolated || initialTab == 1 ? (int) Math.clamp(craftingTab1AttributeLevel, 0.0, craftingTab1LevelProviders.size()) : 0;
			tabLevels[1] = !isolated || initialTab == 2 ? (int) Math.clamp(craftingTab2AttributeLevel, 0.0, craftingTab2LevelProviders.size()) : 0;
			tabLevels[2] = !isolated || initialTab == 3 ? (int) Math.clamp(craftingTab3AttributeLevel, 0.0, craftingTab3LevelProviders.size()) : 0;
			tabLevels[3] = !isolated || initialTab == 4 ? (int) Math.clamp(craftingTab4AttributeLevel, 0.0, craftingTab4LevelProviders.size()) : 0;
		} else if (serverConfig.crafting_bench_level_calculation.get() == RPGCrafting.CraftingLevelCalculation.HIGHER_VALUE) {
			tabLevels[0] = !isolated || initialTab == 1 ? Math.max(craftingTab1LevelProviders.size(), craftingTab1AttributeLevel) : 0;
			tabLevels[1] = !isolated || initialTab == 2 ? Math.max(craftingTab2LevelProviders.size(), craftingTab2AttributeLevel) : 0;
			tabLevels[2] = !isolated || initialTab == 3 ? Math.max(craftingTab3LevelProviders.size(), craftingTab3AttributeLevel) : 0;
			tabLevels[3] = !isolated || initialTab == 4 ? Math.max(craftingTab4LevelProviders.size(), craftingTab4AttributeLevel) : 0;
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

		return new CraftingBenchBlockScreenHandlerFactory(initialTab, tabProvidersInReach, storageProvidersInReach, tabLevels, isolated);
	}

	public static class CraftingBenchBlockScreenHandlerFactory implements ExtendedScreenHandlerFactory<CraftingBenchBlockScreenHandler.CraftingBenchBlockData> {

		private final int initialTab;
		private final byte tabProvidersInReach;
		private final byte storageProvidersInReach;
		private final int[] tabLevels;
		private final boolean isolated;

		public CraftingBenchBlockScreenHandlerFactory(int initialTab, byte tabProvidersInReach, byte storageProvidersInReach, int[] tabLevels, boolean isolated) {
			this.initialTab = initialTab;
			this.tabProvidersInReach = tabProvidersInReach;
			this.storageProvidersInReach = storageProvidersInReach;
			this.tabLevels = tabLevels;
			this.isolated = isolated;
		}

		@Override
		public CraftingBenchBlockScreenHandler.CraftingBenchBlockData getScreenOpeningData(ServerPlayerEntity player) {
			return new CraftingBenchBlockScreenHandler.CraftingBenchBlockData(this.initialTab, this.tabProvidersInReach, this.storageProvidersInReach, this.tabLevels, this.isolated);
		}

		@Override
		public Text getDisplayName() {
			return Text.translatable("gui.crafting_bench.default_title");
		}

		@Nullable
		@Override
		public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
			return new CraftingBenchBlockScreenHandler(syncId, playerInventory, player.getEnderChestInventory(), ((DuckPlayerEntityMixin) player).rpgcrafting$getStashInventory(), this.initialTab, this.tabProvidersInReach, this.storageProvidersInReach, this.tabLevels, this.isolated);
		}
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

	public static int removeItemStackIngredientFromInventory(PlayerInventory playerInventory, Inventory inventory, int loopEndIndex, int loopIndexOffset, RPGCraftingRecipe.RPGItemStackIngredient itemStackIngredient, int itemStackIngredientCount) {

		for (int j = 0; j < loopEndIndex; j++) {
			if (RPGCraftingRecipe.checkItemStackIngredient(itemStackIngredient, inventory.getStack(loopIndexOffset + j))) {
				int oldItemStackIngredientCount = itemStackIngredientCount;
				ItemStack itemStack = inventory.getStack(loopIndexOffset + j).copy();
				int stackCount = itemStack.getCount();
				if (stackCount > itemStackIngredientCount) {
					itemStack.setCount(stackCount - itemStackIngredientCount);
					itemStackIngredientCount = 0;
					inventory.setStack(loopIndexOffset + j, itemStack);
				} else {
					itemStackIngredientCount -= stackCount;
					inventory.setStack(loopIndexOffset + j, ItemStack.EMPTY);
				}
				int itemStackIngredientCountDelta = oldItemStackIngredientCount - itemStackIngredientCount;

				Item recipeRemainderItem = itemStack.getItem().getRecipeRemainder();
				if (recipeRemainderItem != null) {
					ItemStack remainderStack = recipeRemainderItem.getDefaultStack();
					remainderStack.setCount(itemStackIngredientCountDelta);
					playerInventory.offerOrDrop(remainderStack);
				}
				if (itemStackIngredientCount <= 0) {
					break;
				}
			}
		}
		return itemStackIngredientCount;
	}

	public static boolean removeRPGIngredientFromInventory(PlayerInventory playerInventory, Inventory inventory, int loopEndIndex, int loopIndexOffset, RPGCraftingRecipe.RPGIngredient rpgIngredient) {

		for (int j = 0; j < loopEndIndex; j++) {
			if (rpgIngredient.ingredient().test(inventory.getStack(loopIndexOffset + j))) {
				ItemStack itemStack = inventory.getStack(loopIndexOffset + j).copy();
				int stackCount = itemStack.getCount();
				if (stackCount >= 1) {
					itemStack.setCount(stackCount - 1);
					inventory.setStack(loopIndexOffset + j, itemStack);
				} else {
					inventory.setStack(loopIndexOffset + j, ItemStack.EMPTY);
				}
				if (itemStack.getItem().hasRecipeRemainder()) {
					playerInventory.offerOrDrop(itemStack.getItem().getRecipeRemainder(itemStack));
				}
				return true;
			}
		}
		return false;
	}

}
