package com.chouzz.skyresourcereforge.alchemy.block.entity;

import com.chouzz.skyresourcereforge.heat.HeatSources;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipeInput;
import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class CrucibleBlockEntity extends BlockEntity {
    private static final int TANK_CAPACITY = 4000;
    private static final int MAX_ITEM_AMOUNT = 4000;
    private static final int BASE_SPEED = 8;

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final FluidTank fluidTank = new FluidTank(TANK_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    private int itemAmount = 0;
    private ItemStack itemIn = ItemStack.EMPTY;

    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUCIBLE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public FluidTank getTank() {
        return fluidTank;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int amount) {
        this.itemAmount = amount;
        setChanged();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CrucibleBlockEntity blockEntity) {
        if (level.isClientSide) return;

        AABB aabb = new AABB(pos.getX(), pos.getY() + 0.2, pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, aabb);
        for (ItemEntity entity : entities) {
            if (!entity.getItem().isEmpty()) {
                blockEntity.insertStack(entity.getItem());
                if (entity.getItem().isEmpty()) {
                    entity.discard();
                }
            }
        }

        BlockEntity above = level.getBlockEntity(pos.above());
        if (above instanceof com.chouzz.skyresourcereforge.block.entity.CrucibleInserterBlockEntity inserter) {
            ItemStack stack = inserter.getInventory().getStackInSlot(0);
            if (!stack.isEmpty()) {
                blockEntity.insertStack(stack);
            }
        }

        if (blockEntity.itemAmount > 0) {
            int melt = Math.min(blockEntity.getHeatSourceVal(level, pos), blockEntity.itemAmount);
            if (!blockEntity.itemIn.isEmpty() && melt > 0
                    && blockEntity.fluidTank.getFluidAmount() + melt <= blockEntity.fluidTank.getCapacity()) {
                ProcessRecipe recipe = blockEntity.getCrucibleRecipe(blockEntity.itemIn);
                if (recipe != null && !recipe.getFluidOutputs().isEmpty()) {
                    FluidStack output = recipe.getFluidOutputs().get(0).copy();
                    output.setAmount(melt);
                    blockEntity.fluidTank.fill(output, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
                    blockEntity.itemAmount -= melt;
                }
            }

            if (blockEntity.fluidTank.getFluidAmount() == 0 && blockEntity.itemAmount == 0) {
                blockEntity.itemIn = ItemStack.EMPTY;
            }
            blockEntity.setChanged();
        }
    }

    private ProcessRecipe getCrucibleRecipe(ItemStack stack) {
        if (level == null || stack.isEmpty()) return null;
        ProcessRecipeInput input = new ProcessRecipeInput(List.of(stack));
        return level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.CRUCIBLE.get(), input, level)
                .map(holder -> holder.value())
                .orElse(null);
    }

    private void insertStack(ItemStack stack) {
        if (level == null || stack.isEmpty()) return;
        ProcessRecipe recipe = getCrucibleRecipe(stack);
        if (recipe == null || recipe.getFluidOutputs().isEmpty()) {
            return;
        }
        int amount = recipe.getFluidOutputs().get(0).getAmount();
        if (itemAmount + amount > MAX_ITEM_AMOUNT) {
            return;
        }
        if (fluidTank.getFluid().isEmpty() && itemAmount == 0) {
            itemIn = stack.copyWithCount(1);
        }
        if (itemIn.isEmpty() || ItemStack.isSameItemSameComponents(itemIn, stack)) {
            itemAmount += amount;
            stack.shrink(recipe.getInputs().isEmpty() ? 1 : recipe.getInputs().get(0).count());
            setChanged();
        }
    }

    private int getHeatSourceVal(Level level, BlockPos pos) {
        if (HeatSources.isValidHeatSource(pos.below(), level)) {
            int base = HeatSources.getHeatSourceValue(pos.below(), level);
            if (base > 0) {
                return Math.max((int) ((float) base * (float) BASE_SPEED / 8f), 1);
            }
        }
        return 0;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.put("fluid", fluidTank.writeToNBT(registries, new CompoundTag()));
        tag.putInt("itemAmount", itemAmount);
        if (!itemIn.isEmpty()) {
            CompoundTag itemTag = new CompoundTag();
            itemIn.save(registries, itemTag);
            tag.put("itemIn", itemTag);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        if (tag.contains("fluid")) {
            fluidTank.readFromNBT(registries, tag.getCompound("fluid"));
        }
        itemAmount = tag.getInt("itemAmount");
        if (tag.contains("itemIn")) {
            itemIn = ItemStack.parseOptional(registries, tag.getCompound("itemIn"));
        } else {
            itemIn = ItemStack.EMPTY;
        }
    }

    public void dropInventory() {
        if (level == null) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
        }
        fluidTank.drain(fluidTank.getFluidAmount(), net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
    }
}
