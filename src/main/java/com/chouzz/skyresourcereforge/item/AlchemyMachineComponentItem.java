package com.chouzz.skyresourcereforge.item;

import com.chouzz.skyresourcereforge.heat.HeatVariants;
import com.chouzz.skyresourcereforge.registration.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AlchemyMachineComponentItem extends Item {
    public AlchemyMachineComponentItem(Properties properties) {
        super(properties);
    }

    public static int getVariantIndex(ItemStack stack) {
        Integer index = stack.get(ModDataComponents.ALCHEMY_MACHINE_INDEX.get());
        if (index != null && index >= 0 && index < HeatVariants.size()) {
            return index;
        }
        return 0;
    }

    public static void setVariantIndex(ItemStack stack, int index) {
        stack.set(ModDataComponents.ALCHEMY_MACHINE_INDEX.get(), index < 0 ? 0 : Math.min(index, HeatVariants.size() - 1));
    }

    public static ItemStack createStack(int index, Item item) {
        ItemStack stack = new ItemStack(item);
        setVariantIndex(stack, index);
        return stack;
    }

    @Override
    public Component getName(ItemStack stack) {
        String variant = HeatVariants.getName(getVariantIndex(stack));
        return Component.translatable("item.skyresourcereforge.alchemy." + variant);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        String variant = HeatVariants.getName(getVariantIndex(stack));
        return "item.skyresourcereforge.alchemy." + variant;
    }
}
