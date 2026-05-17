package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.component.type.OpensRPGCraftingScreenComponent;
import com.github.theredbrain.rpgcrafting.util.RPGCraftingHelper;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;

public class ServerEventRegistry {

	public static void initializeServerEvents() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (!world.isClient()) {
				ItemStack usedItemStack = player.getStackInHand(hand);
				OpensRPGCraftingScreenComponent opensRPGCraftingScreenComponent = usedItemStack.get(DataComponentRegistry.OPENS_RPG_CRAFTING_SCREEN_COMPONENT);
				if (opensRPGCraftingScreenComponent != null) {
					player.openHandledScreen(RPGCraftingHelper.supplyRPGCraftingScreenHandlerFactoryFromItem(world, player, opensRPGCraftingScreenComponent.crafting_tab()));
					return TypedActionResult.success(usedItemStack, true);
				}
			}
			return TypedActionResult.pass(ItemStack.EMPTY);
		});
	}
}
