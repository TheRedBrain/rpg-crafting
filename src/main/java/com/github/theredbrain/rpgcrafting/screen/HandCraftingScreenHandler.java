package com.github.theredbrain.rpgcrafting.screen;

import com.github.theredbrain.rpgcrafting.data.CraftingRecipe;
import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgcrafting.network.packet.CraftFromCraftingBenchPacket;
import com.github.theredbrain.rpgcrafting.network.packet.CraftFromHandCraftingPacket;
import com.github.theredbrain.rpgcrafting.registry.CraftingRecipesRegistry;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HandCraftingScreenHandler extends ScreenHandler {


//    private BlockPos blockPos;
//    private boolean isStorageTabProviderInReach;
//    private boolean isTab0ProviderInReach;
//    private boolean isTab1ProviderInReach;
//    private boolean isTab2ProviderInReach;
//    private boolean isTab3ProviderInReach;
//    private boolean isStorageArea0ProviderInReach;
//    private boolean isStorageArea1ProviderInReach;
//    private boolean isStorageArea2ProviderInReach;
//    private boolean isStorageArea3ProviderInReach;
//    private boolean isStorageArea4ProviderInReach;
    long lastTakeTime;
    private final Property selectedRecipe = Property.create();
    private final Property shouldScreenCalculateCraftingStatus = Property.create();
    private final World world;
    private List<Identifier> craftingRecipesIdentifierList;
    private List<Identifier> handCraftingRecipesIdentifierList = new ArrayList<>(List.of());
//    private List<Identifier> tab0StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
//    private List<Identifier> tab0SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
//    private List<Identifier> tab1StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
//    private List<Identifier> tab1SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
//    private List<Identifier> tab2StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
//    private List<Identifier> tab2SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
//    private List<Identifier> tab3StandardCraftingRecipesIdentifierList = new ArrayList<>(List.of());
//    private List<Identifier> tab3SpecialCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Ingredient> currentIngredients = new ArrayList<>(List.of());
//    private int[] tabLevels;
//    private int currentTab;
//    private RecipeType currentRecipeType;
    private final PlayerInventory playerInventory;
//    private final EnderChestInventory enderChestInventory;
//    private final SimpleInventory stashInventory;
//
//    public HandCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
//
//        this(syncId, playerInventory, playerInventory.player.getEnderChestInventory(), ((DuckPlayerEntityMixin)playerInventory.player).rpgcrafting$getStashInventory(), data.blockPos, data.initialTab, data.tabProvidersInReach, data.storageProvidersInReach, data.tabLevels);
//    }

    public HandCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerTypesRegistry.HAND_CRAFTING_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getWorld();
//        this.blockPos = blockPos;
//        this.currentTab = initialTab;
//        this.currentRecipeType = RecipeType.STANDARD;
//        this.isStorageTabProviderInReach = (tabProvidersInReach & 1 << 0) != 0;
//        this.isTab0ProviderInReach = (tabProvidersInReach & 1 << 1) != 0;
//        this.isTab1ProviderInReach = (tabProvidersInReach & 1 << 2) != 0;
//        this.isTab2ProviderInReach = (tabProvidersInReach & 1 << 3) != 0;
//        this.isTab3ProviderInReach = (tabProvidersInReach & 1 << 4) != 0;
//        this.isStorageArea0ProviderInReach = (storageProvidersInReach & 1 << 0) != 0;
//        this.isStorageArea1ProviderInReach = (storageProvidersInReach & 1 << 1) != 0;
//        this.isStorageArea2ProviderInReach = (storageProvidersInReach & 1 << 2) != 0;
//        this.isStorageArea3ProviderInReach = (storageProvidersInReach & 1 << 3) != 0;
//        this.isStorageArea4ProviderInReach = (storageProvidersInReach & 1 << 4) != 0;
//        this.tabLevels = tabLevels;
//        this.enderChestInventory = enderChestInventory;
//        this.stashInventory = stashInventory;

        this.craftingRecipesIdentifierList = CraftingRecipesRegistry.registeredCraftingRecipes.keySet().stream().toList(); // TODO rework?

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
//        // stash area 0: 36 - 41
//        for (i = 0; i < 6; ++i) {
//            this.addSlot(new Slot(enderChestInventory, i, 170 + i * 18, 19));
//        }
//        // stash area 1: 42 - 49
//        for (i = 0; i < 2; ++i) {
//            for (int j = 0; j < 4; ++j) {
//                this.addSlot(new Slot(stashInventory, j + i * 4, 206 + j * 18, 42 + i * 18));
//            }
//        }
//        // stash area 2: 50 - 61
//        for (i = 0; i < 3; ++i) {
//            for (int j = 0; j < 4; ++j) {
//                this.addSlot(new Slot(stashInventory, 8 + j + i * 4, 206 + j * 18, 83 + i * 18));
//            }
//        }
//        // stash area 3: 62 - 75
//        for (i = 0; i < 2; ++i) {
//            for (int j = 0; j < 7; ++j) {
//                this.addSlot(new Slot(stashInventory, 20 + j + i * 7, 69 + j * 18, 42 + i * 18));
//            }
//        }
//        // stash area 4: 76 - 96
//        for (i = 0; i < 3; ++i) {
//            for (int j = 0; j < 7; ++j) {
//                this.addSlot(new Slot(enderChestInventory, 6 + j + i * 7, 69 + j * 18, 83 + i * 18));
//            }
//        }
        this.addProperty(this.selectedRecipe);
        this.selectedRecipe.set(-1);
        this.addProperty(this.shouldScreenCalculateCraftingStatus);
        this.shouldScreenCalculateCraftingStatus.set(0);
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
        if (id == -1) {
            if (this.selectedRecipe.get() > -1 && player instanceof ServerPlayerEntity serverPlayerEntity) {
                List<Identifier> currentRecipeList = this.getCurrentCraftingRecipesList();
                ServerPlayNetworking.send(serverPlayerEntity,
                        new CraftFromHandCraftingPacket(
                                currentRecipeList.get(this.selectedRecipe.get()).toString()
                        )
                );
            }
        } else if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
        }
        return true;
    }

//    public BlockPos getBlockPos() {
//        return this.blockPos;
//    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

//    public int[] getTabLevels() {
//        return this.tabLevels;
//    }
//
//    public EnderChestInventory getEnderChestInventory() {
//        return this.enderChestInventory;
//    }
//
//    public SimpleInventory getStashInventory() {
//        return this.stashInventory;
//    }

    public PlayerInventory getPlayerInventory() {
        return this.playerInventory;
    }

//    public boolean isStorageTabProviderInReach() {
//        return this.isStorageTabProviderInReach;
//    }
//
//    public boolean isTab0ProviderInReach() {
//        return this.isTab0ProviderInReach;
//    }
//
//    public boolean isTab1ProviderInReach() {
//        return this.isTab1ProviderInReach;
//    }
//
//    public boolean isTab2ProviderInReach() {
//        return this.isTab2ProviderInReach;
//    }
//
//    public boolean isTab3ProviderInReach() {
//        return this.isTab3ProviderInReach;
//    }
//
//    public boolean isStorageArea0ProviderInReach() {
//        return this.isStorageArea0ProviderInReach;
//    }
//
//    public boolean isStorageArea1ProviderInReach() {
//        return this.isStorageArea1ProviderInReach;
//    }
//
//    public boolean isStorageArea2ProviderInReach() {
//        return this.isStorageArea2ProviderInReach;
//    }
//
//    public boolean isStorageArea3ProviderInReach() {
//        return this.isStorageArea3ProviderInReach;
//    }
//
//    public boolean isStorageArea4ProviderInReach() {
//        return this.isStorageArea4ProviderInReach;
//    }
//
//    public int getCurrentTab() {
//        return this.currentTab;
//    }
//
//    public void setCurrentTab(int currentTab) {
//        this.currentTab = currentTab;
//        this.selectedRecipe.set(-1);
//    }

//    public RecipeType getCurrentRecipeType() {
//        return this.currentRecipeType;
//    }
//
//    public void setCurrentRecipeType(boolean isStandard) {
//        this.currentRecipeType = isStandard ? RecipeType.STANDARD : RecipeType.SPECIAL;
//        this.selectedRecipe.set(-1);
//    }

    public List<Identifier> getCurrentCraftingRecipesList() {
        return this.handCraftingRecipesIdentifierList;
    }

    public int shouldScreenCalculateCraftingStatus() {
        return this.shouldScreenCalculateCraftingStatus.get();
    }

    public void setShouldScreenCalculateCraftingStatus(int shouldScreenCalculateCraftingStatus) {
        this.shouldScreenCalculateCraftingStatus.set(shouldScreenCalculateCraftingStatus);
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < this.getCurrentCraftingRecipesList().size();
    }

    public void calculateUnlockedRecipes() {

        PlayerAdvancementTracker playerAdvancementTracker = null;
        ServerAdvancementLoader serverAdvancementLoader = null;

        if (this.playerInventory.player instanceof ServerPlayerEntity serverPlayerEntity) {
            playerAdvancementTracker = serverPlayerEntity.getAdvancementTracker();
            MinecraftServer minecraftServer = serverPlayerEntity.getServer();
            if (minecraftServer != null) {
                serverAdvancementLoader = minecraftServer.getAdvancementLoader();
            }
        }
        String unlockAdvancementIdentifier;

        if (playerAdvancementTracker != null && serverAdvancementLoader != null) {
            this.handCraftingRecipesIdentifierList.clear();
//            this.tab1StandardCraftingRecipesIdentifierList.clear();
//            this.tab2StandardCraftingRecipesIdentifierList.clear();
//            this.tab3StandardCraftingRecipesIdentifierList.clear();
//            this.tab0SpecialCraftingRecipesIdentifierList.clear();
//            this.tab1SpecialCraftingRecipesIdentifierList.clear();
//            this.tab2SpecialCraftingRecipesIdentifierList.clear();
//            this.tab3SpecialCraftingRecipesIdentifierList.clear();

            for (int i = 0; i < this.craftingRecipesIdentifierList.size(); i++) {

                CraftingRecipe craftingRecipe = CraftingRecipesRegistry.registeredCraftingRecipes.get(this.craftingRecipesIdentifierList.get(i));
                unlockAdvancementIdentifier = craftingRecipe.unlockAdvancement();

                AdvancementEntry unlockAdvancementEntry = null;
                if (!unlockAdvancementIdentifier.isEmpty()) {
                    unlockAdvancementEntry = serverAdvancementLoader.get(Identifier.tryParse(unlockAdvancementIdentifier));
                }
//                boolean bl = ;
//
//                if (bl) {
                    int tab = craftingRecipe.tab();
//                    RecipeType recipeType = craftingRecipe.recipeType();
                    if (tab == 0) {
                        if (!this.world.getGameRules().getBoolean(GameRules.DO_LIMITED_CRAFTING) || unlockAdvancementIdentifier.isEmpty() || (unlockAdvancementEntry != null && playerAdvancementTracker.getProgress(unlockAdvancementEntry).isDone())) {
                            this.handCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
//                        } else {
//                            this.tab0SpecialCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                        }
//                    } else if (tab == 1) {
//                        if (recipeType == RecipeType.STANDARD) {
//                            this.tab1StandardCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
//                        } else {
//                            this.tab1SpecialCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
//                        }
//                    } else if (tab == 2) {
//                        if (recipeType == RecipeType.STANDARD) {
//                            this.tab2StandardCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
//                        } else {
//                            this.tab2SpecialCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
//                        }
//                    } else if (tab == 3) {
//                        if (recipeType == RecipeType.STANDARD) {
//                            this.tab3StandardCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
//                        } else {
//                            this.tab3SpecialCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
//                        }
                    }
                }
            }
        }
    }
