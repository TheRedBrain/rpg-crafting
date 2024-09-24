package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record CraftFromCraftingBenchPacket(String recipeIdentifier,
										   boolean useStorageInventory) implements CustomPayload {
	public static final CustomPayload.Id<CraftFromCraftingBenchPacket> PACKET_ID = new CustomPayload.Id<>(RPGCrafting.identifier("craft_from_crafting_bench"));
	public static final PacketCodec<RegistryByteBuf, CraftFromCraftingBenchPacket> PACKET_CODEC = PacketCodec.of(CraftFromCraftingBenchPacket::write, CraftFromCraftingBenchPacket::new);

	public CraftFromCraftingBenchPacket(RegistryByteBuf registryByteBuf) {
		this(registryByteBuf.readString(), registryByteBuf.readBoolean());
	}

	private void write(RegistryByteBuf registryByteBuf) {
		registryByteBuf.writeString(recipeIdentifier);
		registryByteBuf.writeBoolean(useStorageInventory);
	}

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
