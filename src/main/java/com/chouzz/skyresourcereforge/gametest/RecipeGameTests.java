package com.chouzz.skyresourcereforge.gametest;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.recipe.CountedIngredient;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipeInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RecipeGameTests {
    private static final int CRAFTING_GRID_SIZE = 9;
    private static final int CRAFTING_GRID_WIDTH = 3;
    private static final int MAX_FAILURES_IN_MESSAGE = 15;

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
                    + String.join("\n", failures.subList(0, Math.min(MAX_FAILURES_IN_MESSAGE, failures.size()))));
            return;
        }

        helper.succeed();
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
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients.size() > CRAFTING_GRID_SIZE) {
            failures.add(recipeId + " -> ingredient count exceeds 3x3 crafting grid");
            return;
        }

        List<ItemStack> grid = new ArrayList<>(Collections.nCopies(CRAFTING_GRID_SIZE, ItemStack.EMPTY));
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
            grid.set(i, stack);
        }

        CraftingInput input = CraftingInput.of(CRAFTING_GRID_WIDTH, CRAFTING_GRID_WIDTH, grid);
        if (!recipe.matches(input, level)) {
            failures.add(recipeId + " -> crafting recipe did not match reconstructed 3x3 grid");
            return;
        }

        assertAssembleMatchesResult(recipeId, recipe.assemble(input, registries), recipe.getResultItem(registries), failures);
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
            failures.add(recipeId + " -> expected result item is empty");
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

    @SuppressWarnings("unchecked")
    private static <T extends Recipe<?>> RecipeType<T> castRecipeType(RecipeType<?> recipeType) {
        return (RecipeType<T>) recipeType;
    }
}
