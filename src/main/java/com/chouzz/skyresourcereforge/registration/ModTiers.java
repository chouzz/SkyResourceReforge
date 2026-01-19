package com.chouzz.skyresourcereforge.registration;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.SimpleTier;

import java.util.function.Supplier;

public class ModTiers {
    public static final Tier CACTUS = new SimpleTier(
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            4, // max uses
            5.0f, // efficiency
            1.0f, // attack damage
            5, // enchantability
            () -> Ingredient.EMPTY // repair ingredient
    );
}
