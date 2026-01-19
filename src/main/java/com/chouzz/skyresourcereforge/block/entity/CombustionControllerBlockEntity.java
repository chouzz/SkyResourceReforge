package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CombustionControllerBlockEntity extends BlockEntity {
    private int cooldownTicks = 0;

    public CombustionControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMBUSTION_CONTROLLER.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CombustionControllerBlockEntity blockEntity) {
        if (level.isClientSide) return;

        if (blockEntity.cooldownTicks > 0) {
            blockEntity.cooldownTicks--;
            blockEntity.setChanged();
        }
        
        // TODO: Implement actual crafting logic (checking for items in world, heat, etc.)
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("cooldown", cooldownTicks);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        cooldownTicks = tag.getInt("cooldown");
    }
}
