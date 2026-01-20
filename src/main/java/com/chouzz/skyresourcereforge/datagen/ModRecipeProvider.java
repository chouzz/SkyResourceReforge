package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

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
                        List.of(Ingredient.of(Items.COAL)),
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
    }
}
