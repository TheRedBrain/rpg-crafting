package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record OpenHandCraftingScreenPacket() implements CustomPayload {
	public static final Id<OpenHandCraftingScreenPacket> PACKET_ID = new Id<>(RPGCrafting.identifier("open_hand_crafting_screen"));
	public static final PacketCodec<RegistryByteBuf, OpenHandCraftingScreenPacket> PACKET_CODEC = PacketCodec.of(OpenHandCraftingScreenPacket::write, OpenHandCraftingScreenPacket::new);

	public OpenHandCraftingScreenPacket(PacketByteBuf buf) {
		this();
	}

	private void write(RegistryByteBuf registryByteBuf) {
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
