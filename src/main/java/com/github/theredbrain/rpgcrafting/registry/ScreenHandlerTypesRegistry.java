package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypesRegistry {
    public static final ScreenHandlerType<CraftingBenchBlockScreenHandler> CRAFTING_BENCH_BLOCK_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(CraftingBenchBlockScreenHandler::new);

    public static void registerAll() {
        Registry.register(Registries.SCREEN_HANDLER, RPGCrafting.identifier("crafting_bench"), CRAFTING_BENCH_BLOCK_SCREEN_HANDLER);
    }
}
