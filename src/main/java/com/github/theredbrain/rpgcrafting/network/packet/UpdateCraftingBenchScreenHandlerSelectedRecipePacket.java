package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record UpdateCraftingBenchScreenHandlerSelectedRecipePacket(int newSelectedRecipe) implements CustomPayload {
	public static final Id<UpdateCraftingBenchScreenHandlerSelectedRecipePacket> PACKET_ID = new Id<>(RPGCrafting.identifier("update_crafting_bench_screen_handler_selected_recipe"));
	public static final PacketCodec<RegistryByteBuf, UpdateCraftingBenchScreenHandlerSelectedRecipePacket> PACKET_CODEC = PacketCodec.of(UpdateCraftingBenchScreenHandlerSelectedRecipePacket::write, UpdateCraftingBenchScreenHandlerSelectedRecipePacket::new);

	public UpdateCraftingBenchScreenHandlerSelectedRecipePacket(RegistryByteBuf registryByteBuf) {
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
