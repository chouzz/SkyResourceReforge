package com.chouzz.skyresourcereforge.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Fluid Dropper block - drops fluid blocks below.
 * Ported from SkyResources FluidDropperBlock.
 */
public class FluidDropperBlock extends BaseEntityBlock {

    public FluidDropperBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return MapCodec.unit(new FluidDropperBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(net.minecraft.core.BlockPos pos, BlockState state) {
        return new com.chouzz.skyresourcereforge.block.entity.FluidDropperBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, be) -> {
            if (be instanceof com.chouzz.skyresourcereforge.block.entity.FluidDropperBlockEntity dropper) {
                com.chouzz.skyresourcereforge.block.entity.FluidDropperBlockEntity.tick(lvl, pos, st, dropper);
            }
        };
    }
}
