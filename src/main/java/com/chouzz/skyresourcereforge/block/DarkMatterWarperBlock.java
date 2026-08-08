package com.chouzz.skyresourcereforge.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Dark Matter Warper block - teleports entities.
 * Ported from SkyResources BlockDarkMatterWarper.
 * TODO: Implement warping functionality
 */
public class DarkMatterWarperBlock extends BaseEntityBlock {

    public static final MapCodec<DarkMatterWarperBlock> CODEC = simpleCodec(DarkMatterWarperBlock::new);

    public DarkMatterWarperBlock(Properties properties) {
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
        return new com.chouzz.skyresourcereforge.block.entity.DarkMatterWarperBlockEntity(pos, state);
    }
}
