package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.heat.HeatVariants;
import com.chouzz.skyresourcereforge.item.HeatComponentItem;
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
    }

    private void addHeatComponentRecipes(RecipeOutput output) {
        for (int i = 0; i < HeatVariants.size(); i++) {
            Ingredient material;
            Item dust;

            switch (i) {
                case 0 -> {
                    material = Ingredient.of(ItemTags.PLANKS);
                    dust = Items.GUNPOWDER;
                }
                case 1 -> {
                    material = Ingredient.of(Blocks.STONE);
                    dust = Items.GUNPOWDER;
                }
                case 2 -> {
                    material = Ingredient.of(cTag("ingots/bronze"));
                    dust = Items.GUNPOWDER;
                }
                case 3 -> {
                    material = Ingredient.of(Items.IRON_INGOT);
                    dust = Items.GUNPOWDER;
                }
                case 4 -> {
                    material = Ingredient.of(cTag("ingots/steel"));
                    dust = Items.BLAZE_POWDER;
                }
                case 5 -> {
                    material = Ingredient.of(cTag("ingots/electrum"));
                    dust = Items.BLAZE_POWDER;
                }
                case 6 -> {
                    material = Ingredient.of(Items.NETHER_BRICK);
                    dust = Items.BLAZE_POWDER;
                }
                case 7 -> {
                    material = Ingredient.of(cTag("ingots/lead"));
                    dust = Items.BLAZE_POWDER;
                }
                case 8 -> {
                    material = Ingredient.of(cTag("ingots/manyullyn"));
                    dust = Items.REDSTONE;
                }
                case 9 -> {
                    material = Ingredient.of(cTag("ingots/signalum"));
                    dust = Items.REDSTONE;
                }
                case 10 -> {
                    material = Ingredient.of(Blocks.END_STONE);
                    dust = Items.REDSTONE;
                }
                case 11 -> {
                    material = Ingredient.of(cTag("ingots/enderium"));
                    dust = Items.REDSTONE;
                }
                case 12 -> {
                    material = Ingredient.of(Blocks.OBSIDIAN);
                    dust = Items.GLOWSTONE_DUST;
                }
                case 13 -> {
                    material = Ingredient.of(Items.QUARTZ);
                    dust = Items.GLOWSTONE_DUST;
                }
                case 14 -> {
                    material = Ingredient.of(cTag("ingots/osmium"));
                    dust = Items.BLAZE_POWDER;
                }
                case 15 -> {
                    material = Ingredient.of(cTag("ingots/refined_obsidian"));
                    dust = Items.REDSTONE;
                }
                default -> {
                    continue;
                }
            }

            ItemStack outputStack = HeatComponentItem.createStack(i, ModItems.HEAT_COMPONENT.get());
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputStack)
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XXX")
                .define('X', material)
                .define('Y', Ingredient.of(dust))
                .unlockedBy("has_" + dust.builtInRegistryHolder().key().location().getPath(), has(dust))
                .save(output, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "heat_component/" + HeatVariants.getName(i)));
        }
    }

    private static TagKey<Item> cTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
