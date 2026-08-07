package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.item.BaseComponentItem;
import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class WildlifeAttractorBlockEntity extends BlockEntity {
    private static final int MATTER_TIME_DEFAULT = 320;
    private static final int WATER_USAGE_DEFAULT = 20;
    private static final int WATER_CAPACITY_DEFAULT = 4000;
    private static final int SPAWN_CHANCE = 600;

    private static final List<String> ANIMAL_IDS = List.of(
            "minecraft:sheep", "minecraft:cow", "minecraft:chicken", "minecraft:pig",
            "minecraft:rabbit", "minecraft:horse", "minecraft:parrot"
    );

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return !stack.isEmpty()
                    && stack.getItem() instanceof BaseComponentItem
                    && BaseComponentItem.getVariantIndex(stack) == 0;
        }
    };

    private final FluidTank tank = new FluidTank(WATER_CAPACITY_DEFAULT, fluidStack -> fluidStack.getFluid() == Fluids.WATER) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    private int matterLeft = 0;

    public WildlifeAttractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WILDLIFE_ATTRACTOR.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public FluidTank getTank() {
        return tank;
    }

    public int getMatterLeft() {
        return matterLeft;
    }

    public int getMatterTime() {
        return MATTER_TIME_DEFAULT;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WildlifeAttractorBlockEntity blockEntity) {
        if (level.isClientSide) return;

        if (level.hasNeighborSignal(pos)) {
            blockEntity.setChanged();
            return;
        }

        if (blockEntity.matterLeft <= 0) {
            ItemStack fuel = blockEntity.inventory.getStackInSlot(0);
            if (!fuel.isEmpty() && blockEntity.inventory.isItemValid(0, fuel)) {
                fuel.shrink(1);
                blockEntity.matterLeft = MATTER_TIME_DEFAULT;
                blockEntity.setChanged();
            }
        }

        if (blockEntity.matterLeft > 0
                && blockEntity.tank.getFluidAmount() >= WATER_USAGE_DEFAULT) {
            blockEntity.tank.drain(WATER_USAGE_DEFAULT, IFluidHandler.FluidAction.EXECUTE);
            blockEntity.matterLeft--;

            if (level.getRandom().nextInt(SPAWN_CHANCE) == 0) {
                blockEntity.spawnRandomAnimal(level, pos);
            }
            blockEntity.setChanged();
        }
    }

    private void spawnRandomAnimal(Level level, BlockPos pos) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;

        String animalId = ANIMAL_IDS.get(level.getRandom().nextInt(ANIMAL_IDS.size()));
        ResourceLocation rl = ResourceLocation.parse(animalId);
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(rl);
        if (entityType == null) return;

        double x = pos.getX() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 4;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 4;

        if (entityType.create(level) instanceof Mob mob) {
            mob.moveTo(x, y, z, level.getRandom().nextFloat() * 360F, 0F);
            mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(BlockPos.containing(x, y, z)), MobSpawnType.TRIGGERED, null);
            if (mob.checkSpawnObstruction(level)) {
                level.addFreshEntity(mob);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.put("tank", tank.writeToNBT(registries, new CompoundTag()));
        tag.putInt("matterLeft", matterLeft);
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
        matterLeft = tag.getInt("matterLeft");
    }

    public void dropInventory() {
        if (level == null) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
        }
        tank.drain(tank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
    }
}
