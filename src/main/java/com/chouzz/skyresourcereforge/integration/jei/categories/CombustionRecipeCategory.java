package com.chouzz.skyresourcereforge.integration.jei.categories;

import java.util.List;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.integration.jei.SkyResourceJEIPlugin;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModBlocks;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CombustionRecipeCategory implements IRecipeCategory<ProcessRecipe> {
    private static final int[] slotInputStacks = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final int slotOutput = 9;

    private final IDrawable background;
    private final IDrawable icon;

    public CombustionRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "textures/gui/jei/combustion.png"),
            0, 0, 137, 71
        );
        this.icon = guiHelper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK,
            new ItemStack(ModBlocks.COMBUSTION_CONTROLLER.get())
        );
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<ProcessRecipe> getRecipeType() {
        return SkyResourceJEIPlugin.COMBUSTION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.skyresourcereforge.recipe.combustion");
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
        // Input slots in 3x3 grid
        for (int i = 0; i < Math.min(9, recipe.getInputs().size()); i++) {
            int x = (i % 3) * 18 + 3;
            int y = (i / 3) * 18 + 9;

            var ingredient = recipe.getInputs().get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                .addIngredients(VanillaTypes.ITEM_STACK, ingredient.getStacksWithCount());
        }

        // Output slot
        List<ItemStack> outputs = recipe.getOutputs();
        if (!outputs.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 97, 27)
                .addItemStack(outputs.get(0));
        }
    }
}
