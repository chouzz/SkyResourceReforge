package com.chouzz.skyresourcereforge.alchemy.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class InfusionStoneItem extends Item {
    private final int baseDurability;

    public InfusionStoneItem(int baseDurability, Properties properties) {
        super(properties);
        this.baseDurability = baseDurability;
    }

    public int getBaseDurability() {
        return baseDurability;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }
}
