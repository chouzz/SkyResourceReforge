package com.chouzz.skyresourcereforge.item;

import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public class WaterExtractorItem extends Item {
    public static final int CAPACITY = 5000;

    public WaterExtractorItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public int getMaxUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player) || level.isClientSide) return;

        if (getMaxUseDuration(stack, entity) - timeLeft < 20) return;

        HitResult hitResult = player.pick(5.0D, 0.0F, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
            BlockState state = level.getBlockState(pos);
            
            IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
            if (handler == null) return;

            // Extract from block
            Optional<RecipeHolder<ProcessRecipe>> recipeOpt = level.getRecipeManager()
                    .getRecipeFor(ModRecipeTypes.WATER_EXTRACTOR_EXTRACT.get(), new SimpleItemInput(new ItemStack(state.getBlock().asItem())), level);

            if (recipeOpt.isPresent()) {
                ProcessRecipe recipe = recipeOpt.get().value();
                if (recipe.getFluidOutputs().isEmpty()) return;
                FluidStack fluid = recipe.getFluidOutputs().get(0).copy();
                
                int filled = handler.fill(fluid, IFluidHandler.FluidAction.SIMULATE);
                if (filled == fluid.getAmount()) {
                    handler.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                    BlockState resultState = getRecipeBlockOutput(recipe);
                    level.setBlockAndUpdate(pos, resultState != null ? resultState : Blocks.AIR.defaultBlockState());
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            } else if (state.is(Blocks.WATER) && state.getFluidState().isSource()) {
                FluidStack water = new FluidStack(net.minecraft.world.level.material.Fluids.WATER, FluidType.BUCKET_VOLUME);
                int filled = handler.fill(water, IFluidHandler.FluidAction.SIMULATE);
                if (filled == water.getAmount()) {
                    handler.fill(water, IFluidHandler.FluidAction.EXECUTE);
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;

        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();
        
        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (handler == null) return InteractionResult.FAIL;

        // Try to fill fluid handler block
        IFluidHandler blockHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, context.getClickedFace());
        if (blockHandler != null) {
            FluidStack drained = handler.drain(CAPACITY, IFluidHandler.FluidAction.SIMULATE);
            if (!drained.isEmpty()) {
                int filled = blockHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                handler.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                return InteractionResult.SUCCESS;
            }
        }

        // Insert into block (recipe)
        FluidStack currentFluid = handler.getFluidInTank(0);
        if (!currentFluid.isEmpty()) {
            Optional<RecipeHolder<ProcessRecipe>> recipeOpt = level.getRecipeManager()
                    .getRecipeFor(ModRecipeTypes.WATER_EXTRACTOR_INSERT.get(), new SimpleItemInput(new ItemStack(state.getBlock().asItem())), level);
            
            if (recipeOpt.isPresent()) {
                ProcessRecipe recipe = recipeOpt.get().value();
                if (recipe.getFluidInputs().isEmpty()) return InteractionResult.PASS;
                BlockState resultState = getRecipeBlockOutput(recipe);
                if (resultState == null) return InteractionResult.PASS;
                FluidStack requiredFluid = recipe.getFluidInputs().get(0);
                if (FluidStack.isSameFluidSameComponents(currentFluid, requiredFluid) && currentFluid.getAmount() >= requiredFluid.getAmount()) {
                    handler.drain(requiredFluid.getAmount(), IFluidHandler.FluidAction.EXECUTE);
                    level.setBlockAndUpdate(pos, resultState);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (handler != null) {
            FluidStack fluid = handler.getFluidInTank(0);
            tooltip.add(Component.literal("Water: " + fluid.getAmount() + " / " + CAPACITY + " mB"));
        }
    }

    /**
     * Resolves the block output from a recipe, null-checking Block.byItem() which
     * returns null when the output item is not a BlockItem. Returns null when the
     * recipe has no outputs or the output is not a block, so callers can decide
     * how to handle the absence (e.g. abort vs. fall back to AIR).
     *
     * @return the block state if the recipe has a valid block output, or null
     */
    @Nullable
    private static BlockState getRecipeBlockOutput(ProcessRecipe recipe) {
        if (recipe.getOutputs().isEmpty()) return null;
        Block block = Block.byItem(recipe.getOutputs().get(0).getItem());
        return block != null ? block.defaultBlockState() : null;
    }

    private static record SimpleItemInput(ItemStack stack) implements RecipeInput {
        @Override
        public ItemStack getItem(int index) { return stack; }
        @Override
        public int size() { return 1; }
    }
}
