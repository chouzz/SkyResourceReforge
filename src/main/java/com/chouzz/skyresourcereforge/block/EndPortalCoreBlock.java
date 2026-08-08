package com.chouzz.skyresourcereforge.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * End Portal Core block.
 * Ported from SkyResources BlockEndPortalCore.
 * TODO: Implement end portal functionality
 */
public class EndPortalCoreBlock extends BaseEntityBlock {

    public static final MapCodec<EndPortalCoreBlock> CODEC = simpleCodec(EndPortalCoreBlock::new);

    public EndPortalCoreBlock(Properties properties) {
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
        return new com.chouzz.skyresourcereforge.block.entity.EndPortalCoreBlockEntity(pos, state);
    }
}
