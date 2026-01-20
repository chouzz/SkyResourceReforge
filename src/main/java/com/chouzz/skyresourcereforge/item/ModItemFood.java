package com.chouzz.skyresourcereforge.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

/**
 * Base food item for SkyResourceReforge.
 * Ported from SkyResources ModItemFood.
 */
public class ModItemFood extends Item {
    public ModItemFood(int nutrition, float saturation) {
        this(nutrition, saturation, false);
    }

    public ModItemFood(int nutrition, float saturation, boolean canAlwaysEat) {
        super(new Item.Properties().food(
            new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation)
                .build()
        ));
    }
}
