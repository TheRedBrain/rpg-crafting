package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class UpdateCraftingBenchScreenHandlerSelectedRecipePacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateCraftingBenchScreenHandlerSelectedRecipePacket> {
	@Override
	public void receive(UpdateCraftingBenchScreenHandlerSelectedRecipePacket payload, ServerPlayNetworking.Context context) {
		if (context.player().currentScreenHandler instanceof CraftingBenchBlockScreenHandler craftingBenchBlockScreenHandler) {
			craftingBenchBlockScreenHandler.setSelectedRecipe(payload.newSelectedRecipe());
		}
	}
}
