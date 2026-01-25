package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.heat.HeatVariants;
import com.chouzz.skyresourcereforge.alchemy.item.ItemOreAlchDust;
import com.chouzz.skyresourcereforge.alchemy.item.OreRegisterInfo;
import com.chouzz.skyresourcereforge.item.HeatComponentItem;
import com.chouzz.skyresourcereforge.item.HeatProviderItem;
import com.chouzz.skyresourcereforge.recipe.CountedIngredient;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

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
                                CountedIngredient.of(Ingredient.of(ModItems.TECH_COMPONENT.get()), 4),
                                CountedIngredient.of(Ingredient.of(ModItems.ALCHEMY_COMPONENT.get()), 4),
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
                .define('X', ModItems.BASE_COMPONENT.get())
                .define('Y', Items.ENDER_EYE)
                .define('Z', ModItems.TECH_COMPONENT.get())
                .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "silverfish_disruptor"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.LIFE_INFUSER.get())
                .pattern("XXX")
                .pattern(" X ")
                .pattern(" Y ")
                .define('X', ItemTags.LOGS)
                .define('Y', ModItems.INFUSION_STONE_ALCHEMICAL.get())
                .unlockedBy("has_infusion_stone_alchemical", has(ModItems.INFUSION_STONE_ALCHEMICAL.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "life_infuser"));

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

        // === ROCK GRINDER RECIPES ===
        // Rock grinder recipes use parameter as base chance (0.0 to 1.0+), fortune multiplies chance

        // Cobblestone -> Gravel (100% chance)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "rock_grinder/cobblestone_to_gravel"),
            new ProcessRecipe(
                ModRecipeTypes.ROCK_GRINDER.getId(),
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
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "rock_grinder/gravel_to_sand"),
            new ProcessRecipe(
                ModRecipeTypes.ROCK_GRINDER.getId(),
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
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "rock_grinder/gravel_to_flint"),
            new ProcessRecipe(
                ModRecipeTypes.ROCK_GRINDER.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.GRAVEL), 1)),
                List.of(new ItemStack(Items.FLINT)),
                List.of(),
                List.of(),
                0.3f
            ),
            null
        );

        // Stone -> Tech Component variant 0 (44% chance)
        // Note: Using base component as placeholder since tech component variants aren't fully implemented
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "rock_grinder/stone_to_component"),
            new ProcessRecipe(
                ModRecipeTypes.ROCK_GRINDER.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.STONE), 1)),
                List.of(new ItemStack(ModItems.BASE_COMPONENT.get())),
                List.of(),
                List.of(),
                0.44f
            ),
            null
        );

        // Netherrack -> Base Component (44% chance)
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "rock_grinder/netherrack_to_component"),
            new ProcessRecipe(
                ModRecipeTypes.ROCK_GRINDER.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.NETHERRACK), 1)),
                List.of(new ItemStack(ModItems.BASE_COMPONENT.get())),
                List.of(),
                List.of(),
                0.44f
            ),
            null
        );

        // Logs -> Base Component (150% chance, can get 1-2 outputs)
        // Using oak log as representative for all logs
        output.accept(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "rock_grinder/log_to_component"),
            new ProcessRecipe(
                ModRecipeTypes.ROCK_GRINDER.getId(),
                List.of(CountedIngredient.of(Ingredient.of(Blocks.OAK_LOG), 1)),
                List.of(new ItemStack(ModItems.BASE_COMPONENT.get())),
                List.of(),
                List.of(),
                1.5f
            ),
            null
        );

        addHeatComponentRecipes(output);
        addHeatProviderRecipes(output);
        addOreAlchDustRecipes(output);
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

    private void addOreAlchDustRecipes(RecipeOutput output) {
        List<Ingredient> components = List.of(
            Ingredient.of(Items.ROTTEN_FLESH),
            Ingredient.of(Items.WHEAT),
            Ingredient.of(Items.PUMPKIN_SEEDS),
            Ingredient.of(Items.BONE),
            Ingredient.of(Items.SUGAR),
            Ingredient.of(Items.WHEAT),
            Ingredient.of(Items.IRON_INGOT),
            Ingredient.of(Items.GOLD_INGOT),
            Ingredient.of(ModItems.BASE_COMPONENT.get()),
            Ingredient.of(Blocks.CLAY),
            Ingredient.of(Items.LAPIS_LAZULI),
            Ingredient.of(Items.MAGMA_CREAM),
            Ingredient.of(Items.CLAY_BALL),
            Ingredient.of(Items.DRAGON_BREATH),
            Ingredient.of(Items.CHARCOAL),
            Ingredient.of(Blocks.OBSIDIAN),
            Ingredient.of(Items.SUGAR),
            Ingredient.of(ModItems.TECH_COMPONENT.get()),
            Ingredient.of(Blocks.SOUL_SAND),
            Ingredient.of(Items.PRISMARINE_SHARD),
            Ingredient.of(ModItems.BASE_COMPONENT.get()),
            Ingredient.of(Items.DIAMOND),
            Ingredient.of(Items.GLOWSTONE_DUST),
            Ingredient.of(Items.ROTTEN_FLESH),
            Ingredient.of(ModItems.BASE_COMPONENT.get())
        );

        int variantCount = Math.min(ItemOreAlchDust.oreInfos.size(), components.size());
        for (int i = 0; i < variantCount; i++) {
            OreRegisterInfo info = ItemOreAlchDust.oreInfos.get(i);
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
}
