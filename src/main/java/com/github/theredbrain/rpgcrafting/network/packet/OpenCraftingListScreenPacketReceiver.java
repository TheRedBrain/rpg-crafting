package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.screen.RecipeListScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

public class OpenCraftingListScreenPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<OpenCraftingListScreenPacket> {

	@Override
	public void receive(OpenCraftingListScreenPacket payload, ServerPlayNetworking.Context context) {
		context.player().openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new RecipeListScreenHandler(syncId, inventory), Text.translatable("gui.crafting_list.title")));
	}
}
