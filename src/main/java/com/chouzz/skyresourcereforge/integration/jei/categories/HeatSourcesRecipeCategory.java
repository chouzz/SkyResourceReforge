package com.chouzz.skyresourcereforge.integration.jei.categories;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.integration.jei.HeatSourceRecipe;
import com.chouzz.skyresourcereforge.integration.jei.SkyResourceJEIPlugin;
import com.chouzz.skyresourcereforge.item.HeatProviderItem;
import com.chouzz.skyresourcereforge.registration.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class HeatSourcesRecipeCategory implements IRecipeCategory<HeatSourceRecipe> {
    private static final int NAME_X = 20;
    private static final int NAME_Y = 2;
    private static final int VALUE_X = 20;
    private static final int VALUE_Y = 14;

    private final IDrawable background;
    private final IDrawable icon;

    public HeatSourcesRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
            ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "textures/gui/jei/blank.png"),
            20, 0, 110, 25
        );
        this.icon = guiHelper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK,
            HeatProviderItem.createStack(0, ModItems.HEAT_PROVIDER.get())
        );
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<HeatSourceRecipe> getRecipeType() {
        return SkyResourceJEIPlugin.HEAT_SOURCES_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.skyresourcereforge.recipe.heat_sources");
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
    public void setRecipe(IRecipeLayoutBuilder builder, HeatSourceRecipe recipe, IFocusGroup focuses) {
        if (!recipe.stack().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 0, 2)
                .addItemStack(recipe.stack());
        }
    }

    @Override
    public void draw(HeatSourceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        ItemStack stack = recipe.stack();
        Component name = stack.isEmpty() ? recipe.name() : stack.getHoverName();
        Component heatValue = Component.translatable("jei.skyresourcereforge.heat_source.value", recipe.heat());
        guiGraphics.drawString(Minecraft.getInstance().font, name, NAME_X, NAME_Y, 0xFF808080, false);
        guiGraphics.drawString(Minecraft.getInstance().font, heatValue, VALUE_X, VALUE_Y, 0xFF808080, false);
    }
}
