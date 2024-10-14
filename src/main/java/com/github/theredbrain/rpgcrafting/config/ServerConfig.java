package com.github.theredbrain.rpgcrafting.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
		name = "server"
)
public class ServerConfig implements ConfigData {
	@Comment("""
			The radius around crafting bench blocks where they check for blocks that enable additional tabs and that increase tab levels.
			""")
	public int crafting_bench_block_reach_radius = 10;
	@Comment("""
			The default hand crafting level.
			""")
	public int default_hand_crafting_level = 0;
	@Comment("""
			Show all recipes in the recipe list screen.
			When set to false, only unlocked recipes are visible.
			Recipes can be unlocked via gaining advancements, like in vanilla Minecraft
			""")
	public boolean show_locked_recipes_in_recipe_list = true;
	@Comment("""
			Show all recipes in the hand crafting / crafting bench screens.
			When set to false, only unlocked recipes are visible.
			Recipes can be unlocked via gaining advancements, like in vanilla Minecraft
			""")
	public boolean show_locked_recipes_in_crafting_screens = false;
	@Comment("""
			Show all unlocked special recipes in the hand crafting / crafting bench screens.
			When set to false, special recipes are only visible when unlocked and when all ingredients are available.
			Recipes can be unlocked via gaining advancements, like in vanilla Minecraft
			""")
	public boolean show_all_unlocked_special_recipes = false;
	@Comment("The crafting list screen can be accessed via hotkey")
	public boolean is_crafting_list_screen_hotkey_enabled = true;
	@Comment("The hand crafting screen can be accessed via hotkey")
	public boolean is_hand_crafting_screen_hotkey_enabled = true;

	public ServerConfig() {

	}
}
