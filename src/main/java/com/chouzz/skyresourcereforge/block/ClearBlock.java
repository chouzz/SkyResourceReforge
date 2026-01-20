package com.chouzz.skyresourcereforge.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * A completely transparent/clear block with no collision.
 * Ported from SkyResources ClearBlock.
 */
public class ClearBlock extends Block {
    public ClearBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}
