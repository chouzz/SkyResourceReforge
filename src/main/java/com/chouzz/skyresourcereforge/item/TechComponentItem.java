package com.chouzz.skyresourcereforge.item;

import com.chouzz.skyresourcereforge.registration.ModDataComponents;
import net.minecraft.network.chat.Component;
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

    /** Named index constants — single source of truth for variant positions. */
    public static final int STONE_CRUSHED = 0;
    public static final int RADIOACTIVE_MIX = 1;
    public static final int FROZEN_IRON_INGOT = 2;
    public static final int NETHERRACK_CRUSHED = 3;

    public TechComponentItem(Properties properties) {
        super(properties);
    }

    public static List<String> getNames() {
        return NAMES;
    }

    public static int getVariantIndex(ItemStack stack) {
        Integer index = stack.get(ModDataComponents.TECH_COMPONENT_INDEX.get());
        if (index != null && index >= 0 && index < NAMES.size()) {
            return index;
        }
        return 0;
    }

    public static void setVariantIndex(ItemStack stack, int index) {
        stack.set(ModDataComponents.TECH_COMPONENT_INDEX.get(), index < 0 ? 0 : Math.min(index, NAMES.size() - 1));
    }

    public static ItemStack createStack(int index, Item item) {
        ItemStack stack = new ItemStack(item);
        setVariantIndex(stack, index);
        return stack;
    }

    public static ItemStack getStack(String name) {
        int index = NAMES.indexOf(name);
        if (index >= 0) {
            return createStack(index, com.chouzz.skyresourcereforge.registration.ModItems.TECH_COMPONENT.get());
        }
        return ItemStack.EMPTY;
    }

    @Override
    public Component getName(ItemStack stack) {
        String variant = NAMES.get(getVariantIndex(stack));
        return Component.translatable("item.skyresourcereforge.tech_component." + variant);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        String variant = NAMES.get(getVariantIndex(stack));
        return "item.skyresourcereforge.tech_component." + variant;
    }
}
