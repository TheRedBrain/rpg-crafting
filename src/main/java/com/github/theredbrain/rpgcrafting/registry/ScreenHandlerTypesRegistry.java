package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import com.github.theredbrain.rpgcrafting.screen.CraftingListScreenHandler;
import com.github.theredbrain.rpgcrafting.screen.HandCraftingScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypesRegistry {
    public static final ExtendedScreenHandlerType<CraftingBenchBlockScreenHandler, CraftingBenchBlockScreenHandler.CraftingBenchBlockData> CRAFTING_BENCH_BLOCK_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(CraftingBenchBlockScreenHandler::new, CraftingBenchBlockScreenHandler.CraftingBenchBlockData.PACKET_CODEC);
    public static final ScreenHandlerType<CraftingListScreenHandler> CRAFTING_LIST_SCREEN_HANDLER = new ScreenHandlerType<>(CraftingListScreenHandler::new, FeatureSet.of(FeatureFlags.VANILLA));
    public static final ScreenHandlerType<HandCraftingScreenHandler> HAND_CRAFTING_SCREEN_HANDLER = new ScreenHandlerType<>(HandCraftingScreenHandler::new, FeatureSet.of(FeatureFlags.VANILLA));

    public static void registerAll() {
        Registry.register(Registries.SCREEN_HANDLER, RPGCrafting.identifier("crafting_bench"), CRAFTING_BENCH_BLOCK_SCREEN_HANDLER);
        Registry.register(Registries.SCREEN_HANDLER, RPGCrafting.identifier("crafting_list"), CRAFTING_LIST_SCREEN_HANDLER);
        Registry.register(Registries.SCREEN_HANDLER, RPGCrafting.identifier("hand_crafting"), HAND_CRAFTING_SCREEN_HANDLER);
    }
}
