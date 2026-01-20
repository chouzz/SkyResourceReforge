package com.chouzz.skyresourcereforge.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BaseComponentItem extends Item {
    private static final List<String> NAMES = List.of(
            "plant_matter",
            "steel_power_component",
            "frozen_iron_cooling_component",
            "dark_matter",
            "enriched_bonemeal",
            "sawdust",
            "quartz_amp",
            "light_matter"
    );

    public BaseComponentItem(Properties properties) {
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
