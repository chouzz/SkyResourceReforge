package com.chouzz.skyresourcereforge.heat;

import com.chouzz.skyresourcereforge.api.IHeatSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;

public class HeatSources {
    private static final Map<BlockState, Integer> VALID_HEAT_SOURCES = new HashMap<>();

    public static void addHeatSource(BlockState blockState, int value) {
        VALID_HEAT_SOURCES.put(blockState, value);
    }

    public static boolean isValidHeatSource(BlockPos pos, Level level) {
        BlockState state = level.getBlockState(pos);
        if (VALID_HEAT_SOURCES.containsKey(state)) {
            return true;
        }

        for (Map.Entry<BlockState, Integer> entry : VALID_HEAT_SOURCES.entrySet()) {
            if (entry.getKey().getBlock() == state.getBlock()) {
                return true;
            }
        }

        BlockEntity be = level.getBlockEntity(pos);
        return be instanceof IHeatSource && ((IHeatSource) be).getHeatValue() > 0;
    }

    public static int getHeatSourceValue(BlockPos pos, Level level) {
        BlockState state = level.getBlockState(pos);

        if (VALID_HEAT_SOURCES.containsKey(state)) {
            return VALID_HEAT_SOURCES.get(state);
        }

        for (Map.Entry<BlockState, Integer> entry : VALID_HEAT_SOURCES.entrySet()) {
            if (entry.getKey().getBlock() == state.getBlock()) {
                return entry.getValue();
            }
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof IHeatSource) {
            return ((IHeatSource) be).getHeatValue();
        }
        
        return 0;
    }

    public static Map<BlockState, Integer> getHeatSources() {
        return VALID_HEAT_SOURCES;
    }

    public static void registerDefaults() {
        addHeatSource(net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState(), 8);
        addHeatSource(net.minecraft.world.level.block.Blocks.LAVA.defaultBlockState(), 6);
        addHeatSource(net.minecraft.world.level.block.Blocks.TORCH.defaultBlockState(), 1);
        addHeatSource(net.minecraft.world.level.block.Blocks.OBSIDIAN.defaultBlockState(), 3);
        addHeatSource(net.minecraft.world.level.block.Blocks.MAGMA_BLOCK.defaultBlockState(), 9);
    }
}
