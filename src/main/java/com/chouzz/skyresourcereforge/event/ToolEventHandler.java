package com.chouzz.skyresourcereforge.event;

import java.util.List;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.item.ItemKnife;
import com.chouzz.skyresourcereforge.item.ItemRockGrinder;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipeInput;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = SkyResourceReforge.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ToolEventHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        // getLevel() returns LevelAccessor, check if it's ServerLevel
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;

        // Use getPlayer() instead of getEntity()
        Player player = event.getPlayer();
        // Check if player is ServerPlayer
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ItemStack heldItem = player.getMainHandItem();
        BlockPos pos = event.getPos();
        // Use getState() instead of getBlockState()
        BlockState state = event.getState();

        if (heldItem.getItem() instanceof ItemKnife) {
            handleKnifeBreak(serverLevel, serverPlayer, heldItem, pos, state, event);
        } else if (heldItem.getItem() instanceof ItemRockGrinder) {
            handleRockGrinderBreak(serverLevel, serverPlayer, heldItem, pos, state, event);
        }
    }

    private static void handleKnifeBreak(ServerLevel level, ServerPlayer player, ItemStack tool, BlockPos pos, BlockState state, BlockEvent.BreakEvent event) {
        // Get knife recipes
        List<ProcessRecipe> recipes = level.getRecipeManager()
            .getAllRecipesFor(ModRecipeTypes.KNIFE.get())
            .stream()
            .map(recipeHolder -> recipeHolder.value())
            .toList();

        // Check each recipe
        for (ProcessRecipe recipe : recipes) {
            ProcessRecipeInput input = new ProcessRecipeInput(List.of(new ItemStack(state.getBlock())));
            if (recipe.matches(input, level)) {
                // Cancel normal block breaking
                event.setCanceled(true);

                // Damage tool - use correct method signature
                tool.hurtAndBreak(1, level, player, (stack) -> {});

                // Apply Fortune bonus
                var enchantmentLookup = level.holderLookup(net.minecraft.core.registries.Registries.ENCHANTMENT);
                Holder.Reference<net.minecraft.world.item.enchantment.Enchantment> fortuneHolder =
                    enchantmentLookup.getOrThrow(Enchantments.FORTUNE);
                int fortuneLevel = tool.getEnchantmentLevel(fortuneHolder);
                float chance = ((fortuneLevel + 3F) / 3F);

                // Spawn output with chance
                while (chance >= 1) {
                    popItem(level, recipe.getOutputs().get(0).copy(), pos);
                    chance -= 1;
                }
                if (level.getRandom().nextFloat() <= chance) {
                    popItem(level, recipe.getOutputs().get(0).copy(), pos);
                }

                // Destroy block without drops
                level.destroyBlock(pos, false);
                return;
            }
        }
    }

    private static void handleRockGrinderBreak(ServerLevel level, ServerPlayer player, ItemStack tool, BlockPos pos, BlockState state, BlockEvent.BreakEvent event) {
        // Get rock grinder recipes
        List<ProcessRecipe> recipes = level.getRecipeManager()
            .getAllRecipesFor(ModRecipeTypes.ROCK_GRINDER.get())
            .stream()
            .map(recipeHolder -> recipeHolder.value())
            .toList();

        // Check each recipe
        for (ProcessRecipe recipe : recipes) {
            ProcessRecipeInput input = new ProcessRecipeInput(List.of(new ItemStack(state.getBlock())));
            if (recipe.matches(input, level)) {
                // Cancel normal block breaking
                event.setCanceled(true);

                // Damage tool
                tool.hurtAndBreak(1, level, player, (stack) -> {});

                // Get base chance from recipe parameter
                float baseChance = recipe.getParameter();

                // Apply Fortune bonus
                var enchantmentLookup = level.holderLookup(net.minecraft.core.registries.Registries.ENCHANTMENT);
                Holder.Reference<net.minecraft.world.item.enchantment.Enchantment> fortuneHolder =
                    enchantmentLookup.getOrThrow(Enchantments.FORTUNE);
                int fortuneLevel = tool.getEnchantmentLevel(fortuneHolder);
                float chance = baseChance * ((fortuneLevel + 3F) / 3F);

                // Spawn output with chance
                while (chance >= 1) {
                    popItem(level, recipe.getOutputs().get(0).copy(), pos);
                    chance -= 1;
                }
                if (level.getRandom().nextFloat() <= chance) {
                    popItem(level, recipe.getOutputs().get(0).copy(), pos);
                }

                // Destroy block without drops
                level.destroyBlock(pos, false);
                return;
            }
        }
    }

    private static void popItem(ServerLevel level, ItemStack stack, BlockPos pos) {
        net.minecraft.world.entity.item.ItemEntity itemEntity = new net.minecraft.world.entity.item.ItemEntity(
            level,
            pos.getX() + 0.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5,
            stack
        );
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }
}
