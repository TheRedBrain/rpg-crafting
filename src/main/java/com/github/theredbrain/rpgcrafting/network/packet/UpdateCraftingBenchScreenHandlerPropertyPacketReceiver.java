package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class UpdateCraftingBenchScreenHandlerPropertyPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateCraftingBenchScreenHandlerPropertyPacket> {
	@Override
	public void receive(UpdateCraftingBenchScreenHandlerPropertyPacket payload, ServerPlayNetworking.Context context) {
		if (context.player().currentScreenHandler instanceof CraftingBenchBlockScreenHandler craftingBenchBlockScreenHandler) {
			craftingBenchBlockScreenHandler.setShouldScreenCalculateCraftingStatus(payload.shouldScreenCalculateCraftingStatus());
		}
	}
}
