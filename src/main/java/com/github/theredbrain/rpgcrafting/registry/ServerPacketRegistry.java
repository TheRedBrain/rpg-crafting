package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.network.packet.CraftFromCraftingBenchPacket;
import com.github.theredbrain.rpgcrafting.network.packet.CraftFromCraftingBenchPacketReceiver;
import com.github.theredbrain.rpgcrafting.network.packet.ToggleUseStashForCraftingPacket;
import com.github.theredbrain.rpgcrafting.network.packet.ToggleUseStashForCraftingPacketReceiver;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateCraftingBenchScreenHandlerPropertyPacket;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateCraftingBenchScreenHandlerPropertyPacketReceiver;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerPacketRegistry {
	public static void init() {

		PayloadTypeRegistry.playC2S().register(CraftFromCraftingBenchPacket.PACKET_ID, CraftFromCraftingBenchPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(CraftFromCraftingBenchPacket.PACKET_ID, new CraftFromCraftingBenchPacketReceiver());

		PayloadTypeRegistry.playC2S().register(ToggleUseStashForCraftingPacket.PACKET_ID, ToggleUseStashForCraftingPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(ToggleUseStashForCraftingPacket.PACKET_ID, new ToggleUseStashForCraftingPacketReceiver());

		PayloadTypeRegistry.playC2S().register(UpdateCraftingBenchScreenHandlerPropertyPacket.PACKET_ID, UpdateCraftingBenchScreenHandlerPropertyPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UpdateCraftingBenchScreenHandlerPropertyPacket.PACKET_ID, new UpdateCraftingBenchScreenHandlerPropertyPacketReceiver());

	}
}
