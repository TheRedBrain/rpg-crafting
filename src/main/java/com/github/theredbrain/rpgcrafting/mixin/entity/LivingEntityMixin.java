package com.github.theredbrain.rpgcrafting.mixin.entity;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void rpgcrafting$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue()
				.add(RPGCrafting.HAND_CRAFTING_LEVEL)
		;
	}
}
