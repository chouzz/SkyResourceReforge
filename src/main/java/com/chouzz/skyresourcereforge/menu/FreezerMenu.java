package com.chouzz.skyresourcereforge.menu;

import com.chouzz.skyresourcereforge.block.entity.FreezerBlockEntity;
import com.chouzz.skyresourcereforge.registration.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FreezerMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final ContainerData data;
    private final IItemHandler inventory;

    public FreezerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf data) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new ItemStackHandler(2), new SimpleContainerData(1));
    }

    public FreezerMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, IItemHandler inventory, ContainerData data) {
        super(ModMenuTypes.FREEZER.get(), containerId);
        this.access = access;
        this.inventory = inventory;
        this.data = data;

        int inputSlots = inventory.getSlots() / 2;

        // Tile inventory slots
        int y = 0;
        for (int row = 0; row < (inputSlots + 4) / 5; row++) {
            for (int col = 0; col < Math.min(5, inputSlots - row * 5); col++) {
                int slotIndex = row * 5 + col;
                if (slotIndex < inputSlots) {
                    // Input slot
                    this.addSlot(new SlotItemHandler(inventory, slotIndex, 53 + col * 18, 22 + row * 36));
                    // Output slot
                    this.addSlot(new SlotItemHandler(inventory, slotIndex + inputSlots, 53 + col * 18, 40 + row * 36) {
                        @Override
                        public boolean mayPlace(ItemStack stack) {
                            return false;
                        }
                    });
                }
                y = row;
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

        this.addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            int inputSlots = inventory.getSlots() / 2;

            int playerSlotEnd = inputSlots * 2 + 36;

            if (index >= inputSlots && index < inputSlots * 2) {
                // Output slot
                if (!this.moveItemStackTo(itemstack1, inputSlots * 2, playerSlotEnd, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index < inputSlots) {
                // Input slot
                if (!this.moveItemStackTo(itemstack1, inputSlots * 2, playerSlotEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, inputSlots * 2, false)) {
                return ItemStack.EMPTY;
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
        return stillValid(this.access, player, com.chouzz.skyresourcereforge.registration.ModBlocks.MINI_FREEZER.get());
    }

    public float getFreezerSpeed() {
        return this.data.get(0) / 100.0f;
    }
}
