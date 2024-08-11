package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ToggleUseStashForCraftingPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<ToggleUseStashForCraftingPacket> {
    @Override
    public void receive(ToggleUseStashForCraftingPacket packet, ServerPlayerEntity player, PacketSender responseSender) {
        ((DuckPlayerEntityMixin)player).rpgcrafting$setUseStashForCrafting(packet.useStashForCrafting);
    }
}
