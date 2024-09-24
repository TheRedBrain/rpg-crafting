package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class Tags {
	public static final TagKey<Block> PROVIDES_CRAFTING_TAB_1_LEVEL = TagKey.of(RegistryKeys.BLOCK, RPGCrafting.identifier("provides_crafting_tab_1_level"));
	public static final TagKey<Block> PROVIDES_CRAFTING_TAB_2_LEVEL = TagKey.of(RegistryKeys.BLOCK, RPGCrafting.identifier("provides_crafting_tab_2_level"));
	public static final TagKey<Block> PROVIDES_CRAFTING_TAB_3_LEVEL = TagKey.of(RegistryKeys.BLOCK, RPGCrafting.identifier("provides_crafting_tab_3_level"));
	public static final TagKey<Block> PROVIDES_CRAFTING_TAB_4_LEVEL = TagKey.of(RegistryKeys.BLOCK, RPGCrafting.identifier("provides_crafting_tab_4_level"));
	public static final TagKey<Block> PROVIDES_STORAGE_AREA_0 = TagKey.of(RegistryKeys.BLOCK, RPGCrafting.identifier("provides_storage_area_0"));
	public static final TagKey<Block> PROVIDES_STORAGE_AREA_1 = TagKey.of(RegistryKeys.BLOCK, RPGCrafting.identifier("provides_storage_area_1"));
	public static final TagKey<Block> PROVIDES_STORAGE_AREA_2 = TagKey.of(RegistryKeys.BLOCK, RPGCrafting.identifier("provides_storage_area_2"));
	public static final TagKey<Block> PROVIDES_STORAGE_AREA_3 = TagKey.of(RegistryKeys.BLOCK, RPGCrafting.identifier("provides_storage_area_3"));
	public static final TagKey<Block> PROVIDES_STORAGE_AREA_4 = TagKey.of(RegistryKeys.BLOCK, RPGCrafting.identifier("provides_storage_area_4"));
}
