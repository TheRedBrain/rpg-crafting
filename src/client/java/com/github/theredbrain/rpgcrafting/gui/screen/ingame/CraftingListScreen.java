package com.github.theredbrain.rpgcrafting.gui.screen.ingame;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.data.CraftingRecipe;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateCraftingBenchScreenHandlerPropertyPacket;
import com.github.theredbrain.rpgcrafting.registry.CraftingRecipesRegistry;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import com.github.theredbrain.rpgcrafting.screen.CraftingListScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CraftingListScreen extends HandledScreen<CraftingListScreenHandler> {
    private static final Identifier RECIPE_SELECTED_TEXTURE = RPGCrafting.identifier("container/crafting_bench_screen/recipe_selected");
    private static final Identifier RECIPE_HIGHLIGHTED_TEXTURE = RPGCrafting.identifier("container/crafting_bench_screen/recipe_highlighted");
    private static final Identifier RECIPE_TEXTURE = RPGCrafting.identifier("container/crafting_bench_screen/recipe");
    public static final Identifier CRAFTING_LIST_BACKGROUND_TEXTURE = RPGCrafting.identifier("textures/gui/container/crafting_bench/crafting_list_background.png");
    private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = RPGCrafting.identifier("scroll_bar/scroller_vertical_6_7");
    private static final Identifier SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE = RPGCrafting.identifier("scroll_bar/scroller_vertical_6_7_disabled");

    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollPosition;

    public CraftingListScreen(CraftingListScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        this.backgroundWidth = 284;
        this.backgroundHeight = 233;

        this.playerInventoryTitleX = 62;
        this.playerInventoryTitleY = 139;
        super.init();

        this.handler.calculateUnlockedRecipes();

        ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
                1
        ));
    }

    @Override
    protected void handledScreenTick() {
        if (this.handler.shouldScreenCalculateRecipeList() != 0) {
            ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
                    0
            ));
        }
        this.handler.calculateUnlockedRecipes();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
//        if (this.currentTab >= 0) {
            int i = this.x + 62;
            int j = this.y + 63;
            int k = this.scrollPosition + 18;

            for(int l = this.scrollPosition; l < k; ++l) {
                int m = l - this.scrollPosition;
                double d = mouseX - (double)(i + m % 3 * 18);
                double e = mouseY - (double)(j + m / 3 * 18);
                if (d >= 0.0 && e >= 0.0 && d < 18.0 && e < 18.0 && this.client != null && this.client.interactionManager != null && this.handler.onButtonClick(this.client.player, l)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.client.interactionManager.clickButton(this.handler.syncId, l);

                    return true;
                }
            }

            i = this.x + 119;
            j = this.y + 27;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 108)) {
                this.mouseClicked = true;
            }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) { // TODO
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
            context.drawTexture(CRAFTING_LIST_BACKGROUND_TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
            int k;
            int index = 0;
            List<Identifier> recipeList = this.handler.getCurrentCraftingRecipesList();
            int recipeCounter = recipeList.size();
            for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 18, recipeCounter); i++) {
                if (i > recipeList.size()) {
                    break;
                }
                CraftingRecipe craftingRecipe = CraftingRecipesRegistry.registeredCraftingRecipes.get(recipeList.get(i));
                if (craftingRecipe != null) {

                    x = this.x + 62 + (index % 3 * 18);
                    y = this.y + 27 + (index / 3) * 18;

                    ItemStack resultItemStack = craftingRecipe.result();
//                    k = x + y * this.backgroundWidth;
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
            if (selectedRecipe != -1) {
                CraftingRecipe craftingRecipe = CraftingRecipesRegistry.registeredCraftingRecipes.get(recipeList.get(selectedRecipe));
                List<ItemStack> ingredientStacks = craftingRecipe.ingredients();
                CraftingBenchBlockScreenHandler.RecipeType recipeType = craftingRecipe.recipeType();
                int level = craftingRecipe.level();
                int tab = craftingRecipe.tab();
                ItemStack resultItemStack = craftingRecipe.result();

                x = this.x + 135;
                y = this.y + 20;
//                k = x + y * this.backgroundWidth;
                context.drawItemWithoutEntity(resultItemStack, x, y);
                context.drawItemInSlot(this.textRenderer, resultItemStack, x, y);

                context.drawText(this.textRenderer, resultItemStack.getName(), x + 20, y + 4, 4210752, false);
            }
    }

    private boolean shouldScroll() {
        return this.handler.getCurrentCraftingRecipesList().size() > 18;
    }

    protected int getMaxScroll() {
        return (this.handler.getCurrentCraftingRecipesList().size() + 3 - 1) / 3 - 3; // TODO ?
    }
}
