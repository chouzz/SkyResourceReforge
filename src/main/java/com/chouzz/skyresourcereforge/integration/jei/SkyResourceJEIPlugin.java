package com.chouzz.skyresourcereforge.integration.jei;

import java.util.ArrayList;
import java.util.List;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.alchemy.item.DirtyGemItem;
import com.chouzz.skyresourcereforge.alchemy.item.ItemOreAlchDust;
import com.chouzz.skyresourcereforge.heat.HeatVariants;
import com.chouzz.skyresourcereforge.integration.jei.categories.CauldronCleanRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.CombustionRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.CondenserRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.CrucibleRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.FreezerRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.FusionRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.HandheldRockGrinderRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.InfusionRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.KnifeRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.RockGrinderRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.WaterExtractorExtractRecipeCategory;
import com.chouzz.skyresourcereforge.integration.jei.categories.WaterExtractorInsertRecipeCategory;
import com.chouzz.skyresourcereforge.item.HeatComponentItem;
import com.chouzz.skyresourcereforge.item.HeatProviderItem;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

/**
 * JEI Plugin for SkyResourceReforge
 *
 * Registers multi-subtype items (dirty gems and ore alchemical dust) with JEI
 * so all variants show up correctly in the JEI interface.
 * Also registers recipe categories and recipes for all custom machines.
 */
@JeiPlugin
public class SkyResourceJEIPlugin implements IModPlugin {

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "plugin");

    // JEI Recipe Types
    public static final RecipeType<ProcessRecipe> COMBUSTION_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "combustion", ProcessRecipe.class);
    public static final RecipeType<ProcessRecipe> WATER_EXTRACTOR_EXTRACT_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "water_extractor_extract", ProcessRecipe.class);
    public static final RecipeType<ProcessRecipe> WATER_EXTRACTOR_INSERT_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "water_extractor_insert", ProcessRecipe.class);
    public static final RecipeType<ProcessRecipe> ROCK_GRINDER_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "rock_grinder", ProcessRecipe.class);
    public static final RecipeType<ProcessRecipe> CAULDRON_CLEAN_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "cauldron_clean", ProcessRecipe.class);
    public static final RecipeType<ProcessRecipe> FREEZER_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "freezer", ProcessRecipe.class);
    public static final RecipeType<ProcessRecipe> FUSION_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "fusion", ProcessRecipe.class);
    public static final RecipeType<ProcessRecipe> INFUSION_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "infusion", ProcessRecipe.class);
    public static final RecipeType<ProcessRecipe> CONDENSER_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "condenser", ProcessRecipe.class);
    public static final RecipeType<ProcessRecipe> CRUCIBLE_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "crucible", ProcessRecipe.class);

    // Tool-based recipe types
    public static final RecipeType<ProcessRecipe> KNIFE_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "knife", ProcessRecipe.class);
    public static final RecipeType<ProcessRecipe> HANDHELD_ROCK_GRINDER_TYPE =
        RecipeType.create(SkyResourceReforge.MODID, "handheld_rock_grinder", ProcessRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        SkyResourceReforge.LOGGER.info("Registering SkyResource Reforge JEI recipe categories...");

        registration.addRecipeCategories(
            new CombustionRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new WaterExtractorExtractRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new WaterExtractorInsertRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new RockGrinderRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new CauldronCleanRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new FreezerRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new FusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new InfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new CondenserRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new CrucibleRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new KnifeRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
            new HandheldRockGrinderRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );

        SkyResourceReforge.LOGGER.info("Registered JEI recipe categories for SkyResource Reforge");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        SkyResourceReforge.LOGGER.info("Registering SkyResource Reforge JEI recipes...");

        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        // Register recipes for each type
        registration.addRecipes(COMBUSTION_TYPE, getRecipes(recipeManager, ModRecipeTypes.COMBUSTION));
        registration.addRecipes(WATER_EXTRACTOR_EXTRACT_TYPE, getRecipes(recipeManager, ModRecipeTypes.WATER_EXTRACTOR_EXTRACT));
        registration.addRecipes(WATER_EXTRACTOR_INSERT_TYPE, getRecipes(recipeManager, ModRecipeTypes.WATER_EXTRACTOR_INSERT));
        registration.addRecipes(ROCK_GRINDER_TYPE, getRecipes(recipeManager, ModRecipeTypes.ROCK_GRINDER));
        registration.addRecipes(CAULDRON_CLEAN_TYPE, getRecipes(recipeManager, ModRecipeTypes.CAULDRON_CLEAN));
        registration.addRecipes(FREEZER_TYPE, getRecipes(recipeManager, ModRecipeTypes.FREEZER));
        registration.addRecipes(FUSION_TYPE, getRecipes(recipeManager, ModRecipeTypes.FUSION));
        registration.addRecipes(INFUSION_TYPE, getRecipes(recipeManager, ModRecipeTypes.INFUSION));
        registration.addRecipes(CONDENSER_TYPE, getRecipes(recipeManager, ModRecipeTypes.CONDENSER));
        registration.addRecipes(CRUCIBLE_TYPE, getRecipes(recipeManager, ModRecipeTypes.CRUCIBLE));

        // Register tool-based recipes
        registration.addRecipes(KNIFE_TYPE, getRecipes(recipeManager, ModRecipeTypes.KNIFE));
        registration.addRecipes(HANDHELD_ROCK_GRINDER_TYPE, getRecipes(recipeManager, ModRecipeTypes.ROCK_GRINDER));

        // Add JEI descriptions for key items
        List<ItemStack> cactusKnives = List.of(new ItemStack(ModItems.CACTUS_KNIFE.get()));
        registration.addIngredientInfo(cactusKnives, VanillaTypes.ITEM_STACK,
            Component.translatable("jei.skyresourcereforge.description.cactus_knife"));

        List<ItemStack> blazePowderBlocks = List.of(new ItemStack(ModBlocks.BLAZE_POWDER_BLOCK.get()));
        registration.addIngredientInfo(blazePowderBlocks, VanillaTypes.ITEM_STACK,
            Component.translatable("jei.skyresourcereforge.description.blaze_powder_block"));

        SkyResourceReforge.LOGGER.info("Registered JEI recipes for SkyResource Reforge");
    }

    private List<ProcessRecipe> getRecipes(RecipeManager recipeManager, net.neoforged.neoforge.registries.DeferredHolder<net.minecraft.world.item.crafting.RecipeType<?>, net.minecraft.world.item.crafting.RecipeType<ProcessRecipe>> recipeTypeHolder) {
        return recipeManager.getAllRecipesFor(recipeTypeHolder.get())
            .stream()
            .map(RecipeHolder::value)
            .toList();
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        SkyResourceReforge.LOGGER.info("Registering SkyResource Reforge JEI recipe catalysts...");

        // Combustion - Use COMBUSTION_CONTROLLER as the catalyst
        registration.addRecipeCatalyst(
            new ItemStack(ModBlocks.COMBUSTION_CONTROLLER.get()),
            COMBUSTION_TYPE
        );

        // Water Extractor blocks
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AQUEOUS_CONCENTRATOR.get()), WATER_EXTRACTOR_EXTRACT_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AQUEOUS_DECONCENTRATOR.get()), WATER_EXTRACTOR_EXTRACT_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AQUEOUS_CONCENTRATOR.get()), WATER_EXTRACTOR_INSERT_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AQUEOUS_DECONCENTRATOR.get()), WATER_EXTRACTOR_INSERT_TYPE);

        // Rock Grinder
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ROCK_CRUSHER.get()), ROCK_GRINDER_TYPE);

        // Cauldron Clean
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ROCK_CLEANER.get()), CAULDRON_CLEAN_TYPE);

        // Freezer
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MINI_FREEZER.get()), FREEZER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.IRON_FREEZER.get()), FREEZER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.LIGHT_FREEZER.get()), FREEZER_TYPE);

        // Fusion Table
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FUSION_TABLE.get()), FUSION_TYPE);

        // Infusion stones
        registration.addRecipeCatalyst(new ItemStack(ModItems.INFUSION_STONE_SANDSTONE.get()), INFUSION_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.INFUSION_STONE_RED_SANDSTONE.get()), INFUSION_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.INFUSION_STONE_ALCHEMICAL.get()), INFUSION_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.LIFE_INFUSER.get()), INFUSION_TYPE);

        // Crucible
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRUCIBLE.get()), CRUCIBLE_TYPE);

        // Knife tools
        registration.addRecipeCatalyst(new ItemStack(ModItems.CACTUS_KNIFE.get()), KNIFE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.STONE_KNIFE.get()), KNIFE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.IRON_KNIFE.get()), KNIFE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.DIAMOND_KNIFE.get()), KNIFE_TYPE);

        // Rock Grinder tools
        registration.addRecipeCatalyst(new ItemStack(ModItems.STONE_GRINDER.get()), HANDHELD_ROCK_GRINDER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.IRON_GRINDER.get()), HANDHELD_ROCK_GRINDER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.DIAMOND_GRINDER.get()), HANDHELD_ROCK_GRINDER_TYPE);
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
