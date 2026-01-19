package com.chouzz.skyresourcereforge.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class MagmafiedStoneBlock extends BaseBlock {
    public MagmafiedStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Logic depends on crystal fluid, which will be added later.
    }
}
