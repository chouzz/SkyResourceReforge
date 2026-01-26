package com.chouzz.skyresourcereforge.alchemy.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.chouzz.skyresourcereforge.registration.ModDataComponents;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Blocks;

public class DirtyGemItem extends Item {

    public static List<GemRegisterInfo> gemInfos = new ArrayList<>();
    private static ArrayList<String> names = new ArrayList<>();
    private static DirtyGemItem instance;

    public DirtyGemItem(Properties properties) {
        super(properties);
        instance = this;
    }

    public static void initGems() {
        if (!gemInfos.isEmpty()) {
            return; // Already initialized
        }

        // STONE parent gems (25 variants)
        addGem("emerald", 0xFF12DB3A, 0.015F, Blocks.STONE);
        addGem("diamond", 0xFF6BFFFD, 0.033F, Blocks.STONE);
        addGem("ruby", 0xFFFA1E1E, 0.015F, Blocks.STONE);
        addGem("sapphire", 0xFF1E46FA, 0.015F, Blocks.STONE);
        addGem("peridot", 0xFF1CB800, 0.015F, Blocks.STONE);
        addGem("red_garnet", 0xFFC90014, 0.015F, Blocks.STONE);
        addGem("yellow_garnet", 0xFFF7FF0F, 0.015F, Blocks.STONE);
        addGem("apatite", 0xFF2B95FF, 0.600F, Blocks.STONE);
        addGem("amber", 0xFFF5CC53, 0.021F, Blocks.STONE);
        addGem("onyx", 0xFF3D3D3D, 0.021F, Blocks.STONE);
        addGem("agate", 0xFFFF63FF, 0.021F, Blocks.STONE);
        addGem("opal", 0xFFDEDEDE, 0.021F, Blocks.STONE);
        addGem("amethyst", 0xFF780078, 0.018F, Blocks.STONE);
        addGem("aquamarine", 0xFF36E7FF, 0.018F, Blocks.STONE);
        addGem("heliodor", 0xFFFFFF7D, 0.018F, Blocks.STONE);
        addGem("morganite", 0xFFFA61FF, 0.018F, Blocks.STONE);
        addGem("beryl", 0xFF46E334, 0.015F, Blocks.STONE);
        addGem("indicolite", 0xFF39E6BD, 0.015F, Blocks.STONE);
        addGem("garnet", 0xFFFF9999, 0.015F, Blocks.STONE);
        addGem("topaz", 0xFFFFD399, 0.015F, Blocks.STONE);
        addGem("iolite", 0xFF9502CF, 0.012F, Blocks.STONE);
        addGem("chaos", 0xFFFFE6FB, 0.009F, Blocks.STONE);
        addGem("dark", 0xFF242424, 0.27F, Blocks.STONE);
        addGem("lapis", 0xFF075BBA, 0.54F, Blocks.STONE);
        addGem("quartz_black", 0xFF171717, 0.36F, Blocks.STONE);
        addGem("certus_quartz", 0xFFB0F4F7, 0.48F, Blocks.STONE, "crystalCertusQuartz");

        // NETHERRACK parent gems (18 variants)
        addGem("lepidolite", 0xFF57008A, 0.021F, Blocks.NETHERRACK);
        addGem("malachite", 0xFF23AD00, 0.021F, Blocks.NETHERRACK);
        addGem("moldavite", 0xFFADFF99, 0.021F, Blocks.NETHERRACK);
        addGem("jasper", 0xFF874800, 0.018F, Blocks.NETHERRACK);
        addGem("turquoise", 0xFF2EF2C8, 0.018F, Blocks.NETHERRACK);
        addGem("moonstone", 0xFF016A8A, 0.018F, Blocks.NETHERRACK);
        addGem("carnelian", 0xFF630606, 0.018F, Blocks.NETHERRACK);
        addGem("golden_beryl", 0xFFD6AE2B, 0.015F, Blocks.NETHERRACK);
        addGem("citrine", 0xFF871616, 0.015F, Blocks.NETHERRACK);
        addGem("ametrine", 0xFFA300BF, 0.015F, Blocks.NETHERRACK);
        addGem("tanzanite", 0xFF00076E, 0.015F, Blocks.NETHERRACK);
        addGem("violet_sapphire", 0xFF451287, 0.012F, Blocks.NETHERRACK);
        addGem("alexandrite", 0xFFE3E3E3, 0.012F, Blocks.NETHERRACK);
        addGem("blue_topaz", 0xFF1000C4, 0.012F, Blocks.NETHERRACK);
        addGem("spinel", 0xFF750000, 0.012F, Blocks.NETHERRACK);
        addGem("black_diamond", 0xFF262626, 0.009F, Blocks.NETHERRACK);
        addGem("quartz", 0xFFFFFFFF, 0.42F, Blocks.NETHERRACK);

        // END_STONE parent gems (1 variant)
        addGem("ender_essence", 0xFF356E19, 0.009F, Blocks.END_STONE);
    }

    public static void addGem(String name, int color, float rarity, net.minecraft.world.level.block.Block parentBlock) {
        addGem(name, color, rarity, parentBlock, null);
    }

    public static void addGem(String name, int color, float rarity, net.minecraft.world.level.block.Block parentBlock, String oreOverride) {
        gemInfos.add(new GemRegisterInfo(name, color, rarity, new ItemStack(parentBlock), oreOverride));
    }

    public static GemRegisterInfo getGemInfo(int index) {
        if (index < 0 || index >= gemInfos.size()) {
            return null;
        }
        return gemInfos.get(index);
    }

    public static int getGemIndex(ItemStack stack) {
        Integer index = stack.get(ModDataComponents.DIRTY_GEM_INDEX.get());
        if (index != null) {
            return index;
        }
        return stack.getDamageValue();
    }

    public static void setGemIndex(ItemStack stack, int index) {
        stack.set(ModDataComponents.DIRTY_GEM_INDEX.get(), index);
    }

    private static void ensureNamesInitialized() {
        if (names.isEmpty()) {
            for (GemRegisterInfo gem : gemInfos) {
                names.add(gem.name);
            }
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        ensureNamesInitialized();
        int index = getGemIndex(stack);
        if (index >= 0 && index < names.size()) {
            return Component.translatable("item.skyresourcereforge.dirty_gem." + names.get(index));
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        int index = getGemIndex(stack);
        if (index >= 0 && index < gemInfos.size()) {
            GemRegisterInfo info = gemInfos.get(index);
            tooltip.add(Component.translatable("tooltip.skyresourcereforge.dirty_gem.rarity",
                    String.format("%.2f%%", info.rarity * 100)));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }

    public static ItemStack getStack(String name) {
        ensureNamesInitialized();
        int index = names.indexOf(name);
        if (index >= 0) {
            ItemStack stack = new ItemStack(instance, 1);
            setGemIndex(stack, index);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public static ArrayList<String> getNames() {
        ensureNamesInitialized();
        return names;
    }

    public static boolean isDirtyGem(ItemStack stack) {
        return stack.getItem() instanceof DirtyGemItem;
    }

    public static List<ItemStack> getCleanGemOutputs(int index) {
        GemRegisterInfo info = getGemInfo(index);
        if (info == null) {
            return List.of();
        }
        return getCleanGemOutputs(info);
    }

    public static ItemStack getPreferredCleanGemOutput(int index) {
        List<ItemStack> outputs = getCleanGemOutputs(index);
        if (outputs.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return outputs.get(0).copy();
    }

    private static List<ItemStack> getCleanGemOutputs(GemRegisterInfo info) {
        List<ItemStack> outputs = new ArrayList<>();
        for (TagKey<Item> tagKey : getGemTags(info)) {
            Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM.getTag(tagKey);
            if (tag.isEmpty()) {
                continue;
            }
            for (var holder : tag.get()) {
                outputs.add(new ItemStack(holder.value()));
            }
        }
        return outputs;
    }

    private static List<TagKey<Item>> getGemTags(GemRegisterInfo info) {
        List<TagKey<Item>> tags = new ArrayList<>();
        if (info.oreOverride != null && !info.oreOverride.isBlank()) {
            if ("crystalCertusQuartz".equals(info.oreOverride)) {
                tags.add(cTag("crystals/certus_quartz"));
                tags.add(cTag("gems/certus_quartz"));
            }
        } else {
            tags.add(cTag("gems/" + info.name));
            if ("lapis".equals(info.name)) {
                tags.add(cTag("gems/lapis_lazuli"));
            }
            if ("quartz".equals(info.name)) {
                tags.add(cTag("gems/nether_quartz"));
            }
            if ("quartz_black".equals(info.name)) {
                tags.add(cTag("gems/black_quartz"));
            }
        }
        return tags;
    }

    private static TagKey<Item> cTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
