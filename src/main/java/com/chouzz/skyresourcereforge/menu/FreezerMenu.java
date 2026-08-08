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
    /** Number of input (and output) slots — always 1 for Freezer. */
    public static final int INPUT_SLOT_COUNT = 1;

    private final ContainerLevelAccess access;
    private final ContainerData data;

    public FreezerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf data) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new ItemStackHandler(INPUT_SLOT_COUNT * 2), new SimpleContainerData(1));
    }

    public FreezerMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, IItemHandler inventory, ContainerData data) {
        super(ModMenuTypes.FREEZER.get(), containerId);
        this.access = access;
        this.data = data;

        // Tile inventory slots: fixed 1 input + 1 output layout
        this.addSlot(new SlotItemHandler(inventory, 0, 53, 22));
        this.addSlot(new SlotItemHandler(inventory, 1, 53, 40) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

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

            int tileSlots = INPUT_SLOT_COUNT * 2;
            int playerSlotEnd = tileSlots + 36;

            if (index >= INPUT_SLOT_COUNT && index < tileSlots) {
                // Output slot
                if (!this.moveItemStackTo(itemstack1, tileSlots, playerSlotEnd, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index < INPUT_SLOT_COUNT) {
                // Input slot
                if (!this.moveItemStackTo(itemstack1, tileSlots, playerSlotEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, tileSlots, false)) {
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

    public int getInputSlotCount() {
        return INPUT_SLOT_COUNT;
    }
}
