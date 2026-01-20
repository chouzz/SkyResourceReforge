package com.chouzz.skyresourcereforge.alchemy.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Life Injector block entity.
 * Ported from SkyResources LifeInjectorTile.
 * TODO: Implement life injection processing
 */
public class LifeInjectorBlockEntity extends BlockEntity {
    public LifeInjectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LIFE_INJECTOR.get(), pos, state);
    }
}
