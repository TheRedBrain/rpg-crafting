package com.github.theredbrain.rpgcrafting.entity.player;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.google.common.collect.HashMultimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;

public class PlayerHelper {

	public static HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getNaturalAttributeModifiers() {
		HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> hashMultimap = HashMultimap.create();
		hashMultimap.put(RPGCrafting.HAND_CRAFTING_LEVEL, new EntityAttributeModifier(RPGCrafting.identifier("natural_hand_crafting_level_modifier"), RPGCrafting.SERVER_CONFIG.naturalPlayerAttributeValues.natural_hand_crafting_level, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(RPGCrafting.CRAFTING_TAB_1_LEVEL, new EntityAttributeModifier(RPGCrafting.identifier("natural_crafting_tab_1_level_modifier"), RPGCrafting.SERVER_CONFIG.naturalPlayerAttributeValues.natural_crafting_tab_1_level, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(RPGCrafting.CRAFTING_TAB_2_LEVEL, new EntityAttributeModifier(RPGCrafting.identifier("natural_crafting_tab_2_level_modifier"), RPGCrafting.SERVER_CONFIG.naturalPlayerAttributeValues.natural_crafting_tab_2_level, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(RPGCrafting.CRAFTING_TAB_3_LEVEL, new EntityAttributeModifier(RPGCrafting.identifier("natural_crafting_tab_3_level_modifier"), RPGCrafting.SERVER_CONFIG.naturalPlayerAttributeValues.natural_crafting_tab_3_level, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(RPGCrafting.CRAFTING_TAB_4_LEVEL, new EntityAttributeModifier(RPGCrafting.identifier("natural_crafting_tab_4_level_modifier"), RPGCrafting.SERVER_CONFIG.naturalPlayerAttributeValues.natural_crafting_tab_4_level, EntityAttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}
}
