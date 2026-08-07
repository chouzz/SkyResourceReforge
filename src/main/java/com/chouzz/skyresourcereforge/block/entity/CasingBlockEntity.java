package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.api.IHeatSource;
import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CasingBlockEntity extends BlockEntity implements IHeatSource {
    private int currentHeat = 0;
    private final ItemStackHandler inventory = new ItemStackHandler(18) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

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

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("heat", currentHeat);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        currentHeat = tag.getInt("heat");
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
    }

    public void dropInventory() {
        if (level == null) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
        }
    }
}
