package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.screen.HandCraftingScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class UpdateHandCraftingScreenHandlerSelectedRecipePacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateHandCraftingScreenHandlerSelectedRecipePacket> {
    @Override
    public void receive(UpdateHandCraftingScreenHandlerSelectedRecipePacket payload, ServerPlayNetworking.Context context) {
        if (context.player().currentScreenHandler instanceof HandCraftingScreenHandler handCraftingBlockScreenHandler) {
            handCraftingBlockScreenHandler.setSelectedRecipe(payload.newSelectedRecipe());
        }
    }
}
