package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record ToggleUseStashForCraftingPacket(boolean useStashForCrafting) implements CustomPayload {
    public static final CustomPayload.Id<ToggleUseStashForCraftingPacket> PACKET_ID = new CustomPayload.Id<>(RPGCrafting.identifier("toggle_stash_for_crafting"));
    public static final PacketCodec<RegistryByteBuf, ToggleUseStashForCraftingPacket> PACKET_CODEC = PacketCodec.of(ToggleUseStashForCraftingPacket::write, ToggleUseStashForCraftingPacket::new);

    public ToggleUseStashForCraftingPacket(RegistryByteBuf registryByteBuf) {
        this(registryByteBuf.readBoolean());
    }

    private void write(RegistryByteBuf registryByteBuf) {
        registryByteBuf.writeBoolean(useStashForCrafting);
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
