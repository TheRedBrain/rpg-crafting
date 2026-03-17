package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.component.type.OpensRPGCraftingScreenComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class DataComponentRegistry {
	public static final ComponentType<OpensRPGCraftingScreenComponent> OPENS_RPG_CRAFTING_SCREEN_COMPONENT = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			RPGCrafting.identifier("opens_rpg_crafting_screen"),
			ComponentType.<OpensRPGCraftingScreenComponent>builder().codec(OpensRPGCraftingScreenComponent.CODEC).packetCodec(OpensRPGCraftingScreenComponent.PACKET_CODEC).cache().build()
	);

	public static void init() {
	}
}
