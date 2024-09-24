package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record UpdateRecipeListScreenHandlerSelectedRecipePacket(int newSelectedRecipe) implements CustomPayload {
	public static final Id<UpdateRecipeListScreenHandlerSelectedRecipePacket> PACKET_ID = new Id<>(RPGCrafting.identifier("update_recipe_list_screen_handler_selected_recipe"));
	public static final PacketCodec<RegistryByteBuf, UpdateRecipeListScreenHandlerSelectedRecipePacket> PACKET_CODEC = PacketCodec.of(UpdateRecipeListScreenHandlerSelectedRecipePacket::write, UpdateRecipeListScreenHandlerSelectedRecipePacket::new);

	public UpdateRecipeListScreenHandlerSelectedRecipePacket(RegistryByteBuf registryByteBuf) {
		this(registryByteBuf.readInt());
	}

	private void write(RegistryByteBuf registryByteBuf) {
		registryByteBuf.writeInt(newSelectedRecipe);
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}
