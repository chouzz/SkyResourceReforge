package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fluid Dropper block entity.
 * Ported from SkyResources FluidDropperTile.
 * TODO: Implement fluid handling
 */
public class FluidDropperBlockEntity extends BlockEntity {
    public FluidDropperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_DROPPER.get(), pos, state);
    }
}
