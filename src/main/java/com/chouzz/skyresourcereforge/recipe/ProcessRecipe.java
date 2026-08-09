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
import io.netty.handler.codec.DecoderException;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessRecipe implements Recipe<RecipeInput> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessRecipe.class);
    /** Set of recipeTypeId values that failed to resolve, to log each only once. */
    private static final Set<ResourceLocation> UNRESOLVED_WARNINGS = ConcurrentHashMap.newKeySet();

    private final ResourceLocation recipeTypeId;
    private final List<CountedIngredient> inputs;
    private final List<ItemStack> outputs;
    private final List<FluidStack> fluidInputs;
    private final List<FluidStack> fluidOutputs;
    private final float parameter;

    public ProcessRecipe(ResourceLocation recipeTypeId, List<CountedIngredient> inputs, List<ItemStack> outputs,
                         List<FluidStack> fluidInputs, List<FluidStack> fluidOutputs, float parameter) {
        this.recipeTypeId = recipeTypeId;
        this.inputs = List.copyOf(inputs);
        this.outputs = List.copyOf(outputs);
        this.fluidInputs = List.copyOf(fluidInputs);
        this.fluidOutputs = List.copyOf(fluidOutputs);
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
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.PROCESS_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        Optional<RecipeType<?>> type = BuiltInRegistries.RECIPE_TYPE.getOptional(recipeTypeId);
        if (type.isPresent()) {
            return type.get();
        }
        if (recipeTypeId.equals(Serializer.DEFAULT_TYPE)) {
            return ModRecipeTypes.COMBUSTION.get();
        }
        if (UNRESOLVED_WARNINGS.add(recipeTypeId)) {
            LOGGER.warn("ProcessRecipe has unknown recipeType '{}' — falling back to COMBUSTION. "
                    + "Check that the recipe type is registered and the ID is spelled correctly.",
                    recipeTypeId);
        }
        return ModRecipeTypes.COMBUSTION.get();
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
        // Strict size check must be evaluated BEFORE mergeStacks, because
        // merging reduces the item count and would falsely reject valid
        // strict-mode recipes that have duplicate ingredients.
        if (input.strict() && items.size() != inputs.size()) {
            return false;
        }
        if (input.mergeStacks()) {
            items = mergeStacks(items);
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
        int ni = inputs.size();
        int nj = items.size();

        // Build bipartite adjacency: adj[i] = list of item indices that satisfy ingredient i
        List<List<Integer>> adj = new ArrayList<>(ni);
        for (int i = 0; i < ni; i++) {
            CountedIngredient ingredient = inputs.get(i);
            List<Integer> matches = new ArrayList<>();
            for (int j = 0; j < nj; j++) {
                if (ingredient.test(items.get(j)) && items.get(j).getCount() >= ingredient.count()) {
                    matches.add(j);
                }
            }
            adj.add(matches);
        }

        // Try bipartite matching first (covers the non-merge case correctly).
        int[] matchJ = new int[nj]; // matchJ[j] = ingredient index matched to item j, -1 if unmatched
        Arrays.fill(matchJ, -1);
        int matchCount = 0;
        boolean[] visited = new boolean[nj];
        for (int i = 0; i < ni; i++) {
            Arrays.fill(visited, false);
            if (tryAugmentIngredient(adj, matchJ, visited, i, nj)) {
                matchCount++;
            }
        }
        if (matchCount == ni) return true;

        // Bipartite matching failed. Check if count-aware matching succeeds:
        // allow one item stack to satisfy multiple ingredient slots, deducting
        // the consumed count. This handles the mergeStacks case where e.g.
        // a merged A×2 stack should satisfy two A×1 ingredient slots.
        // Uses greedy first-fit assignment with backtracking when the first
        // choice exhausts a stack needed by a later ingredient.
        int[] remaining = new int[nj];
        for (int j = 0; j < nj; j++) {
            remaining[j] = items.get(j).getCount();
        }
        // Try greedy assignment; if it fails, attempt swapping the last
        // failed ingredient's candidate to see if a different item frees up
        // a stack for it.
        boolean solved = tryGreedyAssign(inputs, items, remaining);
        if (solved) return true;

        // Greedy failed — attempt a single swap: for each pair of ingredients
        // (i, k) where both matched different item slots, try swapping their
        // assignments to break a conflict.
        return trySwapAssign(inputs, items, remaining);
    }

    private static boolean tryGreedyAssign(List<CountedIngredient> inputs,
                                           List<ItemStack> items, int[] remaining) {
        int ni = inputs.size();
        int nj = items.size();
        for (int i = 0; i < ni; i++) {
            int required = inputs.get(i).count();
            boolean found = false;
            for (int j = 0; j < nj; j++) {
                if (remaining[j] >= required && inputs.get(i).test(items.get(j))) {
                    remaining[j] -= required;
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    private static boolean trySwapAssign(List<CountedIngredient> inputs,
                                         List<ItemStack> items, int[] remaining) {
        int ni = inputs.size();
        int nj = items.size();
        // Try assigning ingredient i to item slot j, and if that fails later,
        // try skipping j for i to reserve it for a later ingredient.
        for (int skip = 0; skip < ni; skip++) {
            // Reset remaining
            for (int j = 0; j < nj; j++) {
                remaining[j] = items.get(j).getCount();
            }
            // First pass: skip the preferred item for ingredient[skip]
            boolean ok = true;
            for (int i = 0; i < ni; i++) {
                int required = inputs.get(i).count();
                boolean found = false;
                int startJ = (i == skip) ? 1 : 0; // skip first match for this ingredient
                for (int j = startJ; j < nj; j++) {
                    if (remaining[j] >= required && inputs.get(i).test(items.get(j))) {
                        remaining[j] -= required;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    ok = false;
                    break;
                }
            }
            if (ok) return true;
        }
        return false;
    }

    /**
     * Try to find an augmenting path for ingredient i (left side) using Kuhn's algorithm.
     */
    private static boolean tryAugmentIngredient(List<List<Integer>> adj, int[] matchJ,
                                                boolean[] visited, int u, int nj) {
        for (int v : adj.get(u)) {
            if (visited[v]) continue;
            visited[v] = true;
            if (matchJ[v] == -1 || tryAugmentIngredient(adj, matchJ, visited, matchJ[v], nj)) {
                matchJ[v] = u;
                return true;
            }
        }
        return false;
    }

    private boolean matchFluids(List<FluidStack> fluids, boolean strict) {
        if (fluidInputs.isEmpty()) {
            return !strict || fluids.isEmpty();
        }
        if (strict && fluids.size() != fluidInputs.size()) {
            return false;
        }
        int ni = fluidInputs.size();
        int nj = fluids.size();
        List<List<Integer>> adj = new ArrayList<>(ni);
        for (int i = 0; i < ni; i++) {
            FluidStack recipeFluid = fluidInputs.get(i);
            List<Integer> matches = new ArrayList<>();
            for (int j = 0; j < nj; j++) {
                FluidStack stack = fluids.get(j);
                if (FluidStack.isSameFluidSameComponents(stack, recipeFluid) && stack.getAmount() >= recipeFluid.getAmount()) {
                    matches.add(j);
                }
            }
            adj.add(matches);
        }
        int[] matchJ = new int[nj];
        Arrays.fill(matchJ, -1);
        int matchCount = 0;
        boolean[] visited = new boolean[nj];
        for (int i = 0; i < ni; i++) {
            Arrays.fill(visited, false);
            if (tryAugmentIngredient(adj, matchJ, visited, i, nj)) {
                matchCount++;
            }
        }
        return matchCount == ni;
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

        private static final int MAX_COLLECTION_SIZE = 65536;

        private static int readBoundedSize(RegistryFriendlyByteBuf buffer, String field) {
            int size = buffer.readInt();
            if (size < 0 || size > MAX_COLLECTION_SIZE) {
                throw new DecoderException("ProcessRecipe " + field + " size out of bounds: " + size);
            }
            return size;
        }

        private static ProcessRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ResourceLocation recipeTypeId = ResourceLocation.STREAM_CODEC.decode(buffer);
            int inputSize = readBoundedSize(buffer, "inputs");
            List<CountedIngredient> inputs = new java.util.ArrayList<>(inputSize);
            for (int i = 0; i < inputSize; i++) {
                inputs.add(CountedIngredient.STREAM_CODEC.decode(buffer));
            }
            int outputSize = readBoundedSize(buffer, "outputs");
            List<ItemStack> outputs = new java.util.ArrayList<>(outputSize);
            for (int i = 0; i < outputSize; i++) {
                outputs.add(ItemStack.STREAM_CODEC.decode(buffer));
            }
            int fluidInputSize = readBoundedSize(buffer, "fluidInputs");
            List<FluidStack> fluidInputs = new java.util.ArrayList<>(fluidInputSize);
            for (int i = 0; i < fluidInputSize; i++) {
                fluidInputs.add(FluidStack.STREAM_CODEC.decode(buffer));
            }
            int fluidOutputSize = readBoundedSize(buffer, "fluidOutputs");
            List<FluidStack> fluidOutputs = new java.util.ArrayList<>(fluidOutputSize);
            for (int i = 0; i < fluidOutputSize; i++) {
                fluidOutputs.add(FluidStack.STREAM_CODEC.decode(buffer));
            }
            float parameter = buffer.readFloat();
            return new ProcessRecipe(recipeTypeId, inputs, outputs, fluidInputs, fluidOutputs, parameter);
        }
    }
}
