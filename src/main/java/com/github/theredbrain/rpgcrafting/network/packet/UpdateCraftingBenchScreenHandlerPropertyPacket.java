package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record UpdateCraftingBenchScreenHandlerPropertyPacket(int shouldScreenCalculateCraftingStatus) implements CustomPayload {
    public static final CustomPayload.Id<UpdateCraftingBenchScreenHandlerPropertyPacket> PACKET_ID = new CustomPayload.Id<>(RPGCrafting.identifier("update_crafting_bench_screen_handler_property"));
    public static final PacketCodec<RegistryByteBuf, UpdateCraftingBenchScreenHandlerPropertyPacket> PACKET_CODEC = PacketCodec.of(UpdateCraftingBenchScreenHandlerPropertyPacket::write, UpdateCraftingBenchScreenHandlerPropertyPacket::new);

    public UpdateCraftingBenchScreenHandlerPropertyPacket(RegistryByteBuf registryByteBuf) {
        this(registryByteBuf.readInt());
    }

    private void write(RegistryByteBuf registryByteBuf) {
        registryByteBuf.writeInt(shouldScreenCalculateCraftingStatus);
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
