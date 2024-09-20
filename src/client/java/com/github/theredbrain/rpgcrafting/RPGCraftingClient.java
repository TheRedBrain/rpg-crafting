package com.github.theredbrain.rpgcrafting;

import com.github.theredbrain.rpgcrafting.gui.screen.ingame.CraftingBenchBlockScreen;
import com.github.theredbrain.rpgcrafting.registry.ClientPacketRegistry;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class RPGCraftingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HandledScreens.register(ScreenHandlerTypesRegistry.CRAFTING_BENCH_BLOCK_SCREEN_HANDLER, CraftingBenchBlockScreen::new);

		ClientPacketRegistry.init();
	}
}