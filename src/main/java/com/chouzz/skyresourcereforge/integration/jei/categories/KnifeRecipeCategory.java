package com.chouzz.skyresourcereforge.integration.jei.categories;

import java.util.List;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.integration.jei.SkyResourceJEIPlugin;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
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

public class KnifeRecipeCategory implements IRecipeCategory<ProcessRecipe> {
    private static final int SLOT_PIXEL_OFFSET = 1;
    private final IDrawable background;
    private final IDrawable icon;
    private final List<ItemStack> knifeStacks;

    public KnifeRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "textures/gui/jei/infusion.png"),
            32, 0, 96, 50
        );
        this.icon = guiHelper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK,
            new ItemStack(ModItems.IRON_KNIFE.get())
        );
        this.knifeStacks = List.of(
            new ItemStack(ModItems.CACTUS_KNIFE.get()),
            new ItemStack(ModItems.STONE_KNIFE.get()),
            new ItemStack(ModItems.IRON_KNIFE.get()),
            new ItemStack(ModItems.DIAMOND_KNIFE.get())
        );
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<ProcessRecipe> getRecipeType() {
        return SkyResourceJEIPlugin.KNIFE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.skyresourcereforge.recipe.knife");
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
        builder.addSlot(RecipeIngredientRole.INPUT, 0 + SLOT_PIXEL_OFFSET, 1 + SLOT_PIXEL_OFFSET)
            .addItemStacks(knifeStacks);

        for (int i = 0; i < recipe.getInputs().size(); i++) {
            var ingredient = recipe.getInputs().get(i);
            List<ItemStack> inputStacks = new java.util.ArrayList<>();
            for (ItemStack stack : ingredient.ingredient().getItems()) {
                ItemStack copy = stack.copy();
                copy.setCount(ingredient.count());
                inputStacks.add(copy);
            }
            builder.addSlot(RecipeIngredientRole.INPUT, 21 + i * 18 + SLOT_PIXEL_OFFSET, 29 + SLOT_PIXEL_OFFSET)
                .addIngredients(VanillaTypes.ITEM_STACK, inputStacks);
        }

        List<ItemStack> outputs = recipe.getOutputs();
        if (!outputs.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 74 + SLOT_PIXEL_OFFSET, 15 + SLOT_PIXEL_OFFSET)
                .addItemStack(outputs.get(0));
        }
    }
}
