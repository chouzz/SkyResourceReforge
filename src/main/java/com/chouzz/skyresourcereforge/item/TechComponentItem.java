package com.chouzz.skyresourcereforge.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TechComponentItem extends Item {
    private static final List<String> NAMES = List.of(
            "stone_crushed",
            "radioactive_mix",
            "frozen_iron_ingot",
            "netherrack_crushed"
    );

    public TechComponentItem(Properties properties) {
        super(properties);
    }

    public static List<String> getNames() {
        return NAMES;
    }

    public static ItemStack getStack(String name) {
        int index = NAMES.indexOf(name);
        if (index >= 0) {
            // TODO: Use ItemStack components or separate items for variants in 1.21.1
            // For now, return a simple stack
            return ItemStack.EMPTY; // Placeholder
        }
        return ItemStack.EMPTY;
    }
}
