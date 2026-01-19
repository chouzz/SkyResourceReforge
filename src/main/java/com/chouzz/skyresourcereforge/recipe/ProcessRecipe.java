package com.chouzz.skyresourcereforge.recipe;

import com.chouzz.skyresourcereforge.registration.ModRecipeSerializers;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class ProcessRecipe implements Recipe<RecipeInput> {
    private final List<Ingredient> inputs;
    private final List<ItemStack> outputs;
    private final List<FluidStack> fluidInputs;
    private final List<FluidStack> fluidOutputs;
    private final float parameter;

    public ProcessRecipe(List<Ingredient> inputs, List<ItemStack> outputs, List<FluidStack> fluidInputs, List<FluidStack> fluidOutputs, float parameter) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.fluidInputs = fluidInputs;
        this.fluidOutputs = fluidOutputs;
        this.parameter = parameter;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        // Matching is usually handled by the machine's TileEntity logic in SkyResources
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
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

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.PROCESS_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        // This is a bit tricky since one class might be used for multiple types.
        // For now we'll default to COMBUSTION or make it dynamic if needed.
        return ModRecipeTypes.COMBUSTION.get();
    }

    public List<Ingredient> getInputs() { return inputs; }
    public List<ItemStack> getOutputs() { return outputs; }
    public List<FluidStack> getFluidInputs() { return fluidInputs; }
    public List<FluidStack> getFluidOutputs() { return fluidOutputs; }
    public float getParameter() { return parameter; }

    public static class Serializer implements RecipeSerializer<ProcessRecipe> {
        private static final MapCodec<ProcessRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.inputs),
                ItemStack.STRICT_CODEC.listOf().fieldOf("outputs").forGetter(r -> r.outputs),
                FluidStack.CODEC.listOf().optionalFieldOf("fluidInputs", List.of()).forGetter(r -> r.fluidInputs),
                FluidStack.CODEC.listOf().optionalFieldOf("fluidOutputs", List.of()).forGetter(r -> r.fluidOutputs),
                Codec.FLOAT.fieldOf("parameter").forGetter(r -> r.parameter)
        ).apply(inst, ProcessRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, ProcessRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<ProcessRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ProcessRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ProcessRecipe recipe) {
            buffer.writeInt(recipe.inputs.size());
            for (Ingredient ingredient : recipe.inputs) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }
            buffer.writeInt(recipe.outputs.size());
            for (ItemStack stack : recipe.outputs) {
                ItemStack.STREAM_CODEC.encode(buffer, stack);
            }
            buffer.writeInt(recipe.fluidInputs.size());
            for (FluidStack stack : recipe.fluidInputs) {
                FluidStack.STREAM_CODEC.encode(buffer, stack);
            }
            buffer.writeInt(recipe.fluidOutputs.size());
            for (FluidStack stack : recipe.fluidOutputs) {
                FluidStack.STREAM_CODEC.encode(buffer, stack);
            }
            buffer.writeFloat(recipe.parameter);
        }

        private static ProcessRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            int inputSize = buffer.readInt();
            List<Ingredient> inputs = new java.util.ArrayList<>(inputSize);
            for (int i = 0; i < inputSize; i++) {
                inputs.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            }
            int outputSize = buffer.readInt();
            List<ItemStack> outputs = new java.util.ArrayList<>(outputSize);
            for (int i = 0; i < outputSize; i++) {
                outputs.add(ItemStack.STREAM_CODEC.decode(buffer));
            }
            int fluidInputSize = buffer.readInt();
            List<FluidStack> fluidInputs = new java.util.ArrayList<>(fluidInputSize);
            for (int i = 0; i < fluidInputSize; i++) {
                fluidInputs.add(FluidStack.STREAM_CODEC.decode(buffer));
            }
            int fluidOutputSize = buffer.readInt();
            List<FluidStack> fluidOutputs = new java.util.ArrayList<>(fluidOutputSize);
            for (int i = 0; i < fluidOutputSize; i++) {
                fluidOutputs.add(FluidStack.STREAM_CODEC.decode(buffer));
            }
            float parameter = buffer.readFloat();
            return new ProcessRecipe(inputs, outputs, fluidInputs, fluidOutputs, parameter);
        }
    }
}
