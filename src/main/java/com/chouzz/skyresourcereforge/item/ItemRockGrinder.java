package com.chouzz.skyresourcereforge.item;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ItemRockGrinder extends DiggerItem {
    public ItemRockGrinder(Tier tier, TagKey<Block> blocks, Properties properties) {
        super(tier, blocks, properties);
    }
}
