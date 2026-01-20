package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * End Portal Core block entity.
 * Ported from SkyResources TileEndPortalCore.
 * TODO: Implement end portal functionality
 */
public class EndPortalCoreBlockEntity extends BlockEntity {
    public EndPortalCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.END_PORTAL_CORE.get(), pos, state);
    }
}
