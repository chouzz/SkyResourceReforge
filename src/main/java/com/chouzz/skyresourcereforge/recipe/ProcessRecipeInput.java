package com.chouzz.skyresourcereforge.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record ProcessRecipeInput(List<ItemStack> items, List<FluidStack> fluids, float parameter, boolean strict, boolean mergeStacks)
        implements RecipeInput {
    public ProcessRecipeInput(List<ItemStack> items) {
        this(items, List.of(), Float.MAX_VALUE, true, false);
    }

    public ProcessRecipeInput(List<ItemStack> items, List<FluidStack> fluids) {
        this(items, fluids, Float.MAX_VALUE, true, false);
    }

    public ProcessRecipeInput(List<ItemStack> items, List<FluidStack> fluids, float parameter, boolean strict) {
        this(items, fluids, parameter, strict, false);
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }
}
