package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.network.packet.CraftFromCraftingBenchPacket;
import com.github.theredbrain.rpgcrafting.network.packet.CraftFromCraftingBenchPacketReceiver;
import com.github.theredbrain.rpgcrafting.network.packet.CraftFromHandCraftingPacket;
import com.github.theredbrain.rpgcrafting.network.packet.CraftFromHandCraftingPacketReceiver;
import com.github.theredbrain.rpgcrafting.network.packet.OpenCraftingListScreenPacket;
import com.github.theredbrain.rpgcrafting.network.packet.OpenCraftingListScreenPacketReceiver;
import com.github.theredbrain.rpgcrafting.network.packet.OpenHandCraftingScreenPacket;
import com.github.theredbrain.rpgcrafting.network.packet.OpenHandCraftingScreenPacketReceiver;
import com.github.theredbrain.rpgcrafting.network.packet.ToggleUseStashForCraftingPacket;
import com.github.theredbrain.rpgcrafting.network.packet.ToggleUseStashForCraftingPacketReceiver;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateCraftingBenchScreenHandlerPropertyPacket;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateCraftingBenchScreenHandlerPropertyPacketReceiver;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateHandCraftingScreenHandlerPropertyPacket;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateHandCraftingScreenHandlerPropertyPacketReceiver;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerPacketRegistry {
	public static void init() {

		PayloadTypeRegistry.playC2S().register(CraftFromCraftingBenchPacket.PACKET_ID, CraftFromCraftingBenchPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(CraftFromCraftingBenchPacket.PACKET_ID, new CraftFromCraftingBenchPacketReceiver());

		PayloadTypeRegistry.playC2S().register(CraftFromHandCraftingPacket.PACKET_ID, CraftFromHandCraftingPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(CraftFromHandCraftingPacket.PACKET_ID, new CraftFromHandCraftingPacketReceiver());

		PayloadTypeRegistry.playC2S().register(OpenCraftingListScreenPacket.PACKET_ID, OpenCraftingListScreenPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(OpenCraftingListScreenPacket.PACKET_ID, new OpenCraftingListScreenPacketReceiver());

		PayloadTypeRegistry.playC2S().register(OpenHandCraftingScreenPacket.PACKET_ID, OpenHandCraftingScreenPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(OpenHandCraftingScreenPacket.PACKET_ID, new OpenHandCraftingScreenPacketReceiver());

		PayloadTypeRegistry.playC2S().register(ToggleUseStashForCraftingPacket.PACKET_ID, ToggleUseStashForCraftingPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(ToggleUseStashForCraftingPacket.PACKET_ID, new ToggleUseStashForCraftingPacketReceiver());

		PayloadTypeRegistry.playC2S().register(UpdateCraftingBenchScreenHandlerPropertyPacket.PACKET_ID, UpdateCraftingBenchScreenHandlerPropertyPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UpdateCraftingBenchScreenHandlerPropertyPacket.PACKET_ID, new UpdateCraftingBenchScreenHandlerPropertyPacketReceiver());

		PayloadTypeRegistry.playC2S().register(UpdateHandCraftingScreenHandlerPropertyPacket.PACKET_ID, UpdateHandCraftingScreenHandlerPropertyPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UpdateHandCraftingScreenHandlerPropertyPacket.PACKET_ID, new UpdateHandCraftingScreenHandlerPropertyPacketReceiver());

	}
}
