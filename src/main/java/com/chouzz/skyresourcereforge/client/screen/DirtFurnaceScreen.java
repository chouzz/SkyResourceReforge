package com.chouzz.skyresourcereforge.client.screen;

import com.chouzz.skyresourcereforge.menu.DirtFurnaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DirtFurnaceScreen extends AbstractContainerScreen<DirtFurnaceMenu> {
    private static final ResourceLocation FURNACE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");

    public DirtFurnaceScreen(DirtFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(FURNACE_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        int totalBurnTime = this.menu.getTotalBurnTime();
        int burnLeftScaled = totalBurnTime > 0 ? this.menu.getBurnTime() * 13 / totalBurnTime : 0;
        if (burnLeftScaled > 0) {
            guiGraphics.blit(FURNACE_TEXTURE, x + 56, y + 36 + 12 - burnLeftScaled, 176, 12 - burnLeftScaled, 14, burnLeftScaled + 1);
        }

        int totalCookTime = this.menu.getTotalCookTime();
        int cookProgressScaled = totalCookTime > 0 ? this.menu.getCookTime() * 24 / totalCookTime : 0;
        if (cookProgressScaled > 0) {
            guiGraphics.blit(FURNACE_TEXTURE, x + 79, y + 34, 176, 14, cookProgressScaled + 1, 16);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (this.imageWidth - this.font.width(this.title)) / 2;
        guiGraphics.drawString(this.font, this.title, x, 6, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0x404040, false);
    }
}
