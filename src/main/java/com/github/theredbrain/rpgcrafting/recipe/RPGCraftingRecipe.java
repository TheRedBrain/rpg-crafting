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
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RPGCraftingRecipe implements Recipe<MultipleStackRecipeInput> {
	public final List<Ingredient> ingredients;
	public final ItemStack result;
	public int level;
	public int tab;
	public final String recipeType;
	public final boolean showNotification;

	public RPGCraftingRecipe(List<Ingredient> ingredients, ItemStack result, int level, int tab, String recipeType, boolean showNotification) {
		this.ingredients = ingredients;
		this.result = result;
		this.level = level;
		this.tab = tab;
		this.recipeType = recipeType;
		this.showNotification = showNotification;
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
						Ingredient.DISALLOW_EMPTY_CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
						ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
						Codec.INT.optionalFieldOf("level", 0).forGetter(recipe -> recipe.level),
						Codec.INT.optionalFieldOf("tab", 0).forGetter(recipe -> recipe.tab),
						Codec.STRING.optionalFieldOf("recipeType", "").forGetter(recipe -> recipe.recipeType),
						Codec.BOOL.optionalFieldOf("showNotification", true).forGetter(recipe -> recipe.showNotification)
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
			return new RPGCraftingRecipe(ingredients, result, level, tab, recipeType, showNotification);
		}

		private static void write(RegistryByteBuf buf, RPGCraftingRecipe recipe) {
			buf.writeInt(recipe.ingredients.size());
			for (Ingredient ingredient : recipe.ingredients) {
				Ingredient.PACKET_CODEC.encode(buf, ingredient);
			}
			ItemStack.PACKET_CODEC.encode(buf, recipe.result);
			buf.writeInt(recipe.level);
			buf.writeInt(recipe.tab);
			buf.writeString(recipe.recipeType);
			buf.writeBoolean(recipe.showNotification);
		}
	}

	@Override
	public boolean showNotification() {
		return this.showNotification;
	}

}
