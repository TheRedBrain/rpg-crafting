package com.github.theredbrain.rpgcrafting.screen;

import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
//import com.github.theredbrain.rpgcrafting.network.packet.UpdateCraftingBenchScreenHandlerRecipeListPacket;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.recipe.input.MultipleStackRecipeInput;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
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


    private final BlockPos blockPos;
    private final boolean isStorageTabProviderInReach;
    private final boolean isTab0ProviderInReach;
    private final boolean isTab1ProviderInReach;
    private final boolean isTab2ProviderInReach;
    private final boolean isTab3ProviderInReach;
    private final boolean isStorageArea0ProviderInReach;
    private final boolean isStorageArea1ProviderInReach;
    private final boolean isStorageArea2ProviderInReach;
    private final boolean isStorageArea3ProviderInReach;
    private final boolean isStorageArea4ProviderInReach;
    private final int[] tabLevels;
//    long lastTakeTime;
    private final Property selectedRecipe = Property.create();
    private final Property shouldScreenCalculateCraftingStatus = Property.create();
    private final World world;
    private List<RecipeEntry<RPGCraftingRecipe>> rpgCraftingRecipesList = new ArrayList<>(List.of());
    private List<RecipeEntry<RPGCraftingRecipe>> tab0StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<RecipeEntry<RPGCraftingRecipe>> tab0SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<RecipeEntry<RPGCraftingRecipe>> tab1StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<RecipeEntry<RPGCraftingRecipe>> tab1SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<RecipeEntry<RPGCraftingRecipe>> tab2StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<RecipeEntry<RPGCraftingRecipe>> tab2SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<RecipeEntry<RPGCraftingRecipe>> tab3StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<RecipeEntry<RPGCraftingRecipe>> tab3SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private int currentTab;
    private RecipeType currentRecipeType;
    private final PlayerInventory playerInventory;
    private final EnderChestInventory enderChestInventory;
    private final SimpleInventory stashInventory;

    public CraftingBenchBlockScreenHandler(int syncId, PlayerInventory playerInventory, CraftingBenchBlockData data) {

        this(syncId, playerInventory, playerInventory.player.getEnderChestInventory(), ((DuckPlayerEntityMixin)playerInventory.player).rpgcrafting$getStashInventory(), data.blockPos, data.initialTab, data.tabProvidersInReach, data.storageProvidersInReach, data.tabLevels);
    }

    public CraftingBenchBlockScreenHandler(int syncId, PlayerInventory playerInventory, EnderChestInventory enderChestInventory, SimpleInventory stashInventory, BlockPos blockPos, int  initialTab, byte tabProvidersInReach, byte storageProvidersInReach, int[] tabLevels) {
        super(ScreenHandlerTypesRegistry.CRAFTING_BENCH_BLOCK_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getWorld();
        this.blockPos = blockPos;
        this.currentTab = initialTab;
        this.currentRecipeType = RecipeType.STANDARD;
        this.isStorageTabProviderInReach = (tabProvidersInReach & 1 << 0) != 0;
        this.isTab0ProviderInReach = (tabProvidersInReach & 1 << 1) != 0;
        this.isTab1ProviderInReach = (tabProvidersInReach & 1 << 2) != 0;
        this.isTab2ProviderInReach = (tabProvidersInReach & 1 << 3) != 0;
        this.isTab3ProviderInReach = (tabProvidersInReach & 1 << 4) != 0;
        this.isStorageArea0ProviderInReach = (storageProvidersInReach & 1 << 0) != 0;
        this.isStorageArea1ProviderInReach = (storageProvidersInReach & 1 << 1) != 0;
        this.isStorageArea2ProviderInReach = (storageProvidersInReach & 1 << 2) != 0;
        this.isStorageArea3ProviderInReach = (storageProvidersInReach & 1 << 3) != 0;
        this.isStorageArea4ProviderInReach = (storageProvidersInReach & 1 << 4) != 0;
        this.tabLevels = tabLevels;
        this.enderChestInventory = enderChestInventory;
        this.stashInventory = stashInventory;


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
        this.addProperty(this.selectedRecipe);
        this.selectedRecipe.set(-1);
        this.addProperty(this.shouldScreenCalculateCraftingStatus);
        this.shouldScreenCalculateCraftingStatus.set(0);
        this.populateRecipeLists();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        // TODO
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
//        if (id == -1) {
//            if (this.selectedRecipe.get() > -1 && this.currentTab >= 0 && player instanceof ServerPlayerEntity serverPlayerEntity) {
//                List<RecipeEntry<RPGCraftingRecipe>> currentRecipeList = this.getCurrentCraftingRecipesList();
//
//                RPGCrafting.info("craft: " + currentRecipeList.get(this.selectedRecipe.get()).id().toString());
////                ServerPlayNetworking.send(playerEntity,
////                        new CraftFromCraftingBenchPacket(
////                                currentRecipeList.get(this.selectedRecipe.get()).id().toString(),
////                                ((DuckPlayerEntityMixin)player).rpgcrafting$useStashForCrafting()
////                        )
////                );
//                this.craftFromWorkBench(serverPlayerEntity, currentRecipeList.get(this.selectedRecipe.get()), this.getCraftingInputInventory(((DuckPlayerEntityMixin) this.getPlayerInventory().player).rpgcrafting$useStashForCrafting()));
//            }
//        } else
//        if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
//        }
        return true;
    }

//    public void onCraftButtonClick(PlayerEntity player, RecipeEntry<RPGCraftingRecipe> recipeEntry) {
//        if ()
//    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public void setSelectedRecipe(int newSelectedRecipe) {
        this.selectedRecipe.set(newSelectedRecipe);
    }

    public int[] getTabLevels() {
        return this.tabLevels;
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

    public boolean isStorageTabProviderInReach() {
        return this.isStorageTabProviderInReach;
    }

    public boolean isTab0ProviderInReach() {
        return this.isTab0ProviderInReach;
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

    public List<RecipeEntry<RPGCraftingRecipe>> getCurrentCraftingRecipesList() {
        return this.currentTab == 3 ? (this.currentRecipeType == RecipeType.SPECIAL ? this.tab3SpecialCraftingRecipesIdentifierList : this.tab3StandardCraftingRecipesIdentifierList) : this.currentTab == 2 ? (this.currentRecipeType == RecipeType.SPECIAL ? this.tab2SpecialCraftingRecipesIdentifierList : this.tab2StandardCraftingRecipesIdentifierList) : this.currentTab == 1 ? (this.currentRecipeType == RecipeType.SPECIAL ? this.tab1SpecialCraftingRecipesIdentifierList : this.tab1StandardCraftingRecipesIdentifierList) : this.currentTab == 0 ? (this.currentRecipeType == RecipeType.SPECIAL ? this.tab0SpecialCraftingRecipesIdentifierList : this.tab0StandardCraftingRecipesIdentifierList) : new ArrayList<>();
    }

    public int shouldScreenCalculateCraftingStatus() {
        return this.shouldScreenCalculateCraftingStatus.get();
    }

    public void setShouldScreenCalculateCraftingStatus(int shouldScreenCalculateCraftingStatus) {
        this.shouldScreenCalculateCraftingStatus.set(shouldScreenCalculateCraftingStatus);
    }

//    public boolean isInBounds(int id) {
//        if (this.currentTab >= 0) {
//            boolean bl = id < this.getCurrentCraftingRecipesList().size();
//            return id >= 0 && bl;
//        }
//        return false;
//    }

    public MultipleStackRecipeInput getCraftingInputInventory(boolean useStashForCrafting) {

        int playerInventorySize = 27; // TODO inventory size attributes

        int playerHotbarSize = 9; // TODO inventory size attributes

        int stash0InventorySize = useStashForCrafting && this.isStorageArea0ProviderInReach ? 6 : 0;

        int stash1InventorySize = useStashForCrafting && this.isStorageArea1ProviderInReach ? 8 : 0;

        int stash2InventorySize = useStashForCrafting && this.isStorageArea2ProviderInReach ? 12 : 0;

        int stash3InventorySize = useStashForCrafting && this.isStorageArea3ProviderInReach ? 14 : 0;

        int stash4InventorySize = useStashForCrafting && this.isStorageArea4ProviderInReach ? 21 : 0;

        SimpleInventory craftingInputInventory = new SimpleInventory(
                playerInventorySize
                        + playerHotbarSize
                        + stash0InventorySize
                        + stash1InventorySize
                        + stash2InventorySize
                        + stash3InventorySize
                        + stash4InventorySize
        );

        int k = 0;
        int j;
        for (j = 0; j < playerInventorySize; j++) { // TODO inventory size attributes
            craftingInputInventory.setStack(k + j, this.getPlayerInventory().getStack(j).copy());
        }
        k = k + playerInventorySize;

        for (j = 0; j < stash0InventorySize; j++) {
            craftingInputInventory.setStack(k + j, this.getEnderChestInventory().getStack(j).copy());
        }

        for (j = 0; j < stash1InventorySize; j++) {
            craftingInputInventory.setStack(k + j, this.getStashInventory().getStack(j).copy());
        }

        for (j = 0; j < stash2InventorySize; j++) {
            craftingInputInventory.setStack(k + j, this.getStashInventory().getStack(8 + j).copy());
        }

        for (j = 0; j < stash3InventorySize; j++) {
            craftingInputInventory.setStack(k + j, this.getStashInventory().getStack(20 + j).copy());
        }

        for (j = 0; j < stash4InventorySize; j++) {
            craftingInputInventory.setStack(k + j, this.getEnderChestInventory().getStack(6 + j).copy());
        }

        return new MultipleStackRecipeInput(craftingInputInventory.getHeldStacks(), craftingInputInventory.size());
    }

//    public boolean matchRPGCraftingRecipe() {
//        World world = this.getPlayerInventory().player.getWorld();
//        List<RecipeEntry<RPGCraftingRecipe>> activeRecipeList = this.getCurrentCraftingRecipesList();
//        int selectedRecipe = this.getSelectedRecipe();
//        if (selectedRecipe >= 0) {
//            RecipeEntry<RPGCraftingRecipe> craftingRecipeEntry = activeRecipeList.get(selectedRecipe);
//            return craftingRecipeEntry.value().matches(this.getCraftingInputInventory(((DuckPlayerEntityMixin) this.getPlayerInventory().player).rpgcrafting$useStashForCrafting()), world);
//        }
//        return false;
//    }

    public void updateRPGCraftingRecipesList() {
        this.rpgCraftingRecipesList.clear();
        List<RecipeEntry<RPGCraftingRecipe>> newRecipeEntryList = this.world.getRecipeManager().listAllOfType(RPGCraftingRecipe.Type.INSTANCE);
        this.rpgCraftingRecipesList.addAll(newRecipeEntryList);
    }

//    public void syncRPGCraftingRecipesList(List<Identifier> rpgCraftingRecipeIdentifierList) {
//        if (this.world.isClient) {
//            RPGCrafting.info("syncRecipeList");
//            this.rpgCraftingRecipesList.clear();
//            for (Identifier identifier : rpgCraftingRecipeIdentifierList) {
//                Optional<RecipeEntry<?>> optional = this.world.getRecipeManager().get(identifier);
//                if (optional.isPresent()) {
//                    RecipeEntry<RPGCraftingRecipe> recipeEntry = (RecipeEntry<RPGCraftingRecipe>) optional.get();
//                    this.rpgCraftingRecipesList.add(recipeEntry);
//                }
//            }
//        }
//    }

    public void populateRecipeLists() {
//        if (this.playerInventory.player instanceof ServerPlayerEntity playerEntity) {
            this.tab0StandardCraftingRecipesIdentifierList.clear();
            this.tab1StandardCraftingRecipesIdentifierList.clear();
            this.tab2StandardCraftingRecipesIdentifierList.clear();
            this.tab3StandardCraftingRecipesIdentifierList.clear();
            this.tab0SpecialCraftingRecipesIdentifierList.clear();
            this.tab1SpecialCraftingRecipesIdentifierList.clear();
            this.tab2SpecialCraftingRecipesIdentifierList.clear();
            this.tab3SpecialCraftingRecipesIdentifierList.clear();

            for (RecipeEntry<RPGCraftingRecipe> rpgCraftingRecipeEntry : this.rpgCraftingRecipesList) {

                RPGCraftingRecipe rpgCraftingRecipe = rpgCraftingRecipeEntry.value();
//                if (rpgCraftingRecipe.isUnlocked(playerEntity, this.world)) {
                // TODO recipe levels
                    int tab = rpgCraftingRecipe.tab;
                    RecipeType recipeType = RecipeType.valueOf(rpgCraftingRecipe.recipeType);
                    if (tab == 0) {
                        if (recipeType == RecipeType.STANDARD) {
                            this.tab0StandardCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
                        } else {
                            this.tab0SpecialCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
                        }
                    } else if (tab == 1) {
                        if (recipeType == RecipeType.STANDARD) {
                            this.tab1StandardCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
                        } else {
                            this.tab1SpecialCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
                        }
                    } else if (tab == 2) {
                        if (recipeType == RecipeType.STANDARD) {
                            this.tab2StandardCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
                        } else {
                            this.tab2SpecialCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
                        }
                    } else if (tab == 3) {
                        if (recipeType == RecipeType.STANDARD) {
                            this.tab3StandardCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
                        } else {
                            this.tab3SpecialCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
                        }
                    }
                }
//            }
//            List<Identifier> rpgCraftingRecipeIdentifierList = new ArrayList<>();
//            for (RecipeEntry<RPGCraftingRecipe> recipeEntry : this.rpgCraftingRecipesList) {
//                rpgCraftingRecipeIdentifierList.add(recipeEntry.id());
//            }
//            ServerPlayNetworking.send(
//                    playerEntity,
//                    new UpdateCraftingBenchScreenHandlerRecipeListPacket(
//                            rpgCraftingRecipeIdentifierList
//                    )
//            );

//        }
    }

//    private boolean isRPGCraftingRecipeUnlocked(RecipeEntry<RPGCraftingRecipe> rpgCraftingRecipeEntry, ServerPlayerEntity playerEntity) {
//        boolean bl = true;
//
//        PlayerAdvancementTracker playerAdvancementTracker = null;
//        ServerAdvancementLoader serverAdvancementLoader = null;
//
//        playerAdvancementTracker = playerEntity.getAdvancementTracker();
//        MinecraftServer minecraftServer = playerEntity.getServer();
//        if (minecraftServer != null) {
//            serverAdvancementLoader = minecraftServer.getAdvancementLoader();
//        }
//        String unlockAdvancementIdentifier;
//
//        if (playerAdvancementTracker != null && serverAdvancementLoader != null) {
//            unlockAdvancementIdentifier = rpgCraftingRecipeEntry.value().unlockAdvancement;
//
//            AdvancementEntry unlockAdvancementEntry = null;
//            if (!unlockAdvancementIdentifier.isEmpty()) {
//                unlockAdvancementEntry = serverAdvancementLoader.get(Identifier.tryParse(unlockAdvancementIdentifier));
//            }
//            bl = !this.world.getGameRules().getBoolean(GameRules.DO_LIMITED_CRAFTING) || unlockAdvancementIdentifier.isEmpty() || (unlockAdvancementEntry != null && playerAdvancementTracker.getProgress(unlockAdvancementEntry).isDone());
//
//        }
//        return bl;
//    }

//    private void craftFromWorkBench(ServerPlayerEntity serverPlayerEntity, RecipeEntry<RPGCraftingRecipe> rpgCraftingRecipeEntry, MultipleStackRecipeInput multipleStackRecipeInput) {
//
//        boolean useStorageInventory = ((DuckPlayerEntityMixin) serverPlayerEntity).rpgcrafting$useStashForCrafting();
//        RPGCraftingRecipe craftingRecipe = rpgCraftingRecipeEntry.value();
//
//            int playerInventorySize = this.getPlayerInventory().size();
//
//            int stash0InventorySize = useStorageInventory && this.isStorageArea0ProviderInReach() ? 6 : 0;
//
//            int stash1InventorySize = useStorageInventory && this.isStorageArea1ProviderInReach() ? 8 : 0;
//
//            int stash2InventorySize = useStorageInventory && this.isStorageArea2ProviderInReach() ? 12 : 0;
//
//            int stash3InventorySize = useStorageInventory && this.isStorageArea3ProviderInReach() ? 14 : 0;
//
//            int stash4InventorySize = useStorageInventory && this.isStorageArea4ProviderInReach() ? 21 : 0;
//
//            ItemStack itemStack;
//
//            for (Ingredient ingredient : craftingRecipe.ingredients) {
//
//                int j;
//
//                // TODO play test which inventory normally contains the most crafting ingredients and should be checked first
//
//                for (j = 0; j < playerInventorySize; j++) {
//                    if (ingredient.test(this.getPlayerInventory().getStack(j))) {
//                        itemStack = this.getPlayerInventory().getStack(j).copy();
//                        int stackCount = itemStack.getCount();
//                        if (stackCount >= 1) {
//                            itemStack.setCount(stackCount - 1);
//                            this.getPlayerInventory().setStack(j, itemStack);
//						} else {
//                            this.getPlayerInventory().setStack(j, ItemStack.EMPTY);
//						}
//						break;
//					}
//                }
//
//                for (j = 0; j < stash0InventorySize; j++) {
//                    if (ingredient.test(this.getEnderChestInventory().getStack(j))) {
//                        itemStack = this.getEnderChestInventory().getStack(j).copy();
//                        int stackCount = itemStack.getCount();
//                        if (stackCount >= 1) {
//                            itemStack.setCount(stackCount - 1);
//                            this.getEnderChestInventory().setStack(j, itemStack);
//						} else {
//                            this.getEnderChestInventory().setStack(j, ItemStack.EMPTY);
//						}
//						break;
//					}
//                }
//
//                for (j = 0; j < stash1InventorySize; j++) {
//                    if (ingredient.test(this.getStashInventory().getStack(j))) {
//                        itemStack = this.getStashInventory().getStack(j).copy();
//                        int stackCount = itemStack.getCount();
//                        if (stackCount >= 1) {
//                            itemStack.setCount(stackCount - 1);
//                            this.getStashInventory().setStack(j, itemStack);
//						} else {
//                            this.getStashInventory().setStack(j, ItemStack.EMPTY);
//						}
//						break;
//					}
//                }
//
//                for (j = 0; j < stash2InventorySize; j++) {
//                    if (ingredient.test(this.getStashInventory().getStack(8 + j))) {
//                        itemStack = this.getStashInventory().getStack(8 + j).copy();
//                        int stackCount = itemStack.getCount();
//                        if (stackCount >= 1) {
//                            itemStack.setCount(stackCount - 1);
//                            this.getStashInventory().setStack(8 + j, itemStack);
//						} else {
//                            this.getStashInventory().setStack(8 + j, ItemStack.EMPTY);
//						}
//						break;
//					}
//                }
//
//                for (j = 0; j < stash3InventorySize; j++) {
//                    if (ingredient.test(this.getStashInventory().getStack(20 + j))) {
//                        itemStack = this.getStashInventory().getStack(20 + j).copy();
//                        int stackCount = itemStack.getCount();
//                        if (stackCount >= 1) {
//                            itemStack.setCount(stackCount - 1);
//                            this.getStashInventory().setStack(20 + j, itemStack);
//						} else {
//                            this.getStashInventory().setStack(20 + j, ItemStack.EMPTY);
//						}
//						break;
//					}
//                }
//
//                for (j = 0; j < stash4InventorySize; j++) {
//                    if (ingredient.test(this.getEnderChestInventory().getStack(6 + j))) {
//                        itemStack = this.getEnderChestInventory().getStack(6 + j).copy();
//                        int stackCount = itemStack.getCount();
//                        if (stackCount >= 1) {
//                            itemStack.setCount(stackCount - 1);
//                            this.getEnderChestInventory().setStack(6 + j, itemStack);
//						} else {
//                            this.getEnderChestInventory().setStack(6 + j, ItemStack.EMPTY);
//						}
//						break;
//					}
//                }
//            }
//            serverPlayerEntity.getInventory().offerOrDrop(craftingRecipe.craft(multipleStackRecipeInput, world.getRegistryManager()));
//            this.updateRPGCraftingRecipesList();
//            this.calculateUnlockedRecipes();
//            this.setShouldScreenCalculateCraftingStatus(1);
//    }

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
