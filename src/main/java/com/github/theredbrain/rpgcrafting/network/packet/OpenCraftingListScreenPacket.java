package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record OpenCraftingListScreenPacket() implements CustomPayload {
	public static final Id<OpenCraftingListScreenPacket> PACKET_ID = new Id<>(RPGCrafting.identifier("open_crafting_list_screen"));
	public static final PacketCodec<RegistryByteBuf, OpenCraftingListScreenPacket> PACKET_CODEC = PacketCodec.of(OpenCraftingListScreenPacket::write, OpenCraftingListScreenPacket::new);

	public OpenCraftingListScreenPacket(PacketByteBuf buf) {
		this();
	}

	private void write(RegistryByteBuf registryByteBuf) {
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
