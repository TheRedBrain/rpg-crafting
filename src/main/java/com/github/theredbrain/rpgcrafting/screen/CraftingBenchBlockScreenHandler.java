package com.github.theredbrain.rpgcrafting.screen;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.recipe.input.MultipleStackRecipeInput;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpgcrafting.screen.slot.RPGCraftingResultSlot;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import com.mojang.serialization.Codec;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CraftingBenchBlockScreenHandler extends ScreenHandler {

	private static final int TAB_1 = 1;
	private static final int TAB_2 = 2;
	private static final int TAB_3 = 3;
	private static final int TAB_4 = 4;
	private final BlockPos blockPos;
	private final boolean isStorageTabProviderInReach;
	private final boolean isTab1ProviderInReach;
	private final boolean isTab2ProviderInReach;
	private final boolean isTab3ProviderInReach;
	private final boolean isTab4ProviderInReach;
	private final boolean isStorageArea0ProviderInReach;
	private final boolean isStorageArea1ProviderInReach;
	private final boolean isStorageArea2ProviderInReach;
	private final boolean isStorageArea3ProviderInReach;
	private final boolean isStorageArea4ProviderInReach;
	private final int[] tabLevels;
	private final Property selectedRecipe = Property.create();
	private final Property shouldScreenCalculateCraftingStatus = Property.create();
	private final World world;
	private List<RecipeEntry<RPGCraftingRecipe>> rpgCraftingRecipesList = new ArrayList<>(List.of());
	private List<RecipeEntry<RPGCraftingRecipe>> tab1StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
	private List<RecipeEntry<RPGCraftingRecipe>> tab1SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
	private List<RecipeEntry<RPGCraftingRecipe>> tab2StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
	private List<RecipeEntry<RPGCraftingRecipe>> tab2SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
	private List<RecipeEntry<RPGCraftingRecipe>> tab3StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
	private List<RecipeEntry<RPGCraftingRecipe>> tab3SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
	private List<RecipeEntry<RPGCraftingRecipe>> tab4StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
	private List<RecipeEntry<RPGCraftingRecipe>> tab4SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
	private int currentTab;
	private RecipeType currentRecipeType;
	private final PlayerInventory playerInventory;
	private final EnderChestInventory enderChestInventory;
	private final SimpleInventory stashInventory;
	private final SimpleInventory craftingResultInventory;
	private final SimpleInventory craftingResultIngredientsInventory;

	public CraftingBenchBlockScreenHandler(int syncId, PlayerInventory playerInventory, CraftingBenchBlockData data) {

		this(syncId, playerInventory, playerInventory.player.getEnderChestInventory(), ((DuckPlayerEntityMixin) playerInventory.player).rpgcrafting$getStashInventory(), data.blockPos, data.initialTab, data.tabProvidersInReach, data.storageProvidersInReach, data.tabLevels);
	}

	public CraftingBenchBlockScreenHandler(int syncId, PlayerInventory playerInventory, EnderChestInventory enderChestInventory, SimpleInventory stashInventory, BlockPos blockPos, int initialTab, byte tabProvidersInReach, byte storageProvidersInReach, int[] tabLevels) {
		super(ScreenHandlerTypesRegistry.CRAFTING_BENCH_BLOCK_SCREEN_HANDLER, syncId);
		this.playerInventory = playerInventory;
		this.world = playerInventory.player.getWorld();
		this.blockPos = blockPos;
		this.currentTab = initialTab;
		this.currentRecipeType = RecipeType.STANDARD;
		this.isStorageTabProviderInReach = (tabProvidersInReach & 1 << 0) != 0;
		this.isTab1ProviderInReach = (tabProvidersInReach & 1 << 1) != 0;
		this.isTab2ProviderInReach = (tabProvidersInReach & 1 << 2) != 0;
		this.isTab3ProviderInReach = (tabProvidersInReach & 1 << 3) != 0;
		this.isTab4ProviderInReach = (tabProvidersInReach & 1 << 4) != 0;
		this.isStorageArea0ProviderInReach = (storageProvidersInReach & 1 << 0) != 0;
		this.isStorageArea1ProviderInReach = (storageProvidersInReach & 1 << 1) != 0;
		this.isStorageArea2ProviderInReach = (storageProvidersInReach & 1 << 2) != 0;
		this.isStorageArea3ProviderInReach = (storageProvidersInReach & 1 << 3) != 0;
		this.isStorageArea4ProviderInReach = (storageProvidersInReach & 1 << 4) != 0;
		this.tabLevels = tabLevels;
		this.enderChestInventory = enderChestInventory;
		this.stashInventory = stashInventory;
		this.craftingResultInventory = new SimpleInventory(1);
		this.craftingResultIngredientsInventory = new SimpleInventory(4);


		this.updateRPGCraftingRecipesList();

		int i;
		// hotbar 0 - 8
		for (i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 62 + i * 18, 209));
		}
		// main inventory 9 - 35
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + (i + 1) * 9, 62 + j * 18, 151 + i * 18));
			}
		}
		// stash area 0: 36 - 41
		for (i = 0; i < 6; ++i) {
			this.addSlot(new Slot(enderChestInventory, i, 170 + i * 18, 19));
		}
		// stash area 1: 42 - 49
		for (i = 0; i < 2; ++i) {
			for (int j = 0; j < 4; ++j) {
				this.addSlot(new Slot(stashInventory, j + i * 4, 206 + j * 18, 42 + i * 18));
			}
		}
		// stash area 2: 50 - 61
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 4; ++j) {
				this.addSlot(new Slot(stashInventory, 8 + j + i * 4, 206 + j * 18, 83 + i * 18));
			}
		}
		// stash area 3: 62 - 75
		for (i = 0; i < 2; ++i) {
			for (int j = 0; j < 7; ++j) {
				this.addSlot(new Slot(stashInventory, 20 + j + i * 7, 69 + j * 18, 42 + i * 18));
			}
		}
		// stash area 4: 76 - 96
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 7; ++j) {
				this.addSlot(new Slot(enderChestInventory, 6 + j + i * 7, 69 + j * 18, 83 + i * 18));
			}
		}

		// crafting result slots 97 - 101
		this.addSlot(new RPGCraftingResultSlot(this.craftingResultInventory, 0, 135, 22));
		for (i = 0; i < 4; ++i) {
			this.addSlot(new RPGCraftingResultSlot(this.craftingResultIngredientsInventory, i, 135 + (i * 18), 92));
		}

		// Inventory Size Attributes compatibility
		int activeHotbarSize = RPGCrafting.getActiveHotbarSize(playerInventory.player);
		int activeInventorySize = RPGCrafting.getActiveInventorySize(playerInventory.player);
		for (i = 0; i < 9; i++) {
			((SlotCustomization) this.slots.get(i)).slotcustomizationapi$setDisabledOverride(i >= activeHotbarSize);
		}
		for (i = 9; i < 36; i++) {
			((SlotCustomization) this.slots.get(i)).slotcustomizationapi$setDisabledOverride(i >= 9 + activeInventorySize);
		}

		this.addProperty(this.selectedRecipe);
		this.selectedRecipe.set(-1);
		this.addProperty(this.shouldScreenCalculateCraftingStatus);
		this.shouldScreenCalculateCraftingStatus.set(0);
		this.populateRecipeLists();
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot2 = this.slots.get(slot);
		if (slot2 != null && slot2.hasStack()) {
			ItemStack itemStack2 = slot2.getStack();
			itemStack = itemStack2.copy();
			if (slot < 36) {
				boolean bl = true;
				if (this.isStorageArea0ProviderInReach) {
					if (this.insertItem(itemStack2, 36, 42, false)) {
						bl = false;
					}
				}
				if (this.isStorageArea1ProviderInReach) {
					if (this.insertItem(itemStack2, 42, 50, false)) {
						bl = false;
					}
				}
				if (this.isStorageArea2ProviderInReach) {
					if (this.insertItem(itemStack2, 50, 62, false)) {
						bl = false;
					}
				}
				if (this.isStorageArea3ProviderInReach) {
					if (this.insertItem(itemStack2, 62, 76, false)) {
						bl = false;
					}
				}
				if (this.isStorageArea4ProviderInReach) {
					if (this.insertItem(itemStack2, 76, 97, false)) {
						bl = false;
					}
				}
				if (bl) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 0, 36, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot2.setStack(ItemStack.EMPTY);
			} else {
				slot2.markDirty();
			}
		}

		return itemStack;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		this.selectedRecipe.set(id);
		return true;
	}

	//region getter/setter

	public int[] getTabLevels() {
		return tabLevels;
	}

	public int getSelectedRecipe() {
		return this.selectedRecipe.get();
	}

	public void setSelectedRecipe(int newSelectedRecipe) {
		this.selectedRecipe.set(newSelectedRecipe);
	}

	public EnderChestInventory getEnderChestInventory() {
		return this.enderChestInventory;
	}

	public SimpleInventory getStashInventory() {
		return this.stashInventory;
	}

	public PlayerInventory getPlayerInventory() {
		return this.playerInventory;
	}

	public SimpleInventory getCraftingResultInventory() {
		return this.craftingResultInventory;
	}

	public SimpleInventory getCraftingResultIngredientsInventory() {
		return this.craftingResultIngredientsInventory;
	}

	public boolean isStorageTabProviderInReach() {
		return this.isStorageTabProviderInReach;
	}

	public boolean isTab1ProviderInReach() {
		return this.isTab1ProviderInReach;
	}

	public boolean isTab2ProviderInReach() {
		return this.isTab2ProviderInReach;
	}

	public boolean isTab3ProviderInReach() {
		return this.isTab3ProviderInReach;
	}

	public boolean isTab4ProviderInReach() {
		return this.isTab4ProviderInReach;
	}

	public boolean isStorageArea0ProviderInReach() {
		return this.isStorageArea0ProviderInReach;
	}

	public boolean isStorageArea1ProviderInReach() {
		return this.isStorageArea1ProviderInReach;
	}

	public boolean isStorageArea2ProviderInReach() {
		return this.isStorageArea2ProviderInReach;
	}

	public boolean isStorageArea3ProviderInReach() {
		return this.isStorageArea3ProviderInReach;
	}

	public boolean isStorageArea4ProviderInReach() {
		return this.isStorageArea4ProviderInReach;
	}

	public int getCurrentTab() {
		return this.currentTab;
	}

	public void setCurrentTab(int currentTab) {
		this.currentTab = currentTab;
		this.selectedRecipe.set(-1);
	}

	public RecipeType getCurrentRecipeType() {
		return this.currentRecipeType;
	}

	public void setCurrentRecipeType(boolean isStandard) {
		this.currentRecipeType = isStandard ? RecipeType.STANDARD : RecipeType.SPECIAL;
		this.selectedRecipe.set(-1);
	}
	//endregion getter/setter

	public List<RecipeEntry<RPGCraftingRecipe>> getCurrentCraftingRecipesList() {
		return this.currentTab == TAB_4 ? (this.currentRecipeType == RecipeType.SPECIAL ? this.tab4SpecialCraftingRecipesIdentifierList : this.tab4StandardCraftingRecipesIdentifierList) : this.currentTab == TAB_3 ? (this.currentRecipeType == RecipeType.SPECIAL ? this.tab3SpecialCraftingRecipesIdentifierList : this.tab3StandardCraftingRecipesIdentifierList) : this.currentTab == TAB_2 ? (this.currentRecipeType == RecipeType.SPECIAL ? this.tab2SpecialCraftingRecipesIdentifierList : this.tab2StandardCraftingRecipesIdentifierList) : this.currentTab == TAB_1 ? (this.currentRecipeType == RecipeType.SPECIAL ? this.tab1SpecialCraftingRecipesIdentifierList : this.tab1StandardCraftingRecipesIdentifierList) : new ArrayList<>();
	}

	public int shouldScreenCalculateCraftingStatus() {
		return this.shouldScreenCalculateCraftingStatus.get();
	}

	public void setShouldScreenCalculateCraftingStatus(int shouldScreenCalculateCraftingStatus) {
		this.shouldScreenCalculateCraftingStatus.set(shouldScreenCalculateCraftingStatus);
	}

	public MultipleStackRecipeInput getCraftingInputInventory(boolean useStashForCrafting) {

		int playerHotbarSize = RPGCrafting.getActiveHotbarSize(playerInventory.player);

		int playerInventorySize = RPGCrafting.getActiveInventorySize(playerInventory.player);

		int stash0InventorySize = useStashForCrafting && this.isStorageArea0ProviderInReach ? 6 : 0;

		int stash1InventorySize = useStashForCrafting && this.isStorageArea1ProviderInReach ? 8 : 0;

		int stash2InventorySize = useStashForCrafting && this.isStorageArea2ProviderInReach ? 12 : 0;

		int stash3InventorySize = useStashForCrafting && this.isStorageArea3ProviderInReach ? 14 : 0;

		int stash4InventorySize = useStashForCrafting && this.isStorageArea4ProviderInReach ? 21 : 0;

		SimpleInventory craftingInputInventory = new SimpleInventory(
				playerHotbarSize
						+ playerInventorySize
						+ stash0InventorySize
						+ stash1InventorySize
						+ stash2InventorySize
						+ stash3InventorySize
						+ stash4InventorySize
		);

		int k = 0;
		int j;
		for (j = 0; j < playerHotbarSize; j++) {
			craftingInputInventory.setStack(k + j, this.getPlayerInventory().getStack(j).copy());
		}
		k = k + playerHotbarSize;

		for (j = 0; j < playerInventorySize; j++) {
			craftingInputInventory.setStack(k + j, this.getPlayerInventory().getStack(9 + j).copy());
		}
		k = k + playerInventorySize;

		for (j = 0; j < stash0InventorySize; j++) {
			craftingInputInventory.setStack(k + j, this.getEnderChestInventory().getStack(j).copy());
		}
		k = k + stash0InventorySize;

		for (j = 0; j < stash1InventorySize; j++) {
			craftingInputInventory.setStack(k + j, this.getStashInventory().getStack(j).copy());
		}
		k = k + stash1InventorySize;

		for (j = 0; j < stash2InventorySize; j++) {
			craftingInputInventory.setStack(k + j, this.getStashInventory().getStack(8 + j).copy());
		}
		k = k + stash2InventorySize;

		for (j = 0; j < stash3InventorySize; j++) {
			craftingInputInventory.setStack(k + j, this.getStashInventory().getStack(20 + j).copy());
		}
		k = k + stash3InventorySize;

		for (j = 0; j < stash4InventorySize; j++) {
			craftingInputInventory.setStack(k + j, this.getEnderChestInventory().getStack(6 + j).copy());
		}

		return new MultipleStackRecipeInput(craftingInputInventory.getHeldStacks(), craftingInputInventory.size());
	}

	public void updateRPGCraftingRecipesList() {
		this.rpgCraftingRecipesList.clear();
		List<RecipeEntry<RPGCraftingRecipe>> newRecipeEntryList = this.world.getRecipeManager().listAllOfType(RPGCraftingRecipe.Type.INSTANCE);
		this.rpgCraftingRecipesList.addAll(newRecipeEntryList);
	}

	public void populateRecipeLists() {
		this.tab1StandardCraftingRecipesIdentifierList.clear();
		this.tab2StandardCraftingRecipesIdentifierList.clear();
		this.tab3StandardCraftingRecipesIdentifierList.clear();
		this.tab4StandardCraftingRecipesIdentifierList.clear();
		this.tab1SpecialCraftingRecipesIdentifierList.clear();
		this.tab2SpecialCraftingRecipesIdentifierList.clear();
		this.tab3SpecialCraftingRecipesIdentifierList.clear();
		this.tab4SpecialCraftingRecipesIdentifierList.clear();

		for (RecipeEntry<RPGCraftingRecipe> rpgCraftingRecipeEntry : this.rpgCraftingRecipesList) {

			RPGCraftingRecipe rpgCraftingRecipe = rpgCraftingRecipeEntry.value();
			int tab = rpgCraftingRecipe.tab;
			int level = rpgCraftingRecipe.level;
			RecipeType recipeType = RecipeType.valueOf(rpgCraftingRecipe.recipeType);
			if (tab == TAB_1 && this.tabLevels[0] >= level) {
				if (recipeType == RecipeType.STANDARD) {
					this.tab1StandardCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
				} else if (rpgCraftingRecipeEntry.value().matches(this.getCraftingInputInventory(((DuckPlayerEntityMixin) this.getPlayerInventory().player).rpgcrafting$useStashForCrafting()), world) || RPGCrafting.serverConfig.show_all_unlocked_special_recipes) {
					this.tab1SpecialCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
				}
			} else if (tab == TAB_2 && this.tabLevels[1] >= level) {
				if (recipeType == RecipeType.STANDARD) {
					this.tab2StandardCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
				} else if (rpgCraftingRecipeEntry.value().matches(this.getCraftingInputInventory(((DuckPlayerEntityMixin) this.getPlayerInventory().player).rpgcrafting$useStashForCrafting()), world) || RPGCrafting.serverConfig.show_all_unlocked_special_recipes) {
					this.tab2SpecialCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
				}
			} else if (tab == TAB_3 && this.tabLevels[2] >= level) {
				if (recipeType == RecipeType.STANDARD) {
					this.tab3StandardCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
				} else if (rpgCraftingRecipeEntry.value().matches(this.getCraftingInputInventory(((DuckPlayerEntityMixin) this.getPlayerInventory().player).rpgcrafting$useStashForCrafting()), world) || RPGCrafting.serverConfig.show_all_unlocked_special_recipes) {
					this.tab3SpecialCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
				}
			} else if (tab == TAB_4 && this.tabLevels[3] >= level) {
				if (recipeType == RecipeType.STANDARD) {
					this.tab4StandardCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
				} else if (rpgCraftingRecipeEntry.value().matches(this.getCraftingInputInventory(((DuckPlayerEntityMixin) this.getPlayerInventory().player).rpgcrafting$useStashForCrafting()), world) || RPGCrafting.serverConfig.show_all_unlocked_special_recipes) {
					this.tab4SpecialCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
				}
			}
		}
	}

	public record CraftingBenchBlockData(
			BlockPos blockPos,
			int initialTab,
			byte tabProvidersInReach,
			byte storageProvidersInReach,
			int[] tabLevels
	) {

		public static final PacketCodec<RegistryByteBuf, CraftingBenchBlockData> PACKET_CODEC = PacketCodec.of(CraftingBenchBlockData::write, CraftingBenchBlockData::new);

		public CraftingBenchBlockData(RegistryByteBuf registryByteBuf) {
			this(registryByteBuf.readBlockPos(), registryByteBuf.readInt(), registryByteBuf.readByte(), registryByteBuf.readByte(), registryByteBuf.readIntArray());
		}

		private void write(RegistryByteBuf registryByteBuf) {
			registryByteBuf.writeBlockPos(blockPos);
			registryByteBuf.writeInt(initialTab);
			registryByteBuf.writeByte(tabProvidersInReach);
			registryByteBuf.writeByte(storageProvidersInReach);
			registryByteBuf.writeIntArray(tabLevels);
		}
	}

	public static enum RecipeType implements StringIdentifiable {
		STANDARD("standard"),
		SPECIAL("special");

		private final String name;

		private RecipeType(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return this.name;
		}

		public static final Codec<RecipeType> CODEC = Codec.STRING.xmap(RecipeType::valueOf, Enum::name);

		public static Optional<RecipeType> byName(String name) {
			return Arrays.stream(RecipeType.values()).filter(recipeType -> recipeType.asString().equals(name)).findFirst();
		}

		public Text asText() {
			return Text.translatable("gui.crafting_bench_block.recipeType." + this.name);
		}
	}
}
