package com.github.theredbrain.rpgcrafting.config;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

public class ServerConfig extends Config {

	public ServerConfig() {
		super(RPGCrafting.identifier("server"));
	}

	@Comment("""
			The radius around crafting bench blocks where they check for blocks that enable additional tabs and that increase tab levels.
			""")
	public ValidatedInt crafting_bench_block_reach_radius = new ValidatedInt(10);
	@Comment("""
			The default hand crafting level.
			""")
	public ValidatedInt default_hand_crafting_level = new ValidatedInt(0);
	@Comment("""
			Show all recipes in the recipe list screen.
			When set to false, only unlocked recipes are visible.
			Recipes can be unlocked via gaining advancements, like in vanilla Minecraft
			""")
	public ValidatedBoolean show_locked_recipes_in_recipe_list = new ValidatedBoolean(true);
	@Comment("""
			Show all recipes in the hand crafting / crafting bench screens.
			When set to false, only unlocked recipes are visible.
			Recipes can be unlocked via gaining advancements, like in vanilla Minecraft
			""")
	public ValidatedBoolean show_locked_recipes_in_crafting_screens = new ValidatedBoolean(false);
	@Comment("""
			Show all unlocked special recipes in the hand crafting / crafting bench screens.
			When set to false, special recipes are only visible when unlocked and when all ingredients are available.
			Recipes can be unlocked via gaining advancements, like in vanilla Minecraft
			""")
	public ValidatedBoolean show_all_unlocked_special_recipes = new ValidatedBoolean(false);
	@Comment("The crafting list screen can be accessed via hotkey")
	public ValidatedBoolean is_crafting_list_screen_hotkey_enabled = new ValidatedBoolean(true);
	@Comment("The hand crafting screen can be accessed via hotkey")
	public ValidatedBoolean is_hand_crafting_screen_hotkey_enabled = new ValidatedBoolean(true);
}
