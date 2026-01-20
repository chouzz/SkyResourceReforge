package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class DirtFurnaceBlockEntity extends BlockEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private int burnTime = 0;
    private int cookTime = 0;
    private int totalCookTime = 200;

    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> DirtFurnaceBlockEntity.this.burnTime;
                case 1 -> DirtFurnaceBlockEntity.this.totalCookTime; // Reuse for max burn time
                case 2 -> DirtFurnaceBlockEntity.this.cookTime;
                case 3 -> DirtFurnaceBlockEntity.this.totalCookTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> DirtFurnaceBlockEntity.this.burnTime = value;
                case 1 -> {} // Ignore max burn time set
                case 2 -> DirtFurnaceBlockEntity.this.cookTime = value;
                case 3 -> DirtFurnaceBlockEntity.this.totalCookTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public DirtFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DIRT_FURNACE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getTotalCookTime() {
        return totalCookTime;
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DirtFurnaceBlockEntity blockEntity) {
        if (level.isClientSide) return;
        
        boolean wasBurning = blockEntity.isBurning();
        boolean changed = false;

        if (blockEntity.isBurning()) {
            blockEntity.burnTime--;
            changed = true;
        }

        // TODO: Implement full furnace logic with fuel consumption and smelting
        // For now, just update the lit state
        if (wasBurning != blockEntity.isBurning()) {
            state = state.setValue(com.chouzz.skyresourcereforge.block.DirtFurnaceBlock.LIT, blockEntity.isBurning());
            level.setBlock(pos, state, 3);
            changed = true;
        }

        if (changed) {
            blockEntity.setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("burnTime", burnTime);
        tag.putInt("cookTime", cookTime);
        tag.putInt("totalCookTime", totalCookTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        burnTime = tag.getInt("burnTime");
        cookTime = tag.getInt("cookTime");
        totalCookTime = tag.getInt("totalCookTime");
    }

    public void dropInventory() {
        if (level == null) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
        }
    }
}
