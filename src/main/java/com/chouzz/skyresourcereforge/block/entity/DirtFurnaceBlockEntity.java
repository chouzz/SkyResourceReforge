package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.crafting.SingleRecipeInput;
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
    private int totalBurnTime = 0;

    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> DirtFurnaceBlockEntity.this.burnTime;
                case 1 -> DirtFurnaceBlockEntity.this.totalBurnTime;
                case 2 -> DirtFurnaceBlockEntity.this.cookTime;
                case 3 -> DirtFurnaceBlockEntity.this.totalCookTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> DirtFurnaceBlockEntity.this.burnTime = value;
                case 1 -> DirtFurnaceBlockEntity.this.totalBurnTime = value;
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

        if (blockEntity.burnTime > 0) {
            blockEntity.burnTime--;
        }

        ItemStack fuelStack = blockEntity.inventory.getStackInSlot(1);
        ItemStack inputStack = blockEntity.inventory.getStackInSlot(0);
        ItemStack outputStack = blockEntity.inventory.getStackInSlot(2);

        RecipeManager recipeManager = level.getRecipeManager();
        AbstractCookingRecipe recipe = blockEntity.getRecipe(recipeManager, inputStack);

        if ((blockEntity.burnTime > 0 || !fuelStack.isEmpty()) && !inputStack.isEmpty() && recipe != null) {
            if (blockEntity.burnTime == 0) {
                int fuelTime = AbstractFurnaceBlockEntity.getFuel().getOrDefault(fuelStack.getItem(), 0);
                if (fuelTime > 0) {
                    blockEntity.burnTime = fuelTime;
                    blockEntity.totalBurnTime = fuelTime;
                    blockEntity.totalCookTime = recipe.getCookingTime();
                    fuelStack.shrink(1);
                    if (fuelStack.isEmpty()) {
                        blockEntity.inventory.setStackInSlot(1, ItemStack.EMPTY);
                    }
                    changed = true;
                }
            }

            if (blockEntity.burnTime > 0 && blockEntity.canSmelt(recipe, outputStack)) {
                blockEntity.cookTime++;
                if (blockEntity.cookTime >= blockEntity.totalCookTime) {
                    blockEntity.cookTime = 0;
                    blockEntity.smelt(recipe, inputStack, outputStack);
                    changed = true;
                }
            } else {
                blockEntity.cookTime = 0;
            }
        } else {
            blockEntity.cookTime = 0;
        }

        if (wasBurning != blockEntity.isBurning()) {
            state = state.setValue(com.chouzz.skyresourcereforge.block.DirtFurnaceBlock.LIT, blockEntity.isBurning());
            level.setBlock(pos, state, 3);
            changed = true;
        }

        if (changed) {
            blockEntity.setChanged();
        }
    }

    private AbstractCookingRecipe getRecipe(RecipeManager recipeManager, ItemStack inputStack) {
        if (inputStack.isEmpty()) return null;
        SingleRecipeInput input = new SingleRecipeInput(inputStack);
        return recipeManager.getRecipeFor(RecipeType.SMELTING, input, level).map(holder -> holder.value()).orElse(null);
    }

    private boolean canSmelt(AbstractCookingRecipe recipe, ItemStack outputStack) {
        if (recipe == null) return false;
        ItemStack result = recipe.getResultItem(level.registryAccess());
        if (result.isEmpty()) return false;
        if (outputStack.isEmpty()) return true;
        if (!ItemStack.isSameItemSameComponents(outputStack, result)) return false;
        int total = outputStack.getCount() + result.getCount();
        return total <= outputStack.getMaxStackSize();
    }

    private void smelt(AbstractCookingRecipe recipe, ItemStack inputStack, ItemStack outputStack) {
        if (!canSmelt(recipe, outputStack)) return;
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
        if (outputStack.isEmpty()) {
            inventory.setStackInSlot(2, result);
        } else {
            outputStack.grow(result.getCount());
        }
        inputStack.shrink(1);
        if (inputStack.isEmpty()) {
            inventory.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("burnTime", burnTime);
        tag.putInt("totalBurnTime", totalBurnTime);
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
        totalBurnTime = tag.getInt("totalBurnTime");
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
