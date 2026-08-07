package com.chouzz.skyresourcereforge.alchemy.item;

import java.util.List;

import com.chouzz.skyresourcereforge.registration.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AlchemyComponentItem extends Item {
    private static final List<String> NAMES = List.of(
        "cactus_needle",
        "crystal_shard",
        "alch_dust_1",
        "alch_dust_2",
        "alch_dust_3",
        "alch_dust_4",
        "alch_coal",
        "alch_gold_ingot",
        "alch_iron_ingot",
        "alch_gold_needle",
        "alch_diamond"
    );

    public AlchemyComponentItem(int componentType, Properties properties) {
        super(properties);
    }

    public static List<String> getNames() {
        return NAMES;
    }

    public static int getVariantIndex(ItemStack stack) {
        Integer index = stack.get(ModDataComponents.ALCHEMY_COMPONENT_INDEX.get());
        if (index != null && index >= 0 && index < NAMES.size()) {
            return index;
        }
        return 0;
    }

    public static void setVariantIndex(ItemStack stack, int index) {
        stack.set(ModDataComponents.ALCHEMY_COMPONENT_INDEX.get(), index);
    }

    public static ItemStack createStack(int index, Item item) {
        ItemStack stack = new ItemStack(item);
        setVariantIndex(stack, index);
        return stack;
    }

    public static ItemStack getStack(String name) {
        int index = NAMES.indexOf(name);
        if (index >= 0) {
            return createStack(index, com.chouzz.skyresourcereforge.registration.ModItems.ALCHEMY_COMPONENT.get());
        }
        return ItemStack.EMPTY;
    }

    @Override
    public Component getName(ItemStack stack) {
        int index = getVariantIndex(stack);
        if (index >= 0 && index < NAMES.size()) {
            String variant = NAMES.get(index);
            return Component.translatable("item.skyresourcereforge.alchemy_component." + variant);
        }
        return super.getName(stack);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        int index = getVariantIndex(stack);
        if (index >= 0 && index < NAMES.size()) {
            String variant = NAMES.get(index);
            return "item.skyresourcereforge.alchemy_component." + variant;
        }
        return super.getDescriptionId(stack);
    }
}
