package com.chouzz.skyresourcereforge.alchemy.item;

import net.minecraft.world.item.Item;

public class HealthGemItem extends Item {
    private final int healthBonus;

    public HealthGemItem(int healthBonus, Properties properties) {
        super(properties);
        this.healthBonus = healthBonus;
    }

    public int getHealthBonus() {
        return healthBonus;
    }
}
