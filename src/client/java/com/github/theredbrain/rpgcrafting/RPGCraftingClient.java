package com.github.theredbrain.rpgcrafting;

import com.github.theredbrain.rpgcrafting.config.ClientConfig;
import com.github.theredbrain.rpgcrafting.gui.screen.ingame.CraftingBenchBlockScreen;
import com.github.theredbrain.rpgcrafting.gui.screen.ingame.HandCraftingScreen;
import com.github.theredbrain.rpgcrafting.gui.screen.ingame.RecipeListScreen;
import com.github.theredbrain.rpgcrafting.network.packet.OpenCraftingListScreenPacket;
import com.github.theredbrain.rpgcrafting.network.packet.OpenHandCraftingScreenPacket;
import com.github.theredbrain.rpgcrafting.registry.ClientPacketRegistry;
import com.github.theredbrain.rpgcrafting.registry.KeyBindingsRegistry;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class RPGCraftingClient implements ClientModInitializer {
	public static ConfigHolder<ClientConfig> clientConfigHolder;

	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(ClientConfig.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		clientConfigHolder = AutoConfig.getConfigHolder(ClientConfig.class);

		KeyBindingsRegistry.registerKeyBindings();
		HandledScreens.register(ScreenHandlerTypesRegistry.CRAFTING_BENCH_BLOCK_SCREEN_HANDLER, CraftingBenchBlockScreen::new);
		HandledScreens.register(ScreenHandlerTypesRegistry.CRAFTING_LIST_SCREEN_HANDLER, RecipeListScreen::new);
		HandledScreens.register(ScreenHandlerTypesRegistry.HAND_CRAFTING_SCREEN_HANDLER, HandCraftingScreen::new);

		ClientPacketRegistry.init();
	}

	public static void openCraftingListScreen(MinecraftClient client) {
		if (client.player != null) {
			ClientPlayNetworking.send(new OpenCraftingListScreenPacket());
		}
	}

	public static void openHandCraftingScreen(MinecraftClient client) {
		if (client.player != null) {
			ClientPlayNetworking.send(new OpenHandCraftingScreenPacket());
		}
	}
}