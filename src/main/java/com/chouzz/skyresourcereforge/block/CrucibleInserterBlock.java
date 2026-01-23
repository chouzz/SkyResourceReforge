package com.chouzz.skyresourcereforge.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Crucible Inserter block - inserts items into crucibles.
 * Ported from SkyResources BlockCrucibleInserter.
 * TODO: Implement crucible insertion functionality
 */
public class CrucibleInserterBlock extends BaseEntityBlock {

    public CrucibleInserterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return MapCodec.unit(new CrucibleInserterBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(net.minecraft.core.BlockPos pos, BlockState state) {
        return new com.chouzz.skyresourcereforge.block.entity.CrucibleInserterBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos,
                         BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof com.chouzz.skyresourcereforge.block.entity.CrucibleInserterBlockEntity inserter) {
                inserter.dropInventory();
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
