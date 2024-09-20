package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.data.CraftingRecipe;
import com.github.theredbrain.rpgcrafting.registry.CraftingRecipesRegistry;
import com.github.theredbrain.rpgcrafting.screen.HandCraftingScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CraftFromHandCraftingPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<CraftFromHandCraftingPacket> {
    @Override
    public void receive(CraftFromHandCraftingPacket payload, ServerPlayNetworking.Context context) {

        String recipeIdentifier = payload.recipeIdentifier();

        ServerPlayerEntity player = context.player();

        ScreenHandler screenHandler = player.currentScreenHandler;

        CraftingRecipe craftingRecipe = CraftingRecipesRegistry.registeredCraftingRecipes.get(Identifier.of(recipeIdentifier));

        if (craftingRecipe != null && screenHandler instanceof HandCraftingScreenHandler handCraftingScreenHandler) {

            int playerInventorySize = handCraftingScreenHandler.getPlayerInventory().size();

            boolean bl = true;
            ItemStack itemStack;

            for (ItemStack ingredient : craftingRecipe.ingredients()) {

                Item virtualItem = ingredient.getItem();
                int ingredientCount = ingredient.getCount();
                int j;

                // TODO play test which inventory normally contains the most crafting ingredients and should be checked first

                for (j = 0; j < playerInventorySize; j++) {
                    if (handCraftingScreenHandler.getPlayerInventory().getStack(j).isOf(virtualItem)) {
                        itemStack = handCraftingScreenHandler.getPlayerInventory().getStack(j).copy();
                        int stackCount = itemStack.getCount();
                        if (stackCount >= ingredientCount) {
                            itemStack.setCount(stackCount - ingredientCount);
                            handCraftingScreenHandler.getPlayerInventory().setStack(j, itemStack);
                            ingredientCount = 0;
                            break;
                        } else {
                            handCraftingScreenHandler.getPlayerInventory().setStack(j, ItemStack.EMPTY);
                            ingredientCount = ingredientCount - stackCount;
                        }
                    }
                }
                if (ingredientCount > 0) {
                    bl = false;
                }
            }
            if (bl) {
                player.getInventory().offerOrDrop(craftingRecipe.result());
                handCraftingScreenHandler.calculateUnlockedRecipes();
                handCraftingScreenHandler.setShouldScreenCalculateCraftingStatus(1);
            }
        }
    }
}