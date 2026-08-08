package com.chouzz.skyresourcereforge.gametest;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.recipe.CountedIngredient;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipeInput;
import com.chouzz.skyresourcereforge.registration.ModDataComponents;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class RecipeGameTests {
    private static final int CRAFTING_GRID_SIZE = 9;
    private static final int CRAFTING_GRID_WIDTH = 3;
    private static final int MAX_FAILURES_IN_MESSAGE = 8;
    private static final int MAX_CRAFTING_DIMENSION = 3;
    private static final int MAX_FAILURE_LINE_LENGTH = 140;
    private static final ResourceLocation ALCH_GOLD_INGOT_RECIPE_ID =
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alch_gold_ingot");
    private static final ResourceLocation ALCH_IRON_INGOT_RECIPE_ID =
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alch_iron_ingot");
    private static final ResourceLocation ALCH_GOLD_NEEDLE_RECIPE_ID =
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alch_gold_needle");
    private static final ResourceLocation ALCH_DIAMOND_RECIPE_ID =
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alch_diamond");
    private static final ResourceLocation ALCHEMICAL_GLASS_RECIPE_ID =
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alchemical_glass");
    private static final ResourceLocation ALCHEMY_WOOD_RECIPE_ID =
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "alchemy/wood");
    private static final ResourceLocation ALCHEMY_REFINED_OBSIDIAN_RECIPE_ID =
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "alchemy/refinedobsidian");

    private RecipeGameTests() {
    }

    // Requires template file: data/skyresourcereforge/structure/recipe_validation_template.nbt
    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void validateAllModRecipes(GameTestHelper helper) {
        Level level = helper.getLevel();
        RecipeManager recipeManager = level.getRecipeManager();
        HolderLookup.Provider registries = level.registryAccess();

        List<String> failures = new ArrayList<>();
        int validatedCount = 0;

        for (RecipeType<?> recipeType : BuiltInRegistries.RECIPE_TYPE) {
            validatedCount += validateRecipeType(recipeManager, level, registries, castRecipeType(recipeType), failures);
        }

        if (!failures.isEmpty()) {
            helper.fail("Recipe validation failed (" + failures.size() + "/" + validatedCount + "):\n"
                    + formatFailures(failures));
            return;
        }

        helper.succeed();
    }

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void validateRecipeRegistryLoad(GameTestHelper helper) {
        Level level = helper.getLevel();
        ResourceManager resourceManager = level.getServer().getResourceManager();
        RecipeManager recipeManager = level.getRecipeManager();

        Set<ResourceLocation> expectedRecipeIds = collectExpectedRecipeIds(resourceManager);
        List<String> missing = new ArrayList<>();

        for (ResourceLocation recipeId : expectedRecipeIds) {
            if (recipeManager.byKey(recipeId).isEmpty()) {
                missing.add(recipeId.toString());
            }
        }

        if (!missing.isEmpty()) {
            Collections.sort(missing);
            helper.fail("Recipes not loaded into registry (" + missing.size() + "):\n"
                    + formatFailures(missing));
            return;
        }

        helper.succeed();
    }

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void validateAdvancedAlchemyFusionRecipes(GameTestHelper helper) {
        Level level = helper.getLevel();
        RecipeManager recipeManager = level.getRecipeManager();
        HolderLookup.Provider registries = level.registryAccess();

        String goldError = validateProcessRecipeOutputVariant(recipeManager, registries, ALCH_GOLD_INGOT_RECIPE_ID, 7);
        if (goldError != null) {
            helper.fail(goldError);
            return;
        }

        String ironError = validateProcessRecipeOutputVariant(recipeManager, registries, ALCH_IRON_INGOT_RECIPE_ID, 8);
        if (ironError != null) {
            helper.fail(ironError);
            return;
        }

        String goldNeedleError = validateProcessRecipeOutputVariant(recipeManager, registries, ALCH_GOLD_NEEDLE_RECIPE_ID, 9);
        if (goldNeedleError != null) {
            helper.fail(goldNeedleError);
            return;
        }

        String diamondError = validateProcessRecipeOutputVariant(recipeManager, registries, ALCH_DIAMOND_RECIPE_ID, 10);
        if (diamondError != null) {
            helper.fail(diamondError);
            return;
        }

        helper.succeed();
    }

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void validateAlchemicalGlassFusionRecipe(GameTestHelper helper) {
        Level level = helper.getLevel();
        RecipeManager recipeManager = level.getRecipeManager();
        HolderLookup.Provider registries = level.registryAccess();

        String error = validateProcessRecipeOutputItem(
                recipeManager,
                registries,
                ALCHEMICAL_GLASS_RECIPE_ID,
                ModRecipeTypes.FUSION.getId(),
                new ItemStack(ModBlocks.ALCHEMICAL_GLASS.get())
        );
        if (error != null) {
            helper.fail(error);
            return;
        }

        helper.succeed();
    }

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void validateAlchemyMachineComponentRecipes(GameTestHelper helper) {
        Level level = helper.getLevel();
        RecipeManager recipeManager = level.getRecipeManager();
        HolderLookup.Provider registries = level.registryAccess();

        String woodError = validateRecipeOutputDataComponent(
                recipeManager,
                registries,
                ALCHEMY_WOOD_RECIPE_ID,
                ModItems.ALCHEMY.get(),
                ModDataComponents.ALCHEMY_MACHINE_INDEX.get(),
                0,
                ResourceLocation.withDefaultNamespace("crafting")
        );
        if (woodError != null) {
            helper.fail(woodError);
            return;
        }

        String refinedObsidianError = validateRecipeOutputDataComponent(
                recipeManager,
                registries,
                ALCHEMY_REFINED_OBSIDIAN_RECIPE_ID,
                ModItems.ALCHEMY.get(),
                ModDataComponents.ALCHEMY_MACHINE_INDEX.get(),
                15,
                ResourceLocation.withDefaultNamespace("crafting")
        );
        if (refinedObsidianError != null) {
            helper.fail(refinedObsidianError);
            return;
        }

        helper.succeed();
    }

    private static Set<ResourceLocation> collectExpectedRecipeIds(ResourceManager resourceManager) {
        // Some generators/mod setups use `recipe/`, vanilla uses `recipes/`; support both.
        Set<ResourceLocation> ids = new HashSet<>();
        ids.addAll(collectRecipeIdsFromPrefix(resourceManager, "recipe"));
        ids.addAll(collectRecipeIdsFromPrefix(resourceManager, "recipes"));
        return ids;
    }

    private static Collection<ResourceLocation> collectRecipeIdsFromPrefix(ResourceManager resourceManager, String prefix) {
        return resourceManager.listResources(prefix, path -> path.getPath().endsWith(".json"))
                .keySet()
                .stream()
                .filter(id -> id.getNamespace().equals(SkyResourceReforge.MODID))
                .map(id -> toRecipeId(id, prefix))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private static Optional<ResourceLocation> toRecipeId(ResourceLocation resourceId, String prefix) {
        String path = resourceId.getPath();
        String normalizedPrefix = prefix + "/";
        if (!path.startsWith(normalizedPrefix) || !path.endsWith(".json")) {
            return Optional.empty();
        }
        String idPath = path.substring(normalizedPrefix.length(), path.length() - ".json".length());
        if (idPath.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ResourceLocation.fromNamespaceAndPath(resourceId.getNamespace(), idPath));
    }

    private static String validateProcessRecipeOutputVariant(
            RecipeManager recipeManager,
            HolderLookup.Provider registries,
            ResourceLocation recipeId,
            int expectedAlchemyIndex
    ) {
        Optional<RecipeHolder<?>> holder = recipeManager.byKey(recipeId);
        if (holder.isEmpty()) {
            return "Expected recipe missing: " + recipeId;
        }

        Recipe<?> recipe = holder.get().value();
        if (!(recipe instanceof ProcessRecipe processRecipe)) {
            return recipeId + " is not a ProcessRecipe, found: " + recipe.getClass().getSimpleName();
        }

        if (!processRecipe.getRecipeTypeId().equals(ModRecipeTypes.FUSION.getId())) {
            return recipeId + " has wrong process type: " + processRecipe.getRecipeTypeId();
        }

        ItemStack result = processRecipe.getResultItem(registries);
        if (result.isEmpty()) {
            return recipeId + " result is empty";
        }

        if (!result.is(ModItems.ALCHEMY_COMPONENT.get())) {
            return recipeId + " output item is not alchemy_component: " + result;
        }

        Integer outputIndex = result.get(ModDataComponents.ALCHEMY_COMPONENT_INDEX.get());
        if (outputIndex == null || outputIndex != expectedAlchemyIndex) {
            return recipeId + " output index mismatch, expected " + expectedAlchemyIndex + " but got " + outputIndex;
        }

        return null;
    }

    private static String validateProcessRecipeOutputItem(
            RecipeManager recipeManager,
            HolderLookup.Provider registries,
            ResourceLocation recipeId,
            ResourceLocation expectedTypeId,
            ItemStack expectedOutput
    ) {
        Optional<RecipeHolder<?>> holder = recipeManager.byKey(recipeId);
        if (holder.isEmpty()) {
            return "Expected recipe missing: " + recipeId;
        }

        Recipe<?> recipe = holder.get().value();
        if (!(recipe instanceof ProcessRecipe processRecipe)) {
            return recipeId + " is not a ProcessRecipe, found: " + recipe.getClass().getSimpleName();
        }

        if (!processRecipe.getRecipeTypeId().equals(expectedTypeId)) {
            return recipeId + " has wrong process type: " + processRecipe.getRecipeTypeId();
        }

        ItemStack result = processRecipe.getResultItem(registries);
        if (result.isEmpty()) {
            return recipeId + " result is empty";
        }

        if (!ItemStack.isSameItemSameComponents(result, expectedOutput) || result.getCount() != expectedOutput.getCount()) {
            return recipeId + " output mismatch, expected " + expectedOutput + " but got " + result;
        }

        return null;
    }

    private static String validateRecipeOutputDataComponent(
            RecipeManager recipeManager,
            HolderLookup.Provider registries,
            ResourceLocation recipeId,
            net.minecraft.world.item.Item expectedItem,
            net.minecraft.core.component.DataComponentType<Integer> indexComponent,
            int expectedIndex,
            ResourceLocation expectedRecipeType
    ) {
        Optional<RecipeHolder<?>> holder = recipeManager.byKey(recipeId);
        if (holder.isEmpty()) {
            return "Expected recipe missing: " + recipeId;
        }

        Recipe<?> recipe = holder.get().value();
        ResourceLocation recipeType = recipe.getType() == null
                ? ResourceLocation.withDefaultNamespace("unknown")
                : BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType());
        if (!expectedRecipeType.equals(recipeType)) {
            return recipeId + " has wrong recipe type: " + recipeType + ", expected " + expectedRecipeType;
        }

        ItemStack result = recipe.getResultItem(registries);
        if (result.isEmpty()) {
            return recipeId + " result is empty";
        }
        if (!result.is(expectedItem)) {
            return recipeId + " output item mismatch: " + result;
        }

        Integer index = result.get(indexComponent);
        if (index == null || index != expectedIndex) {
            return recipeId + " output index mismatch, expected " + expectedIndex + " but got " + index;
        }

        return null;
    }

    private static <I extends RecipeInput, T extends Recipe<I>> int validateRecipeType(
            RecipeManager recipeManager,
            Level level,
            HolderLookup.Provider registries,
            RecipeType<T> recipeType,
            List<String> failures
    ) {
        int count = 0;
        for (RecipeHolder<T> recipeHolder : recipeManager.getAllRecipesFor(recipeType)) {
            if (!recipeHolder.id().getNamespace().equals(SkyResourceReforge.MODID)) {
                continue;
            }

            count++;
            validateSingleRecipe(level, registries, recipeHolder, failures);
        }
        return count;
    }

    private static void validateSingleRecipe(
            Level level,
            HolderLookup.Provider registries,
            RecipeHolder<?> recipeHolder,
            List<String> failures
    ) {
        Recipe<?> recipe = recipeHolder.value();

        if (recipe instanceof ProcessRecipe processRecipe) {
            validateProcessRecipe(level, registries, recipeHolder.id().toString(), processRecipe, failures);
            return;
        }

        if (recipe instanceof CraftingRecipe craftingRecipe) {
            validateCraftingRecipe(level, registries, recipeHolder.id().toString(), craftingRecipe, failures);
            return;
        }

        if (recipe instanceof SingleItemRecipe singleItemRecipe) {
            validateSingleItemRecipe(level, registries, recipeHolder.id().toString(), singleItemRecipe, failures);
            return;
        }

        ItemStack result = recipe.getResultItem(registries);
        if (result.isEmpty()) {
            failures.add(recipeHolder.id() + " -> unsupported recipe type with empty result: " + recipe.getClass().getSimpleName());
        }
    }

    private static void validateProcessRecipe(
            Level level,
            HolderLookup.Provider registries,
            String recipeId,
            ProcessRecipe recipe,
            List<String> failures
    ) {
        List<ItemStack> inputItems = new ArrayList<>();
        for (CountedIngredient ingredient : recipe.getInputs()) {
            ItemStack stack = firstMatchingStack(ingredient.ingredient());
            if (stack.isEmpty()) {
                failures.add(recipeId + " -> no concrete item for ingredient " + ingredient.ingredient());
                return;
            }
            stack.setCount(ingredient.count());
            inputItems.add(stack);
        }

        List<FluidStack> fluidInputs = recipe.getFluidInputs().stream().map(FluidStack::copy).toList();
        ProcessRecipeInput input = new ProcessRecipeInput(inputItems, fluidInputs, recipe.getParameter(), true, false);

        if (!recipe.matches(input, level)) {
            failures.add(recipeId + " -> process recipe did not match reconstructed input");
            return;
        }

        assertAssembleMatchesResult(recipeId, recipe.assemble(input, registries), recipe.getResultItem(registries), failures);
    }

    private static void validateCraftingRecipe(
            Level level,
            HolderLookup.Provider registries,
            String recipeId,
            CraftingRecipe recipe,
            List<String> failures
    ) {
        int width = CRAFTING_GRID_WIDTH;
        int height = CRAFTING_GRID_WIDTH;
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            width = shapedRecipe.getWidth();
            height = shapedRecipe.getHeight();
        }

        if (width > MAX_CRAFTING_DIMENSION || height > MAX_CRAFTING_DIMENSION) {
            failures.add(recipeId + " -> crafting dimensions exceed 3x3 grid");
            return;
        }

        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients.size() > CRAFTING_GRID_SIZE) {
            failures.add(recipeId + " -> ingredient count exceeds 3x3 crafting grid");
            return;
        }

        List<ItemStack> grid = new ArrayList<>(Collections.nCopies(CRAFTING_GRID_SIZE, ItemStack.EMPTY));
        int[] slotToIngredientIndex = new int[CRAFTING_GRID_SIZE];
        for (int i = 0; i < CRAFTING_GRID_SIZE; i++) {
            slotToIngredientIndex[i] = -1;
        }
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            if (ingredient.isEmpty()) {
                continue;
            }

            ItemStack stack = firstMatchingStack(ingredient);
            if (stack.isEmpty()) {
                failures.add(recipeId + " -> no concrete item for crafting ingredient index " + i);
                return;
            }
            int x = i % width;
            int y = i / width;
            int slot = y * CRAFTING_GRID_WIDTH + x;
            if (slot >= CRAFTING_GRID_SIZE) {
                failures.add(recipeId + " -> ingredient index outside 3x3 bounds");
                return;
            }
            grid.set(slot, stack);
            slotToIngredientIndex[slot] = i;
        }

        CraftingInput input = CraftingInput.of(CRAFTING_GRID_WIDTH, CRAFTING_GRID_WIDTH, grid);
        if (!recipe.matches(input, level)) {
            failures.add(recipeId + " -> crafting recipe did not match reconstructed 3x3 grid");
            return;
        }

        assertAssembleMatchesResult(recipeId, recipe.assemble(input, registries), recipe.getResultItem(registries), failures);

        // Negative case 1: remove one required material.
        List<ItemStack> missingOne = copyGrid(grid);
        int firstIngredientSlot = firstNonEmptySlot(missingOne);
        if (firstIngredientSlot >= 0) {
            missingOne.set(firstIngredientSlot, ItemStack.EMPTY);
            assertCraftingDoesNotMatch(level, recipeId, recipe, missingOne, "missing one required ingredient", failures);
        }

        // Negative case 2: put a definitely unrelated item in one required slot.
        List<ItemStack> wrongItem = copyGrid(grid);
        firstIngredientSlot = firstNonEmptySlot(wrongItem);
        if (firstIngredientSlot >= 0) {
            // Use an item matching NONE of the recipe's ingredients so that
            // shapeless (non-positional) recipes are also properly broken.
            ItemStack nonMatching = firstStackMatchingNoIngredient(ingredients);
            if (!nonMatching.isEmpty()) {
                wrongItem.set(firstIngredientSlot, nonMatching);
                assertCraftingDoesNotMatch(level, recipeId, recipe, wrongItem, "wrong item inserted", failures);
            }
        }

        // Edge case: inject extra unrelated item into an empty slot.
        List<ItemStack> withExtra = copyGrid(grid);
        int emptySlot = firstEmptySlot(withExtra);
        if (emptySlot >= 0) {
            withExtra.set(emptySlot, new ItemStack(Items.BARRIER));
            assertCraftingDoesNotMatch(level, recipeId, recipe, withExtra, "extra unrelated item in empty slot", failures);
        }

        // Edge case: if ingredient item is damageable and damage changes matching, assert no match.
        List<ItemStack> damagedCase = tryBuildDamagedMismatchGrid(grid, ingredients, slotToIngredientIndex);
        if (!damagedCase.isEmpty()) {
            assertCraftingDoesNotMatch(level, recipeId, recipe, damagedCase, "damaged ingredient variant", failures);
        }
    }

    private static void validateSingleItemRecipe(
            Level level,
            HolderLookup.Provider registries,
            String recipeId,
            SingleItemRecipe recipe,
            List<String> failures
    ) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients.isEmpty()) {
            failures.add(recipeId + " -> single-item recipe has no ingredients");
            return;
        }

        ItemStack stack = firstMatchingStack(ingredients.getFirst());
        if (stack.isEmpty()) {
            failures.add(recipeId + " -> no concrete item for single-item recipe input");
            return;
        }

        SingleRecipeInput input = new SingleRecipeInput(stack);
        if (!recipe.matches(input, level)) {
            failures.add(recipeId + " -> single-item recipe did not match reconstructed input");
            return;
        }

        assertAssembleMatchesResult(recipeId, recipe.assemble(input, registries), recipe.getResultItem(registries), failures);
    }

    private static void assertAssembleMatchesResult(
            String recipeId,
            ItemStack assembled,
            ItemStack expected,
            List<String> failures
    ) {
        if (expected.isEmpty()) {
            if (assembled.isEmpty()) {
                return;
            }
            failures.add(recipeId + " -> expected empty result but assembled " + assembled);
            return;
        }

        if (assembled.isEmpty()) {
            failures.add(recipeId + " -> assembled result is empty");
            return;
        }

        if (!ItemStack.isSameItemSameComponents(assembled, expected) || assembled.getCount() != expected.getCount()) {
            failures.add(recipeId + " -> assembled " + assembled + " != expected " + expected);
        }
    }

    private static ItemStack firstMatchingStack(Ingredient ingredient) {
        ItemStack[] stacks = ingredient.getItems();
        if (stacks.length == 0) {
            return ItemStack.EMPTY;
        }
        return stacks[0].copy();
    }

    /**
     * Returns a non-empty item that matches NONE of the given ingredients.
     * Used for negative-case crafting tests where replacing one slot's item must
     * break the recipe on shaped AND shapeless recipes alike. A simple
     * per-ingredient non-matching item is insufficient for shapeless recipes
     * because it may still satisfy a sibling ingredient.
     */
    private static ItemStack firstStackMatchingNoIngredient(List<Ingredient> ingredients) {
        for (var item : BuiltInRegistries.ITEM) {
            if (item == Items.AIR) {
                continue;
            }
            ItemStack stack = new ItemStack(item);
            boolean matchesAny = false;
            for (Ingredient ingredient : ingredients) {
                if (ingredient.test(stack)) {
                    matchesAny = true;
                    break;
                }
            }
            if (!matchesAny) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static void assertCraftingDoesNotMatch(
            Level level,
            String recipeId,
            CraftingRecipe recipe,
            List<ItemStack> grid,
            String caseName,
            List<String> failures
    ) {
        CraftingInput input = CraftingInput.of(CRAFTING_GRID_WIDTH, CRAFTING_GRID_WIDTH, grid);
        if (recipe.matches(input, level)) {
            failures.add(recipeId + " -> should not match for case: " + caseName);
        }
    }

    private static List<ItemStack> copyGrid(List<ItemStack> grid) {
        List<ItemStack> copied = new ArrayList<>(grid.size());
        for (ItemStack stack : grid) {
            copied.add(stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }
        return copied;
    }

    private static int firstNonEmptySlot(List<ItemStack> grid) {
        for (int i = 0; i < grid.size(); i++) {
            if (!grid.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private static int firstEmptySlot(List<ItemStack> grid) {
        for (int i = 0; i < grid.size(); i++) {
            if (grid.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private static List<ItemStack> tryBuildDamagedMismatchGrid(
            List<ItemStack> grid,
            NonNullList<Ingredient> ingredients,
            int[] slotToIngredientIndex
    ) {
        List<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < grid.size(); i++) {
            ItemStack stack = grid.get(i);
            if (!stack.isEmpty() && stack.isDamageableItem()) {
                candidates.add(i);
            }
        }
        if (candidates.isEmpty()) {
            return List.of();
        }

        candidates.sort(Comparator.naturalOrder());
        for (int slot : candidates) {
            ItemStack original = grid.get(slot);
            ItemStack damaged = original.copy();
            damaged.setDamageValue(Math.min(1, damaged.getMaxDamage() - 1));

            int ingredientIndex = slotToIngredientIndex[slot];
            if (ingredientIndex < 0 || ingredientIndex >= ingredients.size()) {
                continue;
            }
            Ingredient ingredient = ingredients.get(ingredientIndex);
            if (ingredient.isEmpty() || ingredient.test(damaged)) {
                continue;
            }

            List<ItemStack> damagedGrid = copyGrid(grid);
            damagedGrid.set(slot, damaged);
            return damagedGrid;
        }

        return List.of();
    }

    private static String formatFailures(List<String> failures) {
        int max = Math.min(MAX_FAILURES_IN_MESSAGE, failures.size());
        List<String> lines = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            String line = failures.get(i);
            if (line.length() > MAX_FAILURE_LINE_LENGTH) {
                lines.add(line.substring(0, MAX_FAILURE_LINE_LENGTH - 3) + "...");
            } else {
                lines.add(line);
            }
        }
        return String.join("\n", lines);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Recipe<?>> RecipeType<T> castRecipeType(RecipeType<?> recipeType) {
        return (RecipeType<T>) recipeType;
    }
}
