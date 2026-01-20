package com.chouzz.skyresourcereforge.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class FleshySnowNuggetItem extends Item {
    public FleshySnowNuggetItem(Properties properties) {
        super(properties.food(new FoodProperties.Builder()
                .nutrition(4)
                .saturationModifier(1.5f)
                .build()));
    }
}
