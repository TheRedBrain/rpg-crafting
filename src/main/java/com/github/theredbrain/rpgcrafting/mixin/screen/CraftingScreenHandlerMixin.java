package com.github.theredbrain.rpgcrafting.mixin.screen;

import com.github.theredbrain.rpgcrafting.registry.BlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingRecipeInput, CraftingRecipe> {

	@Shadow
	@Final
	private ScreenHandlerContext context;

	public CraftingScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
		super(screenHandlerType, i);
	}

	@Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
	public void rpgcrafting$canUse(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(cir.getReturnValue() || canUse(this.context, player, BlockRegistry.CRAFTING_ROOT_BLOCK));
	}

}
