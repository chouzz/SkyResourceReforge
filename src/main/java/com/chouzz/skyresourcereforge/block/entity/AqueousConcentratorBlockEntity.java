package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipeInput;
import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class AqueousConcentratorBlockEntity extends BlockEntity implements IFluidHandler {
    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final FluidTank tank = new FluidTank(4000);
    private int progress = 0;
    private final boolean isConcentrator;

    public AqueousConcentratorBlockEntity(BlockPos pos, BlockState state, boolean isConcentrator) {
        super(isConcentrator ? ModBlockEntities.AQUEOUS_CONCENTRATOR.get() : ModBlockEntities.AQUEOUS_DECONCENTRATOR.get(), pos, state);
        this.isConcentrator = isConcentrator;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public FluidTank getTank() {
        return tank;
    }

    public int getProgress() {
        return progress;
    }

    public boolean isConcentrator() {
        return isConcentrator;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AqueousConcentratorBlockEntity blockEntity) {
        if (level.isClientSide) return;

        if (blockEntity.isConcentrator) {
            blockEntity.updateConcentrate(level);
        } else {
            blockEntity.updateDeconcentrate(level);
        }
        blockEntity.setChanged();
    }

    private void updateConcentrate(Level level) {
        ItemStack input = inventory.getStackInSlot(0);
        if (input.isEmpty() || tank.getFluid().isEmpty()) {
            progress = 0;
            return;
        }

        ProcessRecipeInput recipeInput = new ProcessRecipeInput(
                List.of(input),
                List.of(tank.getFluid()),
                Float.MAX_VALUE,
                true
        );
        ProcessRecipe recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.WATER_EXTRACTOR_INSERT.get(), recipeInput, level)
                .map(holder -> holder.value())
                .orElse(null);

        if (recipe != null && progress < 100) {
            ItemStack output = recipe.getOutputs().get(0).copy();
            if (inventory.insertItem(1, output, true).isEmpty()) {
                progress += 1; // TODO: Configurable speed
            } else {
                progress = 0;
            }
        } else if (recipe == null) {
            progress = 0;
        }

        if (progress >= 100 && recipe != null) {
            ItemStack output = recipe.getOutputs().get(0).copy();
            if (inventory.insertItem(1, output, false).isEmpty()) {
                FluidStack fluidInput = recipe.getFluidInputs().get(0);
                tank.drain(fluidInput, IFluidHandler.FluidAction.EXECUTE);
                int inputCount = recipe.getInputs().isEmpty() ? 1 : recipe.getInputs().get(0).count();
                input.shrink(inputCount);
                if (input.getCount() <= 0) {
                    inventory.setStackInSlot(0, ItemStack.EMPTY);
                }
                progress = 0;
            }
        }
    }

    private void updateDeconcentrate(Level level) {
        RecipeManager recipeManager = level.getRecipeManager();
        ItemStack input = inventory.getStackInSlot(0);
        if (input.isEmpty()) {
            progress = 0;
            return;
        }

        ProcessRecipeInput recipeInput = new ProcessRecipeInput(List.of(input));
        ProcessRecipe recipe = recipeManager.getRecipeFor(ModRecipeTypes.WATER_EXTRACTOR_EXTRACT.get(), recipeInput, level)
                .map(holder -> holder.value())
                .orElse(null);

        if (recipe != null && progress < 100) {
            if (!recipe.getFluidOutputs().isEmpty()) {
                FluidStack fluidOutput = recipe.getFluidOutputs().get(0);
                if (tank.fill(fluidOutput, IFluidHandler.FluidAction.SIMULATE) >= fluidOutput.getAmount()) {
                    boolean canInsertItem = true;
                    if (!recipe.getOutputs().isEmpty()) {
                        ItemStack output = recipe.getOutputs().get(0).copy();
                        canInsertItem = inventory.insertItem(1, output, true).isEmpty();
                    }
                    if (canInsertItem) {
                        progress += 1; // TODO: Configurable speed
                    } else {
                        progress = 0;
                    }
                } else {
                    progress = 0;
                }
            } else {
                progress = 0;
            }
        } else if (recipe == null) {
            progress = 0;
        }

        if (progress >= 100 && recipe != null) {
            boolean inserted = true;
            if (!recipe.getOutputs().isEmpty()) {
                ItemStack output = recipe.getOutputs().get(0).copy();
                inserted = inventory.insertItem(1, output, false).isEmpty();
            }
            if (inserted) {
                FluidStack fluidOutput = recipe.getFluidOutputs().get(0);
                tank.fill(fluidOutput, IFluidHandler.FluidAction.EXECUTE);
                int inputCount = recipe.getInputs().isEmpty() ? 1 : recipe.getInputs().get(0).count();
                input.shrink(inputCount);
                if (input.getCount() <= 0) {
                    inventory.setStackInSlot(0, ItemStack.EMPTY);
                }
                progress = 0;
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.put("tank", tank.writeToNBT(registries, new CompoundTag()));
        tag.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        if (tag.contains("tank")) {
            tank.readFromNBT(registries, tag.getCompound("tank"));
        }
        progress = tag.getInt("progress");
    }

    public void dropInventory() {
        if (level == null) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
        }
    }

    // IFluidHandler implementation
    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.tank.getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.tank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        // Only accept water for concentrator, any fluid for deconcentrator (but typically water output)
        return isConcentrator ? stack.getFluid() == net.minecraft.world.level.material.Fluids.WATER : true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return isConcentrator ? tank.fill(resource, action) : 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return !isConcentrator ? tank.drain(resource, action) : FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return !isConcentrator ? tank.drain(maxDrain, action) : FluidStack.EMPTY;
    }
}
