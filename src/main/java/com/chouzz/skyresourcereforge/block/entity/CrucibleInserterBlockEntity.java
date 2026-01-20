package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Crucible Inserter block entity.
 * Ported from SkyResources TileCrucibleInserter.
 * TODO: Implement crucible insertion
 */
public class CrucibleInserterBlockEntity extends BlockEntity {
    public CrucibleInserterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUCIBLE_INSERTER.get(), pos, state);
    }
}
