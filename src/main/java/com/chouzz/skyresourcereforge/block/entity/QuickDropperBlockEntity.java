package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Quick Dropper block entity - drops items when not powered.
 * Ported from SkyResources TileQuickDropper.
 */
public class QuickDropperBlockEntity extends BlockEntity {
    protected final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public QuickDropperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.QUICK_DROPPER.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, QuickDropperBlockEntity entity) {
        if (level.isClientSide) {
            return;
        }

        // Check if not powered and can drop
        boolean hasPower = level.hasNeighborSignal(pos);
        boolean blockBelowIsSolid = !level.getBlockState(pos.below()).isAir();

        if (!hasPower && !blockBelowIsSolid && !entity.inventory.getStackInSlot(0).isEmpty()) {
            // Drop the item
            var itemEntity = new net.minecraft.world.entity.item.ItemEntity(
                level,
                pos.below().getX() + 0.5,
                pos.below().getY() + 0.5,
                pos.below().getZ() + 0.5,
                entity.inventory.getStackInSlot(0).copy()
            );
            itemEntity.setDeltaMovement(0, 0, 0);
            level.addFreshEntity(itemEntity);
            entity.inventory.setStackInSlot(0, net.minecraft.world.item.ItemStack.EMPTY);
        }
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
        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
        }
    }
}
