package com.chouzz.skyresourcereforge.alchemy.block;

import com.chouzz.skyresourcereforge.alchemy.block.entity.FusionTableBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Alchemy Fusion Table block - combines items through fusion.
 * Ported from SkyResources BlockAlchemyFusionTable.
 * TODO: Implement fusion recipe functionality
 */
public class FusionTableBlock extends BaseEntityBlock {

    public FusionTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return MapCodec.unit(new FusionTableBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(net.minecraft.core.BlockPos pos, BlockState state) {
        return new FusionTableBlockEntity(pos, state);
    }
}
