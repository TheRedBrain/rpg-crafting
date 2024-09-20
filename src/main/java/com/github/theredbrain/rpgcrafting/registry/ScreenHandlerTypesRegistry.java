package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ScreenHandlerTypesRegistry {
    public static final ExtendedScreenHandlerType<CraftingBenchBlockScreenHandler, CraftingBenchBlockScreenHandler.CraftingBenchBlockData> CRAFTING_BENCH_BLOCK_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(CraftingBenchBlockScreenHandler::new, CraftingBenchBlockScreenHandler.CraftingBenchBlockData.PACKET_CODEC);

    public static void registerAll() {
        Registry.register(Registries.SCREEN_HANDLER, RPGCrafting.identifier("crafting_bench"), CRAFTING_BENCH_BLOCK_SCREEN_HANDLER);
    }
}
