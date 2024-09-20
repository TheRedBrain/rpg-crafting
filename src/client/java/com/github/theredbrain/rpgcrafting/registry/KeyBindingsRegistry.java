package com.github.theredbrain.rpgcrafting.registry;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.RPGCraftingClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyBindingsRegistry {

    public static KeyBinding openHandCraftingScreen;
    public static KeyBinding openCraftingListScreen;
    public static boolean openHandCraftingScreenBoolean;
    public static boolean openCraftingListScreenBoolean;

    public static void registerKeyBindings() {
        KeyBindingsRegistry.openCraftingListScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rpgcrafting.craftingListScreen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.rpgcrafting.category"
        ));
        KeyBindingsRegistry.openHandCraftingScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rpgcrafting.handCraftingScreen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.rpgcrafting.category"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KeyBindingsRegistry.openCraftingListScreen.wasPressed()) {
                if (!openCraftingListScreenBoolean) {
                    if (RPGCrafting.serverConfig.is_crafting_list_screen_hotkey_enabled) {
                        RPGCraftingClient.openCraftingListScreen(client);
                    } else if (client.player != null){
                        client.player.sendMessage(Text.translatable("gui.crafting_list.hotkey_disabled_by_server"));
                    }
                }
                openCraftingListScreenBoolean = true;
            } else if (openCraftingListScreenBoolean) {
                openCraftingListScreenBoolean = false;
            }
            if (KeyBindingsRegistry.openHandCraftingScreen.wasPressed()) {
                if (!openHandCraftingScreenBoolean) {
                    if (RPGCrafting.serverConfig.is_hand_crafting_screen_hotkey_enabled) {
                        RPGCraftingClient.openHandCraftingScreen(client);
                    } else if (client.player != null){
                        client.player.sendMessage(Text.translatable("gui.hand_crafting.hotkey_disabled_by_server"));
                    }
                }
                openHandCraftingScreenBoolean = true;
            } else if (openHandCraftingScreenBoolean) {
                openHandCraftingScreenBoolean = false;
            }
        });
    }
}
