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

    public static final DeferredItem<Item> BASE_COMPONENT = ITEMS.register("base_component",
            () -> new com.chouzz.skyresourcereforge.item.BaseComponentItem(new Item.Properties()));

    public static final DeferredItem<Item> TECH_COMPONENT = ITEMS.register("tech_component",
            () -> new com.chouzz.skyresourcereforge.item.TechComponentItem(new Item.Properties()));

    public static final DeferredItem<Item> HEAVY_SNOWBALL = ITEMS.register("heavy_snowball",
            () -> new com.chouzz.skyresourcereforge.item.HeavySnowballItem(new Item.Properties().stacksTo(8), false));

    public static final DeferredItem<Item> HEAVY_EXPLOSIVE_SNOWBALL = ITEMS.register("heavy_explosive_snowball",
            () -> new com.chouzz.skyresourcereforge.item.HeavySnowballItem(new Item.Properties().stacksTo(8), true));

    public static final DeferredItem<Item> FLESHY_SNOW_NUGGET = ITEMS.register("fleshy_snow_nugget",
            () -> new com.chouzz.skyresourcereforge.item.FleshySnowNuggetItem(new Item.Properties()));

    // Alchemy Items
    public static final DeferredItem<Item> ALCHEMY_COMPONENT = ITEMS.register("alchemy_component",
            () -> new com.chouzz.skyresourcereforge.alchemy.item.AlchemyComponentItem(0, new Item.Properties()));

    public static final DeferredItem<Item> INFUSION_STONE_SANDSTONE = ITEMS.register("infusion_stone_sandstone",
            () -> new com.chouzz.skyresourcereforge.alchemy.item.InfusionStoneItem(100, new Item.Properties().durability(100)));

    public static final DeferredItem<Item> INFUSION_STONE_RED_SANDSTONE = ITEMS.register("infusion_stone_red_sandstone",
            () -> new com.chouzz.skyresourcereforge.alchemy.item.InfusionStoneItem(150, new Item.Properties().durability(150)));

    public static final DeferredItem<Item> INFUSION_STONE_ALCHEMICAL = ITEMS.register("infusion_stone_alchemical",
            () -> new com.chouzz.skyresourcereforge.alchemy.item.InfusionStoneItem(300, new Item.Properties().durability(300)));

    public static final DeferredItem<Item> HEALTH_GEM = ITEMS.register("health_gem",
            () -> new com.chouzz.skyresourcereforge.alchemy.item.HealthGemItem(4, new Item.Properties()));

    public static final DeferredItem<Item> ORE_ALCH_DUST = ITEMS.register("ore_alch_dust",
            () -> new com.chouzz.skyresourcereforge.alchemy.item.ItemOreAlchDust(new Item.Properties()));

    // Additional Items
    public static final DeferredItem<Item> SURVIVALIST_FISHING_ROD = ITEMS.register("survivalist_fishing_rod",
            () -> new com.chouzz.skyresourcereforge.item.SurvivalistFishingRodItem());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
