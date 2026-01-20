package com.chouzz.skyresourcereforge.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CactusFruitNeedleBlock extends BaseBlock {
    protected static final VoxelShape SHAPE = Block.box(4.8D, 0.0D, 4.8D, 11.2D, 12.8D, 11.2D);

    public CactusFruitNeedleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
