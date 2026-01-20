package com.chouzz.skyresourcereforge.menu;

import com.chouzz.skyresourcereforge.registration.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CasingMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final IItemHandler inventory;

    public CasingMenu(int containerId, Inventory playerInventory, FriendlyByteBuf data) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new ItemStackHandler(18));
    }

    public CasingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, IItemHandler inventory) {
        super(ModMenuTypes.CASING.get(), containerId);
        this.access = access;
        this.inventory = inventory;

        // Casing inventory slots (3x6)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 6; ++j) {
                int slotIndex = j + i * 6;
                if (slotIndex < inventory.getSlots()) {
                    this.addSlot(new SlotItemHandler(inventory, slotIndex, 62 + j * 18, 18 + i * 18));
                }
            }
        }

        // Player inventory slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Player hotbar slots
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            int casingSlots = Math.min(18, inventory.getSlots());

            if (index < casingSlots) {
                // Casing slot
                if (!this.moveItemStackTo(itemstack1, casingSlots, casingSlots + 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Player inventory
                if (!this.moveItemStackTo(itemstack1, 0, casingSlots, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, com.chouzz.skyresourcereforge.registration.ModBlocks.CASING.get());
    }
}
