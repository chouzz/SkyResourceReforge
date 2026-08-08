package com.chouzz.skyresourcereforge.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record CountedIngredient(Ingredient ingredient, int count) {
    public static final MapCodec<CountedIngredient> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(CountedIngredient::ingredient),
            Codec.INT.fieldOf("count").forGetter(CountedIngredient::count)
    ).apply(instance, CountedIngredient::new));

    public static final Codec<CountedIngredient> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, CountedIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, CountedIngredient::ingredient,
            ByteBufCodecs.VAR_INT, CountedIngredient::count,
            CountedIngredient::new
    );

    public static CountedIngredient of(Ingredient ingredient, int count) {
        return new CountedIngredient(ingredient, count);
    }

    public boolean test(ItemStack stack) {
        return ingredient.test(stack);
    }

    /**
     * Returns a list of item stacks from this ingredient with the count applied.
     * Useful for JEI recipe display where ingredient count must be shown.
     */
    public List<ItemStack> getStacksWithCount() {
        List<ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : ingredient.getItems()) {
            ItemStack copy = stack.copy();
            copy.setCount(count);
            stacks.add(copy);
        }
        return stacks;
    }
}
