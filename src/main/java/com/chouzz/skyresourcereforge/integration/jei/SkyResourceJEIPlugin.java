package com.chouzz.skyresourcereforge.integration.jei;

import java.util.ArrayList;
import java.util.List;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.alchemy.item.DirtyGemItem;
import com.chouzz.skyresourcereforge.alchemy.item.ItemOreAlchDust;
import com.chouzz.skyresourcereforge.heat.HeatVariants;
import com.chouzz.skyresourcereforge.item.HeatComponentItem;
import com.chouzz.skyresourcereforge.item.HeatProviderItem;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
                int index = DirtyGemItem.getGemIndex(stack);
                if (index >= 0 && index < DirtyGemItem.gemInfos.size()) {
                    return DirtyGemItem.gemInfos.get(index).name;
                }
                return "unknown";
            }
        );

        // Register ore alchemical dust subtypes
        registration.registerSubtypeInterpreter(
            ModItems.ORE_ALCH_DUST.get(),
            (stack, context) -> {
                int index = ItemOreAlchDust.getDustIndex(stack);
                if (index >= 0 && index < ItemOreAlchDust.oreInfos.size()) {
                    return ItemOreAlchDust.oreInfos.get(index).name;
                }
                return "unknown";
            }
        );

        registration.registerSubtypeInterpreter(
            ModItems.HEAT_COMPONENT.get(),
            (stack, context) -> HeatVariants.getName(HeatComponentItem.getVariantIndex(stack))
        );

        registration.registerSubtypeInterpreter(
            ModItems.HEAT_PROVIDER.get(),
            (stack, context) -> HeatVariants.getName(HeatProviderItem.getVariantIndex(stack))
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        SkyResourceReforge.LOGGER.info("Registering SkyResource Reforge JEI recipes...");

        // Add JEI descriptions for key items
        // Cactus Knife
        List<ItemStack> cactusKnives = List.of(new ItemStack(ModItems.CACTUS_KNIFE.get()));
        registration.addIngredientInfo(cactusKnives, VanillaTypes.ITEM_STACK,
            Component.translatable("jei.skyresourcereforge.description.cactus_knife"));

        // Blaze Powder Block
        List<ItemStack> blazePowderBlocks = List.of(new ItemStack(ModBlocks.BLAZE_POWDER_BLOCK.get()));
        registration.addIngredientInfo(blazePowderBlocks, VanillaTypes.ITEM_STACK,
            Component.translatable("jei.skyresourcereforge.description.blaze_powder_block"));

        SkyResourceReforge.LOGGER.info("Registered JEI recipes for SkyResource Reforge");
    }

    @Override
    public void registerExtraIngredients(mezz.jei.api.registration.IExtraIngredientRegistration registration) {
        SkyResourceReforge.LOGGER.info("Registering extra ingredients for SkyResource Reforge...");

        // Add all ore alchemical dust variants
        java.util.List<ItemStack> oreDusts = new ArrayList<>();
        for (int i = 0; i < ItemOreAlchDust.oreInfos.size(); i++) {
            ItemStack stack = new ItemStack(ModItems.ORE_ALCH_DUST.get());
            ItemOreAlchDust.setDustIndex(stack, i);
            oreDusts.add(stack);
        }

        // Add all dirty gem variants
        java.util.List<ItemStack> dirtyGems = new ArrayList<>();
        for (int i = 0; i < DirtyGemItem.gemInfos.size(); i++) {
            ItemStack stack = new ItemStack(ModItems.DIRTY_GEM.get());
            DirtyGemItem.setGemIndex(stack, i);
            dirtyGems.add(stack);
        }

        // Add all heat component variants
        java.util.List<ItemStack> heatComponents = new ArrayList<>();
        for (int i = 0; i < HeatVariants.size(); i++) {
            heatComponents.add(HeatComponentItem.createStack(i, ModItems.HEAT_COMPONENT.get()));
        }

        // Add all heat provider variants
        java.util.List<ItemStack> heatProviders = new ArrayList<>();
        for (int i = 0; i < HeatVariants.size(); i++) {
            heatProviders.add(HeatProviderItem.createStack(i, ModItems.HEAT_PROVIDER.get()));
        }

        // Register with JEI
        registration.addExtraItemStacks(oreDusts);
        registration.addExtraItemStacks(dirtyGems);
        registration.addExtraItemStacks(heatComponents);
        registration.addExtraItemStacks(heatProviders);

        SkyResourceReforge.LOGGER.info("Registered {} ore dust and {} dirty gem variants as extra ingredients",
            oreDusts.size(), dirtyGems.size());
    }
}
