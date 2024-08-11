package com.github.theredbrain.rpgcrafting;

import com.github.theredbrain.rpgcrafting.gui.screen.ingame.CraftingBenchBlockScreen;
import com.github.theredbrain.rpgcrafting.registry.CraftingRecipeRegistry;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpgcrafting.registry.ServerPacketRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class RPGCraftingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		HandledScreens.register(ScreenHandlerTypesRegistry.CRAFTING_BENCH_BLOCK_SCREEN_HANDLER, CraftingBenchBlockScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_CRAFTING_RECIPES, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            CraftingRecipeRegistry.decodeRegistry(buffer);
        });
	}
}