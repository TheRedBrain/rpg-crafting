package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.screen.HandCraftingScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class UpdateHandCraftingScreenHandlerPropertyPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateHandCraftingScreenHandlerPropertyPacket> {
    @Override
    public void receive(UpdateHandCraftingScreenHandlerPropertyPacket payload, ServerPlayNetworking.Context context) {
        if (context.player().currentScreenHandler instanceof HandCraftingScreenHandler handCraftingScreenHandler) {
            handCraftingScreenHandler.setShouldScreenCalculateCraftingStatus(payload.shouldScreenCalculateCraftingStatus());
        }
    }
}
