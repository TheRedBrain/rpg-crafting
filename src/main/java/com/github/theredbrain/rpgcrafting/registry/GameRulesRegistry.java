package com.github.theredbrain.rpgcrafting.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class GameRulesRegistry {
	public static final GameRules.Key<GameRules.BooleanRule> KEEP_STASH_INVENTORY =
			GameRuleRegistry.register("keepStashInventory", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
	public static final GameRules.Key<GameRules.BooleanRule> CLEAR_STASH_INVENTORY_ON_DEATH =
			GameRuleRegistry.register("clearStashInventoryOnDeath", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));

	public static void init() {
	}
}
