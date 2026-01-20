package com.chouzz.skyresourcereforge.block;

import com.chouzz.skyresourcereforge.block.entity.AqueousConcentratorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class AqueousConcentratorBlock extends BaseEntityBlock {
    public static final MapCodec<AqueousConcentratorBlock> CODEC = net.minecraft.world.level.block.state.BlockBehaviour.Properties.CODEC
            .fieldOf("properties")
            .xmap(props -> new AqueousConcentratorBlock(props, true), b -> b.properties); // Default for codec
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private final boolean isConcentrator;
    protected final net.minecraft.world.level.block.state.BlockBehaviour.Properties properties;

    public AqueousConcentratorBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties, boolean isConcentrator) {
        super(properties);
        this.properties = properties;
        this.isConcentrator = isConcentrator;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AqueousConcentratorBlockEntity(pos, state, isConcentrator);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, be) -> {
            if (be instanceof AqueousConcentratorBlockEntity aqueous) {
                AqueousConcentratorBlockEntity.tick(lvl, pos, st, aqueous);
            }
        };
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof AqueousConcentratorBlockEntity aqueous) {
                aqueous.dropInventory();
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    public boolean isConcentrator() {
        return isConcentrator;
    }
}
