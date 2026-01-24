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

public class RockGrinderRecipeCategory implements IRecipeCategory<ProcessRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public RockGrinderRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "textures/gui/jei/blank.png"),
            0, 0, 100, 40
        );
        this.icon = guiHelper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK,
            new ItemStack(ModBlocks.ROCK_CRUSHER.get())
        );
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<ProcessRecipe> getRecipeType() {
        return SkyResourceJEIPlugin.ROCK_GRINDER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.skyresourcereforge.recipe.rock_grinder");
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
        for (int i = 0; i < recipe.getInputs().size(); i++) {
            var ingredient = recipe.getInputs().get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, 7 + i * 18, 12)
                .addIngredients(VanillaTypes.ITEM_STACK, List.of(ingredient.ingredient().getItems()));
        }

        List<ItemStack> outputs = recipe.getOutputs();
        if (!outputs.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 12)
                .addItemStack(outputs.get(0));
        }
    }
}
