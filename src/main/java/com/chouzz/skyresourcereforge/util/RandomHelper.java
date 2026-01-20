package com.chouzz.skyresourcereforge.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Utility class for miscellaneous helper methods.
 * Ported from SkyResources RandomHelper with modern APIs.
 */
public class RandomHelper {

    /**
     * Capitalize the first letter of a string.
     */
    public static String capitalizeString(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * Spawn an item entity in the world at the given position.
     */
    public static void spawnItemInWorld(Level level, ItemStack stack, BlockPos pos) {
        if (level == null || stack.isEmpty()) {
            return;
        }
        ItemEntity entity = new ItemEntity(level,
            pos.getX() + 0.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5,
            stack
        );
        level.addFreshEntity(entity);
    }

    /**
     * Calculate 2D distance between two points.
     */
    public static float pointDistancePlane(double x1, double y1, double x2, double y2) {
        return (float) Math.hypot(x1 - x2, y1 - y2);
    }

    /**
     * Check if two item stacks can merge (same item and components).
     */
    public static boolean canStacksMerge(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty() || stack2.isEmpty()) {
            return false;
        }
        if (!ItemStack.isSameItemSameComponents(stack1, stack2)) {
            return false;
        }
        return true;
    }

    /**
     * Calculate how many items can be merged from source to target.
     * @return The number of items that can be merged
     */
    public static int mergeStacks(ItemStack mergeSource, ItemStack mergeTarget, boolean doMerge) {
        if (!canStacksMerge(mergeSource, mergeTarget)) {
            return 0;
        }
        int mergeCount = Math.min(mergeTarget.getMaxStackSize() - mergeTarget.getCount(), mergeSource.getCount());
        if (mergeCount < 1) {
            return 0;
        }
        if (doMerge) {
            mergeTarget.grow(mergeCount);
        }
        return mergeCount;
    }

    /**
     * Fill an item handler with items from a stack.
     * @param inv The item handler to fill
     * @param stack The stack to fill from (will be modified)
     * @param simulate If true, simulate without actually transferring
     * @return The remaining items that couldn't fit
     */
    public static ItemStack fillInventory(IItemHandler inv, ItemStack stack, boolean simulate) {
        if (inv != null) {
            for (int i = 0; i < inv.getSlots(); i++) {
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                ItemStack inside = inv.getStackInSlot(i);
                if (inside.isEmpty()) {
                    ItemStack inserted = inv.insertItem(i, stack, simulate);
                    if (!simulate) {
                        stack = inserted;
                    } else {
                        // For simulation, we need to check how much would fit
                        int insertedCount = stack.getCount() - inserted.getCount();
                        stack = stack.copyWithCount(stack.getCount() - insertedCount);
                    }
                } else if (canStacksMerge(inside, stack)) {
                    int merged = mergeStacks(stack, inside, !simulate);
                    if (!simulate) {
                        stack.shrink(merged);
                    } else {
                        stack = stack.copyWithCount(stack.getCount() - merged);
                    }
                }
            }
        }
        return stack;
    }

    /**
     * Fill an item handler with items from a stack (non-simulating).
     */
    public static ItemStack fillInventory(IItemHandler inv, ItemStack stack) {
        return fillInventory(inv, stack, false);
    }
}
