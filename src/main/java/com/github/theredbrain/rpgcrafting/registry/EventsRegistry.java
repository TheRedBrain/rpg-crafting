package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.network.packet.ServerConfigSyncPacket;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class EventsRegistry {
    public static void initializeEvents() {
        PayloadTypeRegistry.playS2C().register(ServerConfigSyncPacket.PACKET_ID, ServerConfigSyncPacket.PACKET_CODEC);
//        PayloadTypeRegistry.playS2C().register(CraftingRecipesSyncPacket.PACKET_ID, CraftingRecipesSyncPacket.PACKET_CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayNetworking.send(handler.player, new ServerConfigSyncPacket(RPGCrafting.serverConfig));
//            ServerPlayNetworking.send(handler.player, new CraftingRecipesSyncPacket(CraftingRecipesRegistry.registeredCraftingRecipes));
        });
//        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
//            for (ServerPlayerEntity player : PlayerLookup.all(server)) {
////                ServerPlayNetworking.send(player, new CraftingRecipesSyncPacket(CraftingRecipesRegistry.registeredCraftingRecipes));
//            }
//        });
    }
}
