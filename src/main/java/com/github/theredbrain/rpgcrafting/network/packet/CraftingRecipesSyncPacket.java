package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.data.CraftingRecipe;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public record CraftingRecipesSyncPacket(Map<Identifier, CraftingRecipe> registeredCraftingRecipes) implements CustomPayload {
	public static final Id<CraftingRecipesSyncPacket> PACKET_ID = new Id<>(RPGCrafting.identifier("equipment_sets_sync"));
	public static final PacketCodec<RegistryByteBuf, CraftingRecipesSyncPacket> PACKET_CODEC = PacketCodec.of(CraftingRecipesSyncPacket::write, CraftingRecipesSyncPacket::read);

	public static CraftingRecipesSyncPacket read(RegistryByteBuf registryByteBuf) {
		Map<Identifier, CraftingRecipe> newCraftingRecipes = new HashMap<>();
		int i = registryByteBuf.readInt();
		for (int j = 0; j < i; j++) {
			Identifier identifier = registryByteBuf.readIdentifier();
			CraftingRecipe craftingRecipe = registryByteBuf.decodeAsJson(CraftingRecipe.CODEC);
			newCraftingRecipes.put(identifier, craftingRecipe);
		}
		return new CraftingRecipesSyncPacket(newCraftingRecipes);
	}

	private void write(RegistryByteBuf registryByteBuf) {
		registryByteBuf.writeInt(registeredCraftingRecipes.size());
		for (var entry : registeredCraftingRecipes.entrySet()) {
			registryByteBuf.writeIdentifier(entry.getKey());
			registryByteBuf.encodeAsJson(CraftingRecipe.CODEC, entry.getValue());
		}
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return PACKET_ID;
	}
}