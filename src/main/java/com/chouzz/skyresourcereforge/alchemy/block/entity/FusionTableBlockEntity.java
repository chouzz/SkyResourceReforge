package com.chouzz.skyresourcereforge.alchemy.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fusion Table block entity.
 * Ported from SkyResources TileAlchemyFusionTable.
 * TODO: Implement fusion recipe processing
 */
public class FusionTableBlockEntity extends BlockEntity {
    public FusionTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FUSION_TABLE.get(), pos, state);
    }
}
