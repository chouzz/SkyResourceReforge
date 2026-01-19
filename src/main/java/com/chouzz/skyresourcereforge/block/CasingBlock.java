package com.chouzz.skyresourcereforge.block;

import com.chouzz.skyresourcereforge.block.entity.CasingBlockEntity;
import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CasingBlock extends BaseEntityBlock {
    public static final MapCodec<CasingBlock> CODEC = simpleCodec(CasingBlock::new);

    public CasingBlock(Properties properties) {
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CasingBlockEntity(pos, state);
    }
}
