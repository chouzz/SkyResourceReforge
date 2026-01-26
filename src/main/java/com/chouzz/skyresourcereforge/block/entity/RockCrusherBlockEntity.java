package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RockCrusherBlockEntity extends BlockEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private float progress = 0;
    private final List<ItemStack> bufferStacks = new ArrayList<>();

    public RockCrusherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ROCK_CRUSHER.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public float getProgress() {
        return progress;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RockCrusherBlockEntity blockEntity) {
        if (level.isClientSide) return;
        if (!blockEntity.bufferStacks.isEmpty() && !blockEntity.fullOutput()) {
            blockEntity.addToOutput(1);
            blockEntity.addToOutput(2);
            blockEntity.addToOutput(3);
            blockEntity.setChanged();
            return;
        }

        ItemStack input = blockEntity.inventory.getStackInSlot(0);
        if (input.isEmpty()) {
            blockEntity.progress = 0;
            blockEntity.setChanged();
            return;
        }

        ProcessRecipeInput recipeInput = new ProcessRecipeInput(List.of(input));
        List<ProcessRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.ROCK_CRUSHER.get())
                .stream()
                .map(holder -> holder.value())
                .toList();

        boolean hasRecipe = false;
        for (ProcessRecipe recipe : recipes) {
            if (recipe.matches(recipeInput, level)) {
                hasRecipe = true;
                break;
            }
        }

        if (!hasRecipe) {
            blockEntity.progress = 0;
            blockEntity.setChanged();
            return;
        }

        if (blockEntity.progress < 100) {
            blockEntity.progress += 2;
        }

        if (blockEntity.progress >= 100) {
            for (ProcessRecipe recipe : recipes) {
                if (!recipe.matches(recipeInput, level)) {
                    continue;
                }
                float chance = recipe.getParameter() * 1.2f;
                while (chance >= 1f) {
                    blockEntity.bufferStacks.add(recipe.getOutputs().get(0).copy());
                    chance -= 1f;
                }
                if (level.random.nextFloat() <= chance) {
                    blockEntity.bufferStacks.add(recipe.getOutputs().get(0).copy());
                }
            }
            input.shrink(1);
            if (input.isEmpty()) {
                blockEntity.inventory.setStackInSlot(0, ItemStack.EMPTY);
            }
            blockEntity.progress = 0;
        }
        blockEntity.setChanged();
    }

    private void addToOutput(int slot) {
        if (bufferStacks.isEmpty()) {
            return;
        }
        ItemStack stack = bufferStacks.get(bufferStacks.size() - 1);
        ItemStack remaining = inventory.insertItem(slot, stack, false);
        if (remaining.isEmpty()) {
            bufferStacks.remove(bufferStacks.size() - 1);
        } else {
            bufferStacks.set(bufferStacks.size() - 1, remaining);
        }
    }

    private boolean fullOutput() {
        return !inventory.getStackInSlot(1).isEmpty()
                && !inventory.getStackInSlot(2).isEmpty()
                && !inventory.getStackInSlot(3).isEmpty();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putFloat("progress", progress);
        ListTag bufferTag = new ListTag();
        for (ItemStack stack : bufferStacks) {
            CompoundTag itemTag = new CompoundTag();
            stack.save(registries, itemTag);
            bufferTag.add(itemTag);
        }
        tag.put("buffer", bufferTag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        progress = tag.getFloat("progress");
        bufferStacks.clear();
        if (tag.contains("buffer")) {
            ListTag bufferTag = tag.getList("buffer", CompoundTag.TAG_COMPOUND);
            for (int i = 0; i < bufferTag.size(); i++) {
                CompoundTag itemTag = bufferTag.getCompound(i);
                bufferStacks.add(ItemStack.parseOptional(registries, itemTag));
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
