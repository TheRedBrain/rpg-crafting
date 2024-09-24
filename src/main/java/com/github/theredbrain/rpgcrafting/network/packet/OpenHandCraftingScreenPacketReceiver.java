package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.screen.HandCraftingScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

public class OpenHandCraftingScreenPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<OpenHandCraftingScreenPacket> {

	@Override
	public void receive(OpenHandCraftingScreenPacket payload, ServerPlayNetworking.Context context) {
		context.player().openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new HandCraftingScreenHandler(syncId, inventory), Text.translatable("gui.hand_crafting.title")));
	}
}
