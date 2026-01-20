package com.chouzz.skyresourcereforge.alchemy.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Life Infuser block entity.
 * Ported from SkyResources LifeInfuserTile.
 * TODO: Implement life infusion processing
 */
public class LifeInfuserBlockEntity extends BlockEntity {
    public LifeInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LIFE_INFUSER.get(), pos, state);
    }
}
