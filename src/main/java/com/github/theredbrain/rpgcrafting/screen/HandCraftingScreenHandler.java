package com.github.theredbrain.rpgcrafting.screen;

import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.recipe.input.MultipleStackRecipeInput;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class HandCraftingScreenHandler extends ScreenHandler {

    private final Property selectedRecipe = Property.create();
    private final Property shouldScreenCalculateCraftingStatus = Property.create();
    private final World world;
    private List<RecipeEntry<RPGCraftingRecipe>> rpgCraftingRecipesList = new ArrayList<>(List.of());
    private List<RecipeEntry<RPGCraftingRecipe>> handCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private final PlayerInventory playerInventory;

    public HandCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerTypesRegistry.HAND_CRAFTING_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getWorld();

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
//            if (this.selectedRecipe.get() > -1 && player instanceof ServerPlayerEntity serverPlayerEntity) {
//                List<Identifier> currentRecipeList = this.getCurrentCraftingRecipesList();
//                ServerPlayNetworking.send(serverPlayerEntity,
//                        new CraftFromHandCraftingPacket(
//                                currentRecipeList.get(this.selectedRecipe.get()).toString()
//                        )
//                );
//            }
//        } else if (this.isInBounds(id)) {
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

    public List<RecipeEntry<RPGCraftingRecipe>> getCurrentCraftingRecipesList() {
        return this.handCraftingRecipesIdentifierList;
    }

	public int getHandCraftingLevels() {
        return ((DuckPlayerEntityMixin) this.playerInventory.player).rpgcrafting$getActiveHandCraftingLevel();
	}

    public int shouldScreenCalculateCraftingStatus() {
        return this.shouldScreenCalculateCraftingStatus.get();
    }

    public void setShouldScreenCalculateCraftingStatus(int shouldScreenCalculateCraftingStatus) {
        this.shouldScreenCalculateCraftingStatus.set(shouldScreenCalculateCraftingStatus);
    }

    public MultipleStackRecipeInput getCraftingInputInventory() {

        int playerInventorySize = 27; // TODO inventory size attributes

        int playerHotbarSize = 9; // TODO inventory size attributes

        SimpleInventory craftingInputInventory = new SimpleInventory(
                playerInventorySize
                        + playerHotbarSize
        );

        int k = 0;
        int j;
        for (j = 0; j < playerInventorySize; j++) { // TODO inventory size attributes
            craftingInputInventory.setStack(k + j, this.getPlayerInventory().getStack(j).copy());
        }
        k = k + playerInventorySize;

        return new MultipleStackRecipeInput(craftingInputInventory.getHeldStacks(), craftingInputInventory.size());
    }

    public void updateRPGCraftingRecipesList() {
        this.rpgCraftingRecipesList.clear();
        List<RecipeEntry<RPGCraftingRecipe>> newRecipeEntryList = this.world.getRecipeManager().listAllOfType(RPGCraftingRecipe.Type.INSTANCE);
        this.rpgCraftingRecipesList.addAll(newRecipeEntryList);
    }

    public void populateRecipeLists() {

            this.handCraftingRecipesIdentifierList.clear();

        for (RecipeEntry<RPGCraftingRecipe> rpgCraftingRecipeEntry : this.rpgCraftingRecipesList) {
//
            // TODO recipe levels
            RPGCraftingRecipe rpgCraftingRecipe = rpgCraftingRecipeEntry.value();
            int tab = rpgCraftingRecipe.tab;
            if (tab == 0) {
                this.handCraftingRecipesIdentifierList.add(rpgCraftingRecipeEntry);
            }
		}
	}
}
