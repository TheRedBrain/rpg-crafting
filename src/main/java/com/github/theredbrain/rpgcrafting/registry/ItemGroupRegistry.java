package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public class ItemGroupRegistry {
    public static final RegistryKey<ItemGroup> RPG_CRAFTING_BLOCKS = RegistryKey.of(RegistryKeys.ITEM_GROUP, RPGCrafting.identifier("rpg_crafting_block"));

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, RPG_CRAFTING_BLOCKS, FabricItemGroup.builder()
                .icon(() -> new ItemStack(Items.IRON_SWORD))
                .displayName(Text.translatable("itemGroup.rpgcrafting.rpg_crafting_block"))
                .build());
    }
}
