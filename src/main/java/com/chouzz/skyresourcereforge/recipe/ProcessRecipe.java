package com.chouzz.skyresourcereforge.recipe;

import com.chouzz.skyresourcereforge.registration.ModRecipeSerializers;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProcessRecipe implements Recipe<RecipeInput> {
    private final ResourceLocation recipeTypeId;
    private final List<CountedIngredient> inputs;
    private final List<ItemStack> outputs;
    private final List<FluidStack> fluidInputs;
    private final List<FluidStack> fluidOutputs;
    private final float parameter;

    public ProcessRecipe(ResourceLocation recipeTypeId, List<CountedIngredient> inputs, List<ItemStack> outputs,
                         List<FluidStack> fluidInputs, List<FluidStack> fluidOutputs, float parameter) {
        this.recipeTypeId = recipeTypeId;
        this.inputs = inputs;
        this.outputs = outputs;
        this.fluidInputs = fluidInputs;
        this.fluidOutputs = fluidOutputs;
        this.parameter = parameter;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        if (input instanceof ProcessRecipeInput processInput) {
            return matches(processInput);
        }
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }
        return matches(new ProcessRecipeInput(items));
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
        Optional<RecipeType<?>> type = BuiltInRegistries.RECIPE_TYPE.getOptional(recipeTypeId);
        return type.orElse(ModRecipeTypes.COMBUSTION.get());
    }

    public ResourceLocation getRecipeTypeId() { return recipeTypeId; }
    public List<CountedIngredient> getInputs() { return inputs; }
    public List<ItemStack> getOutputs() { return outputs; }
    public List<FluidStack> getFluidInputs() { return fluidInputs; }
    public List<FluidStack> getFluidOutputs() { return fluidOutputs; }
    public float getParameter() { return parameter; }

    private boolean matches(ProcessRecipeInput input) {
        List<ItemStack> items = filterNonEmpty(input.items());
        List<FluidStack> fluids = filterNonEmptyFluids(input.fluids());
        if (input.mergeStacks()) {
            items = mergeStacks(items);
        }
        if (input.strict() && items.size() != inputs.size()) {
            return false;
        }
        if (!matchItems(items, input.strict())) {
            return false;
        }
        if (!matchFluids(fluids, input.strict())) {
            return false;
        }
        return input.parameter() >= parameter;
    }

    private List<ItemStack> filterNonEmpty(List<ItemStack> items) {
        List<ItemStack> filtered = new ArrayList<>();
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                filtered.add(stack.copy());
            }
        }
        return filtered;
    }

    private List<FluidStack> filterNonEmptyFluids(List<FluidStack> fluids) {
        List<FluidStack> filtered = new ArrayList<>();
        for (FluidStack stack : fluids) {
            if (!stack.isEmpty()) {
                filtered.add(stack.copy());
            }
        }
        return filtered;
    }

    private List<ItemStack> mergeStacks(List<ItemStack> items) {
        List<ItemStack> merged = new ArrayList<>();
        for (ItemStack stack : items) {
            boolean mergedInto = false;
            for (ItemStack existing : merged) {
                if (ItemStack.isSameItemSameComponents(existing, stack)) {
                    existing.grow(stack.getCount());
                    mergedInto = true;
                    break;
                }
            }
            if (!mergedInto) {
                merged.add(stack.copy());
            }
        }
        return merged;
    }

    private boolean matchItems(List<ItemStack> items, boolean strict) {
        if (inputs.isEmpty()) {
            return !strict || items.isEmpty();
        }
        List<Integer> used = new ArrayList<>();
        if (strict) {
            for (ItemStack stack : items) {
                boolean valid = false;
                int index = 0;
                for (CountedIngredient ingredient : inputs) {
                    if (!used.contains(index) && ingredient.test(stack) && stack.getCount() >= ingredient.count()) {
                        used.add(index);
                        valid = true;
                        break;
                    }
                    index++;
                }
                if (!valid) {
                    return false;
                }
            }
            return used.size() == inputs.size();
        }
        for (CountedIngredient ingredient : inputs) {
            boolean valid = false;
            for (int i = 0; i < items.size(); i++) {
                if (used.contains(i)) {
                    continue;
                }
                ItemStack stack = items.get(i);
                if (ingredient.test(stack) && stack.getCount() >= ingredient.count()) {
                    used.add(i);
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                return false;
            }
        }
        return true;
    }

    private boolean matchFluids(List<FluidStack> fluids, boolean strict) {
        if (fluidInputs.isEmpty()) {
            return !strict || fluids.isEmpty();
        }
        if (strict && fluids.size() != fluidInputs.size()) {
            return false;
        }
        List<Integer> used = new ArrayList<>();
        for (FluidStack recipeFluid : fluidInputs) {
            boolean valid = false;
            for (int i = 0; i < fluids.size(); i++) {
                if (used.contains(i)) {
                    continue;
                }
                FluidStack stack = fluids.get(i);
                if (FluidStack.isSameFluidSameComponents(stack, recipeFluid) && stack.getAmount() >= recipeFluid.getAmount()) {
                    used.add(i);
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                return false;
            }
        }
        return true;
    }

    public static class Serializer implements RecipeSerializer<ProcessRecipe> {
        private static final ResourceLocation DEFAULT_TYPE = ModRecipeTypes.COMBUSTION.getId();
        private static final MapCodec<ProcessRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                ResourceLocation.CODEC.optionalFieldOf("recipeType", DEFAULT_TYPE).forGetter(r -> r.recipeTypeId),
                CountedIngredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.inputs),
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
            ResourceLocation.STREAM_CODEC.encode(buffer, recipe.recipeTypeId);
            buffer.writeInt(recipe.inputs.size());
            for (CountedIngredient ingredient : recipe.inputs) {
                CountedIngredient.STREAM_CODEC.encode(buffer, ingredient);
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
            ResourceLocation recipeTypeId = ResourceLocation.STREAM_CODEC.decode(buffer);
            int inputSize = buffer.readInt();
            List<CountedIngredient> inputs = new java.util.ArrayList<>(inputSize);
            for (int i = 0; i < inputSize; i++) {
                inputs.add(CountedIngredient.STREAM_CODEC.decode(buffer));
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
            return new ProcessRecipe(recipeTypeId, inputs, outputs, fluidInputs, fluidOutputs, parameter);
        }
    }
}
