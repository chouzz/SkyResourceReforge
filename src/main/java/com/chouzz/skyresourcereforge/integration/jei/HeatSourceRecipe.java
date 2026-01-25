package com.chouzz.skyresourcereforge.integration.jei;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record HeatSourceRecipe(ItemStack stack, Component name, int heat) {
}
