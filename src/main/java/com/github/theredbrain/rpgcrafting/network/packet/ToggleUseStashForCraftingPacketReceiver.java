package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ToggleUseStashForCraftingPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<ToggleUseStashForCraftingPacket> {
    @Override
    public void receive(ToggleUseStashForCraftingPacket payload, ServerPlayNetworking.Context context) {
        ((DuckPlayerEntityMixin)context.player()).rpgcrafting$setUseStashForCrafting(payload.useStashForCrafting());
    }
}
