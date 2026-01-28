package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;
import net.minecraft.data.PackOutput;

/**
 * English language provider.
 * Contains all English translations for SkyResource Reforge.
 */
public class EnglishLanguageProvider extends ModLanguageProvider {

    public EnglishLanguageProvider(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addCreativeTab() {
        add("itemGroup.skyresourcereforge.main", "SkyResource Reforge");
    }

    @Override
    protected void addKnives() {
        addItem(ModItems.CACTUS_KNIFE, "Cactus Cutting Knife");
        addItem(ModItems.STONE_KNIFE, "Stone Cutting Knife");
        addItem(ModItems.IRON_KNIFE, "Iron Cutting Knife");
        addItem(ModItems.DIAMOND_KNIFE, "Diamond Cutting Knife");
    }

    @Override
    protected void addGrinders() {
        addItem(ModItems.STONE_GRINDER, "Stone Rock Grinder");
        addItem(ModItems.IRON_GRINDER, "Iron Rock Grinder");
        addItem(ModItems.DIAMOND_GRINDER, "Diamond Rock Grinder");
    }

    @Override
    protected void addMiscItems() {
        addItem(ModItems.WATER_EXTRACTOR, "Water Extractor");
        addItem(ModItems.NETHER_BRICK_CONDENSER, "Nether Brick Condenser");
        addItem(ModItems.NETHER_BRICK_COMBUSTION_HEATER, "Nether Brick Combustion Heater");
        addItem(ModItems.CACTUS_FRUIT, "Cactus Fruit");
        addItem(ModItems.HEAVY_SNOWBALL, "Heavy Snowball");
        addItem(ModItems.HEAVY_EXPLOSIVE_SNOWBALL, "Explosive Heavy Snowball");
        addItem(ModItems.FLESHY_SNOW_NUGGET, "Fleshy Snow Nugget");
        addItem(ModItems.DARK_MATTER, "Dark Matter");
        addItem(ModItems.LIGHT_MATTER, "Light Matter");
        addItem(ModItems.HEAT_COMPONENT, "Heat Component");
        addItem(ModItems.HEAT_PROVIDER, "Heat Provider");

        String[] baseNames = {
            "Plant Matter",
            "Steel Power Component",
            "Frozen Iron Cooling Component",
            "Enriched Bonemeal",
            "Sawdust",
            "Quartz Amplifier"
        };

        for (int i = 0; i < BASE_COMPONENT_NAMES.length; i++) {
            add("item.skyresourcereforge.base_component." + BASE_COMPONENT_NAMES[i], baseNames[i]);
        }

        String[] techNames = {
            "Stone Crushed",
            "Radioactive Mix",
            "Frozen Iron Ingot",
            "Netherrack Crushed"
        };

        for (int i = 0; i < TECH_COMPONENT_NAMES.length; i++) {
            add("item.skyresourcereforge.tech_component." + TECH_COMPONENT_NAMES[i], techNames[i]);
        }
    }

    @Override
    protected void addBlocks() {
        addBlock(ModBlocks.COMPRESSED_COAL_BLOCK, "Hardened Coal Block");
        addBlock(ModBlocks.COMBUSTION_CONTROLLER, "Smart Combustion Controller");
        addBlock(ModBlocks.COMBUSTION_COLLECTOR, "Combustion Collector");
        addBlock(ModBlocks.ROCK_CRUSHER, "Rock Crusher");
        addBlock(ModBlocks.ROCK_CLEANER, "Rock Cleaner");
        addBlock(ModBlocks.CASING, "Casing");
        addBlock(ModBlocks.SANDY_NETHERRACK, "Sandy Netherrack");
        addBlock(ModBlocks.COAL_INFUSED_BLOCK, "Alchemical Coal Block");
        addBlock(ModBlocks.DARK_MATTER_BLOCK, "Dark Matter Block");
        addBlock(ModBlocks.LIGHT_MATTER_BLOCK, "Light Matter Block");
        addBlock(ModBlocks.BLAZE_POWDER_BLOCK, "Blaze Powder Block");
        addBlock(ModBlocks.MAGMAFIED_STONE, "Magmafied Stone");
        addBlock(ModBlocks.DRY_CACTUS, "Dehydrated Cactus");
        addBlock(ModBlocks.CACTUS_FRUIT_NEEDLE, "Cactus Fruit On A Needle");
        addBlock(ModBlocks.DIRT_FURNACE, "Dirt Furnace");
        addBlock(ModBlocks.MINI_FREEZER, "Mini Freezer");
        addBlock(ModBlocks.IRON_FREEZER, "Iron Freezer");
        addBlock(ModBlocks.LIGHT_FREEZER, "Light Matter Freezer");
        addBlock(ModBlocks.AQUEOUS_CONCENTRATOR, "Aqueous Concentrator");
        addBlock(ModBlocks.AQUEOUS_DECONCENTRATOR, "Aqueous Deconcentrator");
        addBlock(ModBlocks.HEAVY_SNOW, "Heavy Snow");
        addBlock(ModBlocks.PETRIFIED_WOOD, "Petrified Wood");
        addBlock(ModBlocks.PETRIFIED_PLANKS, "Petrified Wood Planks");
        addBlock(ModBlocks.SILVERFISH_DISRUPTOR, "Lepisma Saccharina Teleportation Disruptor");
        addBlock(ModBlocks.CRUCIBLE, "Crucible");
        addBlock(ModBlocks.LIFE_INFUSER, "Life Infuser");
        addBlock(ModBlocks.FUSION_TABLE, "Alchemical Fusion Table");
    }

    @Override
    protected void addOreAlchemicalDusts() {
        for (String oreName : ORE_NAMES) {
            String displayName = capitalizeFirstLetter(oreName) + " Alchemical Ore Dust";
            add("item.skyresourcereforge.ore_alch_dust." + oreName, displayName);
        }
    }

    @Override
    protected void addAlchemyItems() {
        String[] alchemyNames = {
            "Cactus Needle",
            "Crystal Shard",
            "Alchemical Dust I",
            "Alchemical Dust II",
            "Alchemical Dust III",
            "Alchemical Dust IV",
            "Alchemical Coal",
            "Alchemical Gold Ingot",
            "Alchemical Iron Ingot",
            "Alchemical Gold Needle",
            "Alchemical Diamond"
        };

        for (int i = 0; i < ALCHEMY_COMPONENT_NAMES.length; i++) {
            add("item.skyresourcereforge.alchemy_component." + ALCHEMY_COMPONENT_NAMES[i], alchemyNames[i]);
        }
        addItem(ModItems.HEALTH_GEM, "Health Gem");
        addItem(ModItems.ORE_ALCH_DUST, "Alchemical Ore Dust");
        addItem(ModItems.DIRTY_GEM, "Dirty Gem");
    }

    @Override
    protected void addDirtyGems() {
        String[] gemDisplayNames = {
            "Emerald", "Diamond", "Ruby", "Sapphire", "Peridot",
            "Red Garnet", "Yellow Garnet", "Apatite", "Amber", "Onyx",
            "Agate", "Opal", "Amethyst", "Aquamarine", "Heliodor",
            "Morganite", "Beryl", "Indicolite", "Garnet", "Topaz",
            "Iolite", "Chaos", "Dark", "Lapis Lazuli", "Black Quartz", "Certus Quartz",
            "Lepidolite", "Malachite", "Moldavite", "Jasper", "Turquoise",
            "Moonstone", "Carnelian", "Golden Beryl", "Citrine", "Ametrine",
            "Tanzanite", "Violet Sapphire", "Alexandrite", "Blue Topaz",
            "Spinel", "Black Diamond", "Quartz", "Ender Essence"
        };

        for (int i = 0; i < GEM_NAMES.length; i++) {
            add("item.skyresourcereforge.dirty_gem." + GEM_NAMES[i], gemDisplayNames[i] + " Gem");
        }
    }

    @Override
    protected void addHeatVariants() {
        String[] variantDisplayNames = {
            "Wood", "Stone", "Bronze", "Iron", "Steel", "Electrum", "Nether Brick", "Lead",
            "Manyullyn", "Signalum", "End Stone", "Enderium", "Dark Matter", "Light Matter",
            "Osmium", "Refined Obsidian"
        };

        for (int i = 0; i < HEAT_VARIANT_NAMES.length; i++) {
            add("item.skyresourcereforge.heat_component." + HEAT_VARIANT_NAMES[i],
                variantDisplayNames[i] + " Heat Component");
            add("item.skyresourcereforge.heat_provider." + HEAT_VARIANT_NAMES[i],
                variantDisplayNames[i] + " Heat Provider");
        }
    }

    @Override
    protected void addTooltips() {
        add("tooltip.skyresourcereforge.ore_alch_dust.rarity", "Rarity: %s");
        add("tooltip.skyresourcereforge.dirty_gem.rarity", "Rarity: %s");
        add("tooltip.skyresourcereforge.base_component.plant_matter", "Used like bonemeal on crops.");
    }

    @Override
    protected void addJEIDescriptions() {
        add("jei.skyresourcereforge.description.cactus_knife",
            "Obtained by Shift + Right Clicking a Cactus.\n\n Note: You will take damage during this process.");
        add("jei.skyresourcereforge.description.blaze_powder_block",
            "Heat above a heat source to turn into lava.");
        add("jei.skyresourcereforge.description.nether_brick_condenser",
            "Insert into a Casing to run Condenser recipes.");
        add("jei.skyresourcereforge.description.nether_brick_combustion_heater",
            "Insert into a Casing to run Combustion recipes.");
        add("jei.skyresourcereforge.heat_source.value", "%s Heat");
    }

    @Override
    protected void addJEIRecipeCategories() {
        add("jei.skyresourcereforge.recipe.combustion", "Combustion");
        add("jei.skyresourcereforge.recipe.water_extractor_extract", "Water Extractor (Extract)");
        add("jei.skyresourcereforge.recipe.water_extractor_insert", "Water Extractor (Insert)");
        add("jei.skyresourcereforge.recipe.rock_crusher", "Rock Crusher");
        add("jei.skyresourcereforge.recipe.rock_grinder", "Rock Grinder");
        add("jei.skyresourcereforge.recipe.cauldron_clean", "Cauldron Clean");
        add("jei.skyresourcereforge.recipe.freezer", "Freezer");
        add("jei.skyresourcereforge.recipe.fusion", "Fusion");
        add("jei.skyresourcereforge.recipe.infusion", "Infusion");
        add("jei.skyresourcereforge.recipe.condenser", "Condenser");
        add("jei.skyresourcereforge.recipe.crucible", "Crucible");
        add("jei.skyresourcereforge.recipe.heat_sources", "Heat Sources");
        add("jei.skyresourcereforge.recipe.knife", "Knife");
        add("jei.skyresourcereforge.recipe.handheld_rock_grinder", "Handheld Rock Grinder");
    }
}
