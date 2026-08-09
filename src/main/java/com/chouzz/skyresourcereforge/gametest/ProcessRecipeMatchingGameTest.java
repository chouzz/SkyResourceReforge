package com.chouzz.skyresourcereforge.gametest;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.recipe.CountedIngredient;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipeInput;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * GameTests that directly exercise the ProcessRecipe matching algorithm
 * (bipartite matching, greedy/swap fallback, strict/non-strict mode,
 * mergeStacks, fluid matching, and parameter boundary) using constructed
 * recipes and inputs.
 * These tests do NOT require the live recipe registry — they build synthetic
 * ProcessRecipe instances and assert matches() behaviour.
 */
public final class ProcessRecipeMatchingGameTest {

    private static final ResourceLocation DUMMY_TYPE = ModRecipeTypes.COMBUSTION.getId();

    private ProcessRecipeMatchingGameTest() {
    }

    // ---------- helpers ----------

    private static CountedIngredient ci(Ingredient ing, int count) {
        return CountedIngredient.of(ing, count);
    }

    private static Ingredient vanillaIngredient(net.minecraft.world.item.Item item) {
        return Ingredient.of(item);
    }

    /**
     * Build a simple item-only ProcessRecipe (no fluids, param=0).
     */
    private static ProcessRecipe itemRecipe(List<CountedIngredient> inputs) {
        return new ProcessRecipe(
                DUMMY_TYPE,
                inputs,
                List.of(new ItemStack(Items.DIAMOND)),
                List.of(),
                List.of(),
                0.0f
        );
    }

    /**
     * Build a ProcessRecipe with fluids.
     */
    private static ProcessRecipe fluidRecipe(List<CountedIngredient> inputs, List<FluidStack> fluidInputs) {
        return new ProcessRecipe(
                DUMMY_TYPE,
                inputs,
                List.of(new ItemStack(Items.DIAMOND)),
                fluidInputs,
                List.of(),
                0.0f
        );
    }

    /**
     * Build a ProcessRecipe with a parameter requirement.
     */
    private static ProcessRecipe paramRecipe(List<CountedIngredient> inputs, float param) {
        return new ProcessRecipe(
                DUMMY_TYPE,
                inputs,
                List.of(new ItemStack(Items.DIAMOND)),
                List.of(),
                List.of(),
                param
        );
    }

    private static ProcessRecipeInput strictInput(List<ItemStack> items) {
        return new ProcessRecipeInput(items, List.of(), Float.MAX_VALUE, true, false);
    }

    private static ProcessRecipeInput nonStrictInput(List<ItemStack> items) {
        return new ProcessRecipeInput(items, List.of(), Float.MAX_VALUE, false, false);
    }

    private static ProcessRecipeInput mergeInput(List<ItemStack> items) {
        return new ProcessRecipeInput(items, List.of(), Float.MAX_VALUE, false, true);
    }

    private static ProcessRecipeInput mergeStrictInput(List<ItemStack> items) {
        return new ProcessRecipeInput(items, List.of(), Float.MAX_VALUE, true, true);
    }

    private static ProcessRecipeInput fluidInput(List<ItemStack> items, List<FluidStack> fluids) {
        return new ProcessRecipeInput(items, fluids, Float.MAX_VALUE, true, false);
    }

    private static ProcessRecipeInput paramInput(List<ItemStack> items, float param) {
        return new ProcessRecipeInput(items, List.of(), param, false, false);
    }

    // ---------- Test 1: Exact match + wrong-item rejection ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void exactMatchAndWrongItemRejection(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        ProcessRecipe recipe = itemRecipe(List.of(
                ci(vanillaIngredient(Items.IRON_INGOT), 1),
                ci(vanillaIngredient(Items.GOLD_INGOT), 1)
        ));

        // Exact match should succeed (strict)
        boolean ok = recipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.GOLD_INGOT)
        )), level);
        if (!ok) failures.add("exact match should succeed in strict mode");

        // Wrong item should fail (strict)
        boolean bad = recipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.DIAMOND)
        )), level);
        if (bad) failures.add("wrong item should fail in strict mode");

        // Missing item should fail (strict)
        boolean missing = recipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT)
        )), level);
        if (missing) failures.add("missing item should fail in strict mode");

        if (!failures.isEmpty()) {
            helper.fail("exactMatchAndWrongItemRejection: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 2: Reordered distinct ingredients (bipartite matching) ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void reorderedBipartiteMatching(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        ProcessRecipe recipe = itemRecipe(List.of(
                ci(vanillaIngredient(Items.IRON_INGOT), 1),
                ci(vanillaIngredient(Items.GOLD_INGOT), 1),
                ci(vanillaIngredient(Items.DIAMOND), 1)
        ));

        // Reversed order should still match
        boolean ok = recipe.matches(strictInput(List.of(
                new ItemStack(Items.DIAMOND),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(Items.IRON_INGOT)
        )), level);
        if (!ok) failures.add("reversed item order should still match");

        // Arbitrary permutation
        boolean ok2 = recipe.matches(strictInput(List.of(
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.DIAMOND)
        )), level);
        if (!ok2) failures.add("permuted item order should still match");

        if (!failures.isEmpty()) {
            helper.fail("reorderedBipartiteMatching: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 3: Two identical ingredients needing two stacks ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void duplicateIngredientTwoStacks(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        // Recipe requires 2 separate iron ingot ingredients (e.g. shapeless-like)
        ProcessRecipe recipe = itemRecipe(List.of(
                ci(vanillaIngredient(Items.IRON_INGOT), 1),
                ci(vanillaIngredient(Items.IRON_INGOT), 1)
        ));

        // Two separate iron stacks should match
        boolean ok = recipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.IRON_INGOT)
        )), level);
        if (!ok) failures.add("two separate iron stacks should match two iron-1 ingredients");

        // Only one iron stack should fail (strict — sizes differ)
        boolean bad = recipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT)
        )), level);
        if (bad) failures.add("single iron stack should not match two iron-1 ingredients (strict)");

        if (!failures.isEmpty()) {
            helper.fail("duplicateIngredientTwoStacks: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 4: Count below per-ingredient threshold fails ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void countBelowThresholdFails(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        ProcessRecipe recipe = itemRecipe(List.of(
                ci(vanillaIngredient(Items.IRON_INGOT), 3)
        ));

        // Stack of 2 should fail
        boolean bad = recipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT, 2)
        )), level);
        if (bad) failures.add("iron×2 should not match iron×3 ingredient");

        // Stack of 3 should succeed
        boolean ok = recipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT, 3)
        )), level);
        if (!ok) failures.add("iron×3 should match iron×3 ingredient");

        if (!failures.isEmpty()) {
            helper.fail("countBelowThresholdFails: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 5: mergeStacks collapsing split stacks ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void mergeStacksCollapsesSplitItems(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        ProcessRecipe recipe = itemRecipe(List.of(
                ci(vanillaIngredient(Items.IRON_INGOT), 2)
        ));

        // Two separate iron×1 stacks, with mergeStacks=true, should merge to iron×2 and match
        boolean ok = recipe.matches(mergeInput(List.of(
                new ItemStack(Items.IRON_INGOT, 1),
                new ItemStack(Items.IRON_INGOT, 1)
        )), level);
        if (!ok) failures.add("two iron×1 with merge should match iron×2 ingredient");

        // Same without merge and strict → fails (item count 2 != ingredient count 1)
        boolean bad = recipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT, 1),
                new ItemStack(Items.IRON_INGOT, 1)
        )), level);
        if (bad) failures.add("two iron×1 without merge should NOT match iron×2 ingredient (strict, sizes differ)");

        if (!failures.isEmpty()) {
            helper.fail("mergeStacksCollapsesSplitItems: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 6: Strict rejects extra items, non-strict accepts ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void strictRejectsExtraItems(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        ProcessRecipe recipe = itemRecipe(List.of(
                ci(vanillaIngredient(Items.IRON_INGOT), 1)
        ));

        // Strict: one ingredient, one item → match
        boolean ok = recipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT)
        )), level);
        if (!ok) failures.add("strict: 1 item should match 1 ingredient");

        // Strict: one ingredient, two items → fail (size mismatch before merge)
        boolean bad = recipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.GOLD_INGOT)
        )), level);
        if (bad) failures.add("strict: 2 items should not match 1 ingredient");

        // Non-strict: one ingredient, two items (extra) → match
        boolean ok2 = recipe.matches(nonStrictInput(List.of(
                new ItemStack(Items.IRON_INGOT),
                new ItemStack(Items.GOLD_INGOT)
        )), level);
        if (!ok2) failures.add("non-strict: 2 items (1 needed + 1 extra) should match");

        if (!failures.isEmpty()) {
            helper.fail("strictRejectsExtraItems: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 7: Fluid bipartite matching ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void fluidBipartiteMatching(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        FluidStack water100 = new FluidStack(
                net.minecraft.world.level.material.Fluids.WATER, 100);
        FluidStack lava100 = new FluidStack(
                net.minecraft.world.level.material.Fluids.LAVA, 100);
        FluidStack water50 = new FluidStack(
                net.minecraft.world.level.material.Fluids.WATER, 50);

        ProcessRecipe recipe = fluidRecipe(
                List.of(),
                List.of(water100, lava100)
        );

        // Correct order → match
        boolean ok = recipe.matches(fluidInput(List.of(), List.of(water100, lava100)), level);
        if (!ok) failures.add("correct fluid order should match");

        // Reversed order → match (bipartite)
        boolean ok2 = recipe.matches(fluidInput(List.of(), List.of(lava100, water100)), level);
        if (!ok2) failures.add("reversed fluid order should match (bipartite)");

        // Amount below threshold → fail
        boolean bad = recipe.matches(fluidInput(List.of(), List.of(water50, lava100)), level);
        if (bad) failures.add("water×50 should not match water×100 requirement");

        // Missing fluid → fail
        boolean bad2 = recipe.matches(fluidInput(List.of(), List.of(water100)), level);
        if (bad2) failures.add("missing lava should fail");

        if (!failures.isEmpty()) {
            helper.fail("fluidBipartiteMatching: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 8: Parameter boundary (>= equality vs below) ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void parameterBoundary(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        ProcessRecipe recipe = paramRecipe(List.of(
                ci(vanillaIngredient(Items.IRON_INGOT), 1)
        ), 5.0f);

        // Parameter exactly equal → match
        boolean ok = recipe.matches(paramInput(List.of(
                new ItemStack(Items.IRON_INGOT)
        ), 5.0f), level);
        if (!ok) failures.add("parameter exactly equal (5.0) should match");

        // Parameter above threshold → match
        boolean ok2 = recipe.matches(paramInput(List.of(
                new ItemStack(Items.IRON_INGOT)
        ), 6.0f), level);
        if (!ok2) failures.add("parameter above threshold (6.0) should match");

        // Parameter below threshold → fail
        boolean bad = recipe.matches(paramInput(List.of(
                new ItemStack(Items.IRON_INGOT)
        ), 4.9f), level);
        if (bad) failures.add("parameter below threshold (4.9) should fail");

        if (!failures.isEmpty()) {
            helper.fail("parameterBoundary: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 9: Empty-input semantics ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void emptyInputSemantics(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        // Recipe with no item ingredients: strict with no items → match
        ProcessRecipe emptyRecipe = itemRecipe(List.of());
        boolean ok = emptyRecipe.matches(strictInput(List.of()), level);
        if (!ok) failures.add("empty recipe should match empty strict input");

        // Recipe with no item ingredients: strict with items → fail (extra items)
        boolean bad = emptyRecipe.matches(strictInput(List.of(
                new ItemStack(Items.IRON_INGOT)
        )), level);
        if (bad) failures.add("empty recipe should NOT match non-empty strict input");

        // Recipe with no item ingredients: non-strict with items → match
        boolean ok2 = emptyRecipe.matches(nonStrictInput(List.of(
                new ItemStack(Items.IRON_INGOT)
        )), level);
        if (!ok2) failures.add("empty recipe should match non-empty non-strict input (extra ignored)");

        if (!failures.isEmpty()) {
            helper.fail("emptyInputSemantics: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 10: mergeStacks + strict (size check happens before merge) ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void mergeWithStrictDuplicateIngredients(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        // Recipe requires 2 identical iron×1 ingredient slots
        // With merge+strict: size check (2==2) passes before merge, then merge creates iron×2,
        // and greedy fallback consumes iron×2 into both iron×1 slots.
        ProcessRecipe recipe = itemRecipe(List.of(
                ci(vanillaIngredient(Items.IRON_INGOT), 1),
                ci(vanillaIngredient(Items.IRON_INGOT), 1)
        ));

        // Two iron×1 with merge+strict → match (size check passes, merge creates iron×2, greedy satisfies both)
        boolean ok = recipe.matches(mergeStrictInput(List.of(
                new ItemStack(Items.IRON_INGOT, 1),
                new ItemStack(Items.IRON_INGOT, 1)
        )), level);
        if (!ok) failures.add("two iron×1 with merge+strict should match two iron×1 ingredients");

        if (!failures.isEmpty()) {
            helper.fail("mergeWithStrictDuplicateIngredients: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 11: Greedy/swap fallback (single stack satisfies multiple ingredient slots) ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void greedyFallbackSingleStackMultipleSlots(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        // Recipe requires 2 iron×1 ingredient slots but input has only 1 iron×2 stack.
        // Bipartite fails (1 stack can't match 2 ingredients). Greedy fallback kicks in:
        // first iron×1 consumes 1 from iron×2, second iron×1 consumes the remaining 1.
        ProcessRecipe recipe = itemRecipe(List.of(
                ci(vanillaIngredient(Items.IRON_INGOT), 1),
                ci(vanillaIngredient(Items.IRON_INGOT), 1)
        ));

        // Non-strict: single iron×2 should satisfy two iron×1 ingredient slots via greedy
        boolean ok = recipe.matches(nonStrictInput(List.of(
                new ItemStack(Items.IRON_INGOT, 2)
        )), level);
        if (!ok) failures.add("single iron×2 should match two iron×1 ingredients via greedy fallback");

        // Single iron×1 should NOT satisfy two iron×1 ingredient slots (not enough)
        boolean bad = recipe.matches(nonStrictInput(List.of(
                new ItemStack(Items.IRON_INGOT, 1)
        )), level);
        if (bad) failures.add("single iron×1 should NOT match two iron×1 ingredients");

        if (!failures.isEmpty()) {
            helper.fail("greedyFallbackSingleStackMultipleSlots: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }

    // ---------- Test 12: Swap fallback success (greedy fails, swap rescues) ----------

    @PrefixGameTestTemplate(false)
    @GameTest(templateNamespace = SkyResourceReforge.MODID, template = "recipe_validation_template", timeoutTicks = 400)
    public static void swapFallbackRescue(GameTestHelper helper) {
        Level level = helper.getLevel();
        List<String> failures = new ArrayList<>();

        // recipe: [iron×1, iron×3, iron×1]   input (non-strict): [iron×3, iron×2]
        // Bipartite fails (2 stacks, 3 slots).
        // Greedy fails: first iron×1 depletes stack0 (3→2), iron×3 can't fit → fail.
        // Swap (skip=0): routes first iron×1 to stack1 (rem [3,1]),
        //   iron×3 to stack0 (rem [0,1]), iron×1 to stack1 (rem [0,0]) → match.
        ProcessRecipe recipe = itemRecipe(List.of(
                ci(vanillaIngredient(Items.IRON_INGOT), 1),
                ci(vanillaIngredient(Items.IRON_INGOT), 3),
                ci(vanillaIngredient(Items.IRON_INGOT), 1)
        ));

        boolean ok = recipe.matches(nonStrictInput(List.of(
                new ItemStack(Items.IRON_INGOT, 3),
                new ItemStack(Items.IRON_INGOT, 2)
        )), level);
        if (!ok) failures.add("[iron×3, iron×2] should match [iron×1, iron×3, iron×1] via swap fallback");

        if (!failures.isEmpty()) {
            helper.fail("swapFallbackRescue: " + String.join("; ", failures));
            return;
        }
        helper.succeed();
    }
}
