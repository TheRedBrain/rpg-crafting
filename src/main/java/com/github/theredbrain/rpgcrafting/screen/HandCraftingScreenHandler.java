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

    private final Property selectedRecipe = Property.create();
    private final Property shouldScreenCalculateCraftingStatus = Property.create();
    private final World world;
    private List<Identifier> craftingRecipesIdentifierList;
    private List<Identifier> handCraftingRecipesIdentifierList = new ArrayList<>(List.of());
    private List<Ingredient> currentIngredients = new ArrayList<>(List.of());
    private final PlayerInventory playerInventory;

    public HandCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerTypesRegistry.HAND_CRAFTING_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getWorld();

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

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public PlayerInventory getPlayerInventory() {
        return this.playerInventory;
    }

    public List<Identifier> getCurrentCraftingRecipesList() {
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

            for (int i = 0; i < this.craftingRecipesIdentifierList.size(); i++) {

                CraftingRecipe craftingRecipe = CraftingRecipesRegistry.registeredCraftingRecipes.get(this.craftingRecipesIdentifierList.get(i));
                unlockAdvancementIdentifier = craftingRecipe.unlockAdvancement();

                AdvancementEntry unlockAdvancementEntry = null;
                if (!unlockAdvancementIdentifier.isEmpty()) {
                    unlockAdvancementEntry = serverAdvancementLoader.get(Identifier.tryParse(unlockAdvancementIdentifier));
                }
				int tab = craftingRecipe.tab();
				if (tab == 0) {
					if (!this.world.getGameRules().getBoolean(GameRules.DO_LIMITED_CRAFTING) || unlockAdvancementIdentifier.isEmpty() || (unlockAdvancementEntry != null && playerAdvancementTracker.getProgress(unlockAdvancementEntry).isDone())) {
						this.handCraftingRecipesIdentifierList.add(this.craftingRecipesIdentifierList.get(i));
					}
				}
			}
		}
	}
}
