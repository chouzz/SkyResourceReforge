package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.block.CombustionControllerBlock;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        
        // Find item entities in the block behind
        AABB aabb = new AABB(posBehind);
        List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class, aabb);
        if (itemEntities.isEmpty()) return;

        // Simplify recipe matching for now: only check first item entity
        // Real implementation should aggregate all items in the AABB
        for (ItemEntity entity : itemEntities) {
            ItemStack stack = entity.getItem();
            Optional<RecipeHolder<ProcessRecipe>> recipeOpt = level.getRecipeManager()
                    .getRecipeFor(ModRecipeTypes.COMBUSTION.get(), new SimpleItemInput(stack), level);

            if (recipeOpt.isPresent()) {
                ProcessRecipe recipe = recipeOpt.get().value();
                if (curHU >= recipe.getParameter()) {
                    // Craft!
                    ItemStack output = recipe.getOutputs().get(0).copy();
                    stack.shrink(1);
                    if (stack.isEmpty()) entity.discard();

                    level.addFreshEntity(new ItemEntity(level, posBehind.getX() + 0.5, posBehind.getY() + 0.5, posBehind.getZ() + 0.5, output));
                    
                    // Reduce heat (simplified)
                    heater.setHeatValue((int) (curHU * 0.8f));
                    cooldownTicks = 20; // 1 second cooldown
                    setChanged();
                    break;
                }
            }
        }
    }

    private BlockPos getPosBehind() {
        return worldPosition.relative(getBlockState().getValue(CombustionControllerBlock.FACING).getOpposite());
    }

    private CasingBlockEntity getHeater(BlockPos posBehind) {
        BlockEntity be = level.getBlockEntity(posBehind.below());
        return be instanceof CasingBlockEntity ? (CasingBlockEntity) be : null;
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

    // Temporary SimpleItemInput for recipe matching
    private static record SimpleItemInput(ItemStack stack) implements RecipeInput {
        @Override
        public ItemStack getItem(int index) { return stack; }
        @Override
        public int size() { return 1; }
    }
}
