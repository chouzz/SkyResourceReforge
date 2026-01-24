package com.chouzz.skyresourcereforge.integration.jei.categories;

import java.util.List;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.integration.jei.SkyResourceJEIPlugin;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;

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

public class InfusionRecipeCategory implements IRecipeCategory<ProcessRecipe> {
    private final IDrawable background;
    private final IDrawable icon;

    public InfusionRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "textures/gui/jei/infusion.png"),
            0, 0, 130, 48
        );
        this.icon = guiHelper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK,
            new ItemStack(ModItems.INFUSION_STONE_SANDSTONE.get())
        );
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<ProcessRecipe> getRecipeType() {
        return SkyResourceJEIPlugin.INFUSION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.skyresourcereforge.recipe.infusion");
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
        // Input stack slot
        if (!recipe.getInputs().isEmpty()) {
            var ingredient = recipe.getInputs().get(0);
            builder.addSlot(RecipeIngredientRole.INPUT, 0, 1)
                .addIngredients(VanillaTypes.ITEM_STACK, List.of(ingredient.ingredient().getItems()));
        }

        // Infusion stone slot (all infusion stones)
        builder.addSlot(RecipeIngredientRole.INPUT, 32, 1)
            .addItemStacks(List.of(
                new ItemStack(ModItems.INFUSION_STONE_SANDSTONE.get()),
                new ItemStack(ModItems.INFUSION_STONE_RED_SANDSTONE.get()),
                new ItemStack(ModItems.INFUSION_STONE_ALCHEMICAL.get())
            ));

        // Input block slot
        if (recipe.getInputs().size() > 1) {
            var ingredient = recipe.getInputs().get(1);
            builder.addSlot(RecipeIngredientRole.INPUT, 53, 29)
                .addIngredients(VanillaTypes.ITEM_STACK, List.of(ingredient.ingredient().getItems()));
        }

        // Output slot
        List<ItemStack> outputs = recipe.getOutputs();
        if (!outputs.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 106, 15)
                .addItemStack(outputs.get(0));
        }
    }
}
