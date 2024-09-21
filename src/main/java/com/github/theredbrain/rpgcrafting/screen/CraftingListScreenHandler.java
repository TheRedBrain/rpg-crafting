package com.github.theredbrain.rpgcrafting.screen;

import com.github.theredbrain.rpgcrafting.data.CraftingRecipe;
import com.github.theredbrain.rpgcrafting.inventory.CraftingListInputInventory;
import com.github.theredbrain.rpgcrafting.registry.CraftingRecipesRegistry;
import com.github.theredbrain.rpgcrafting.registry.GameRulesRegistry;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CraftingListScreenHandler extends ScreenHandler {

    private final Property selectedRecipe = Property.create();
    private final Property shouldScreenCalculateRecipeList = Property.create();
    private final World world;
    private List<Identifier> craftingRecipesIdentifierList;
    private List<Identifier> craftingListRecipesIdentifierList = new ArrayList<>(List.of());
    private final PlayerInventory playerInventory;
    private final CraftingListInputInventory input;

    public CraftingListScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerTypesRegistry.CRAFTING_LIST_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getWorld();
        this.input = new CraftingListInputInventory(this);

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
        // input 36
        this.addSlot(new Slot(this.input, 0, 22, 62));
        this.addProperty(this.selectedRecipe);
        this.selectedRecipe.set(-1);
        this.addProperty(this.shouldScreenCalculateRecipeList);
        this.shouldScreenCalculateRecipeList.set(0);
    }

    public void onContentChanged(Inventory inventory) {
        if (inventory.equals(this.input)) {
            this.shouldScreenCalculateRecipeList.set(1);
            this.selectedRecipe.set(-1);
        }
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
        if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
        }
        return true;
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public PlayerInventory getPlayerInventory() {
        return this.playerInventory;
    }

    public List<Identifier> getCurrentCraftingRecipesList() {
        return this.craftingListRecipesIdentifierList;
    }

    public int shouldScreenCalculateRecipeList() {
        return this.shouldScreenCalculateRecipeList.get();
    }

    public void setShouldScreenCalculateRecipeList(int shouldScreenCalculateRecipeList) {
        this.shouldScreenCalculateRecipeList.set(shouldScreenCalculateRecipeList);
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
            this.craftingListRecipesIdentifierList.clear();

            for (int i = 0; i < this.craftingRecipesIdentifierList.size(); i++) {

                CraftingRecipe craftingRecipe = CraftingRecipesRegistry.registeredCraftingRecipes.get(this.craftingRecipesIdentifierList.get(i));
                unlockAdvancementIdentifier = craftingRecipe.unlockAdvancement();

                AdvancementEntry unlockAdvancementEntry = null;
                if (!unlockAdvancementIdentifier.isEmpty()) {
                    unlockAdvancementEntry = serverAdvancementLoader.get(Identifier.tryParse(unlockAdvancementIdentifier));
                }
                if (!this.world.getGameRules().getBoolean(GameRulesRegistry.SHOW_ALL_RECIPES_IN_CRAFTING_LIST) || unlockAdvancementIdentifier.isEmpty() || (unlockAdvancementEntry != null && playerAdvancementTracker.getProgress(unlockAdvancementEntry).isDone())) {
                    this.craftingListRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
                }
            }
        }
    }
}