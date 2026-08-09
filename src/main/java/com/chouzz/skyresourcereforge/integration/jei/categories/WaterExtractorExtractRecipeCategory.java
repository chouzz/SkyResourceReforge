package com.chouzz.skyresourcereforge.integration.jei.categories;

import java.util.List;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.integration.jei.SkyResourceJEIPlugin;
import com.chouzz.skyresourcereforge.item.WaterExtractorItem;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModItems;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class WaterExtractorExtractRecipeCategory implements IRecipeCategory<ProcessRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public WaterExtractorExtractRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "textures/gui/jei/extractor.png"),
            0, 0, 150, 50
        );
        this.icon = guiHelper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK,
            new ItemStack(ModItems.WATER_EXTRACTOR.get())
        );
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<ProcessRecipe> getRecipeType() {
        return SkyResourceJEIPlugin.WATER_EXTRACTOR_EXTRACT_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.skyresourcereforge.recipe.water_extractor_extract");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ProcessRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 32, 1)
            .addItemStack(new ItemStack(ModItems.WATER_EXTRACTOR.get()));

        // Cap input slots to what fits within the 150px background (max 5 slots before overflow)
        int maxInputs = (150 - 53) / 18;
        int inputCount = Math.min(recipe.getInputs().size(), maxInputs);
        for (int i = 0; i < inputCount; i++) {
            var ingredient = recipe.getInputs().get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, 53 + i * 18, 29)
                .addIngredients(VanillaTypes.ITEM_STACK, ingredient.getStacksWithCount());
        }

        List<ItemStack> outputs = recipe.getOutputs();
        if (!outputs.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 106, 15)
                .addItemStack(outputs.get(0));
        }

        if (!recipe.getFluidOutputs().isEmpty()) {
            var fluid = recipe.getFluidOutputs().get(0);
            builder.addSlot(RecipeIngredientRole.OUTPUT, 132, 4)
                .addIngredient(NeoForgeTypes.FLUID_STACK, fluid)
                .setFluidRenderer(WaterExtractorItem.CAPACITY, false, 14, 42);
        }
    }
}
