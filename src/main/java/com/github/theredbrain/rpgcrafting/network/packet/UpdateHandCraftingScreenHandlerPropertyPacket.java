package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record UpdateHandCraftingScreenHandlerPropertyPacket(int shouldScreenCalculateCraftingStatus) implements CustomPayload {
    public static final Id<UpdateHandCraftingScreenHandlerPropertyPacket> PACKET_ID = new Id<>(RPGCrafting.identifier("update_hand_crafting_screen_handler_property"));
    public static final PacketCodec<RegistryByteBuf, UpdateHandCraftingScreenHandlerPropertyPacket> PACKET_CODEC = PacketCodec.of(UpdateHandCraftingScreenHandlerPropertyPacket::write, UpdateHandCraftingScreenHandlerPropertyPacket::new);

    public UpdateHandCraftingScreenHandlerPropertyPacket(RegistryByteBuf registryByteBuf) {
        this(registryByteBuf.readInt());
    }

    private void write(RegistryByteBuf registryByteBuf) {
        registryByteBuf.writeInt(shouldScreenCalculateCraftingStatus);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
