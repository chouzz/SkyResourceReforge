package com.chouzz.skyresourcereforge.item;

import java.util.List;

import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipeInput;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;

public class ItemKnife extends DiggerItem {
    public ItemKnife(Tier tier, TagKey<Block> blocks, Properties properties) {
        super(tier, blocks, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // Check if this block has a knife recipe on the client side
        Level level = null;
        // We can't get level here easily, so return efficiency for knife-type blocks
        // The actual recipe processing is handled in ToolEventHandler
        return super.getDestroySpeed(stack, state);
    }
}
