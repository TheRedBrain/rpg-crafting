package com.github.theredbrain.rpgcrafting.gui.screen.ingame;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.RPGCraftingClient;
import com.github.theredbrain.rpgcrafting.network.packet.CraftFromHandCraftingPacket;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateHandCraftingScreenHandlerPropertyPacket;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateHandCraftingScreenHandlerSelectedRecipePacket;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.screen.HandCraftingScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class HandCraftingScreen extends HandledScreen<HandCraftingScreenHandler> {
    private static final int RECIPE_FIELD_HEIGTH = 4;
    private static final int RECIPE_FIELD_WIDTH = 3;
    private static final Text HAND_CRAFT_BUTTON_LABEL_TEXT = Text.translatable("gui.hand_crafting.hand_craft_button_label");
    private static final Identifier RECIPE_SELECTED_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe_selected");
    private static final Identifier RECIPE_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe_highlighted");
    private static final Identifier RECIPE_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe");
    public static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/slot.png");
    private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = RPGCrafting.identifier("scroll_bar/scroller_vertical_6_7");
    private static final Identifier SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE = RPGCrafting.identifier("scroll_bar/scroller_vertical_6_7_disabled");
    private final int hotbarSize;
    private final int inventorySize;

    private List<RecipeEntry<RPGCraftingRecipe>> recipeList = new ArrayList<>();

    private ButtonWidget craftButton;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollPosition;
    private final PlayerEntity playerEntity;

    public HandCraftingScreen(HandCraftingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.playerEntity = inventory.player;
        this.hotbarSize = RPGCrafting.getActiveHotbarSize(inventory.player);
        this.inventorySize = RPGCrafting.getActiveInventorySize(inventory.player);
    }

    @Override
    protected void init() {
        this.backgroundWidth = 284;
        this.backgroundHeight = 233;

        this.playerInventoryTitleX = 62;
        this.playerInventoryTitleY = 139;
        super.init();

        this.craftButton = this.addDrawableChild(ButtonWidget.builder(HAND_CRAFT_BUTTON_LABEL_TEXT, button -> this.craft()).dimensions(this.x + 130, this.y + 116, 147, 20).build());

        this.updateRecipeList();
        ClientPlayNetworking.send(new UpdateHandCraftingScreenHandlerPropertyPacket(
                1
        ));
    }

    @Override
    protected void handledScreenTick() {
        if (this.handler.shouldScreenCalculateCraftingStatus() != 0) {
            int oldSelectedRecipe = this.handler.getSelectedRecipe();
            int oldRecipeListHash = this.recipeList.hashCode();
            ClientPlayNetworking.send(new UpdateHandCraftingScreenHandlerPropertyPacket(
                    0
            ));
            this.updateRecipeList();
            this.calculateCraftingStatus();
            int newSelectedRecipe = oldSelectedRecipe;
            if (oldRecipeListHash != this.recipeList.hashCode()) {
                newSelectedRecipe = -1;
            }
            if (oldSelectedRecipe != newSelectedRecipe) {
                ClientPlayNetworking.send(new UpdateHandCraftingScreenHandlerSelectedRecipePacket(
                        newSelectedRecipe
                ));
            }
        }
    }

    private void updateRecipeList() {
        this.recipeList.clear();
        List<RecipeEntry<RPGCraftingRecipe>> newList = this.handler.getCurrentCraftingRecipesList();
        if (this.playerEntity instanceof ClientPlayerEntity clientPlayerEntity) {
            for (RecipeEntry<RPGCraftingRecipe> recipeEntry : newList) {
                if (clientPlayerEntity.getRecipeBook().shouldDisplay(recipeEntry) || RPGCrafting.serverConfig.show_locked_recipes_in_crafting_screens) {
                    this.recipeList.add(recipeEntry);
                }
            }
        }
    }

    private void craft() {
        int selectedRecipe = this.handler.getSelectedRecipe();
        if (this.client != null && this.client.player != null && this.client.interactionManager != null && selectedRecipe < recipeList.size()) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_TAKE_RESULT, 1.0F));
            ClientPlayNetworking.send(new CraftFromHandCraftingPacket(
                    this.recipeList.get(selectedRecipe).id().toString()
            ));
        }
    }

    private void calculateCraftingStatus() {
            boolean craftButtonActive = false;
            World world = this.handler.getPlayerInventory().player.getWorld();
            List<RecipeEntry<RPGCraftingRecipe>> activeRecipeList = this.recipeList;
            int selectedRecipe = this.handler.getSelectedRecipe();
            if (selectedRecipe >= 0 && selectedRecipe < activeRecipeList.size()) {
                RecipeEntry<RPGCraftingRecipe> craftingRecipeEntry = activeRecipeList.get(selectedRecipe);
                craftButtonActive = craftingRecipeEntry.value().matches(this.handler.getCraftingInputInventory(), world);
            }
        this.craftButton.active = craftButtonActive;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
            int i = this.x + 62;
            int j = this.y + 63;
        int k = this.scrollPosition + (RECIPE_FIELD_HEIGTH * RECIPE_FIELD_WIDTH);

            for(int l = this.scrollPosition; l < k; ++l) {
                int m = l - this.scrollPosition;
                double d = mouseX - (double) (i + m % RECIPE_FIELD_WIDTH * 18);
                double e = mouseY - (double) (j + m / RECIPE_FIELD_WIDTH * 18);
                if (d >= 0.0 && e >= 0.0 && d < 18.0 && e < 18.0 && this.client != null && this.client.interactionManager != null && this.handler.onButtonClick(this.client.player, l - this.scrollPosition)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    if (this.isInBounds(l - this.scrollPosition)) {
                        this.client.interactionManager.clickButton(this.handler.syncId, l - this.scrollPosition);
                    } else {
                        this.client.interactionManager.clickButton(this.handler.syncId, -1);
                    }

                    ClientPlayNetworking.send(new UpdateHandCraftingScreenHandlerPropertyPacket(
                            1
                    ));
                    return true;
                }
            }

            i = this.x + 119;
            j = this.y + 63;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 72)) {
                this.mouseClicked = true;
            }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + 62;
            int j = i + 54;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5) * 3;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i) + 0.5) * 3;
        }

        return true;
    }

    @Override
    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = this.x;
        int y = this.y;
        int k;
        int m;
        boolean showInactiveSlots = RPGCraftingClient.clientConfigHolder.getConfig().generalClientConfig.show_inactive_slots;

        context.drawTexture(RPGCrafting.identifier("textures/gui/container/crafting_bench/tab_0_background.png"), x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);

        for (k = 0; k < (showInactiveSlots ? 27 : Math.min(this.inventorySize, 27)); ++k) {
            m = (k / 9);
            context.drawTexture(SLOT_TEXTURE, x + 61 + (k - (m * 9)) * 18, y + 150 + (m * 18), 0, 0, 18, 18, 18, 18);
        }
        for (k = 0; k < (showInactiveSlots ? 9 : Math.min(this.hotbarSize, 9)); ++k) {
            context.drawTexture(SLOT_TEXTURE, x + 61 + k * 18, y + 208, 0, 0, 18, 18, 18, 18);
        }

        int index = 0;
        List<RecipeEntry<RPGCraftingRecipe>> recipeList = this.recipeList;
            int recipeCounter = recipeList.size();
            for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + (RECIPE_FIELD_HEIGTH * RECIPE_FIELD_WIDTH), recipeCounter); i++) {
                if (i > recipeList.size()) {
                    break;
                }
                RPGCraftingRecipe craftingRecipe = recipeList.get(i).value();
                if (craftingRecipe != null && this.client != null && this.client.world != null) {

                    x = this.x + 62 + (index % RECIPE_FIELD_WIDTH * 18);
                    y = this.y + 63 + (index / RECIPE_FIELD_WIDTH) * 18;

                    ItemStack resultItemStack = craftingRecipe.getResult(this.client.world.getRegistryManager());
                    Identifier identifier;
                    if (i == this.handler.getSelectedRecipe()) {
                        identifier = RECIPE_SELECTED_TEXTURE;
                    } else if (mouseX >= x && mouseY >= y && mouseX < x + 18 && mouseY < y + 18) {
                        identifier = RECIPE_HIGHLIGHTED_TEXTURE;
                    } else {
                        identifier = RECIPE_TEXTURE;
                    }
                    context.drawGuiTexture(identifier, x, y, 18, 18);
                    context.drawItemWithoutEntity(resultItemStack, x + 1, y + 1);
                    context.drawItemInSlot(this.textRenderer, resultItemStack, x + 1, y + 1);

                    index++;
                }
            }
            x = this.x;
            y = this.y;
            k = (int)(65.0F * this.scrollAmount);
            Identifier identifier = this.shouldScroll() ? SCROLLER_VERTICAL_6_7_TEXTURE : SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE;
            context.drawGuiTexture(identifier, x + 119, y + 63 + k, 6, 7);

            int selectedRecipe = this.handler.getSelectedRecipe();
        if (selectedRecipe != -1 && this.client != null && this.client.world != null && selectedRecipe < recipeList.size()) {
            RPGCraftingRecipe craftingRecipe = recipeList.get(selectedRecipe).value();
            ItemStack resultItemStack = craftingRecipe.getResult(this.client.world.getRegistryManager());

            x = this.x + 135;
            y = this.y + 20;
            context.drawItemWithoutEntity(resultItemStack, x, y);
            context.drawItemInSlot(this.textRenderer, resultItemStack, x, y);

            context.drawText(this.textRenderer, resultItemStack.getName(), x + 20, y + 4, 4210752, false);
        }
    }

    private boolean isInBounds(int id) {
        boolean bl = id < this.recipeList.size();
        return id >= 0 && bl;
    }

    private boolean shouldScroll() {
        return this.recipeList.size() > (RECIPE_FIELD_HEIGTH * RECIPE_FIELD_WIDTH);
    }

    protected int getMaxScroll() {
//        return (this.recipeList.size() + 3 - 1) / 3 - 3; // TODO testing
        return (this.recipeList.size() + RECIPE_FIELD_WIDTH - 1) / RECIPE_FIELD_WIDTH - RECIPE_FIELD_WIDTH;
    }
}
