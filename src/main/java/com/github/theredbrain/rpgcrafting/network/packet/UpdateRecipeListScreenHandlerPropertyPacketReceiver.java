package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.screen.RecipeListScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class UpdateRecipeListScreenHandlerPropertyPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateRecipeListScreenHandlerPropertyPacket> {
	@Override
	public void receive(UpdateRecipeListScreenHandlerPropertyPacket payload, ServerPlayNetworking.Context context) {
		if (context.player().currentScreenHandler instanceof RecipeListScreenHandler recipeListScreenHandler) {
			recipeListScreenHandler.setShouldScreenCalculateRecipeList(payload.shouldScreenCalculateRecipeList());
		}
	}
}
