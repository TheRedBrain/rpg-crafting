package com.github.theredbrain.rpgcrafting.recipe;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.recipe.input.MultipleStackRecipeInput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentType;
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
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class RPGCraftingRecipe implements Recipe<MultipleStackRecipeInput> {
	public final List<ItemStackIngredient> itemStackIngredients;
	public final List<Ingredient> ingredients;
	public final ItemStack result;
	public int level;
	public int tab;
	public final String recipeType;
	public final boolean showNotification;
	public final boolean requiresUnlockAdvancement;

	public RPGCraftingRecipe(List<ItemStackIngredient> itemStackIngredients, List<Ingredient> ingredients, ItemStack result, int level, int tab, String recipeType, boolean showNotification, boolean requiresUnlockAdvancement) {
		this.itemStackIngredients = itemStackIngredients;
		this.ingredients = ingredients;
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

		for (ItemStackIngredient itemStackIngredient : this.itemStackIngredients) {
			int ingredientCount = itemStackIngredient.itemStack.getCount();
			for (j = 0; j < inputSize; j++) {
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
		for (Ingredient ingredient : this.ingredients) {
			for (j = 0; j < inputSize; j++) {
				bl = false;
				if (ingredient.test(playerInventoryCopy.getStack(j))) {
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

		for (ItemStackIngredient itemStackIngredient : this.itemStackIngredients) {
			for (j = 0; j < inputSize; j++) {
				if (checkItemStackIngredient(itemStackIngredient, inputCopy.getStack(j))) {
					return true;
				}
			}
		}
		for (Ingredient ingredient : this.ingredients) {
			for (j = 0; j < inputSize; j++) {
				if (ingredient.test(inputCopy.getStack(j))) {
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

	public static class Serializer implements RecipeSerializer<RPGCraftingRecipe> {

		public static final Serializer INSTANCE = new Serializer();

		public static final Identifier ID = RPGCrafting.identifier("rpg_crafting_recipe");

		public static final MapCodec<RPGCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
						ItemStackIngredient.CODEC.listOf().fieldOf("itemStackIngredients").forGetter(recipe -> recipe.itemStackIngredients),
						Ingredient.DISALLOW_EMPTY_CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
						ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
						Codec.INT.optionalFieldOf("level", 0).forGetter(recipe -> recipe.level),
						Codec.INT.optionalFieldOf("tab", 0).forGetter(recipe -> recipe.tab),
						Codec.STRING.optionalFieldOf("recipeType", "").forGetter(recipe -> recipe.recipeType),
						Codec.BOOL.optionalFieldOf("showNotification", true).forGetter(recipe -> recipe.showNotification),
						Codec.BOOL.optionalFieldOf("requiresUnlockAdvancement", false).forGetter(recipe -> recipe.requiresUnlockAdvancement)
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
			int itemStackIngredientsSize = buf.readInt();
			List<ItemStackIngredient> itemStackIngredients = new ArrayList<>();
			for (int i = 0; i < itemStackIngredientsSize; i++) {
				itemStackIngredients.add(ItemStackIngredient.PACKET_CODEC.decode(buf));
			}
			int ingredientsSize = buf.readInt();
			List<Ingredient> ingredients = new ArrayList<>();
			for (int i = 0; i < ingredientsSize; i++) {
				ingredients.add(Ingredient.PACKET_CODEC.decode(buf));
			}
			ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
			int level = buf.readInt();
			int tab = buf.readInt();
			String recipeType = buf.readString();
			boolean showNotification = buf.readBoolean();
			boolean requiresUnlockAdvancement = buf.readBoolean();
			return new RPGCraftingRecipe(itemStackIngredients, ingredients, result, level, tab, recipeType, showNotification, requiresUnlockAdvancement);
		}

		private static void write(RegistryByteBuf buf, RPGCraftingRecipe recipe) {
			buf.writeInt(recipe.itemStackIngredients.size());
			for (ItemStackIngredient ingredient : recipe.itemStackIngredients) {
				ItemStackIngredient.PACKET_CODEC.encode(buf, ingredient);
			}
			buf.writeInt(recipe.ingredients.size());
			for (Ingredient ingredient : recipe.ingredients) {
				Ingredient.PACKET_CODEC.encode(buf, ingredient);
			}
			ItemStack.PACKET_CODEC.encode(buf, recipe.result);
			buf.writeInt(recipe.level);
			buf.writeInt(recipe.tab);
			buf.writeString(recipe.recipeType);
			buf.writeBoolean(recipe.showNotification);
			buf.writeBoolean(recipe.requiresUnlockAdvancement);
		}
	}

	public static boolean checkItemStackIngredient(ItemStackIngredient itemStackIngredient, ItemStack itemStack) {
		if (itemStackIngredient.completeComponentMatch) {
			return ItemStack.areItemsAndComponentsEqual(itemStackIngredient.itemStack, itemStack);
		} else {
			if (!itemStackIngredient.itemStack.isOf(itemStack.getItem())) {
				return false;
			} else {
				return itemStackIngredient.itemStack.isEmpty() && itemStack.isEmpty() ? true : itemStack.getComponentChanges().entrySet().containsAll(itemStackIngredient.itemStack.getComponentChanges().entrySet());
			}
		}
	}

	@Override
	public boolean showNotification() {
		return this.showNotification;
	}

	public record ItemStackIngredient(
			ItemStack itemStack,
			boolean completeComponentMatch
	) {

		public static final PacketCodec<RegistryByteBuf, ItemStackIngredient> PACKET_CODEC = new PacketCodec<RegistryByteBuf, ItemStackIngredient>() {
			public ItemStackIngredient decode(RegistryByteBuf registryByteBuf) {
				ItemStack itemStack = ItemStack.PACKET_CODEC.decode(registryByteBuf);
				boolean compareAllComponents = PacketCodecs.BOOL.decode(registryByteBuf);
				return new ItemStackIngredient(itemStack, compareAllComponents);
			}

			public void encode(RegistryByteBuf registryByteBuf, ItemStackIngredient itemStackIngredient) {
				ItemStack.PACKET_CODEC.encode(registryByteBuf, itemStackIngredient.itemStack);
				PacketCodecs.BOOL.encode(registryByteBuf, itemStackIngredient.completeComponentMatch);
			}
		};

		public static final Codec<ItemStackIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ItemStack.VALIDATED_CODEC.fieldOf("itemStack").forGetter(x -> x.itemStack),
				Codec.BOOL.optionalFieldOf("completeComponentMatch", true).forGetter(x -> x.completeComponentMatch)
		).apply(instance, ItemStackIngredient::new));

		public ItemStackIngredient(
				ItemStack itemStack,
				boolean completeComponentMatch
		) {
			this.itemStack = itemStack;
			this.completeComponentMatch = completeComponentMatch;
		}
	}

}
