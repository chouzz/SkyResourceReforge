package com.chouzz.skyresourcereforge.client.screen;

import com.chouzz.skyresourcereforge.menu.FreezerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FreezerScreen extends AbstractContainerScreen<FreezerMenu> {
    private static final ResourceLocation BLANK_INVENTORY = ResourceLocation.fromNamespaceAndPath("skyresourcereforge", "textures/gui/blank_inventory.png");

    public FreezerScreen(FreezerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // Draw background
        guiGraphics.blit(BLANK_INVENTORY, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // Draw slot backgrounds
        int inputSlots = this.menu.getInputSlotCount();
        for (int row = 0; row < (inputSlots + 4) / 5; row++) {
            for (int col = 0; col < Math.min(5, inputSlots - row * 5); col++) {
                guiGraphics.blit(BLANK_INVENTORY, x + 52 + col * 18, y + 21 + row * 36, 7, 83, 18, 18);
                guiGraphics.blit(BLANK_INVENTORY, x + 52 + col * 18, y + 39 + row * 36, 7, 83, 18, 18);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (this.imageWidth - this.font.width(this.title)) / 2;
        guiGraphics.drawString(this.font, this.title, x, 6, 0x404040, false);
        guiGraphics.drawString(this.font, Component.literal("Speed: x" + this.menu.getFreezerSpeed()), 100, 60, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, 72, 0x404040, false);
    }
}
