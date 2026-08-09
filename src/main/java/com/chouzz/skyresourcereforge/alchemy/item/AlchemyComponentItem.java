package com.chouzz.skyresourcereforge.alchemy.item;

import java.util.List;

import com.chouzz.skyresourcereforge.registration.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AlchemyComponentItem extends Item {
    private final int defaultComponentType;

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

    /** Named index constants — single source of truth for variant positions. */
    public static final int CACTUS_NEEDLE = 0;
    public static final int CRYSTAL_SHARD = 1;
    public static final int ALCH_DUST_1 = 2;
    public static final int ALCH_DUST_2 = 3;
    public static final int ALCH_DUST_3 = 4;
    public static final int ALCH_DUST_4 = 5;
    public static final int ALCH_COAL = 6;
    public static final int ALCH_GOLD_INGOT = 7;
    public static final int ALCH_IRON_INGOT = 8;
    public static final int ALCH_GOLD_NEEDLE = 9;
    public static final int ALCH_DIAMOND = 10;

    public AlchemyComponentItem(int componentType, Properties properties) {
        super(properties);
        this.defaultComponentType = Math.min(Math.max(componentType, 0), NAMES.size() - 1);
    }

    public static List<String> getNames() {
        return NAMES;
    }

    public int getVariantIndex(ItemStack stack) {
        Integer index = stack.get(ModDataComponents.ALCHEMY_COMPONENT_INDEX.get());
        if (index != null && index >= 0 && index < NAMES.size()) {
            return index;
        }
        return defaultComponentType;
    }

    public static void setVariantIndex(ItemStack stack, int index) {
        stack.set(ModDataComponents.ALCHEMY_COMPONENT_INDEX.get(), index < 0 ? 0 : Math.min(index, NAMES.size() - 1));
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
