package com.github.theredbrain.rpgcrafting.network.packet;

import com.github.theredbrain.rpgcrafting.screen.RecipeListScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class UpdateRecipeListScreenHandlerSelectedRecipePacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateRecipeListScreenHandlerSelectedRecipePacket> {
	@Override
	public void receive(UpdateRecipeListScreenHandlerSelectedRecipePacket payload, ServerPlayNetworking.Context context) {
		if (context.player().currentScreenHandler instanceof RecipeListScreenHandler recipeListScreenHandler) {
			recipeListScreenHandler.setSelectedRecipe(payload.newSelectedRecipe());
		}
	}
}
