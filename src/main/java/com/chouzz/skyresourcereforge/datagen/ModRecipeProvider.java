package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
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
        // Example combustion recipe: Coal -> Compressed Coal Block
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
    }
}
