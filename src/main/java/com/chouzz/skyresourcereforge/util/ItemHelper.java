package com.chouzz.skyresourcereforge.util;

import com.chouzz.skyresourcereforge.item.ItemRockGrinder;
import com.chouzz.skyresourcereforge.registration.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for item-related operations.
 * Ported from SkyResources ItemHelper with modern APIs.
 */
public class ItemHelper {
    private static final List<ItemStack> infusionStones = new ArrayList<>();
    private static final List<ItemStack> rockGrinders = new ArrayList<>();
    private static final List<ItemStack> knives = new ArrayList<>();

    /**
     * Initialize the item registries with mod items.
     * Call this during mod initialization.
     */
    public static void init() {
        infusionStones.clear();
        rockGrinders.clear();
        knives.clear();

        // Register infusion stones
        addInfusionStone(ModItems.INFUSION_STONE_SANDSTONE.get());
        addInfusionStone(ModItems.INFUSION_STONE_RED_SANDSTONE.get());
        addInfusionStone(ModItems.INFUSION_STONE_ALCHEMICAL.get());

        // Register rock grinders
        addRockGrinder(ModItems.STONE_GRINDER.get());
        addRockGrinder(ModItems.IRON_GRINDER.get());
        addRockGrinder(ModItems.DIAMOND_GRINDER.get());

        // Register knives
        addKnife(ModItems.CACTUS_KNIFE.get());
        addKnife(ModItems.STONE_KNIFE.get());
        addKnife(ModItems.IRON_KNIFE.get());
        addKnife(ModItems.DIAMOND_KNIFE.get());
    }

    public static List<ItemStack> getInfusionStones() {
        return Collections.unmodifiableList(infusionStones);
    }

    public static void addInfusionStone(Item item) {
        infusionStones.add(new ItemStack(item));
    }

    public static List<ItemStack> getRockGrinders() {
        return Collections.unmodifiableList(rockGrinders);
    }

    public static void addRockGrinder(Item item) {
        rockGrinders.add(new ItemStack(item));
    }

    public static List<ItemStack> getKnives() {
        return Collections.unmodifiableList(knives);
    }

    public static void addKnife(Item item) {
        knives.add(new ItemStack(item));
    }

    /**
     * Check if two item stacks are equal for crafting purposes.
     * Modern version that doesn't rely on OreDictionary.
     */
    public static boolean itemStacksEqual(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty() && stack2.isEmpty()) {
            return true;
        }
        if (stack1.isEmpty() || stack2.isEmpty()) {
            return false;
        }
        return ItemStack.isSameItemSameComponents(stack1, stack2);
    }
}
