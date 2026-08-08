package com.chouzz.skyresourcereforge.block;

import com.chouzz.skyresourcereforge.block.entity.QuickDropperBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Quick Dropper block - drops items when not powered and block below is air.
 * Ported from SkyResources BlockQuickDropper.
 */
public class QuickDropperBlock extends BaseEntityBlock {

    public static final MapCodec<QuickDropperBlock> CODEC = simpleCodec(QuickDropperBlock::new);

    public QuickDropperBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new QuickDropperBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, com.chouzz.skyresourcereforge.registration.ModBlockEntities.QUICK_DROPPER.get(),
            QuickDropperBlockEntity::tick);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof QuickDropperBlockEntity dropper) {
                dropper.dropInventory();
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
