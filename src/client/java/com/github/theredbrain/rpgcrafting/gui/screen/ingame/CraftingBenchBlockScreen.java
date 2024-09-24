package com.github.theredbrain.rpgcrafting.gui.screen.ingame;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.RPGCraftingClient;
import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgcrafting.gui.widget.ItemButtonWidget;
import com.github.theredbrain.rpgcrafting.network.packet.CraftFromCraftingBenchPacket;
import com.github.theredbrain.rpgcrafting.network.packet.ToggleUseStashForCraftingPacket;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateCraftingBenchScreenHandlerPropertyPacket;
import com.github.theredbrain.rpgcrafting.network.packet.UpdateCraftingBenchScreenHandlerSelectedRecipePacket;
import com.github.theredbrain.rpgcrafting.recipe.RPGCraftingRecipe;
import com.github.theredbrain.rpgcrafting.registry.BlockRegistry;
import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
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
public class CraftingBenchBlockScreen extends HandledScreen<CraftingBenchBlockScreenHandler> {
	private static final int RECIPE_FIELD_HEIGTH = 4;
	private static final int RECIPE_FIELD_WIDTH = 3;
	private static final int TAB_1 = 1;
	private static final int TAB_2 = 2;
	private static final int TAB_3 = 3;
	private static final int TAB_4 = 4;
	private static final Text TOGGLE_USE_STASH_FOR_CRAFTING_ON_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_use_stash_for_crafting_button_label.on");
	private static final Text TOGGLE_USE_STASH_FOR_CRAFTING_OFF_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_use_stash_for_crafting_button_label.off");
	private static final Text TOGGLE_STANDARD_CRAFTING_TAB_1_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_standard_crafting_button_label.1");
	private static final Text TOGGLE_STANDARD_CRAFTING_TAB_2_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_standard_crafting_button_label.2");
	private static final Text TOGGLE_STANDARD_CRAFTING_TAB_3_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_standard_crafting_button_label.3");
	private static final Text TOGGLE_STANDARD_CRAFTING_TAB_4_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_standard_crafting_button_label.4");
	private static final Text TOGGLE_SPECIAL_CRAFTING_TAB_1_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_special_crafting_button_label.1");
	private static final Text TOGGLE_SPECIAL_CRAFTING_TAB_2_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_special_crafting_button_label.2");
	private static final Text TOGGLE_SPECIAL_CRAFTING_TAB_3_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_special_crafting_button_label.3");
	private static final Text TOGGLE_SPECIAL_CRAFTING_TAB_4_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.toggle_special_crafting_button_label.4");
	private static final Text STANDARD_CRAFT_TAB_1_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.standard_craft_button_label.1");
	private static final Text STANDARD_CRAFT_TAB_2_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.standard_craft_button_label.2");
	private static final Text STANDARD_CRAFT_TAB_3_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.standard_craft_button_label.3");
	private static final Text STANDARD_CRAFT_TAB_4_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.standard_craft_button_label.4");
	private static final Text SPECIAL_CRAFT_TAB_1_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.special_craft_button_label.1");
	private static final Text SPECIAL_CRAFT_TAB_2_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.special_craft_button_label.2");
	private static final Text SPECIAL_CRAFT_TAB_3_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.special_craft_button_label.3");
	private static final Text SPECIAL_CRAFT_TAB_4_BUTTON_LABEL_TEXT = Text.translatable("gui.crafting_bench.special_craft_button_label.4");
	private static final Identifier RECIPE_SELECTED_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe_selected");
	private static final Identifier RECIPE_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe_highlighted");
	private static final Identifier RECIPE_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe");
	public static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/slot.png");
	public static final Identifier STORAGE_TAB_BACKGROUND_TEXTURE = RPGCrafting.identifier("textures/gui/container/crafting_bench/storage_tab_background.png");
	public static final Identifier STASH_AREA_0_BACKGROUND_TEXTURE = RPGCrafting.identifier("textures/gui/container/crafting_bench/stash_area_0_background.png");
	public static final Identifier STASH_AREA_1_BACKGROUND_TEXTURE = RPGCrafting.identifier("textures/gui/container/crafting_bench/stash_area_1_background.png");
	public static final Identifier STASH_AREA_2_BACKGROUND_TEXTURE = RPGCrafting.identifier("textures/gui/container/crafting_bench/stash_area_2_background.png");
	public static final Identifier STASH_AREA_3_BACKGROUND_TEXTURE = RPGCrafting.identifier("textures/gui/container/crafting_bench/stash_area_3_background.png");
	public static final Identifier STASH_AREA_4_BACKGROUND_TEXTURE = RPGCrafting.identifier("textures/gui/container/crafting_bench/stash_area_4_background.png");
	private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = RPGCrafting.identifier("scroll_bar/scroller_vertical_6_7");
	private static final Identifier SCROLLER_VERTICAL_6_7_DISABLED_TEXTURE = RPGCrafting.identifier("scroll_bar/scroller_vertical_6_7_disabled");
	private final int hotbarSize;
	private final int inventorySize;

	private List<RecipeEntry<RPGCraftingRecipe>> recipeList = new ArrayList<>();

	private ButtonWidget toggleUseStashForCraftingButton;

	private ButtonWidget toggleTab1Button;
	private ButtonWidget toggleTab2Button;
	private ButtonWidget toggleTab3Button;
	private ButtonWidget toggleTab4Button;
	private ButtonWidget toggleStorageTabButton;
	private ButtonWidget toggleStandardCraftingButton;
	private ButtonWidget toggleSpecialCraftingButton;
	private ButtonWidget craftButton;
	private int currentTab;
	private float scrollAmount;
	private boolean mouseClicked;
	private boolean useStashForCrafting;
	private boolean isStorageArea0ProviderInReach;
	private boolean isStorageArea1ProviderInReach;
	private boolean isStorageArea2ProviderInReach;
	private boolean isStorageArea3ProviderInReach;
	private boolean isStorageArea4ProviderInReach;
	private int scrollPosition;
	private final PlayerEntity playerEntity;

	public CraftingBenchBlockScreen(CraftingBenchBlockScreenHandler handler, PlayerInventory inventory, Text title) {
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

		this.isStorageArea0ProviderInReach = this.handler.isStorageArea0ProviderInReach();
		this.isStorageArea1ProviderInReach = this.handler.isStorageArea1ProviderInReach();
		this.isStorageArea2ProviderInReach = this.handler.isStorageArea2ProviderInReach();
		this.isStorageArea3ProviderInReach = this.handler.isStorageArea3ProviderInReach();
		this.isStorageArea4ProviderInReach = this.handler.isStorageArea4ProviderInReach();

		this.useStashForCrafting = ((DuckPlayerEntityMixin) this.playerEntity).rpgcrafting$useStashForCrafting();
		this.toggleUseStashForCraftingButton = this.addDrawableChild(ButtonWidget.builder(useStashForCrafting ? TOGGLE_USE_STASH_FOR_CRAFTING_ON_BUTTON_LABEL_TEXT : TOGGLE_USE_STASH_FOR_CRAFTING_OFF_BUTTON_LABEL_TEXT, button -> this.toggleUseStorageForCrafting()).dimensions(this.x + 61, this.y + 17, 104, 20).build());

		this.toggleTab1Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_TAB_1_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(1)).dimensions(this.x + 7, this.y + 17, 50, 20).build());
		this.toggleTab1Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_TAB_1_PROVIDER_BLOCK.getName()));

		this.toggleTab2Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_TAB_2_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(2)).dimensions(this.x + 7, this.y + 41, 50, 20).build());
		this.toggleTab2Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_TAB_2_PROVIDER_BLOCK.getName()));

		this.toggleTab3Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_TAB_3_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(3)).dimensions(this.x + 7, this.y + 65, 50, 20).build());
		this.toggleTab3Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_TAB_3_PROVIDER_BLOCK.getName()));

		this.toggleTab4Button = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.CRAFTING_TAB_4_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(4)).dimensions(this.x + 7, this.y + 89, 50, 20).build());
		this.toggleTab4Button.setTooltip(Tooltip.of(BlockRegistry.CRAFTING_TAB_4_PROVIDER_BLOCK.getName()));

		this.toggleStorageTabButton = this.addDrawableChild(ItemButtonWidget.builder(BlockRegistry.STORAGE_AREA_0_PROVIDER_BLOCK.asItem().getDefaultStack(), button -> this.toggleTab(-1)).dimensions(this.x + 7, this.y + 116, 50, 20).build());
		this.toggleStorageTabButton.setTooltip(Tooltip.of(BlockRegistry.STORAGE_AREA_0_PROVIDER_BLOCK.getName()));

		this.toggleStandardCraftingButton = this.addDrawableChild(ButtonWidget.builder(TOGGLE_STANDARD_CRAFTING_TAB_1_BUTTON_LABEL_TEXT, button -> this.toggleRecipeType(true)).dimensions(this.x + 61, this.y + 17, 65, 20).build());
		this.toggleSpecialCraftingButton = this.addDrawableChild(ButtonWidget.builder(TOGGLE_SPECIAL_CRAFTING_TAB_1_BUTTON_LABEL_TEXT, button -> this.toggleRecipeType(false)).dimensions(this.x + 61, this.y + 41, 65, 20).build());
		this.craftButton = this.addDrawableChild(ButtonWidget.builder(STANDARD_CRAFT_TAB_1_BUTTON_LABEL_TEXT, button -> this.craft()).dimensions(this.x + 130, this.y + 116, 147, 20).build());

		this.updateRecipeList();
		this.updateWidgets();

		ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
				1
		));
	}

	@Override
	protected void handledScreenTick() {
		if (this.handler.shouldScreenCalculateCraftingStatus() != 0) {
			int oldSelectedRecipe = this.handler.getSelectedRecipe();
			int oldRecipeListHash = this.recipeList.hashCode();
			ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
					0
			));
			this.updateRecipeList();
			this.calculateCraftingStatus();
			int newSelectedRecipe = oldSelectedRecipe;
			if (oldRecipeListHash != this.recipeList.hashCode()) {
				newSelectedRecipe = -1;
			}
			ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerSelectedRecipePacket(
					newSelectedRecipe
			));
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

	private void updateWidgets() {
		this.currentTab = handler.getCurrentTab();
		boolean isCurrentRecipeTypeStandard = handler.getCurrentRecipeType() == CraftingBenchBlockScreenHandler.RecipeType.STANDARD;

		for (int i = 36; i < this.handler.slots.size(); i++) {
			boolean showStorageSlots = this.currentTab == -1;
			if (i < 42) {
				((SlotCustomization) this.handler.slots.get(i)).slotcustomizationapi$setDisabledOverride(!(showStorageSlots && this.isStorageArea0ProviderInReach));
			} else if (i < 50) {
				((SlotCustomization) this.handler.slots.get(i)).slotcustomizationapi$setDisabledOverride(!(showStorageSlots && this.isStorageArea1ProviderInReach));
			} else if (i < 62) {
				((SlotCustomization) this.handler.slots.get(i)).slotcustomizationapi$setDisabledOverride(!(showStorageSlots && this.isStorageArea2ProviderInReach));
			} else if (i < 76) {
				((SlotCustomization) this.handler.slots.get(i)).slotcustomizationapi$setDisabledOverride(!(showStorageSlots && this.isStorageArea3ProviderInReach));
			} else {
				((SlotCustomization) this.handler.slots.get(i)).slotcustomizationapi$setDisabledOverride(!(showStorageSlots && this.isStorageArea4ProviderInReach));
			}
		}

		this.toggleUseStashForCraftingButton.visible = false;

		this.toggleStandardCraftingButton.visible = false;
		this.toggleSpecialCraftingButton.visible = false;
		this.craftButton.visible = false;

		this.toggleTab1Button.visible = false;
		this.toggleTab2Button.visible = false;
		this.toggleTab3Button.visible = false;
		this.toggleTab4Button.visible = false;
		this.toggleStorageTabButton.visible = this.handler.isStorageTabProviderInReach();

		this.toggleTab1Button.active = this.currentTab != TAB_1;
		this.toggleTab2Button.active = this.currentTab != TAB_2;
		this.toggleTab3Button.active = this.currentTab != TAB_3;
		this.toggleTab4Button.active = this.currentTab != TAB_4;
		this.toggleStorageTabButton.active = this.currentTab != -1;

		int y = this.y + 17;

		if (this.handler.isTab1ProviderInReach()) {
			this.toggleTab1Button.visible = true;
			this.toggleTab1Button.setY(y);
			y += 24;
		}
		if (this.handler.isTab2ProviderInReach()) {
			this.toggleTab2Button.visible = true;
			this.toggleTab2Button.setY(y);
			y += 24;
		}
		if (this.handler.isTab3ProviderInReach()) {
			this.toggleTab3Button.visible = true;
			this.toggleTab3Button.setY(y);
			y += 24;
		}
		if (this.handler.isTab4ProviderInReach()) {
			this.toggleTab4Button.visible = true;
			this.toggleTab4Button.setY(y);
		}

		if (this.currentTab >= 1) {

			this.toggleStandardCraftingButton.visible = true;
			this.toggleSpecialCraftingButton.visible = true;
			this.craftButton.visible = true;

			this.toggleStandardCraftingButton.active = !isCurrentRecipeTypeStandard;
			this.toggleSpecialCraftingButton.active = isCurrentRecipeTypeStandard;

			Text toggleStandardCraftingButtonMessage = Text.empty();
			Text toggleSpecialCraftingButtonMessage = Text.empty();
			Text craftButtonMessage = Text.empty();

			if (this.currentTab == TAB_1) {
				toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_1_BUTTON_LABEL_TEXT;
				toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_1_BUTTON_LABEL_TEXT;
				craftButtonMessage = isCurrentRecipeTypeStandard ? STANDARD_CRAFT_TAB_1_BUTTON_LABEL_TEXT : SPECIAL_CRAFT_TAB_1_BUTTON_LABEL_TEXT;
			} else if (this.currentTab == TAB_2) {
				toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_2_BUTTON_LABEL_TEXT;
				toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_2_BUTTON_LABEL_TEXT;
				craftButtonMessage = isCurrentRecipeTypeStandard ? STANDARD_CRAFT_TAB_2_BUTTON_LABEL_TEXT : SPECIAL_CRAFT_TAB_2_BUTTON_LABEL_TEXT;
			} else if (this.currentTab == TAB_3) {
				toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_3_BUTTON_LABEL_TEXT;
				toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_3_BUTTON_LABEL_TEXT;
				craftButtonMessage = isCurrentRecipeTypeStandard ? STANDARD_CRAFT_TAB_3_BUTTON_LABEL_TEXT : SPECIAL_CRAFT_TAB_3_BUTTON_LABEL_TEXT;
			} else if (this.currentTab == TAB_4) {
				toggleStandardCraftingButtonMessage = TOGGLE_STANDARD_CRAFTING_TAB_4_BUTTON_LABEL_TEXT;
				toggleSpecialCraftingButtonMessage = TOGGLE_SPECIAL_CRAFTING_TAB_4_BUTTON_LABEL_TEXT;
				craftButtonMessage = isCurrentRecipeTypeStandard ? STANDARD_CRAFT_TAB_4_BUTTON_LABEL_TEXT : SPECIAL_CRAFT_TAB_4_BUTTON_LABEL_TEXT;
			}

			this.toggleStandardCraftingButton.setMessage(toggleStandardCraftingButtonMessage);
			this.toggleSpecialCraftingButton.setMessage(toggleSpecialCraftingButtonMessage);
			this.craftButton.setMessage(craftButtonMessage);

		} else {

			this.toggleUseStashForCraftingButton.visible = true;

		}

		this.scrollPosition = 0;
		this.scrollAmount = 0.0f;
	}

	private void toggleUseStorageForCrafting() {
		this.useStashForCrafting = !this.useStashForCrafting;
		ClientPlayNetworking.send(new ToggleUseStashForCraftingPacket(
				this.useStashForCrafting
		));
		this.toggleUseStashForCraftingButton.setMessage(this.useStashForCrafting ? TOGGLE_USE_STASH_FOR_CRAFTING_ON_BUTTON_LABEL_TEXT : TOGGLE_USE_STASH_FOR_CRAFTING_OFF_BUTTON_LABEL_TEXT);
		this.updateWidgets();

		ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
				1
		));
	}

	private void toggleTab(int tabIndex) {
		this.handler.setCurrentTab(tabIndex);
		this.handler.setCurrentRecipeType(true);
		this.updateWidgets();

		ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
				1
		));
	}

	private void toggleRecipeType(boolean isStandard) {
		this.handler.setCurrentRecipeType(isStandard);
		this.updateWidgets();

		ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
				1
		));
	}

	private void craft() {
		int selectedRecipe = this.handler.getSelectedRecipe();
		if (this.client != null && this.client.player != null && this.client.interactionManager != null && selectedRecipe < recipeList.size()) {
			MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_TAKE_RESULT, 1.0F));
			ClientPlayNetworking.send(new CraftFromCraftingBenchPacket(
					this.recipeList.get(selectedRecipe).id().toString(),
					((DuckPlayerEntityMixin) this.client.player).rpgcrafting$useStashForCrafting()
			));
		}
	}

	private void calculateCraftingStatus() {
		boolean craftButtonActive = false;
		if (this.currentTab >= 1) {
			World world = this.handler.getPlayerInventory().player.getWorld();
			List<RecipeEntry<RPGCraftingRecipe>> activeRecipeList = this.recipeList;
			int selectedRecipe = this.handler.getSelectedRecipe();
			if (selectedRecipe >= 0 && selectedRecipe < activeRecipeList.size()) {
				RecipeEntry<RPGCraftingRecipe> craftingRecipeEntry = activeRecipeList.get(selectedRecipe);
				craftButtonActive = craftingRecipeEntry.value().matches(this.handler.getCraftingInputInventory(((DuckPlayerEntityMixin) this.handler.getPlayerInventory().player).rpgcrafting$useStashForCrafting()), world);
			}
		}
		this.craftButton.active = craftButtonActive;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		this.mouseClicked = false;
		if (this.currentTab >= 1) {
			int i = this.x + 62;
			int j = this.y + 63;
			int k = this.scrollPosition + (RECIPE_FIELD_HEIGTH * RECIPE_FIELD_WIDTH);

			for (int l = this.scrollPosition; l < k; ++l) {
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

					ClientPlayNetworking.send(new UpdateCraftingBenchScreenHandlerPropertyPacket(
							1
					));
					return true;
				}
			}

			i = this.x + 119;
			j = this.y + 63;
			if (mouseX >= (double) i && mouseX < (double) (i + 6) && mouseY >= (double) j && mouseY < (double) (j + 72)) {
				this.mouseClicked = true;
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.mouseClicked && this.shouldScroll()) {
			int i = this.y + 62;
			int j = i + 54;
			this.scrollAmount = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
			this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
			this.scrollPosition = (int) ((double) (this.scrollAmount * (float) this.getMaxScroll()) + 0.5) * 3;
			return true;
		} else {
			return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (this.shouldScroll()) {
			int i = this.getMaxScroll();
			float f = (float) verticalAmount / (float) i;
			this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
			this.scrollPosition = (int) ((double) (this.scrollAmount * (float) i) + 0.5) * 3;
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

		if (this.currentTab == -1) {
			context.drawTexture(STORAGE_TAB_BACKGROUND_TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
			if (this.isStorageArea0ProviderInReach) {
				context.drawTexture(STASH_AREA_0_BACKGROUND_TEXTURE, x + 169, y + 18, 0, 0, 108, 18, 108, 18);
			}
			if (this.isStorageArea1ProviderInReach) {
				context.drawTexture(STASH_AREA_1_BACKGROUND_TEXTURE, x + 205, y + 41, 0, 0, 72, 36, 72, 36);
			}
			if (this.isStorageArea2ProviderInReach) {
				context.drawTexture(STASH_AREA_2_BACKGROUND_TEXTURE, x + 205, y + 82, 0, 0, 72, 54, 72, 54);
			}
			if (this.isStorageArea3ProviderInReach) {
				context.drawTexture(STASH_AREA_3_BACKGROUND_TEXTURE, x + 68, y + 41, 0, 0, 126, 36, 126, 36);
			}
			if (this.isStorageArea4ProviderInReach) {
				context.drawTexture(STASH_AREA_4_BACKGROUND_TEXTURE, x + 68, y + 82, 0, 0, 126, 54, 126, 54);
			}
		} else {
			context.drawTexture(RPGCrafting.identifier("textures/gui/container/crafting_bench/tab_" + this.currentTab + "_background.png"), x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
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
			k = (int) (65.0F * this.scrollAmount);
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

		x = this.x;
		y = this.y;
		for (k = 0; k < (showInactiveSlots ? 27 : Math.min(this.inventorySize, 27)); ++k) {
			m = (k / 9);
			context.drawTexture(SLOT_TEXTURE, x + 61 + (k - (m * 9)) * 18, y + 150 + (m * 18), 0, 0, 18, 18, 18, 18);
		}
		for (k = 0; k < (showInactiveSlots ? 9 : Math.min(this.hotbarSize, 9)); ++k) {
			context.drawTexture(SLOT_TEXTURE, x + 61 + k * 18, y + 208, 0, 0, 18, 18, 18, 18);
		}

	}

	private boolean isInBounds(int id) {
		if (this.handler.getCurrentTab() >= 0) {
			boolean bl = id < this.recipeList.size();
			return id >= 0 && bl;
		}
		return false;
	}

	private boolean shouldScroll() {
		return this.recipeList.size() > (RECIPE_FIELD_HEIGTH * RECIPE_FIELD_WIDTH);
	}

	protected int getMaxScroll() {
//        return (this.recipeList.size() + 3 - 1) / 3 - 3; // TODO testing
		return (this.recipeList.size() + RECIPE_FIELD_WIDTH - 1) / RECIPE_FIELD_WIDTH - RECIPE_FIELD_WIDTH;
	}
}
