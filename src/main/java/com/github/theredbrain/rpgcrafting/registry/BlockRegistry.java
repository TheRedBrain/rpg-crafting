package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.block.AnvilShapedCraftingTabProviderBlock;
import com.github.theredbrain.rpgcrafting.block.CauldronShapedCraftingTabProviderBlock;
import com.github.theredbrain.rpgcrafting.block.FullBlockCraftingTabProviderBlock;
import com.github.theredbrain.rpgcrafting.block.HorizontalFacingFullBlockCraftingTabProviderBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;

public class BlockRegistry {

	// crafting blocks
	public static final Block STORAGE_AREA_0_PROVIDER_BLOCK = registerBlock("storage_area_0_provider_block", new FullBlockCraftingTabProviderBlock(-1, Block.Settings.create()
			.mapColor(MapColor.OAK_TAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.5F)
			.sounds(BlockSoundGroup.WOOD)
			.pistonBehavior(PistonBehavior.BLOCK)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
	public static final Block STORAGE_AREA_1_PROVIDER_BLOCK = registerBlock("storage_area_1_provider_block", new FullBlockCraftingTabProviderBlock(-1, Block.Settings.create()
			.mapColor(MapColor.OAK_TAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.5F)
			.sounds(BlockSoundGroup.WOOD)
			.pistonBehavior(PistonBehavior.BLOCK)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
	public static final Block STORAGE_AREA_2_PROVIDER_BLOCK = registerBlock("storage_area_2_provider_block", new FullBlockCraftingTabProviderBlock(-1, Block.Settings.create()
			.mapColor(MapColor.OAK_TAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.5F)
			.sounds(BlockSoundGroup.WOOD)
			.pistonBehavior(PistonBehavior.BLOCK)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
	public static final Block STORAGE_AREA_3_PROVIDER_BLOCK = registerBlock("storage_area_3_provider_block", new FullBlockCraftingTabProviderBlock(-1, Block.Settings.create()
			.mapColor(MapColor.OAK_TAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.5F)
			.sounds(BlockSoundGroup.WOOD)
			.pistonBehavior(PistonBehavior.BLOCK)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
	public static final Block STORAGE_AREA_4_PROVIDER_BLOCK = registerBlock("storage_area_4_provider_block", new FullBlockCraftingTabProviderBlock(-1, Block.Settings.create()
			.mapColor(MapColor.OAK_TAN)
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.5F)
			.sounds(BlockSoundGroup.WOOD)
			.pistonBehavior(PistonBehavior.BLOCK)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
	public static final Block CRAFTING_TAB_1_PROVIDER_BLOCK = registerBlock("crafting_tab_1_provider_block", new HorizontalFacingFullBlockCraftingTabProviderBlock(1, Block.Settings.create()
			.mapColor(MapColor.OAK_TAN)
			.strength(2.5F)
			.sounds(BlockSoundGroup.WOOD)
			.pistonBehavior(PistonBehavior.BLOCK)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
	public static final Block CRAFTING_TAB_2_PROVIDER_BLOCK = registerBlock("crafting_tab_2_provider_block", new AnvilShapedCraftingTabProviderBlock(2, Block.Settings.create()
			.mapColor(MapColor.IRON_GRAY)
			.requiresTool()
			.strength(5.0F, 1200.0F)
			.sounds(BlockSoundGroup.ANVIL)
			.pistonBehavior(PistonBehavior.BLOCK)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
	public static final Block CRAFTING_TAB_3_PROVIDER_BLOCK = registerBlock("crafting_tab_3_provider_block", new HorizontalFacingFullBlockCraftingTabProviderBlock(3, Block.Settings.create()
			.mapColor(MapColor.STONE_GRAY)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool()
			.strength(3.5F)
			.pistonBehavior(PistonBehavior.BLOCK)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
	public static final Block CRAFTING_TAB_4_PROVIDER_BLOCK = registerBlock("crafting_tab_4_provider_block", new CauldronShapedCraftingTabProviderBlock(4, Block.Settings.create()
			.mapColor(MapColor.STONE_GRAY)
			.requiresTool()
			.strength(2.0F)
			.pistonBehavior(PistonBehavior.BLOCK)
			.nonOpaque()), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);

	private static Block registerBlock(String name, Block block, RegistryKey<ItemGroup> itemGroup) {
		Registry.register(Registries.ITEM, RPGCrafting.identifier(name), new BlockItem(block, new Item.Settings()));
		ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(block));
		return Registry.register(Registries.BLOCK, RPGCrafting.identifier(name), block);
	}

	public static void init() {
	}
}
