package com.chouzz.skyresourcereforge.item;

import com.chouzz.skyresourcereforge.registration.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BaseComponentItem extends Item {
    private static final List<String> NAMES = List.of(
            "plant_matter",
            "steel_power_component",
            "frozen_iron_cooling_component",
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

    public static int getVariantIndex(ItemStack stack) {
        Integer index = stack.get(ModDataComponents.BASE_COMPONENT_INDEX.get());
        if (index != null) {
            return index;
        }
        return 0;
    }

    public static void setVariantIndex(ItemStack stack, int index) {
        stack.set(ModDataComponents.BASE_COMPONENT_INDEX.get(), index);
    }

    public static ItemStack createStack(int index, Item item) {
        ItemStack stack = new ItemStack(item);
        setVariantIndex(stack, index);
        return stack;
    }

    public static ItemStack getStack(String name) {
        int index = NAMES.indexOf(name);
        if (index >= 0) {
            return createStack(index, com.chouzz.skyresourcereforge.registration.ModItems.BASE_COMPONENT.get());
        }
        return ItemStack.EMPTY;
    }

    @Override
    public Component getName(ItemStack stack) {
        String variant = NAMES.get(getVariantIndex(stack));
        return Component.translatable("item.skyresourcereforge.base_component." + variant);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        String variant = NAMES.get(getVariantIndex(stack));
        return "item.skyresourcereforge.base_component." + variant;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (getVariantIndex(stack) == 0) {
            tooltip.add(Component.translatable("tooltip.skyresourcereforge.base_component.plant_matter"));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
