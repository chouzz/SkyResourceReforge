package com.chouzz.skyresourcereforge.block.entity;

import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

/**
 * Fluid Dropper block entity.
 * Ported from SkyResources FluidDropperTile.
 */
public class FluidDropperBlockEntity extends BlockEntity {
    private static final int CAPACITY = 1000;
    private static final Direction[] CHECK_DIRECTIONS = new Direction[] {
            Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    private final FluidTank tank = new FluidTank(CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    public FluidDropperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_DROPPER.get(), pos, state);
    }

    public FluidTank getTank() {
        return tank;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FluidDropperBlockEntity blockEntity) {
        if (level.isClientSide) return;

        blockEntity.pullFromAround(level, pos);

        if (blockEntity.tank.getFluidAmount() >= CAPACITY && level.getBlockState(pos.below()).isAir()) {
            FluidStack fluid = blockEntity.tank.getFluid();
            if (!fluid.isEmpty()) {
                FluidState fluidState = fluid.getFluid().defaultFluidState();
                level.setBlockAndUpdate(pos.below(), fluidState.createLegacyBlock());
                blockEntity.tank.setFluid(FluidStack.EMPTY);
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                        SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.5F, 1.0F);
            }
        }
        blockEntity.setChanged();
    }

    private void pullFromAround(Level level, BlockPos pos) {
        if (level.hasNeighborSignal(pos)) {
            return;
        }
        for (Direction dir : CHECK_DIRECTIONS) {
            BlockPos checkPos = pos.relative(dir);
            IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, checkPos, dir.getOpposite());
            if (handler == null) {
                continue;
            }
            for (int i = 0; i < handler.getTanks(); i++) {
                FluidStack available = handler.getFluidInTank(i);
                if (available.isEmpty()) {
                    continue;
                }
                if (!tank.getFluid().isEmpty() && !FluidStack.isSameFluidSameComponents(available, tank.getFluid())) {
                    continue;
                }
                int space = CAPACITY - tank.getFluidAmount();
                if (space <= 0) {
                    return;
                }
                FluidStack drained = handler.drain(new FluidStack(available.getFluid(), space), IFluidHandler.FluidAction.SIMULATE);
                if (!drained.isEmpty()) {
                    int filled = tank.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                    handler.drain(new FluidStack(available.getFluid(), filled), IFluidHandler.FluidAction.EXECUTE);
                    if (tank.getFluidAmount() >= CAPACITY) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("tank", tank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("tank")) {
            tank.readFromNBT(registries, tag.getCompound("tank"));
        }
    }

    public void dropInventory() {
        if (level == null) return;
        // Drop tank fluid as filled buckets
        if (!tank.getFluid().isEmpty()) {
            net.minecraft.world.item.Item bucketItem = tank.getFluid().getFluid().getBucket();
            if (bucketItem != null && bucketItem != net.minecraft.world.item.Items.AIR) {
                int bucketVolume = net.neoforged.neoforge.fluids.FluidType.BUCKET_VOLUME;
                int bucketCount = tank.getFluidAmount() / bucketVolume;
                if (bucketCount > 0) {
                    net.minecraft.world.Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
                            new net.minecraft.world.item.ItemStack(bucketItem, bucketCount));
                }
            }
        }
    }
}
