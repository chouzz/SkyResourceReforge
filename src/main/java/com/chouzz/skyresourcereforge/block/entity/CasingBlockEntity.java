package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.api.IHeatSource;
import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CasingBlockEntity extends BlockEntity implements IHeatSource {
    private int currentHeat = 0;

    public CasingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CASING.get(), pos, state);
    }

    @Override
    public int getHeatValue() {
        return currentHeat;
    }

    public void setHeatValue(int heat) {
        this.currentHeat = heat;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("heat", currentHeat);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        currentHeat = tag.getInt("heat");
    }
}
