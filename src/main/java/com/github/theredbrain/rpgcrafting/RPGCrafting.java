package com.github.theredbrain.rpgcrafting;

import com.github.theredbrain.rpgcrafting.advancement.criterion.InteractWithRPGCraftingStationCriterion;
import com.github.theredbrain.rpgcrafting.compatibility.InventorySizeAttributesCompat;
import com.github.theredbrain.rpgcrafting.config.ServerConfig;
import com.github.theredbrain.rpgcrafting.registry.AdvancementCriteriaRegistry;
import com.github.theredbrain.rpgcrafting.registry.BlockRegistry;
import com.github.theredbrain.rpgcrafting.registry.ItemGroupRegistry;
import com.github.theredbrain.rpgcrafting.registry.RecipeRegistry;
import com.github.theredbrain.rpgcrafting.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpgcrafting.registry.ServerPacketRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPGCrafting implements ModInitializer {
	public static final String MOD_ID = "rpgcrafting";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static RegistryEntry<EntityAttribute> HAND_CRAFTING_LEVEL;
	public static RegistryEntry<EntityAttribute> CRAFTING_TAB_1_LEVEL;
	public static RegistryEntry<EntityAttribute> CRAFTING_TAB_2_LEVEL;
	public static RegistryEntry<EntityAttribute> CRAFTING_TAB_3_LEVEL;
	public static RegistryEntry<EntityAttribute> CRAFTING_TAB_4_LEVEL;

	public static InteractWithRPGCraftingStationCriterion INTERACTED_WITH_RPG_CRAFTING_STATION;

	public static final boolean isInventorySizeAttributesLoaded = FabricLoader.getInstance().isModLoaded("inventorysizeattributes");

	public static int getActiveInventorySize(PlayerEntity player) {
		return isInventorySizeAttributesLoaded ? InventorySizeAttributesCompat.getActiveInventorySize(player) : 27;
	}

	public static int getActiveHotbarSize(PlayerEntity player) {
		return isInventorySizeAttributesLoaded ? InventorySizeAttributesCompat.getActiveHotbarSize(player) : 9;
	}

	public enum CraftingLevelCalculation {
		ADDITION,
		BLOCKS_REQUIRED,
		HIGHER_VALUE;

		CraftingLevelCalculation() {
		}
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Crafting was RPG-ified!");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new, RegisterType.BOTH);

		// Registry
		AdvancementCriteriaRegistry.init();
		BlockRegistry.init();
		ItemGroupRegistry.init();
		RecipeRegistry.init();
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