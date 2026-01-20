package com.chouzz.skyresourcereforge.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class CactusFruitItem extends Item {
    public CactusFruitItem(Properties properties) {
        super(properties.food(new FoodProperties.Builder()
                .nutrition(3)
                .saturationModifier(2.0f)
                .build()));
    }
}
