package com.chouzz.skyresourcereforge.block;

import com.chouzz.skyresourcereforge.heat.HeatSources;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlazePowderBlock extends BaseBlock {
    public BlazePowderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (HeatSources.isValidHeatSource(pos.below(), level)) {
            int chance = random.nextInt(1000);
            if (chance <= HeatSources.getHeatSourceValue(pos.below(), level)) {
                level.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
            }
        }
    }
}
