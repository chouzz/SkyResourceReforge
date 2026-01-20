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

    public LifeInfuserBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return MapCodec.unit(new LifeInfuserBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()));
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
