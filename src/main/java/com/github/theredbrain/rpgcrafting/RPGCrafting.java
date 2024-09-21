package com.github.theredbrain.rpgcrafting;

import com.github.theredbrain.rpgcrafting.config.ServerConfig;
import com.github.theredbrain.rpgcrafting.config.ServerConfigWrapper;
import com.github.theredbrain.rpgcrafting.registry.BlockRegistry;
import com.github.theredbrain.rpgcrafting.registry.CraftingRecipesRegistry;
import com.github.theredbrain.rpgcrafting.registry.EventsRegistry;
import com.github.theredbrain.rpgcrafting.registry.GameRulesRegistry;
import com.github.theredbrain.rpgcrafting.registry.ItemGroupRegistry;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpgcrafting.registry.ServerPacketRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPGCrafting implements ModInitializer {
	public static final String MOD_ID = "rpgcrafting";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;

	public static RegistryEntry<EntityAttribute> HAND_CRAFTING_LEVEL;

	@Override
	public void onInitialize() {
		LOGGER.info("Crafting was RPG-ified!");

		// Config
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper)AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		// Registry
		BlockRegistry.init();
		ItemGroupRegistry.init();
		CraftingRecipesRegistry.init();
		EventsRegistry.initializeEvents();
		GameRulesRegistry.init();
		ScreenHandlerTypesRegistry.registerAll();
		ServerPacketRegistry.init();
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static void info(String message) {
		LOGGER.info("[" + MOD_ID + "] [info]: " + message);
	}

}