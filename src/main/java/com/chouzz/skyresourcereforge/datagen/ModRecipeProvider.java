package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.heat.HeatVariants;
import com.chouzz.skyresourcereforge.alchemy.item.AlchemyComponentItem;
import com.chouzz.skyresourcereforge.alchemy.item.DirtyGemItem;
import com.chouzz.skyresourcereforge.alchemy.item.GemRegisterInfo;
import com.chouzz.skyresourcereforge.alchemy.item.ItemOreAlchDust;
import com.chouzz.skyresourcereforge.alchemy.item.OreRegisterInfo;
import com.chouzz.skyresourcereforge.item.BaseComponentItem;
import com.chouzz.skyresourcereforge.item.AlchemyMachineComponentItem;
import com.chouzz.skyresourcereforge.item.HeatComponentItem;
import com.chouzz.skyresourcereforge.item.HeatProviderItem;
import com.chouzz.skyresourcereforge.item.TechComponentItem;
import com.chouzz.skyresourcereforge.recipe.CountedIngredient;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import com.chouzz.skyresourcereforge.registration.ModTags;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // ProcessRecipe: combustion
        output.accept(
                ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "combustion/compressed_coal"),
                new ProcessRecipe(
                        ModRecipeTypes.COMBUSTION.getId(),
                        List.of(CountedIngredient.of(Ingredient.of(Items.COAL), 1)),
                        List.of(new ItemStack(ModBlocks.COMPRESSED_COAL_BLOCK.get())),
                        List.of(),
                        List.of(),
                        100.0f
                ),
                null
        );

        output.accept(
                ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "combustion/dark_matter"),
                new ProcessRecipe(
                        ModRecipeTypes.COMBUSTION.getId(),
                        List.of(
                                CountedIngredient.of(Ingredient.of(Items.SOUL_SAND), 5),
                                CountedIngredient.of(Ingredient.of(ModBlocks.COMPRESSED_COAL_BLOCK.get()), 3),
                                CountedIngredient.of(Ingredient.of(Items.NETHERITE_INGOT), 7)
                        ),
                        List.of(new ItemStack(ModItems.DARK_MATTER.get())),
                        List.of(),
                        List.of(),
                        2900.0f
                ),
                null
        );

        output.accept(
                ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "combustion/light_matter"),
                new ProcessRecipe(
                        ModRecipeTypes.COMBUSTION.getId(),
                        List.of(
                                CountedIngredient.of(Ingredient.of(ModBlocks.HEAVY_SNOW.get()), 5),
                                CountedIngredient.of(techComponentIngredient(TechComponentItem.FROZEN_IRON_INGOT), 4),
                                CountedIngredient.of(Ingredient.of(alchemyComponent(AlchemyComponentItem.ALCH_IRON_INGOT)), 4),
                                CountedIngredient.of(Ingredient.of(Blocks.END_STONE), 3)
                        ),
                        List.of(new ItemStack(ModItems.LIGHT_MATTER.get())),
                        List.of(),
                        List.of(),
                        3400.0f
                ),
                null
        );

        // Shaped: fleshy_snow_nugget (2 snowball + 1 rotten_flesh -> 3)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLESHY_SNOW_NUGGET.get(), 3)
                .pattern("XX")
                .pattern("XY")
                .define('X', Items.SNOWBALL)
                .define('Y', Items.ROTTEN_FLESH)
                .unlockedBy("has_snowball", has(Items.SNOWBALL))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fleshy_snow_nugget"));

        // Shaped: knives (tool crafting)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CACTUS_KNIFE.get())
                .pattern(" #")
                .pattern("# ")
                .define('#', Ingredient.of(alchemyComponent(AlchemyComponentItem.CACTUS_NEEDLE)))
                .unlockedBy("has_alchemy_component", has(ModItems.ALCHEMY_COMPONENT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "cactus_knife"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STONE_KNIFE.get())
                .pattern("#  ")
                .pattern("#X ")
                .pattern(" #X")
                .define('#', Blocks.COBBLESTONE)
                .define('X', Items.STICK)
                .unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "stone_knife"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.IRON_KNIFE.get())
                .pattern("#  ")
                .pattern("#X ")
                .pattern(" #X")
                .define('#', Items.IRON_INGOT)
                .define('X', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "iron_knife"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.DIAMOND_KNIFE.get())
                .pattern("#  ")
                .pattern("#X ")
                .pattern(" #X")
                .define('#', Items.DIAMOND)
                .define('X', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "diamond_knife"));

        // Shaped: rock grinders (tool crafting)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STONE_GRINDER.get())
                .pattern("#  ")
                .pattern(" # ")
                .pattern("  X")
                .define('#', Blocks.COBBLESTONE)
                .define('X', Items.STICK)
                .unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "stone_grinder"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.IRON_GRINDER.get())
                .pattern("#  ")
                .pattern(" # ")
                .pattern("  X")
                .define('#', Items.IRON_INGOT)
                .define('X', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "iron_grinder"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.DIAMOND_GRINDER.get())
                .pattern("#  ")
                .pattern(" # ")
                .pattern("  X")
                .define('#', Items.DIAMOND)
                .define('X', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "diamond_grinder"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CACTUS_FRUIT_NEEDLE.get())
                .pattern("X")
                .pattern("Y")
                .define('X', ModItems.CACTUS_FRUIT.get())
                .define('Y', Ingredient.of(alchemyComponent(AlchemyComponentItem.CACTUS_NEEDLE)))
                .unlockedBy("has_cactus_fruit", has(ModItems.CACTUS_FRUIT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "cactus_fruit_needle"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INFUSION_STONE_SANDSTONE.get())
                .pattern("X")
                .pattern("Y")
                .define('X', Ingredient.of(alchemyComponent(AlchemyComponentItem.CACTUS_NEEDLE)))
                .define('Y', Blocks.SANDSTONE)
                .unlockedBy("has_sandstone", has(Blocks.SANDSTONE))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "infusion_stone_sandstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INFUSION_STONE_RED_SANDSTONE.get())
                .pattern("X")
                .pattern("Y")
                .define('X', Ingredient.of(alchemyComponent(AlchemyComponentItem.CACTUS_NEEDLE)))
                .define('Y', Blocks.RED_SANDSTONE)
                .unlockedBy("has_red_sandstone", has(Blocks.RED_SANDSTONE))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "infusion_stone_red_sandstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INFUSION_STONE_ALCHEMICAL.get())
                .pattern("X")
                .pattern("Y")
                .define('X', Ingredient.of(alchemyComponent(AlchemyComponentItem.ALCH_GOLD_NEEDLE)))
                .define('Y', Ingredient.of(alchemyComponent(AlchemyComponentItem.ALCH_DIAMOND)))
                .unlockedBy("has_alchemy_component", has(ModItems.ALCHEMY_COMPONENT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "infusion_stone_alchemical"));

        addPlantMatterRecipes(output);

        ItemStack enrichedBonemeal = baseComponent(BaseComponentItem.ENRICHED_BONEMEAL);
        enrichedBonemeal.setCount(4);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, enrichedBonemeal)
                .requires(Items.ROTTEN_FLESH)
                .requires(Items.BONE_MEAL)
                .requires(Items.BONE_MEAL)
                .requires(Items.BONE_MEAL)
                .unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "base_component/enriched_bonemeal"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, baseComponent(BaseComponentItem.STEEL_POWER_COMPONENT))
                .pattern("XZX")
                .pattern("XYX")
                .pattern("XZX")
                .define('X', Ingredient.of(ModTags.STEEL_POWER_COMPONENT_MATERIALS))
                .define('Y', Ingredient.of(ModTags.BASIC_CIRCUIT))
                .define('Z', Ingredient.of(ModTags.COAL_DUST))
                .unlockedBy("has_steel_power_material", has(ModTags.STEEL_POWER_COMPONENT_MATERIALS))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "base_component/steel_power_component"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, baseComponent(BaseComponentItem.FROZEN_IRON_COOLING_COMPONENT))
                .pattern("XZX")
                .pattern("XYX")
                .pattern("XYX")
                .define('X', techComponentIngredient(TechComponentItem.FROZEN_IRON_INGOT))
                .define('Y', Items.GLOWSTONE_DUST)
                .define('Z', Items.LAPIS_LAZULI)
                .unlockedBy("has_frozen_iron_ingot", has(ModItems.TECH_COMPONENT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "base_component/frozen_iron_cooling_component"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, baseComponent(BaseComponentItem.QUARTZ_AMP))
                .pattern(" X ")
                .pattern("XYX")
                .pattern(" X ")
                .define('X', Items.QUARTZ)
                .define('Y', Items.REDSTONE)
                .unlockedBy("has_quartz", has(Items.QUARTZ))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "base_component/quartz_amp"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NETHER_BRICK_CONDENSER.get())
                .pattern("XYX")
                .pattern("XZX")
                .pattern("X X")
                .define('X', Items.NETHER_BRICK)
                .define('Y', alchemyComponentIngredient(AlchemyComponentItem.ALCH_COAL))
                .define('Z', baseComponentIngredient(BaseComponentItem.FROZEN_IRON_COOLING_COMPONENT))
                .unlockedBy("has_alchemy_component", has(ModItems.ALCHEMY_COMPONENT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "nether_brick_condenser"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NETHER_BRICK_COMBUSTION_HEATER.get())
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XZX")
                .define('X', Items.NETHER_BRICK)
                .define('Y', heatComponentIngredient(HeatVariants.NETHER_BRICK))
                .define('Z', baseComponentIngredient(BaseComponentItem.FROZEN_IRON_COOLING_COMPONENT))
                .unlockedBy("has_heat_component", has(ModItems.HEAT_COMPONENT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "nether_brick_combustion_heater"));

        // Shaped: petrified_planks (1 petrified_wood -> 4)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PETRIFIED_PLANKS.get(), 4)
                .pattern("X")
                .define('X', ModBlocks.PETRIFIED_WOOD.get())
                .unlockedBy("has_petrified_wood", has(ModBlocks.PETRIFIED_WOOD.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "petrified_planks"));

        // Shaped: silverfish_disruptor
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SILVERFISH_DISRUPTOR.get())
                .pattern(" Y ")
                .pattern(" Z ")
                .pattern("XXX")
                .define('X', ModItems.DARK_MATTER.get())
                .define('Y', Items.ENDER_EYE)
                .define('Z', ModItems.LIGHT_MATTER.get())
                .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "silverfish_disruptor"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CASING.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("XXX")
                .define('X', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "casing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLAZE_POWDER_BLOCK.get())
                .pattern("XX")
                .pattern("XX")
                .define('X', Items.BLAZE_POWDER)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "blaze_powder_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.COAL_INFUSED_BLOCK.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', Ingredient.of(alchemyComponent(AlchemyComponentItem.ALCH_COAL)))
                .unlockedBy("has_alchemy_component", has(ModItems.ALCHEMY_COMPONENT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "coal_infused_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_SNOW.get())
                .pattern("XX")
                .pattern("XX")
                .define('X', ModItems.HEAVY_SNOWBALL.get())
                .unlockedBy("has_heavy_snowball", has(ModItems.HEAVY_SNOWBALL.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "heavy_snow"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MINI_FREEZER.get())
                .pattern("X")
                .pattern("X")
                .define('X', Blocks.SNOW)
                .unlockedBy("has_snow", has(Blocks.SNOW))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "mini_freezer"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.IRON_FREEZER.get())
                .pattern("XXX")
                .pattern("XZX")
                .pattern("XXX")
                .define('X', techComponentIngredient(TechComponentItem.FROZEN_IRON_INGOT))
                .define('Z', ModBlocks.MINI_FREEZER.get())
                .unlockedBy("has_mini_freezer", has(ModBlocks.MINI_FREEZER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "iron_freezer"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.LIGHT_FREEZER.get())
                .pattern("XXX")
                .pattern("XZX")
                .pattern("XXX")
                .define('X', ModItems.LIGHT_MATTER.get())
                .define('Z', ModBlocks.IRON_FREEZER.get())
                .unlockedBy("has_iron_freezer", has(ModBlocks.IRON_FREEZER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "light_freezer"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.AQUEOUS_CONCENTRATOR.get())
                .pattern("XAX")
                .pattern("XZX")
                .pattern("XYX")
                .define('X', Ingredient.of(ModTags.STEEL_POWER_COMPONENT_MATERIALS))
                .define('Y', baseComponentIngredient(BaseComponentItem.STEEL_POWER_COMPONENT))
                .define('Z', ModItems.WATER_EXTRACTOR.get())
                .define('A', Blocks.SNOW)
                .unlockedBy("has_water_extractor", has(ModItems.WATER_EXTRACTOR.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "aqueous_concentrator"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.AQUEOUS_DECONCENTRATOR.get())
                .pattern("XAX")
                .pattern("XZX")
                .pattern("XYX")
                .define('X', Ingredient.of(ModTags.STEEL_POWER_COMPONENT_MATERIALS))
                .define('Y', baseComponentIngredient(BaseComponentItem.STEEL_POWER_COMPONENT))
                .define('Z', ModItems.WATER_EXTRACTOR.get())
                .define('A', Blocks.SAND)
                .unlockedBy("has_water_extractor", has(ModItems.WATER_EXTRACTOR.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "aqueous_deconcentrator"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COMBUSTION_CONTROLLER.get())
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XYX")
                .define('X', Ingredient.of(ModTags.STEEL_POWER_COMPONENT_MATERIALS))
                .define('Y', Ingredient.of(ModTags.BASIC_CIRCUIT))
                .unlockedBy("has_basic_circuit", has(ModTags.BASIC_CIRCUIT))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "combustion_controller"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WILDLIFE_ATTRACTOR.get())
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XZX")
                .define('X', Blocks.HAY_BLOCK)
                .define('Y', Blocks.CHEST)
                .define('Z', Items.REDSTONE)
                .unlockedBy("has_hay_block", has(Blocks.HAY_BLOCK))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "wildlife_attractor"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.END_PORTAL_CORE.get())
                .pattern("BYB")
                .pattern("AZA")
                .pattern("XXX")
                .define('X', ModBlocks.DARK_MATTER_BLOCK.get())
                .define('Y', Items.ENDER_EYE)
                .define('Z', baseComponentIngredient(BaseComponentItem.QUARTZ_AMP))
                .define('A', Ingredient.of(alchemyComponent(AlchemyComponentItem.ALCH_IRON_INGOT)))
                .define('B', Blocks.QUARTZ_BLOCK)
                .unlockedBy("has_dark_matter_block", has(ModBlocks.DARK_MATTER_BLOCK.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "end_portal_core"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SANDY_NETHERRACK.get(), 4)
                .pattern("XY")
                .pattern("ZX")
                .define('X', Blocks.SAND)
                .define('Y', Items.NETHER_WART)
                .define('Z', Blocks.NETHERRACK)
                .unlockedBy("has_netherrack", has(Blocks.NETHERRACK))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "sandy_netherrack"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.LIFE_INJECTOR.get())
                .pattern(" Y ")
                .pattern(" X ")
                .pattern("XXX")
                .define('X', ItemTags.LOGS)
                .define('Y', Items.DIAMOND_SWORD)
                .unlockedBy("has_diamond_sword", has(Items.DIAMOND_SWORD))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "life_injector"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CRUCIBLE_INSERTER.get())
                .pattern("XYX")
                .pattern("X X")
                .pattern("X X")
                .define('X', Items.IRON_INGOT)
                .define('Y', Blocks.DROPPER)
                .unlockedBy("has_dropper", has(Blocks.DROPPER))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "crucible_inserter"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WATER_EXTRACTOR.get())
                .pattern("XXX")
                .pattern(" XX")
                .define('X', ItemTags.PLANKS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "water_extractor"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CRUCIBLE.get())
                .pattern("X X")
                .pattern("X X")
                .pattern("XXX")
                .define('X', Items.BRICK)
                .unlockedBy("has_brick", has(Items.BRICK))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "crucible"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FLUID_DROPPER.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .define('X', Blocks.COBBLESTONE)
                .unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fluid_dropper"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COMBUSTION_COLLECTOR.get())
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XXX")
                .define('X', Items.IRON_INGOT)
                .define('Y', Blocks.HOPPER)
                .unlockedBy("has_hopper", has(Blocks.HOPPER))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "combustion_collector"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.QUICK_DROPPER.get())
                .pattern("XXX")
                .pattern("XZX")
                .pattern("XYX")
                .define('X', Items.IRON_INGOT)
                .define('Y', Blocks.DROPPER)
                .define('Z', Blocks.GLOWSTONE)
                .unlockedBy("has_dropper", has(Blocks.DROPPER))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "quick_dropper"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DARK_MATTER_WARPER.get())
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XXX")
                .define('X', Blocks.OBSIDIAN)
                .define('Y', ModItems.DARK_MATTER.get())
                .unlockedBy("has_dark_matter", has(ModItems.DARK_MATTER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "dark_matter_warper"));

        ItemStack stoneHeatComponent = HeatComponentItem.createStack(HeatVariants.STONE, ModItems.HEAT_COMPONENT.get());
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DIRT_FURNACE.get())
                .pattern("X")
                .pattern("Y")
                .define('X', Items.DIRT)
                .define('Y', Ingredient.of(stoneHeatComponent))
                .unlockedBy("has_heat_component", has(ModItems.HEAT_COMPONENT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "dirt_furnace"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SURVIVALIST_FISHING_ROD.get())
                .pattern(" X")
                .pattern("XY")
                .define('X', Items.STICK)
                .define('Y', Items.STRING)
                .unlockedBy("has_string", has(Items.STRING))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "survivalist_fishing_rod"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.HEAVY_EXPLOSIVE_SNOWBALL.get(), 3)
                .requires(ModItems.HEAVY_SNOWBALL.get())
                .requires(ModItems.HEAVY_SNOWBALL.get())
                .requires(ModItems.HEAVY_SNOWBALL.get())
                .requires(Items.GUNPOWDER)
                .unlockedBy("has_heavy_snowball", has(ModItems.HEAVY_SNOWBALL.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "heavy_explosive_snowball"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ROCK_CRUSHER.get())
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XZX")
                .define('X', Ingredient.of(ModTags.STEEL_POWER_COMPONENT_MATERIALS))
                .define('Y', ModItems.DIAMOND_GRINDER.get())
                .define('Z', baseComponentIngredient(BaseComponentItem.STEEL_POWER_COMPONENT))
                .unlockedBy("has_diamond_grinder", has(ModItems.DIAMOND_GRINDER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "rock_crusher"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ROCK_CLEANER.get())
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XZX")
                .define('X', Ingredient.of(ModTags.STEEL_POWER_COMPONENT_MATERIALS))
                .define('Y', Items.CAULDRON)
                .define('Z', baseComponentIngredient(BaseComponentItem.STEEL_POWER_COMPONENT))
                .unlockedBy("has_cauldron", has(Items.CAULDRON))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "rock_cleaner"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.LIFE_INFUSER.get())
                .pattern("XXX")
                .pattern(" X ")
                .pattern(" Y ")
                .define('X', ItemTags.LOGS)
                .define('Y', ModItems.INFUSION_STONE_ALCHEMICAL.get())
                .unlockedBy("has_infusion_stone_alchemical", has(ModItems.INFUSION_STONE_ALCHEMICAL.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "life_infuser"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FUSION_TABLE.get())
                .pattern("XZX")
                .pattern("XYX")
                .pattern("X X")
                .define('X', ItemTags.PLANKS)
                .define('Y', Ingredient.of(alchemyComponent(AlchemyComponentItem.CRYSTAL_SHARD)))
                .define('Z', Ingredient.of(alchemyComponent(AlchemyComponentItem.ALCH_DUST_1)))
                .unlockedBy("has_alchemy_component", has(ModItems.ALCHEMY_COMPONENT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion_table"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DARK_MATTER_BLOCK.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.DARK_MATTER.get())
                .unlockedBy("has_dark_matter", has(ModItems.DARK_MATTER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "dark_matter_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_MATTER_BLOCK.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', ModItems.LIGHT_MATTER.get())
                .unlockedBy("has_light_matter", has(ModItems.LIGHT_MATTER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "light_matter_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DARK_MATTER.get(), 9)
                .requires(ModBlocks.DARK_MATTER_BLOCK.get())
                .unlockedBy("has_dark_matter_block", has(ModBlocks.DARK_MATTER_BLOCK.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "dark_matter_block_to_items"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LIGHT_MATTER.get(), 9)
                .requires(ModBlocks.LIGHT_MATTER_BLOCK.get())
                .unlockedBy("has_light_matter_block", has(ModBlocks.LIGHT_MATTER_BLOCK.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "light_matter_block_to_items"));

        // === KNIFE RECIPES ===
        // Knife recipes have parameter 0 (not used), fortune affects output count

        // Cactus -> Cactus Needle (4 output)
        ItemStack needles = alchemyComponent(AlchemyComponentItem.CACTUS_NEEDLE);
        needles.setCount(4);
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "knife/cactus_to_needle"),
            new ProcessRecipe(
                ModRecipeTypes.KNIFE.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.CACTUS), 1)),
                List.of(needles),
                List.of(),
                List.of(),
                0.0f
            ),
            null
        );

        // Cactus -> Cactus Fruit (2 output)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "knife/cactus_to_fruit"),
            new ProcessRecipe(
                ModRecipeTypes.KNIFE.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.CACTUS), 1)),
                List.of(new ItemStack(ModItems.CACTUS_FRUIT.get(), 2)),
                List.of(),
                List.of(),
                0.0f
            ),
            null
        );

        // Melon Block -> Melon (9 output)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "knife/melon_block_to_melon"),
            new ProcessRecipe(
                ModRecipeTypes.KNIFE.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.MELON), 1)),
                List.of(new ItemStack(Items.MELON_SLICE, 9)),
                List.of(),
                List.of(),
                0.0f
            ),
            null
        );

        // Logs to Planks (6 output each) - Oak, Spruce, Birch, Jungle
        String[] logTypes = {"oak", "spruce", "birch", "jungle"};
        for (int i = 0; i < logTypes.length; i++) {
            final int index = i;
            net.minecraft.world.level.block.Block logBlock = switch (i) {
                case 0 -> Blocks.OAK_LOG;
                case 1 -> Blocks.SPRUCE_LOG;
                case 2 -> Blocks.BIRCH_LOG;
                case 3 -> Blocks.JUNGLE_LOG;
                default -> Blocks.OAK_LOG;
            };
            net.minecraft.world.item.Item plankItem = switch (i) {
                case 0 -> Items.OAK_PLANKS;
                case 1 -> Items.SPRUCE_PLANKS;
                case 2 -> Items.BIRCH_PLANKS;
                case 3 -> Items.JUNGLE_PLANKS;
                default -> Items.OAK_PLANKS;
            };

            output.accept(
                ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "knife/" + logTypes[index] + "_log_to_planks"),
                new ProcessRecipe(
                    ModRecipeTypes.KNIFE.getId(),
                    List.of(CountedIngredient.of(Ingredient.of(logBlock), 1)),
                    List.of(new ItemStack(plankItem, 6)),
                    List.of(),
                    List.of(),
                    0.0f
                ),
                null
            );
        }

        // Acacia Log -> Acacia Planks (6 output)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "knife/acacia_log_to_planks"),
            new ProcessRecipe(
                ModRecipeTypes.KNIFE.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.ACACIA_LOG), 1)),
                List.of(new ItemStack(Items.ACACIA_PLANKS, 6)),
                List.of(),
                List.of(),
                0.0f
            ),
            null
        );

        // Dark Oak Log -> Dark Oak Planks (6 output)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "knife/dark_oak_log_to_planks"),
            new ProcessRecipe(
                ModRecipeTypes.KNIFE.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.DARK_OAK_LOG), 1)),
                List.of(new ItemStack(Items.DARK_OAK_PLANKS, 6)),
                List.of(),
                List.of(),
                0.0f
            ),
            null
        );

        // Planks to Sticks (6 output each) - All 6 plank types
        net.minecraft.world.item.Item[] planks = {
            Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.BIRCH_PLANKS,
            Items.JUNGLE_PLANKS, Items.ACACIA_PLANKS, Items.DARK_OAK_PLANKS
        };
        for (int i = 0; i < planks.length; i++) {
            final int index = i;
            output.accept(
                ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "knife/planks_" + index + "_to_sticks"),
                new ProcessRecipe(
                    ModRecipeTypes.KNIFE.getId(),
                    List.of(CountedIngredient.of(Ingredient.of(planks[i]), 1)),
                    List.of(new ItemStack(Items.STICK, 6)),
                    List.of(),
                    List.of(),
                    0.0f
                ),
                null
            );
        }

        // Petrified Wood -> Petrified Planks (6 output)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "knife/petrified_wood_to_planks"),
            new ProcessRecipe(
                ModRecipeTypes.KNIFE.getId(),
                List.of(CountedIngredient.of(Ingredient.of(ModBlocks.PETRIFIED_WOOD.get()), 1)),
                List.of(new ItemStack(ModBlocks.PETRIFIED_PLANKS.get(), 6)),
                List.of(),
                List.of(),
                0.0f
            ),
            null
        );

        // Petrified Planks -> Sticks (6 output)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "knife/petrified_planks_to_sticks"),
            new ProcessRecipe(
                ModRecipeTypes.KNIFE.getId(),
                List.of(CountedIngredient.of(Ingredient.of(ModBlocks.PETRIFIED_PLANKS.get()), 1)),
                List.of(new ItemStack(Items.STICK, 6)),
                List.of(),
                List.of(),
                0.0f
            ),
            null
        );

        addRockGrinderRecipes(output, ModRecipeTypes.ROCK_GRINDER.getId());
        addRockGrinderRecipes(output, ModRecipeTypes.ROCK_CRUSHER.getId());
        addDirtyGemRecipes(output, ModRecipeTypes.ROCK_GRINDER.getId());

        addCombustionExtras(output);
        addWaterExtractorRecipes(output);
        addFreezerRecipes(output);
        addCrucibleRecipes(output);
        addFusionExtras(output);
        addInfusionExtras(output);
        addHeatComponentRecipes(output);
        addAlchemyMachineComponentRecipes(output);
        addHeatProviderRecipes(output);
        addOreAlchDustRecipes(output);
        addCondenserRecipes(output);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.DRY_CACTUS.get()), RecipeCategory.MISC, Items.BLACK_DYE, 0.2f, 200)
                .unlockedBy("has_dry_cactus", has(ModBlocks.DRY_CACTUS.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "dry_cactus_to_black_dye"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.PETRIFIED_WOOD.get()), RecipeCategory.MISC, Items.COAL, 0.1f, 200)
                .unlockedBy("has_petrified_wood", has(ModBlocks.PETRIFIED_WOOD.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "petrified_wood_to_coal"));
    }

    private void addCombustionExtras(RecipeOutput output) {
        // Crystal Shard: Nether Quartz + Cactus Needle + Blaze Powder
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "combustion/crystal_shard"),
            new ProcessRecipe(
                ModRecipeTypes.COMBUSTION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(Items.QUARTZ), 1),
                    CountedIngredient.of(alchemyComponentIngredient(AlchemyComponentItem.CACTUS_NEEDLE), 1),
                    CountedIngredient.of(Ingredient.of(Items.BLAZE_POWDER), 1)
                ),
                List.of(alchemyComponent(AlchemyComponentItem.CRYSTAL_SHARD)),
                List.of(),
                List.of(),
                600.0f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "combustion/radioactive_mix"),
            new ProcessRecipe(
                ModRecipeTypes.COMBUSTION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(Items.REDSTONE), 1),
                    CountedIngredient.of(Ingredient.of(Items.GLOWSTONE_DUST), 1),
                    CountedIngredient.of(Ingredient.of(Items.GUNPOWDER), 1),
                    CountedIngredient.of(Ingredient.of(Items.BLAZE_POWDER), 1),
                    CountedIngredient.of(Ingredient.of(Items.COAL), 1)
                ),
                List.of(techComponent(TechComponentItem.RADIOACTIVE_MIX)),
                List.of(),
                List.of(),
                1200.0f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "combustion/dry_cactus"),
            new ProcessRecipe(
                ModRecipeTypes.COMBUSTION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(Blocks.BONE_BLOCK), 1),
                    CountedIngredient.of(Ingredient.of(Items.BLACK_DYE), 8),
                    CountedIngredient.of(baseComponentIngredient(BaseComponentItem.PLANT_MATTER), 8)
                ),
                List.of(new ItemStack(ModBlocks.DRY_CACTUS.get())),
                List.of(),
                List.of(),
                400.0f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "combustion/alchemy_component"),
            new ProcessRecipe(
                ModRecipeTypes.COMBUSTION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(Items.GUNPOWDER), 3),
                    CountedIngredient.of(Ingredient.of(Items.BLAZE_POWDER), 2),
                    CountedIngredient.of(Ingredient.of(Items.COAL), 1)
                ),
                List.of(alchemyComponent(AlchemyComponentItem.ALCH_DUST_1)),
                List.of(),
                List.of(),
                335.0f
            ),
            null
        );
    }

    private void addWaterExtractorRecipes(RecipeOutput output) {
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "water_extractor_extract/dry_cactus"),
            new ProcessRecipe(
                ModRecipeTypes.WATER_EXTRACTOR_EXTRACT.getId(),
                List.of(CountedIngredient.of(Ingredient.of(ModBlocks.DRY_CACTUS.get()), 1)),
                List.of(new ItemStack(Blocks.CACTUS)),
                List.of(),
                List.of(new FluidStack(Fluids.WATER, 50)),
                0.0f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "water_extractor_extract/snow"),
            new ProcessRecipe(
                ModRecipeTypes.WATER_EXTRACTOR_EXTRACT.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.SNOW), 1)),
                List.of(),
                List.of(),
                List.of(new FluidStack(Fluids.WATER, 50)),
                0.0f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "water_extractor_extract/leaves"),
            new ProcessRecipe(
                ModRecipeTypes.WATER_EXTRACTOR_EXTRACT.getId(),
                List.of(CountedIngredient.of(Ingredient.of(ItemTags.LEAVES), 1)),
                List.of(),
                List.of(),
                List.of(new FluidStack(Fluids.WATER, 20)),
                0.0f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "water_extractor_insert/dirt_to_clay"),
            new ProcessRecipe(
                ModRecipeTypes.WATER_EXTRACTOR_INSERT.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.DIRT), 1)),
                List.of(new ItemStack(Blocks.CLAY)),
                List.of(new FluidStack(Fluids.WATER, 200)),
                List.of(),
                0.0f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "water_extractor_insert/cactus"),
            new ProcessRecipe(
                ModRecipeTypes.WATER_EXTRACTOR_INSERT.getId(),
                List.of(CountedIngredient.of(Ingredient.of(ModBlocks.DRY_CACTUS.get()), 1)),
                List.of(new ItemStack(Blocks.CACTUS)),
                List.of(new FluidStack(Fluids.WATER, 1200)),
                List.of(),
                0.0f
            ),
            null
        );
    }

    private void addFreezerRecipes(RecipeOutput output) {
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "freezer/snowball_to_heavy"),
            new ProcessRecipe(
                ModRecipeTypes.FREEZER.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Items.SNOWBALL), 4)),
                List.of(new ItemStack(ModItems.HEAVY_SNOWBALL.get())),
                List.of(),
                List.of(),
                40.0f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "freezer/heavy_snow_to_coarse_dirt"),
            new ProcessRecipe(
                ModRecipeTypes.FREEZER.getId(),
                List.of(CountedIngredient.of(Ingredient.of(ModBlocks.HEAVY_SNOW.get()), 1)),
                List.of(new ItemStack(Blocks.COARSE_DIRT)),
                List.of(),
                List.of(),
                800.0f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "freezer/sandy_netherrack_to_soul_sand"),
            new ProcessRecipe(
                ModRecipeTypes.FREEZER.getId(),
                List.of(CountedIngredient.of(Ingredient.of(ModBlocks.SANDY_NETHERRACK.get()), 1)),
                List.of(new ItemStack(Blocks.SOUL_SAND)),
                List.of(),
                List.of(),
                1500.0f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "freezer/iron_ingot_to_tech_component"),
            new ProcessRecipe(
                ModRecipeTypes.FREEZER.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Items.IRON_INGOT), 1)),
                List.of(techComponent(TechComponentItem.FROZEN_IRON_INGOT)),
                List.of(),
                List.of(),
                3000.0f
            ),
            null
        );
    }

    private void addCrucibleRecipes(RecipeOutput output) {
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "crucible/blaze_powder_block"),
            new ProcessRecipe(
                ModRecipeTypes.CRUCIBLE.getId(),
                List.of(CountedIngredient.of(Ingredient.of(ModBlocks.BLAZE_POWDER_BLOCK.get()), 1)),
                List.of(),
                List.of(),
                List.of(new FluidStack(Fluids.LAVA, 1000)),
                0.0f
            ),
            null
        );
    }

    private void addFusionExtras(RecipeOutput output) {
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alch_coal"),
            new ProcessRecipe(
                ModRecipeTypes.FUSION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(Items.COAL), 1),
                    CountedIngredient.of(alchemyComponentIngredient(AlchemyComponentItem.CRYSTAL_SHARD), 1)
                ),
                List.of(alchemyComponent(AlchemyComponentItem.ALCH_COAL)),
                List.of(),
                List.of(),
                0.01f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alch_iron_ingot"),
            new ProcessRecipe(
                ModRecipeTypes.FUSION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(Items.IRON_INGOT), 1),
                    CountedIngredient.of(alchemyComponentIngredient(AlchemyComponentItem.ALCH_COAL), 1)
                ),
                List.of(alchemyComponent(AlchemyComponentItem.ALCH_IRON_INGOT)),
                List.of(),
                List.of(),
                0.012f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alch_gold_ingot"),
            new ProcessRecipe(
                ModRecipeTypes.FUSION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(Items.GOLD_INGOT), 1),
                    CountedIngredient.of(alchemyComponentIngredient(AlchemyComponentItem.ALCH_COAL), 1)
                ),
                List.of(alchemyComponent(AlchemyComponentItem.ALCH_GOLD_INGOT)),
                List.of(),
                List.of(),
                0.014f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alch_gold_needle"),
            new ProcessRecipe(
                ModRecipeTypes.FUSION.getId(),
                List.of(
                    CountedIngredient.of(alchemyComponentIngredient(AlchemyComponentItem.CACTUS_NEEDLE), 1),
                    CountedIngredient.of(alchemyComponentIngredient(AlchemyComponentItem.ALCH_GOLD_INGOT), 1)
                ),
                List.of(alchemyComponent(AlchemyComponentItem.ALCH_GOLD_NEEDLE)),
                List.of(),
                List.of(),
                0.018f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alch_diamond"),
            new ProcessRecipe(
                ModRecipeTypes.FUSION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(Items.DIAMOND), 1),
                    CountedIngredient.of(alchemyComponentIngredient(AlchemyComponentItem.ALCH_GOLD_NEEDLE), 1)
                ),
                List.of(alchemyComponent(AlchemyComponentItem.ALCH_DIAMOND)),
                List.of(),
                List.of(),
                0.02f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/magmafied_stone"),
            new ProcessRecipe(
                ModRecipeTypes.FUSION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(Blocks.MAGMA_BLOCK), 1),
                    CountedIngredient.of(Ingredient.of(Blocks.STONE), 1),
                    CountedIngredient.of(Ingredient.of(alchemyComponent(AlchemyComponentItem.ALCH_COAL)), 2)
                ),
                List.of(new ItemStack(ModBlocks.MAGMAFIED_STONE.get())),
                List.of(),
                List.of(),
                0.009f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/alchemical_glass"),
            new ProcessRecipe(
                ModRecipeTypes.FUSION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(Blocks.SAND), 1),
                    CountedIngredient.of(alchemyComponentIngredient(AlchemyComponentItem.CRYSTAL_SHARD), 1),
                    CountedIngredient.of(Ingredient.of(Items.PRISMARINE_CRYSTALS), 1)
                ),
                List.of(new ItemStack(ModBlocks.ALCHEMICAL_GLASS.get())),
                List.of(),
                List.of(),
                0.004f
            ),
            null
        );

        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/petrified_wood"),
            new ProcessRecipe(
                ModRecipeTypes.FUSION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(ItemTags.LOGS), 1),
                    CountedIngredient.of(Ingredient.of(Items.ROTTEN_FLESH), 1),
                    CountedIngredient.of(Ingredient.of(Items.COAL), 1)
                ),
                List.of(new ItemStack(ModBlocks.PETRIFIED_WOOD.get())),
                List.of(),
                List.of(),
                0.001f
            ),
            null
        );
    }

    private void addInfusionExtras(RecipeOutput output) {
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "infusion/health_gem"),
            new ProcessRecipe(
                ModRecipeTypes.INFUSION.getId(),
                List.of(
                    CountedIngredient.of(Ingredient.of(alchemyComponent(AlchemyComponentItem.ALCH_DIAMOND)), 1),
                    CountedIngredient.of(Ingredient.of(Blocks.CHORUS_FLOWER), 1)
                ),
                List.of(new ItemStack(ModItems.HEALTH_GEM.get())),
                List.of(),
                List.of(),
                15.0f
            ),
            null
        );
    }

    private void addRockGrinderRecipes(RecipeOutput output, ResourceLocation recipeTypeId) {
        // Rock grinder recipes use parameter as base chance (0.0 to 1.0+), fortune multiplies chance
        String prefix = recipeTypeId.getPath();

        // Cobblestone -> Gravel (100% chance)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, prefix + "/cobblestone_to_gravel"),
            new ProcessRecipe(
                recipeTypeId,
                List.of(CountedIngredient.of(Ingredient.of(Blocks.COBBLESTONE), 1)),
                List.of(new ItemStack(Blocks.GRAVEL)),
                List.of(),
                List.of(),
                1.0f
            ),
            null
        );

        // Gravel -> Sand (100% chance)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, prefix + "/gravel_to_sand"),
            new ProcessRecipe(
                recipeTypeId,
                List.of(CountedIngredient.of(Ingredient.of(Blocks.GRAVEL), 1)),
                List.of(new ItemStack(Blocks.SAND)),
                List.of(),
                List.of(),
                1.0f
            ),
            null
        );

        // Gravel -> Flint (30% chance)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, prefix + "/gravel_to_flint"),
            new ProcessRecipe(
                recipeTypeId,
                List.of(CountedIngredient.of(Ingredient.of(Blocks.GRAVEL), 1)),
                List.of(new ItemStack(Items.FLINT)),
                List.of(),
                List.of(),
                0.3f
            ),
            null
        );

        // Stone -> Tech Component variant 0 (44% chance)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, prefix + "/stone_to_component"),
            new ProcessRecipe(
                recipeTypeId,
                List.of(CountedIngredient.of(Ingredient.of(Blocks.STONE), 1)),
                List.of(techComponent(TechComponentItem.STONE_CRUSHED)),
                List.of(),
                List.of(),
                0.44f
            ),
            null
        );

        // Netherrack -> Base Component (44% chance)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, prefix + "/netherrack_to_component"),
            new ProcessRecipe(
                recipeTypeId,
                List.of(CountedIngredient.of(Ingredient.of(Blocks.NETHERRACK), 1)),
                List.of(techComponent(TechComponentItem.NETHERRACK_CRUSHED)),
                List.of(),
                List.of(),
                0.44f
            ),
            null
        );

        // Logs -> Base Component (150% chance, can get 1-2 outputs)
        // Using oak log as representative for all logs
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, prefix + "/log_to_component"),
            new ProcessRecipe(
                recipeTypeId,
                List.of(CountedIngredient.of(Ingredient.of(Blocks.OAK_LOG), 1)),
                List.of(baseComponent(BaseComponentItem.SAWDUST)),
                List.of(),
                List.of(),
                1.5f
            ),
            null
        );
    }

    private void addDirtyGemRecipes(RecipeOutput output, ResourceLocation recipeTypeId) {
        for (int i = 0; i < DirtyGemItem.getGemInfos().size(); i++) {
            GemRegisterInfo info = DirtyGemItem.getGemInfos().get(i);
            ItemStack outputStack = new ItemStack(ModItems.DIRTY_GEM.get());
            DirtyGemItem.setGemIndex(outputStack, i);
            output.accept(
                ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "rock_grinder/dirty_gem/" + info.name),
                new ProcessRecipe(
                    recipeTypeId,
                    List.of(CountedIngredient.of(Ingredient.of(info.parentBlock), 1)),
                    List.of(outputStack),
                    List.of(),
                    List.of(),
                    info.rarity
                ),
                null
            );
        }
    }

    private void addHeatComponentRecipes(RecipeOutput output) {
        List<HeatComponentRecipe> recipes = List.of(
            new HeatComponentRecipe(Ingredient.of(ItemTags.PLANKS), Items.GUNPOWDER),
            new HeatComponentRecipe(Ingredient.of(Blocks.STONE), Items.GUNPOWDER),
            new HeatComponentRecipe(Ingredient.of(cTag("ingots/bronze")), Items.GUNPOWDER),
            new HeatComponentRecipe(Ingredient.of(Items.IRON_INGOT), Items.GUNPOWDER),
            new HeatComponentRecipe(Ingredient.of(cTag("ingots/steel")), Items.BLAZE_POWDER),
            new HeatComponentRecipe(Ingredient.of(cTag("ingots/electrum")), Items.BLAZE_POWDER),
            new HeatComponentRecipe(Ingredient.of(Items.NETHER_BRICK), Items.BLAZE_POWDER),
            new HeatComponentRecipe(Ingredient.of(cTag("ingots/lead")), Items.BLAZE_POWDER),
            new HeatComponentRecipe(Ingredient.of(cTag("ingots/manyullyn")), Items.REDSTONE),
            new HeatComponentRecipe(Ingredient.of(cTag("ingots/signalum")), Items.REDSTONE),
            new HeatComponentRecipe(Ingredient.of(Blocks.END_STONE), Items.REDSTONE),
            new HeatComponentRecipe(Ingredient.of(cTag("ingots/enderium")), Items.REDSTONE),
            new HeatComponentRecipe(Ingredient.of(Blocks.OBSIDIAN), Items.GLOWSTONE_DUST),
            new HeatComponentRecipe(Ingredient.of(Items.QUARTZ), Items.GLOWSTONE_DUST),
            new HeatComponentRecipe(Ingredient.of(cTag("ingots/osmium")), Items.BLAZE_POWDER),
            new HeatComponentRecipe(Ingredient.of(cTag("ingots/refined_obsidian")), Items.REDSTONE)
        );

        int variantCount = Math.min(HeatVariants.size(), recipes.size());
        for (int i = 0; i < variantCount; i++) {
            HeatComponentRecipe recipe = recipes.get(i);
            ItemStack outputStack = HeatComponentItem.createStack(i, ModItems.HEAT_COMPONENT.get());
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputStack)
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XXX")
                .define('X', recipe.material())
                .define('Y', Ingredient.of(recipe.dust()))
                .unlockedBy("has_" + recipe.dust().builtInRegistryHolder().key().location().getPath(), has(recipe.dust()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "heat_component/" + HeatVariants.getName(i)));
        }
    }

    private void addHeatProviderRecipes(RecipeOutput output) {
        List<Ingredient> materials = List.of(
            Ingredient.of(ItemTags.LOGS),
            Ingredient.of(Blocks.STONE),
            Ingredient.of(cTag("ingots/bronze")),
            Ingredient.of(Items.IRON_INGOT),
            Ingredient.of(cTag("ingots/steel")),
            Ingredient.of(cTag("ingots/electrum")),
            Ingredient.of(Items.NETHER_BRICK),
            Ingredient.of(cTag("ingots/lead")),
            Ingredient.of(cTag("ingots/manyullyn")),
            Ingredient.of(cTag("ingots/signalum")),
            Ingredient.of(Blocks.END_STONE),
            Ingredient.of(cTag("ingots/enderium")),
            Ingredient.of(ModItems.DARK_MATTER.get()),
            Ingredient.of(ModItems.LIGHT_MATTER.get()),
            Ingredient.of(cTag("ingots/osmium")),
            Ingredient.of(cTag("ingots/refined_obsidian"))
        );

        int variantCount = Math.min(HeatVariants.size(), materials.size());
        for (int i = 0; i < variantCount; i++) {
            ItemStack outputStack = HeatProviderItem.createStack(i, ModItems.HEAT_PROVIDER.get());
            ItemStack heatComponent = HeatComponentItem.createStack(i, ModItems.HEAT_COMPONENT.get());
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputStack)
                .pattern("XYX")
                .pattern("XYX")
                .pattern("X X")
                .define('X', materials.get(i))
                .define('Y', Ingredient.of(heatComponent))
                .unlockedBy("has_heat_component_" + HeatVariants.getName(i), has(ModItems.HEAT_COMPONENT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "heat_provider/" + HeatVariants.getName(i)));
        }
    }

    private void addAlchemyMachineComponentRecipes(RecipeOutput output) {
        List<Ingredient> materials = List.of(
            Ingredient.of(ItemTags.PLANKS),
            Ingredient.of(Blocks.STONE),
            Ingredient.of(cTag("ingots/bronze")),
            Ingredient.of(Items.IRON_INGOT),
            Ingredient.of(cTag("ingots/steel")),
            Ingredient.of(cTag("ingots/electrum")),
            Ingredient.of(Items.NETHER_BRICK),
            Ingredient.of(cTag("ingots/lead")),
            Ingredient.of(cTag("ingots/manyullyn")),
            Ingredient.of(cTag("ingots/signalum")),
            Ingredient.of(Blocks.END_STONE),
            Ingredient.of(cTag("ingots/enderium")),
            Ingredient.of(ModItems.DARK_MATTER.get()),
            Ingredient.of(ModItems.LIGHT_MATTER.get()),
            Ingredient.of(cTag("ingots/osmium")),
            Ingredient.of(cTag("ingots/refined_obsidian"))
        );

        int variantCount = Math.min(HeatVariants.size(), materials.size());
        for (int i = 0; i < variantCount; i++) {
            ItemStack outputStack = AlchemyMachineComponentItem.createStack(i, ModItems.ALCHEMY.get());
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputStack)
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XXX")
                .define('X', materials.get(i))
                .define('Y', alchemyMaterialDustIngredient(i))
                .unlockedBy("has_alchemy_component", has(ModItems.ALCHEMY_COMPONENT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "alchemy/" + HeatVariants.getName(i)));
        }
    }

    private void addOreAlchDustRecipes(RecipeOutput output) {
        List<Ingredient> components = List.of(
            Ingredient.of(Items.ROTTEN_FLESH),
            Ingredient.of(Items.WHEAT),
            Ingredient.of(Items.PUMPKIN_SEEDS),
            Ingredient.of(Items.BONE),
            Ingredient.of(Items.SUGAR),
            Ingredient.of(Items.WHEAT),
            alchemyComponentIngredient(AlchemyComponentItem.CRYSTAL_SHARD),
            alchemyComponentIngredient(AlchemyComponentItem.CRYSTAL_SHARD),
            baseComponentIngredient(BaseComponentItem.PLANT_MATTER),
            Ingredient.of(Blocks.CLAY),
            alchemyComponentIngredient(AlchemyComponentItem.CRYSTAL_SHARD),
            alchemyComponentIngredient(AlchemyComponentItem.CRYSTAL_SHARD),
            alchemyComponentIngredient(AlchemyComponentItem.CRYSTAL_SHARD),
            Ingredient.of(Items.DRAGON_BREATH),
            Ingredient.of(Items.CHARCOAL),
            Ingredient.of(Blocks.OBSIDIAN),
            Ingredient.of(Items.SUGAR),
            techComponentIngredient(TechComponentItem.RADIOACTIVE_MIX),
            Ingredient.of(Blocks.SOUL_SAND),
            Ingredient.of(Items.PRISMARINE_SHARD),
            baseComponentIngredient(BaseComponentItem.PLANT_MATTER),
            Ingredient.of(Items.DIAMOND),
            techComponentIngredient(TechComponentItem.RADIOACTIVE_MIX),
            techComponentIngredient(TechComponentItem.RADIOACTIVE_MIX),
            techComponentIngredient(TechComponentItem.RADIOACTIVE_MIX)
        );

        int variantCount = Math.min(ItemOreAlchDust.getOreInfos().size(), components.size());
        for (int i = 0; i < variantCount; i++) {
            OreRegisterInfo info = ItemOreAlchDust.getOreInfos().get(i);
            ItemStack outputStack = new ItemStack(ModItems.ORE_ALCH_DUST.get());
            ItemOreAlchDust.setDustIndex(outputStack, i);

            ItemStack oreDust = getOreItemDust(info.rarity);
            List<CountedIngredient> inputs = List.of(
                CountedIngredient.of(components.get(i), 1),
                CountedIngredient.of(Ingredient.of(oreDust), oreDust.getCount())
            );

            output.accept(
                ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/ore_alch_dust/" + info.name + "_from_components"),
                new ProcessRecipe(
                    ModRecipeTypes.FUSION.getId(),
                    inputs,
                    List.of(outputStack),
                    List.of(),
                    List.of(),
                    info.rarity * 0.0008f
                ),
                null
            );

            output.accept(
                ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "fusion/ore_alch_dust/" + info.name + "_from_dust"),
                new ProcessRecipe(
                    ModRecipeTypes.FUSION.getId(),
                    List.of(
                        CountedIngredient.of(Ingredient.of(cTag("dusts/" + info.name)), 1),
                        CountedIngredient.of(Ingredient.of(oreDust), oreDust.getCount())
                    ),
                    List.of(outputStack),
                    List.of(),
                    List.of(),
                    info.rarity * 0.0021f
                ),
                null
            );
        }
    }

    private static ItemStack getOreItemDust(int rarity) {
        if (rarity <= 2) {
            return new ItemStack(Items.GUNPOWDER, 2);
        }
        if (rarity <= 4) {
            return new ItemStack(Items.BLAZE_POWDER, 2);
        }
        if (rarity <= 6) {
            return new ItemStack(Items.GLOWSTONE_DUST, 2);
        }
        if (rarity <= 8) {
            return new ItemStack(Items.LAPIS_LAZULI, 2);
        }
        return new ItemStack(ModItems.DARK_MATTER.get(), 2);
    }

    private static TagKey<Item> cTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    private record HeatComponentRecipe(Ingredient material, Item dust) {
    }

    private ItemStack baseComponent(int index) {
        return BaseComponentItem.createStack(index, ModItems.BASE_COMPONENT.get());
    }

    private ItemStack techComponent(int index) {
        return TechComponentItem.createStack(index, ModItems.TECH_COMPONENT.get());
    }

    private ItemStack alchemyComponent(int index) {
        return AlchemyComponentItem.createStack(index, ModItems.ALCHEMY_COMPONENT.get());
    }

    private Ingredient baseComponentIngredient(int index) {
        return DataComponentIngredient.of(false, baseComponent(index));
    }

    private Ingredient techComponentIngredient(int index) {
        return DataComponentIngredient.of(false, techComponent(index));
    }

    private Ingredient alchemyComponentIngredient(int index) {
        return DataComponentIngredient.of(false, alchemyComponent(index));
    }

    private Ingredient heatComponentIngredient(int index) {
        return DataComponentIngredient.of(false, HeatComponentItem.createStack(index, ModItems.HEAT_COMPONENT.get()));
    }

    private Ingredient alchemyMaterialDustIngredient(int variantIndex) {
        return switch (variantIndex) {
            case 0, 1, 2, 3 -> alchemyComponentIngredient(AlchemyComponentItem.ALCH_DUST_1);
            case 4, 5, 6, 7, 14 -> alchemyComponentIngredient(AlchemyComponentItem.ALCH_DUST_2);
            case 8, 9, 10, 11, 15 -> alchemyComponentIngredient(AlchemyComponentItem.ALCH_DUST_3);
            default -> alchemyComponentIngredient(AlchemyComponentItem.ALCH_DUST_4);
        };
    }

    private void addCondenserRecipes(RecipeOutput output) {
        // Shard multiplication would go here, currently placeholder
    }

    private void addPlantMatterRecipes(RecipeOutput output) {
        addPlantMatterRecipe(output, "saplings", Ingredient.of(ItemTags.SAPLINGS), has(ItemTags.SAPLINGS));
        addPlantMatterRecipe(output, "wheat", Ingredient.of(Items.WHEAT), has(Items.WHEAT));
        addPlantMatterRecipe(output, "leaves", Ingredient.of(ItemTags.LEAVES), has(ItemTags.LEAVES));
        addPlantMatterRecipe(output, "cactus_fruit", Ingredient.of(ModItems.CACTUS_FRUIT.get()), has(ModItems.CACTUS_FRUIT.get()));
    }

    private void addPlantMatterRecipe(RecipeOutput output, String name, Ingredient ingredient, Criterion<?> unlock) {
        ItemStack outputStack = baseComponent(BaseComponentItem.PLANT_MATTER);
        outputStack.setCount(3);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputStack)
            .pattern(" X ")
            .pattern("XXX")
            .pattern(" X ")
            .define('X', ingredient)
            .unlockedBy("has_" + name, unlock)
            .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "base_component/plant_matter_from_" + name));
    }
}
