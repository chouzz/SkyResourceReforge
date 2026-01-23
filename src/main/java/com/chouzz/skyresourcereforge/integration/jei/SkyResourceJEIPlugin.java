package com.chouzz.skyresourcereforge.integration.jei;

import java.util.ArrayList;
import java.util.List;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.alchemy.item.DirtyGemItem;
import com.chouzz.skyresourcereforge.alchemy.item.ItemOreAlchDust;
import com.chouzz.skyresourcereforge.registration.ModItems;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.ModList;

/**
 * JEI Plugin for SkyResourceReforge
 *
 * Registers multi-subtype items (dirty gems and ore alchemical dust) with JEI
 * so all variants show up correctly in the JEI interface.
 */
@JeiPlugin
public class SkyResourceJEIPlugin implements IModPlugin {

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        SkyResourceReforge.LOGGER.info("Registering SkyResource Reforge JEI subtypes...");

        // Register dirty gem subtypes - uses damage value for differentiation
        registration.registerSubtypeInterpreter(
            ModItems.DIRTY_GEM.get(),
            (stack, context) -> {
                int damage = stack.getDamageValue();
                if (damage >= 0 && damage < DirtyGemItem.gemInfos.size()) {
                    return DirtyGemItem.gemInfos.get(damage).name;
                }
                return "unknown";
            }
        );

        // Register ore alchemical dust subtypes
        registration.registerSubtypeInterpreter(
            ModItems.ORE_ALCH_DUST.get(),
            (stack, context) -> {
                int damage = stack.getDamageValue();
                if (damage >= 0 && damage < ItemOreAlchDust.oreInfos.size()) {
                    return ItemOreAlchDust.oreInfos.get(damage).name;
                }
                return "unknown";
            }
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        SkyResourceReforge.LOGGER.info("Registering SkyResource Reforge JEI recipes...");

        // Add all dirty gem variants to JEI item list
        List<ItemStack> dirtyGems = new ArrayList<>();
        for (int i = 0; i < DirtyGemItem.gemInfos.size(); i++) {
            ItemStack stack = new ItemStack(ModItems.DIRTY_GEM.get());
            stack.setDamageValue(i);
            dirtyGems.add(stack);
        }

        // Add all ore alchemical dust variants to JEI item list
        List<ItemStack> oreDusts = new ArrayList<>();
        for (int i = 0; i < ItemOreAlchDust.oreInfos.size(); i++) {
            ItemStack stack = new ItemStack(ModItems.ORE_ALCH_DUST.get());
            stack.setDamageValue(i);
            oreDusts.add(stack);
        }

        SkyResourceReforge.LOGGER.info("Adding {} ore dust variants to JEI with oreInfos size: {}", 
            oreDusts.size(), ItemOreAlchDust.oreInfos.size());

        // Register items with JEI
        List<ItemStack> allVariants = new ArrayList<>();
        allVariants.addAll(dirtyGems);
        allVariants.addAll(oreDusts);

        registration.addIngredientInfo(allVariants, VanillaTypes.ITEM_STACK,
            Component.literal("Multi-subtype items from SkyResource Reforge")
        );

        SkyResourceReforge.LOGGER.info("Registered {} gem variants and {} ore dust variants with JEI",
            dirtyGems.size(), oreDusts.size());
    }
}
