package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.advancement.criterion.InteractWithRPGCraftingStationCriterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AdvancementCriteriaRegistry {

	static {
		RPGCrafting.INTERACTED_WITH_RPG_CRAFTING_STATION = Registry.register(Registries.CRITERION, "rpgcrafting:interacted_with_rpg_crafting_station", new InteractWithRPGCraftingStationCriterion());
	}

	public static void init() {
	}
}
