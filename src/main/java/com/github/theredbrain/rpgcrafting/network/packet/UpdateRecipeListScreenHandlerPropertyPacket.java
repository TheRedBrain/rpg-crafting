package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record UpdateRecipeListScreenHandlerPropertyPacket(
		int shouldScreenCalculateRecipeList) implements CustomPayload {
	public static final Id<UpdateRecipeListScreenHandlerPropertyPacket> PACKET_ID = new Id<>(RPGCrafting.identifier("update_recipe_list_screen_handler_property"));
	public static final PacketCodec<RegistryByteBuf, UpdateRecipeListScreenHandlerPropertyPacket> PACKET_CODEC = PacketCodec.of(UpdateRecipeListScreenHandlerPropertyPacket::write, UpdateRecipeListScreenHandlerPropertyPacket::new);

	public UpdateRecipeListScreenHandlerPropertyPacket(RegistryByteBuf registryByteBuf) {
		this(registryByteBuf.readInt());
	}

	private void write(RegistryByteBuf registryByteBuf) {
		registryByteBuf.writeInt(shouldScreenCalculateRecipeList);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
