package com.chouzz.skyresourcereforge.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A transparent block with optional custom bounding box.
 * Ported from SkyResources TransparentBlock.
 */
public class TransparentBlock extends Block {
    private final VoxelShape shape;

    public TransparentBlock(BlockBehaviour.Properties properties) {
        this(properties, Shapes.block());
    }

    public TransparentBlock(BlockBehaviour.Properties properties, VoxelShape shape) {
        super(properties);
        this.shape = shape;
    }
}
