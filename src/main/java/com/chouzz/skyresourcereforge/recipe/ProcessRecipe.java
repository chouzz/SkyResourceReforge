package com.chouzz.skyresourcereforge.recipe;

import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class ProcessRecipe implements Recipe<ProcessRecipeInput> {
    private final List<Ingredient> inputs;
    private final List<ItemStack> outputs;
    private final List<FluidStack> fluidInputs;
    private final List<FluidStack> fluidOutputs;
    private final float intParameter;

    public ProcessRecipe(List<Ingredient> inputs, List<ItemStack> outputs, List<FluidStack> fluidInputs, List<FluidStack> fluidOutputs, float intParameter) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.fluidInputs = fluidInputs;
        this.fluidOutputs = fluidOutputs;
        this.intParameter = intParameter;
    }

    @Override
    public boolean matches(ProcessRecipeInput input, Level level) {
        if (input.size() < inputs.size()) return false;
        for (int i = 0; i < inputs.size(); i++) {
            if (!inputs.get(i).test(input.getItem(i))) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(ProcessRecipeInput input, HolderLookup.Provider registries) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0);
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public List<Ingredient> getInputs() {
        return inputs;
    }

    public List<FluidStack> getFluidInputs() {
        return fluidInputs;
    }

    public List<FluidStack> getFluidOutputs() {
        return fluidOutputs;
    }

    public float getIntParameter() {
        return intParameter;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.COMBUSTION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.COMBUSTION_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ProcessRecipe> {
        public static final MapCodec<ProcessRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.inputs),
                ItemStack.STRICT_CODEC.listOf().fieldOf("results").forGetter(r -> r.outputs),
                FluidStack.CODEC.listOf().optionalFieldOf("fluidInputs", List.of()).forGetter(r -> r.fluidInputs),
                FluidStack.CODEC.listOf().optionalFieldOf("fluidOutputs", List.of()).forGetter(r -> r.fluidOutputs),
                Codec.FLOAT.fieldOf("parameter").forGetter(r -> r.intParameter)
        ).apply(inst, ProcessRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ProcessRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.inputs,
                ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.outputs,
                FluidStack.STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.fluidInputs,
                FluidStack.STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.fluidOutputs,
                ByteBufCodecs.FLOAT, r -> r.intParameter,
                ProcessRecipe::new
        );

        @Override
        public MapCodec<ProcessRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ProcessRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
