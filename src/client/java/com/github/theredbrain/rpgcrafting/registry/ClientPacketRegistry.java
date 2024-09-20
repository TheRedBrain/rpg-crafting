package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.network.packet.CraftingRecipesSyncPacket;
import com.github.theredbrain.rpgcrafting.network.packet.ServerConfigSyncPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(value = EnvType.CLIENT)
public class ClientPacketRegistry {

	public static void init() {

		ClientPlayNetworking.registerGlobalReceiver(ServerConfigSyncPacket.PACKET_ID, (payload, context) -> {
			RPGCrafting.serverConfig = payload.serverConfig();
		});
		ClientPlayNetworking.registerGlobalReceiver(CraftingRecipesSyncPacket.PACKET_ID, (payload, context) -> {
			CraftingRecipesRegistry.registeredCraftingRecipes = payload.registeredCraftingRecipes();
		});
	}
}
