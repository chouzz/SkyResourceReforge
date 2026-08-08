package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.block.CombustionControllerBlock;
import com.chouzz.skyresourcereforge.recipe.CountedIngredient;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class CombustionControllerBlockEntity extends BlockEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private int cooldownTicks = 0;

    public CombustionControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMBUSTION_CONTROLLER.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        if (cooldownTicks > 0) {
            cooldownTicks--;
            setChanged();
        } else {
            craftSingleItem();
        }
    }

    private void craftSingleItem() {
        if (level == null) return;

        BlockPos posBehind = getPosBehind();
        CasingBlockEntity heater = getHeater(posBehind);
        if (heater == null) return;

        float curHU = heater.getHeatValue();

        AABB aabb = new AABB(posBehind);
        List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class, aabb);
        if (itemEntities.isEmpty()) return;

        List<ItemStack> available = new ArrayList<>();
        for (ItemEntity entity : itemEntities) {
            if (!entity.getItem().isEmpty()) {
                available.add(entity.getItem().copy());
            }
        }
        available = mergeStacks(available);

        ProcessRecipe recipe = selectRecipe(available, curHU);
        if (recipe == null) return;

        List<ItemStack> remaining = consumeInputs(available, recipe);
        if (remaining == null) return;

        for (ItemEntity entity : itemEntities) {
            entity.discard();
        }

        if (recipe.getOutputs().isEmpty()) return;
        ItemStack output = recipe.getOutputs().get(0).copy();
        output = tryInsertCollector(posBehind.below().below(), output);
        if (!output.isEmpty()) {
            level.addFreshEntity(new ItemEntity(level, posBehind.getX() + 0.5, posBehind.getY() + 0.5,
                    posBehind.getZ() + 0.5, output));
        }

        for (ItemStack stack : remaining) {
            if (!stack.isEmpty()) {
                level.addFreshEntity(new ItemEntity(level, posBehind.getX() + 0.5, posBehind.getY() + 0.5,
                        posBehind.getZ() + 0.5, stack));
            }
        }

        heater.setHeatValue((int) (curHU * 0.8f));
        cooldownTicks = 20;
        setChanged();
    }

    private BlockPos getPosBehind() {
        return worldPosition.relative(getBlockState().getValue(CombustionControllerBlock.FACING).getOpposite());
    }

    private CasingBlockEntity getHeater(BlockPos posBehind) {
        BlockEntity be = level.getBlockEntity(posBehind.below());
        return be instanceof CasingBlockEntity ? (CasingBlockEntity) be : null;
    }

    private ProcessRecipe selectRecipe(List<ItemStack> available, float curHU) {
        if (available.isEmpty()) return null;
        ProcessRecipeInput recipeInput = new ProcessRecipeInput(available, List.of(), curHU, false, true);
        var recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.COMBUSTION.get());
        for (var holder : recipes) {
            ProcessRecipe recipe = holder.value();
            if (recipe.getOutputs().isEmpty() || !isOutputAllowed(recipe.getOutputs().get(0))) {
                continue;
            }
            if (recipe.matches(recipeInput, level)) {
                return recipe;
            }
        }
        return null;
    }

    private boolean isOutputAllowed(ItemStack output) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack filter = inventory.getStackInSlot(i);
            if (!filter.isEmpty() && ItemStack.isSameItemSameComponents(filter, output)) {
                return true;
            }
        }
        return false;
    }

    private List<ItemStack> consumeInputs(List<ItemStack> available, ProcessRecipe recipe) {
        List<ItemStack> remaining = new ArrayList<>();
        for (ItemStack stack : available) {
            remaining.add(stack.copy());
        }
        for (CountedIngredient ingredient : recipe.getInputs()) {
            int toConsume = ingredient.count();
            for (int i = 0; i < remaining.size() && toConsume > 0; i++) {
                ItemStack stack = remaining.get(i);
                if (ingredient.test(stack)) {
                    int take = Math.min(stack.getCount(), toConsume);
                    stack.shrink(take);
                    toConsume -= take;
                    if (stack.isEmpty()) {
                        remaining.remove(i);
                        i--;
                    }
                }
            }
            if (toConsume > 0) {
                return null;
            }
        }
        return remaining;
    }

    private List<ItemStack> mergeStacks(List<ItemStack> items) {
        List<ItemStack> merged = new ArrayList<>();
        for (ItemStack stack : items) {
            boolean mergedInto = false;
            for (ItemStack existing : merged) {
                if (ItemStack.isSameItemSameComponents(existing, stack)) {
                    existing.grow(stack.getCount());
                    mergedInto = true;
                    break;
                }
            }
            if (!mergedInto) {
                merged.add(stack.copy());
            }
        }
        return merged;
    }

    private ItemStack tryInsertCollector(BlockPos pos, ItemStack stack) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof CombustionCollectorBlockEntity collector)) {
            return stack;
        }
        for (int i = 0; i < collector.getInventory().getSlots(); i++) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
            stack = collector.getInventory().insertItem(i, stack, false);
        }
        return stack;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("cooldown", cooldownTicks);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        cooldownTicks = tag.getInt("cooldown");
    }

    public void dropInventory() {
        if (level == null) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
        }
    }

}
