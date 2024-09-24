package com.github.theredbrain.rpgcrafting.screen;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.inventory.RecipeListInputInventory;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.recipe.input.MultipleStackRecipeInput;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RecipeListScreenHandler extends ScreenHandler {

    private final Property selectedRecipe = Property.create();
    private final Property shouldScreenCalculateRecipeList = Property.create();
    private final World world;
    private List<RecipeEntry<RPGCraftingRecipe>> rpgCraftingRecipesList = new ArrayList<>(List.of());
    private List<RecipeEntry<RPGCraftingRecipe>> craftingListRecipesIdentifierList = new ArrayList<>(List.of());
    private final PlayerInventory playerInventory;
    private final RecipeListInputInventory input;

    public RecipeListScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerTypesRegistry.CRAFTING_LIST_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getWorld();
        this.input = new RecipeListInputInventory(1, this);

//        this.craftingRecipesIdentifierList = CraftingRecipesRegistry.registeredCraftingRecipes.keySet().stream().toList(); // TODO rework?

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
        // input 36
        this.addSlot(new Slot(this.input, 0, 22, 62));

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
        this.addProperty(this.shouldScreenCalculateRecipeList);
        this.shouldScreenCalculateRecipeList.set(0);
        this.populateRecipeLists();
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
//        if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
//        }
        return true;
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public void setSelectedRecipe(int newSelectedRecipe) {
        this.selectedRecipe.set(newSelectedRecipe);
    }

    public PlayerInventory getPlayerInventory() {
        return this.playerInventory;
    }

    public RecipeListInputInventory getInput() {
        return input;
    }

    public MultipleStackRecipeInput getRecipeListInputInventory() {

        int inputSize = this.getInput().size();

        SimpleInventory craftingInputInventory = new SimpleInventory(
                inputSize
        );

        for (int j = 0; j < inputSize; j++) {
            craftingInputInventory.setStack(j, this.getInput().getStack(j).copy());
        }
        return new MultipleStackRecipeInput(craftingInputInventory.getHeldStacks(), craftingInputInventory.size());
    }

    public List<RecipeEntry<RPGCraftingRecipe>> getCurrentCraftingRecipesList() {
        return this.craftingListRecipesIdentifierList;
    }

    public int shouldScreenCalculateRecipeList() {
        return this.shouldScreenCalculateRecipeList.get();
    }

    public void setShouldScreenCalculateRecipeList(int shouldScreenCalculateRecipeList) {
        this.shouldScreenCalculateRecipeList.set(shouldScreenCalculateRecipeList);
    }

    public void updateRPGCraftingRecipesList() {
        this.rpgCraftingRecipesList.clear();
        List<RecipeEntry<RPGCraftingRecipe>> newRecipeEntryList = this.world.getRecipeManager().listAllOfType(RPGCraftingRecipe.Type.INSTANCE);
        this.rpgCraftingRecipesList.addAll(newRecipeEntryList);
    }

    public void populateRecipeLists() {
        this.craftingListRecipesIdentifierList.clear();
		this.craftingListRecipesIdentifierList.addAll(this.rpgCraftingRecipesList);
    }
}