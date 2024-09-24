package com.github.theredbrain.rpgcrafting.mixin.server.network;

import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

	@Inject(method = "copyFrom", at = @At("TAIL"))
	public void rpgcrafting$copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		((DuckPlayerEntityMixin) this).rpgcrafting$setStashInventory(((DuckPlayerEntityMixin) oldPlayer).rpgcrafting$getStashInventory());
	}
}
