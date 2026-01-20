package com.chouzz.skyresourcereforge.registration;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.item.ItemKnife;
import com.chouzz.skyresourcereforge.item.ItemRockGrinder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SkyResourceReforge.MODID);

    public static final DeferredItem<Item> CACTUS_KNIFE = ITEMS.register("cactus_knife",
            () -> new ItemKnife(ModTiers.CACTUS, BlockTags.MINEABLE_WITH_AXE, new Item.Properties()));

    public static final DeferredItem<Item> STONE_KNIFE = ITEMS.register("stone_knife",
            () -> new ItemKnife(Tiers.STONE, BlockTags.MINEABLE_WITH_AXE, new Item.Properties()));

    public static final DeferredItem<Item> IRON_KNIFE = ITEMS.register("iron_knife",
            () -> new ItemKnife(Tiers.IRON, BlockTags.MINEABLE_WITH_AXE, new Item.Properties()));

    public static final DeferredItem<Item> DIAMOND_KNIFE = ITEMS.register("diamond_knife",
            () -> new ItemKnife(Tiers.DIAMOND, BlockTags.MINEABLE_WITH_AXE, new Item.Properties()));

    public static final DeferredItem<Item> STONE_GRINDER = ITEMS.register("stone_grinder",
            () -> new ItemRockGrinder(Tiers.STONE, BlockTags.MINEABLE_WITH_PICKAXE, new Item.Properties()));

    public static final DeferredItem<Item> IRON_GRINDER = ITEMS.register("iron_grinder",
            () -> new ItemRockGrinder(Tiers.IRON, BlockTags.MINEABLE_WITH_PICKAXE, new Item.Properties()));

    public static final DeferredItem<Item> DIAMOND_GRINDER = ITEMS.register("diamond_grinder",
            () -> new ItemRockGrinder(Tiers.DIAMOND, BlockTags.MINEABLE_WITH_PICKAXE, new Item.Properties()));

    public static final DeferredItem<Item> WATER_EXTRACTOR = ITEMS.register("water_extractor",
            () -> new com.chouzz.skyresourcereforge.item.WaterExtractorItem(new Item.Properties().component(ModDataComponents.FLUID_CONTENT.get(), SimpleFluidContent.EMPTY)));

    public static final DeferredItem<Item> CACTUS_FRUIT = ITEMS.register("cactus_fruit",
            () -> new com.chouzz.skyresourcereforge.item.CactusFruitItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
