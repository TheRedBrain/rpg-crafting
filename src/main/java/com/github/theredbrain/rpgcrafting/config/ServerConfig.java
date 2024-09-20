package com.github.theredbrain.rpgcrafting.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "server"
)
public class ServerConfig implements ConfigData {
    @Comment("""
            The radius around crafting root blocks where tab provider
            blocks can open the crafting screen and enable their crafting tab/level.
            """)
    public int crafting_root_block_reach_radius = 10;
    @Comment("""
            When set to true, interacting with the Crafting Root block
            opens the crafting screen on tab 0.
            Optionally add 'rpgcrafting:crafting_root_block' to
            the 'provides_crafting_tab_0_level' block tag.
            """)
    public RootBlockProvidedScreen crafting_root_block_provided_screen = RootBlockProvidedScreen.NONE;
    @Comment("Additional debug log is shown in the console.")
    public boolean show_debug_log = false;
    @Comment("Additional debug messages are send in game.")
    public boolean show_debug_messages = false;
    public ServerConfig() {

    }

    public enum RootBlockProvidedScreen {
        NONE,
        CRAFTING_GRID_3X3,
        CRAFTING_TAB_0;

        RootBlockProvidedScreen() {
        }
    }
}
