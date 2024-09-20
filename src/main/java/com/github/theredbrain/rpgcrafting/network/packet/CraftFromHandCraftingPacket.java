package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record CraftFromHandCraftingPacket(String recipeIdentifier) implements CustomPayload {
	public static final CustomPayload.Id<CraftFromHandCraftingPacket> PACKET_ID = new CustomPayload.Id<>(RPGCrafting.identifier("craft_from_hand_crafting"));
	public static final PacketCodec<RegistryByteBuf, CraftFromHandCraftingPacket> PACKET_CODEC = PacketCodec.of(CraftFromHandCraftingPacket::write, CraftFromHandCraftingPacket::new);

	public CraftFromHandCraftingPacket(RegistryByteBuf registryByteBuf) {
		this(registryByteBuf.readString());
	}

	private void write(RegistryByteBuf registryByteBuf) {
		registryByteBuf.writeString(recipeIdentifier);
	}

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
