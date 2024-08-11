package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.config.ServerConfig;
import com.github.theredbrain.rpgcrafting.network.packet.*;
import com.google.gson.Gson;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ServerPacketRegistry {

    public static final Identifier SYNC_CRAFTING_RECIPES = RPGCrafting.identifier("sync_crafting_recipes");
    public static void init() {

        ServerPlayNetworking.registerGlobalReceiver(CraftFromCraftingBenchPacket.TYPE, new CraftFromCraftingBenchPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(ToggleUseStashForCraftingPacket.TYPE, new ToggleUseStashForCraftingPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateCraftingBenchScreenHandlerPropertyPacket.TYPE, new UpdateCraftingBenchScreenHandlerPropertyPacketReceiver());

    }

    public static class ServerConfigSync {
        public static Identifier ID = RPGCrafting.identifier("server_config_sync");

        public static PacketByteBuf write(ServerConfig serverConfig) {
            var gson = new Gson();
            var json = gson.toJson(serverConfig);
            var buffer = PacketByteBufs.create();
            buffer.writeString(json);
            return buffer;
        }

        public static ServerConfig read(PacketByteBuf buffer) {
            var gson = new Gson();
            var json = buffer.readString();
            return gson.fromJson(json, ServerConfig.class);
        }
    }
}
