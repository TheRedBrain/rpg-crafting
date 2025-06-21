package com.github.theredbrain.rpgcrafting.config;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

public class ServerConfig extends Config {

	public ServerConfig() {
		super(RPGCrafting.identifier("server"));
	}

	public ValidatedInt crafting_bench_block_reach_radius = new ValidatedInt(10);
	public ValidatedBoolean show_locked_recipes_in_recipe_list = new ValidatedBoolean(true);
	public ValidatedBoolean show_locked_recipes_in_crafting_screens = new ValidatedBoolean(false);
	public ValidatedBoolean show_all_unlocked_special_recipes = new ValidatedBoolean(false);
	public ValidatedBoolean is_crafting_list_screen_hotkey_enabled = new ValidatedBoolean(true);
	public ValidatedBoolean is_hand_crafting_screen_hotkey_enabled = new ValidatedBoolean(true);
	public ValidatedEnum<RPGCrafting.CraftingLevelCalculation> crafting_bench_level_calculation = new ValidatedEnum<RPGCrafting.CraftingLevelCalculation>(RPGCrafting.CraftingLevelCalculation.ADDITION);
}
