package com.github.theredbrain.rpgcrafting.recipe;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.recipe.input.MultipleStackRecipeInput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class RPGCraftingRecipe implements Recipe<MultipleStackRecipeInput> {
	public final Optional<RPGItemStackIngredient> upgradedItemStackIngredient;
	public final String upgradeType;
	public final List<RPGItemStackIngredient> rpgItemStackIngredients;
	public final List<RPGIngredient> rpgIngredients;
	public final ItemStack result;
	public int level;
	public int tab;
	public final String recipeType;
	public final boolean showNotification;
	public final boolean requiresUnlockAdvancement;

	public RPGCraftingRecipe(Optional<RPGItemStackIngredient> upgradedItemStackIngredient, String upgradeType, List<RPGItemStackIngredient> rpgItemStackIngredients, List<RPGIngredient> rpgIngredients, ItemStack result, int level, int tab, String recipeType, boolean showNotification, boolean requiresUnlockAdvancement) {
		this.upgradedItemStackIngredient = upgradedItemStackIngredient;
		this.upgradeType = upgradeType;
		this.rpgItemStackIngredients = rpgItemStackIngredients;
		this.rpgIngredients = rpgIngredients;
		this.result = result;
		this.level = level;
		this.tab = tab;
		this.recipeType = recipeType;
		this.showNotification = showNotification;
		this.requiresUnlockAdvancement = requiresUnlockAdvancement;
	}

	@Override
	public boolean matches(MultipleStackRecipeInput input, World world) {
		boolean bl = true;
		ItemStack itemStack;
		int inputSize = input.getSize();
		Inventory playerInventoryCopy = new SimpleInventory(inputSize);
		int j;

		for (j = 0; j < inputSize; j++) {
			playerInventoryCopy.setStack(j, input.getStackInSlot(j).copy());
		}

		Optional<RPGItemStackIngredient> optionalRPGItemStackIngredient = this.upgradedItemStackIngredient;
		if (optionalRPGItemStackIngredient.isPresent() && !checkItemStackIngredient(optionalRPGItemStackIngredient.get(), playerInventoryCopy.getStack(0))) {
			return false;
		}

		for (RPGItemStackIngredient itemStackIngredient : this.rpgItemStackIngredients) {
			int ingredientCount = itemStackIngredient.itemStack.getCount();
			for (j = 1; j < inputSize; j++) {
				bl = false;
				if (checkItemStackIngredient(itemStackIngredient, playerInventoryCopy.getStack(j))) {
					itemStack = playerInventoryCopy.getStack(j).copy();
					int stackCount = itemStack.getCount();
					if (stackCount > ingredientCount) {
						itemStack.setCount(stackCount - ingredientCount);
						ingredientCount = 0;
						playerInventoryCopy.setStack(j, itemStack);
					} else {
						ingredientCount -= stackCount;
						playerInventoryCopy.setStack(j, ItemStack.EMPTY);
					}
					if (ingredientCount <= 0) {
						bl = true;
						break;
					}
				}
			}
			if (!bl) {
				return false;
			}
		}
		for (RPGIngredient rpgIngredient : this.rpgIngredients) {
			for (j = 1; j < inputSize; j++) {
				bl = false;
				if (rpgIngredient.ingredient().test(playerInventoryCopy.getStack(j))) {
					itemStack = playerInventoryCopy.getStack(j).copy();
					int stackCount = itemStack.getCount();
					if (stackCount > 1) {
						itemStack.setCount(stackCount - 1);
						playerInventoryCopy.setStack(j, itemStack);
						bl = true;
						break;
					} else {
						playerInventoryCopy.setStack(j, ItemStack.EMPTY);
						bl = true;
						break;
					}
				}
			}
			if (!bl) {
				return false;
			}
		}
		return bl;
	}

	public boolean hasIngredient(MultipleStackRecipeInput input) {
		int inputSize = input.getSize();
		Inventory inputCopy = new SimpleInventory(inputSize);
		int j;
		for (j = 0; j < inputSize; j++) {
			inputCopy.setStack(j, input.getStackInSlot(j).copy());
		}

		for (RPGItemStackIngredient itemStackIngredient : this.rpgItemStackIngredients) {
			for (j = 0; j < inputSize; j++) {
				if (checkItemStackIngredient(itemStackIngredient, inputCopy.getStack(j))) {
					return true;
				}
			}
		}
		for (RPGIngredient rpgIngredient : this.rpgIngredients) {
			for (j = 0; j < inputSize; j++) {
				if (rpgIngredient.ingredient().test(inputCopy.getStack(j))) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public ItemStack craft(MultipleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
		return this.result.copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
		return this.result;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	public static class Type implements RecipeType<RPGCraftingRecipe> {
		private Type() {
		}

		public static final Type INSTANCE = new Type();
		public static final String ID = "rpg_crafting_recipe";
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	public interface RecipeFactory {
		RPGCraftingRecipe create(Optional<RPGItemStackIngredient> upgradedItemStackIngredient, String upgradeType, List<RPGItemStackIngredient> rpgItemStackIngredients, List<RPGIngredient> rpgIngredients, ItemStack result, int level, int tab, String recipeType, boolean showNotification, boolean requiresUnlockAdvancement);
	}

	public static class Serializer implements RecipeSerializer<RPGCraftingRecipe> {

		public static final Serializer INSTANCE = new Serializer();

		public static final Identifier ID = RPGCrafting.identifier("rpg_crafting_recipe");

		public static final MapCodec<RPGCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
						RPGItemStackIngredient.CODEC.optionalFieldOf("upgraded_item_stack_ingredient").forGetter(recipe -> recipe.upgradedItemStackIngredient),
						Codec.STRING.optionalFieldOf("upgrade_type", "none").forGetter(recipe -> recipe.recipeType),
						RPGItemStackIngredient.CODEC.listOf().fieldOf("rpg_item_stack_ingredients").forGetter(recipe -> recipe.rpgItemStackIngredients),
						RPGIngredient.CODEC.listOf().fieldOf("rpg_ingredients").forGetter(recipe -> recipe.rpgIngredients),
						ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
						Codec.INT.optionalFieldOf("level", 0).forGetter(recipe -> recipe.level),
						Codec.INT.optionalFieldOf("tab", 0).forGetter(recipe -> recipe.tab),
						Codec.STRING.optionalFieldOf("recipe_type", "").forGetter(recipe -> recipe.recipeType),
						Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(recipe -> recipe.showNotification),
						Codec.BOOL.optionalFieldOf("requires_unlock_advancement", false).forGetter(recipe -> recipe.requiresUnlockAdvancement)
				).apply(instance, RPGCraftingRecipe::new)
		);
		public static final PacketCodec<RegistryByteBuf, RPGCraftingRecipe> PACKET_CODEC = PacketCodec.ofStatic(
				RPGCraftingRecipe.Serializer::write, RPGCraftingRecipe.Serializer::read
		);

		@Override
		public MapCodec<RPGCraftingRecipe> codec() {
			return CODEC;
		}

		@Override
		public PacketCodec<RegistryByteBuf, RPGCraftingRecipe> packetCodec() {
			return PACKET_CODEC;
		}

		private static RPGCraftingRecipe read(RegistryByteBuf buf) {
			Optional<RPGItemStackIngredient> upgradedItemStackIngredient = RPGItemStackIngredient.PACKET_CODEC.collect(PacketCodecs::optional).decode(buf);
			String upgradeType = buf.readString();
			int itemStackIngredientsSize = buf.readInt();
			List<RPGItemStackIngredient> itemStackIngredients = new ArrayList<>();
			for (int i = 0; i < itemStackIngredientsSize; i++) {
				itemStackIngredients.add(RPGItemStackIngredient.PACKET_CODEC.decode(buf));
			}
			int rpgIngredientsSize = buf.readInt();
			List<RPGIngredient> rpgIngredients = new ArrayList<>();
			for (int i = 0; i < rpgIngredientsSize; i++) {
				rpgIngredients.add(RPGIngredient.PACKET_CODEC.decode(buf));
			}
			ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
			int level = buf.readInt();
			int tab = buf.readInt();
			String recipeType = buf.readString();
			boolean showNotification = buf.readBoolean();
			boolean requiresUnlockAdvancement = buf.readBoolean();
			return new RPGCraftingRecipe(upgradedItemStackIngredient, upgradeType, itemStackIngredients, rpgIngredients, result, level, tab, recipeType, showNotification, requiresUnlockAdvancement);
		}

		private static void write(RegistryByteBuf buf, RPGCraftingRecipe recipe) {
			RPGItemStackIngredient.PACKET_CODEC.collect(PacketCodecs::optional).encode(buf, recipe.upgradedItemStackIngredient);
			buf.writeString(recipe.upgradeType);
			buf.writeInt(recipe.rpgItemStackIngredients.size());
			for (RPGItemStackIngredient ingredient : recipe.rpgItemStackIngredients) {
				RPGItemStackIngredient.PACKET_CODEC.encode(buf, ingredient);
			}
			buf.writeInt(recipe.rpgIngredients.size());
			for (RPGIngredient rpgIngredient : recipe.rpgIngredients) {
				RPGIngredient.PACKET_CODEC.encode(buf, rpgIngredient);
			}
			ItemStack.PACKET_CODEC.encode(buf, recipe.result);
			buf.writeInt(recipe.level);
			buf.writeInt(recipe.tab);
			buf.writeString(recipe.recipeType);
			buf.writeBoolean(recipe.showNotification);
			buf.writeBoolean(recipe.requiresUnlockAdvancement);
		}
	}

	public static boolean checkItemStackIngredient(RPGItemStackIngredient rpgItemStackIngredient, ItemStack itemStack) {
		if (rpgItemStackIngredient.completeComponentMatch) {
			return ItemStack.areItemsAndComponentsEqual(rpgItemStackIngredient.itemStack, itemStack);
		} else {
			if (!rpgItemStackIngredient.itemStack.isOf(itemStack.getItem())) {
				return false;
			} else {
				return (rpgItemStackIngredient.itemStack.isEmpty() && itemStack.isEmpty()) || itemStack.getComponentChanges().entrySet().containsAll(rpgItemStackIngredient.itemStack.getComponentChanges().entrySet());
			}
		}
	}

	@Override
	public boolean showNotification() {
		return this.showNotification;
	}

	public record RPGItemStackIngredient(
			ItemStack itemStack,
			boolean completeComponentMatch,
			boolean isConsumed
	) {

		public static final PacketCodec<RegistryByteBuf, RPGItemStackIngredient> PACKET_CODEC = new PacketCodec<RegistryByteBuf, RPGItemStackIngredient>() {
			public RPGItemStackIngredient decode(RegistryByteBuf registryByteBuf) {
				ItemStack itemStack = ItemStack.PACKET_CODEC.decode(registryByteBuf);
				boolean compareAllComponents = PacketCodecs.BOOL.decode(registryByteBuf);
				boolean isConsumed = PacketCodecs.BOOL.decode(registryByteBuf);
				return new RPGItemStackIngredient(itemStack, compareAllComponents, isConsumed);
			}

			public void encode(RegistryByteBuf registryByteBuf, RPGItemStackIngredient itemStackIngredient) {
				ItemStack.PACKET_CODEC.encode(registryByteBuf, itemStackIngredient.itemStack);
				PacketCodecs.BOOL.encode(registryByteBuf, itemStackIngredient.completeComponentMatch);
				PacketCodecs.BOOL.encode(registryByteBuf, itemStackIngredient.isConsumed);
			}
		};

		public static final Codec<RPGItemStackIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ItemStack.VALIDATED_CODEC.fieldOf("item_stack").forGetter(x -> x.itemStack),
				Codec.BOOL.optionalFieldOf("complete_component_match", true).forGetter(x -> x.completeComponentMatch),
				Codec.BOOL.optionalFieldOf("is_consumed", true).forGetter(x -> x.isConsumed)
		).apply(instance, RPGItemStackIngredient::new));

		public RPGItemStackIngredient(
				ItemStack itemStack,
				boolean completeComponentMatch,
				boolean isConsumed
		) {
			this.itemStack = itemStack;
			this.completeComponentMatch = completeComponentMatch;
			this.isConsumed = isConsumed;
		}
	}

	public record RPGIngredient(
			Ingredient ingredient,
			boolean isConsumed
	) {

		public static final PacketCodec<RegistryByteBuf, RPGIngredient> PACKET_CODEC = new PacketCodec<RegistryByteBuf, RPGIngredient>() {
			public RPGIngredient decode(RegistryByteBuf registryByteBuf) {
				Ingredient ingredient = Ingredient.PACKET_CODEC.decode(registryByteBuf);
				boolean isConsumed = PacketCodecs.BOOL.decode(registryByteBuf);
				return new RPGIngredient(ingredient, isConsumed);
			}

			public void encode(RegistryByteBuf registryByteBuf, RPGIngredient itemStackIngredient) {
				Ingredient.PACKET_CODEC.encode(registryByteBuf, itemStackIngredient.ingredient);
				PacketCodecs.BOOL.encode(registryByteBuf, itemStackIngredient.isConsumed);
			}
		};

		public static final Codec<RPGIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
				Codec.BOOL.optionalFieldOf("is_consumed", true).forGetter(x -> x.isConsumed)
		).apply(instance, RPGIngredient::new));

		public RPGIngredient(
				Ingredient ingredient,
				boolean isConsumed
		) {
			this.ingredient = ingredient;
			this.isConsumed = isConsumed;
		}
	}

	public static enum UpgradeType implements StringIdentifiable {
		NONE("none"),
		COPY_COMPONENTS("copy_components");

		private final String name;

		private UpgradeType(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return this.name;
		}

		public static final Codec<UpgradeType> CODEC = Codec.STRING.xmap(UpgradeType::valueOf, Enum::name);

		public static UpgradeType byName(String name) {
			return Arrays.stream(UpgradeType.values()).filter(upgradeType -> upgradeType.asString().equals(name)).findFirst().orElse(NONE);
		}

		public Text asText() {
			return Text.translatable("gui.rpg_crafting_recipe.upgradeType." + this.name);
		}
	}
}
