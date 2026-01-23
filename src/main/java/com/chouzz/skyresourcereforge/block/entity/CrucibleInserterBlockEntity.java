package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Crucible Inserter block entity.
 * Ported from SkyResources TileCrucibleInserter.
 */
public class CrucibleInserterBlockEntity extends BlockEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public CrucibleInserterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUCIBLE_INSERTER.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
    }

    public void dropInventory() {
        if (level == null) return;
        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(0));
    }
}
