package com.chouzz.skyresourcereforge.item;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ItemKnife extends DiggerItem {
    public ItemKnife(Tier tier, TagKey<Block> blocks, Properties properties) {
        super(tier, blocks, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // TODO: Implement recipe-based speed logic once recipe system is ported
        return super.getDestroySpeed(stack, state);
    }

    // TODO: Override onBlockStartBreak or similar when event system is more established
    // and recipe system is ready to handle drops.
}
