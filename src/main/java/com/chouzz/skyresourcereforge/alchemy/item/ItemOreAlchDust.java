package com.chouzz.skyresourcereforge.alchemy.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chouzz.skyresourcereforge.registration.ModDataComponents;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Blocks;

public class ItemOreAlchDust extends Item {

    public static List<OreRegisterInfo> oreInfos = new ArrayList<>();
    private static ArrayList<String> names = new ArrayList<>();
    private static ItemOreAlchDust Instance;

    public ItemOreAlchDust(Properties properties) {
        super(properties);
        Instance = this;
        itemList();
    }

    public static Map<String, Integer> defaultOreRarities() {
        Map<String, Integer> map = new HashMap<>();
        map.put("iron", 3);
        map.put("gold", 5);
        map.put("copper", 1);
        map.put("tin", 3);
        map.put("silver", 4);
        map.put("zinc", 2);
        map.put("nickel", 5);
        map.put("platinum", 7);
        map.put("aluminum", 4);
        map.put("lead", 4);
        map.put("cobalt", 6);
        map.put("ardite", 6);
        map.put("osmium", 6);
        map.put("draconium", 9);
        map.put("titanium", 6);
        map.put("tungsten", 6);
        map.put("chrome", 8);
        map.put("iridium", 11);
        map.put("boron", 5);
        map.put("lithium", 7);
        map.put("magnesium", 5);
        map.put("mithril", 9);
        map.put("yellorium", 6);
        map.put("uranium", 6);
        map.put("thorium", 7);

        return map;
    }

    public static void init() {
        addOreInfo("iron", 0xFFCC0000); // 0
        addOreInfo("gold", 0xFFCCCC00); // 1
        addOreInfo("copper", 0xFFFF6600); // 2
        addOreInfo("tin", 0xFFBFBFBF); // 3
        addOreInfo("silver", 0xFFD1F4FF); // 4
        addOreInfo("zinc", 0xFFFFF7C2); // 5
        addOreInfo("nickel", 0xFFFAF191); // 6
        addOreInfo("platinum", 0xFF44EAFC); // 7
        addOreInfo("aluminum", 0xFFF5FFFD); // 8
        addOreInfo("lead", 0xFF5B2EFF); // 9
        addOreInfo("cobalt", 0xFF0045D9, new ItemStack(Blocks.NETHERRACK)); // 10
        addOreInfo("ardite", 0xFFDE9000, new ItemStack(Blocks.NETHERRACK)); // 11
        addOreInfo("osmium", 0xFF7F13C2); // 12
        addOreInfo("draconium", 0xFF9E6DCF, new ItemStack(Blocks.END_STONE), false); // 13
        addOreInfo("titanium", 0xFFBABABA); // 14
        addOreInfo("tungsten", 0xFF464659, new ItemStack(Blocks.END_STONE)); // 15
        addOreInfo("chrome", 0xFFD6D6D6); // 16
        addOreInfo("iridium", 0xFFE3E3E3); // 17
        addOreInfo("boron", 0xFF9E9E9E); // 18
        addOreInfo("lithium", 0xFFF2F2F2); // 19
        addOreInfo("magnesium", 0xFFFFD4D4); // 20
        addOreInfo("mithril", 0xFF45BCCC); // 21
        addOreInfo("yellorium", 0xFFFFFF2B, new ItemStack(Blocks.STONE), false); // 22
        addOreInfo("uranium", 0xFF16BA00, new ItemStack(Blocks.STONE), false); // 23
        addOreInfo("thorium", 0xFF2B4010, new ItemStack(Blocks.STONE), false); // 24
    }

    public static void addOreInfo(String name, int color) {
        addOreInfo(name, color, new ItemStack(Blocks.STONE));
    }

    public static void addOreInfo(String name, int color, ItemStack block) {
        addOreInfo(name, color, block, true);
    }

    public static void addOreInfo(String name, int color, ItemStack block, boolean autoAdd) {
        Map<String, Integer> rarities = defaultOreRarities();
        int rarity = rarities.getOrDefault(name, 5);
        oreInfos.add(new OreRegisterInfo(name, color, rarity, oreInfos.size(), block, autoAdd));
    }

    public static OreRegisterInfo getFluidInfo(int index) {
        for (OreRegisterInfo f : oreInfos) {
            if (f.dustIndex == index) {
                return f;
            }
        }
        return null;
    }

    public static int getDustIndex(ItemStack stack) {
        Integer index = stack.get(ModDataComponents.ORE_ALCH_DUST_INDEX.get());
        if (index != null) {
            return index;
        }
        return stack.getDamageValue();
    }

    public static void setDustIndex(ItemStack stack, int index) {
        stack.set(ModDataComponents.ORE_ALCH_DUST_INDEX.get(), index);
    }

    private void itemList() {
        if (names.size() == 0) {
            for (int i = 0; i < oreInfos.size(); i++) {
                names.add(oreInfos.get(i).name);
            }
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        int index = getDustIndex(stack);
        if (index >= 0 && index < names.size()) {
            return Component.translatable("item.skyresourcereforge.ore_alch_dust." + names.get(index));
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, java.util.List<Component> tooltip, TooltipFlag flag) {
        int index = getDustIndex(stack);
        if (index >= 0 && index < oreInfos.size()) {
            OreRegisterInfo info = oreInfos.get(index);
            tooltip.add(Component.translatable("tooltip.skyresourcereforge.ore_alch_dust.rarity", info.rarity));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }


    public static ItemStack getStack(String name) {
        int index = names.indexOf(name);
        if (index >= 0) {
            ItemStack stack = new ItemStack(Instance, 1);
            setDustIndex(stack, index);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public static ArrayList<String> getNames() {
        return names;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        int index = getDustIndex(stack);
        if (index >= 0 && index < names.size()) {
            return "item.skyresourcereforge.ore_alch_dust." + names.get(index);
        }
        return super.getDescriptionId(stack);
    }
}