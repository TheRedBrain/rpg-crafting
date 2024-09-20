package com.github.theredbrain.rpgcrafting.data;

import com.github.theredbrain.rpgcrafting.screen.CraftingBenchBlockScreenHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

import java.util.List;

public record CraftingRecipe(
        List<ItemStack> ingredients,
        ItemStack result,
        String unlockAdvancement,
        int tab,
        CraftingBenchBlockScreenHandler.RecipeType recipeType,
        int level
) {

    public static final Codec<CraftingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(x -> x.ingredients),
            ItemStack.CODEC.fieldOf("result").forGetter(CraftingRecipe::result),
            Codec.STRING.optionalFieldOf("unlockAdvancement", "").forGetter(x -> x.unlockAdvancement),
            Codec.INT.optionalFieldOf("tab", 0).forGetter(x -> x.tab),
            CraftingBenchBlockScreenHandler.RecipeType.CODEC.optionalFieldOf("recipeType", CraftingBenchBlockScreenHandler.RecipeType.STANDARD).forGetter(x -> x.recipeType),
            Codec.INT.optionalFieldOf("level", 0).forGetter(x -> x.level)
    ).apply(instance, CraftingRecipe::new));

    public CraftingRecipe(List<ItemStack> ingredients, ItemStack result, String unlockAdvancement, int tab, CraftingBenchBlockScreenHandler.RecipeType recipeType, int level) {
        this.ingredients = ingredients != null ? ingredients : List.of();
        this.result = result;
        this.unlockAdvancement = unlockAdvancement != null ? unlockAdvancement : "";
        this.tab = tab;
        this.recipeType = recipeType;
        this.level = level;
    }

}
