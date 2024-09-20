package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.block.CraftingRootBlock;
import com.github.theredbrain.rpgcrafting.block.CraftingTabProviderBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class BlockRegistry {

    // crafting blocks
    public static final Block CRAFTING_ROOT_BLOCK = registerBlock("crafting_root_block", new CraftingRootBlock(Block.Settings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
    public static final Block STORAGE_AREA_0_PROVIDER_BLOCK = registerBlock("storage_area_0_provider_block", new CraftingTabProviderBlock(-1, Block.Settings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
    public static final Block STORAGE_AREA_1_PROVIDER_BLOCK = registerBlock("storage_area_1_provider_block", new CraftingTabProviderBlock(-1, Block.Settings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
    public static final Block STORAGE_AREA_2_PROVIDER_BLOCK = registerBlock("storage_area_2_provider_block", new CraftingTabProviderBlock(-1, Block.Settings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
    public static final Block STORAGE_AREA_3_PROVIDER_BLOCK = registerBlock("storage_area_3_provider_block", new CraftingTabProviderBlock(-1, Block.Settings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
    public static final Block STORAGE_AREA_4_PROVIDER_BLOCK = registerBlock("storage_area_4_provider_block", new CraftingTabProviderBlock(-1, Block.Settings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
    public static final Block CRAFTING_TAB_0_PROVIDER_BLOCK = registerBlock("crafting_tab_0_provider_block", new CraftingTabProviderBlock(0, Block.Settings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
    public static final Block CRAFTING_TAB_1_PROVIDER_BLOCK = registerBlock("crafting_tab_1_provider_block", new CraftingTabProviderBlock(1, Block.Settings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
    public static final Block CRAFTING_TAB_2_PROVIDER_BLOCK = registerBlock("crafting_tab_2_provider_block", new CraftingTabProviderBlock(2, Block.Settings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);
    public static final Block CRAFTING_TAB_3_PROVIDER_BLOCK = registerBlock("crafting_tab_3_provider_block", new CraftingTabProviderBlock(3, Block.Settings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.RPG_CRAFTING_BLOCKS);

    private static Block registerBlock(String name, Block block, RegistryKey<ItemGroup> itemGroup) {
        Registry.register(Registries.ITEM, RPGCrafting.identifier(name), new BlockItem(block, new Item.Settings()));
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(block));
        return Registry.register(Registries.BLOCK, RPGCrafting.identifier(name), block);
    }

    public static void init() {}
}
