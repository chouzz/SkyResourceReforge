package com.chouzz.skyresourcereforge.alchemy.block;

import com.chouzz.skyresourcereforge.alchemy.block.entity.LifeInjectorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Life Injector block - injects life essence into items.
 * Ported from SkyResources LifeInjectorBlock.
 * TODO: Implement life injection functionality
 */
public class LifeInjectorBlock extends BaseEntityBlock {

    public static final MapCodec<LifeInjectorBlock> CODEC = simpleCodec(LifeInjectorBlock::new);

    public LifeInjectorBlock(Properties properties) {
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
    public BlockEntity newBlockEntity(net.minecraft.core.BlockPos pos, BlockState state) {
        return new LifeInjectorBlockEntity(pos, state);
    }
}
