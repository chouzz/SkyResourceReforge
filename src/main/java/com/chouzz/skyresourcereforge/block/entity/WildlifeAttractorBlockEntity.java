package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Wildlife Attractor block entity.
 * Ported from SkyResources TileWildlifeAttractor.
 * TODO: Implement wildlife attraction
 */
public class WildlifeAttractorBlockEntity extends BlockEntity {
    public WildlifeAttractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WILDLIFE_ATTRACTOR.get(), pos, state);
    }
}
