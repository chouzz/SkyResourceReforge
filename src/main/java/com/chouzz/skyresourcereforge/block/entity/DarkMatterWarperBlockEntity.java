package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Dark Matter Warper block entity.
 * Ported from SkyResources TileDarkMatterWarper.
 * TODO: Implement warping functionality
 */
public class DarkMatterWarperBlockEntity extends BlockEntity {
    public DarkMatterWarperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DARK_MATTER_WARPER.get(), pos, state);
    }
}
