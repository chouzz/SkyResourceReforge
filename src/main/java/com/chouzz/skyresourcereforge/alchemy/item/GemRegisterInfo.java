package com.chouzz.skyresourcereforge.alchemy.item;

import net.minecraft.world.item.ItemStack;

public class GemRegisterInfo {
    public String name;
    public int color;           // ARGB format
    public float rarity;        // Drop chance (lower = rarer)
    public int gemIndex;        // Damage value index
    public ItemStack parentBlock; // STONE/NETHERRACK/END_STONE
    public String oreOverride;  // Optional (e.g., "crystalCertusQuartz")

    public GemRegisterInfo(String name, int color, float rarity, ItemStack parent) {
        this.name = name;
        this.color = color;
        this.rarity = rarity;
        this.parentBlock = parent;
    }

    public GemRegisterInfo(String name, int color, float rarity, ItemStack parent, String oreOverride) {
        this(name, color, rarity, parent);
        this.oreOverride = oreOverride;
    }
}
