package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class FreezerBlockEntity extends BlockEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private float[] timeFreeze = new float[1];
    private final float speed;

    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> (int) (speed * 100);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            // Speed is immutable
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public FreezerBlockEntity(BlockPos pos, BlockState state, float speed) {
        super(ModBlockEntities.MINI_FREEZER.get(), pos, state);
        this.speed = speed;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public float getSpeed() {
        return speed;
    }

    public float[] getTimeFreeze() {
        return timeFreeze;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FreezerBlockEntity blockEntity) {
        if (level.isClientSide) return;

        if (blockEntity.timeFreeze == null) {
            blockEntity.timeFreeze = new float[1];
            return;
        }

        RecipeManager recipeManager = level.getRecipeManager();
        var recipeHolders = recipeManager.getAllRecipesFor(ModRecipeTypes.FREEZER.get());

        for (int i = 0; i < blockEntity.inventory.getSlots() / 2; i++) {
            ItemStack input = blockEntity.inventory.getStackInSlot(i);
            if (input.isEmpty()) {
                blockEntity.timeFreeze[i] = 0;
                continue;
            }

            ProcessRecipe recipe = null;
            for (var holder : recipeHolders) {
                ProcessRecipe r = holder.value();
                if (!r.getInputs().isEmpty() && r.getInputs().get(0).test(input)) {
                    recipe = r;
                    break;
                }
            }

            if (recipe != null && blockEntity.canProcess(recipe.getOutputs().get(0).copy(), i + blockEntity.inventory.getSlots() / 2)) {
                int timeReq = blockEntity.getTimeReq(recipe, input);
                if (blockEntity.timeFreeze[i] >= timeReq) {
                    int amtProcessed = 0;
                    int groups = blockEntity.getGroupsFreezing(recipe, input);
                    for (int amt = 0; amt < groups; amt++) {
                        if (blockEntity.ejectResultSlot(recipe.getOutputs().get(0).copy(), i)) {
                            amtProcessed++;
                        }
                    }
                    ItemStack inputStack = blockEntity.inventory.getStackInSlot(i);
                    // Assume 1 item per recipe input for now (can be enhanced later with count field)
                    inputStack.shrink(1 * amtProcessed);
                    if (inputStack.getCount() <= 0) {
                        blockEntity.inventory.setStackInSlot(i, ItemStack.EMPTY);
                    }
                    blockEntity.timeFreeze[i] = 0;
                } else {
                    blockEntity.timeFreeze[i] += blockEntity.speed * 10;
                }
            } else {
                blockEntity.timeFreeze[i] = 0;
            }
        }

        blockEntity.setChanged();
    }

    private boolean canProcess(ItemStack output, int slotOut) {
        ItemStack existing = inventory.getStackInSlot(slotOut);
        if (existing.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItem(existing, output)) {
            return false;
        }
        int result = existing.getCount() + output.getCount();
        return result <= inventory.getSlotLimit(slotOut) && result <= existing.getMaxStackSize();
    }

    int getGroupsFreezing(ProcessRecipe recipe, ItemStack input) {
        // Assume 1 item per recipe input for now (can be enhanced later with count field)
        return input.getCount();
    }

    public int getTimeReq(ProcessRecipe recipe, ItemStack input) {
        return (int) (recipe.getParameter() * getGroupsFreezing(recipe, input));
    }

    boolean ejectResultSlot(ItemStack output, int inSlot) {
        if (canProcess(output, inSlot + inventory.getSlots() / 2)) {
            inventory.insertItem(inSlot + inventory.getSlots() / 2, output, false);
            return true;
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        if (timeFreeze != null) {
            for (int i = 0; i < timeFreeze.length; i++) {
                tag.putFloat("time" + i, timeFreeze[i]);
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        timeFreeze = new float[1];
        for (int i = 0; i < timeFreeze.length; i++) {
            if (tag.contains("time" + i)) {
                timeFreeze[i] = tag.getFloat("time" + i);
            }
        }
    }

    public void dropInventory() {
        if (level == null) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
        }
    }
}
