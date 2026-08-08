package com.chouzz.skyresourcereforge.alchemy.block;

import com.chouzz.skyresourcereforge.alchemy.block.entity.LifeInfuserBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Life Infuser block - infuses items with life essence.
 * Ported from SkyResources LifeInfuserBlock.
 * TODO: Implement life infusion functionality
 */
public class LifeInfuserBlock extends BaseEntityBlock {

    public static final MapCodec<LifeInfuserBlock> CODEC = simpleCodec(LifeInfuserBlock::new);

    public LifeInfuserBlock(Properties properties) {
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
        return new LifeInfuserBlockEntity(pos, state);
    }
}
