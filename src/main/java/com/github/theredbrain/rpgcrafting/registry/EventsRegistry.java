package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;

public class EventsRegistry {
    private static PacketByteBuf serverConfigSerialized = PacketByteBufs.create();
    public static void initializeEvents() {
        serverConfigSerialized = ServerPacketRegistry.ServerConfigSync.write(RPGCrafting.serverConfig);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            sender.sendPacket(ServerPacketRegistry.ServerConfigSync.ID, serverConfigSerialized); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_CRAFTING_RECIPES, CraftingRecipeRegistry.getEncodedRegistry()); // TODO convert to packet
        });
    }
}
