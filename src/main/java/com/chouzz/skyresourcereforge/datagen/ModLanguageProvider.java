package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, SkyResourceReforge.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.skyresourcereforge.main", "SkyResource Reforge");

        // Knives
        addItem(ModItems.CACTUS_KNIFE, "Cactus Cutting Knife");
        addItem(ModItems.STONE_KNIFE, "Stone Cutting Knife");
        addItem(ModItems.IRON_KNIFE, "Iron Cutting Knife");
        addItem(ModItems.DIAMOND_KNIFE, "Diamond Cutting Knife");

        // Grinders
        addItem(ModItems.STONE_GRINDER, "Stone Rock Grinder");
        addItem(ModItems.IRON_GRINDER, "Iron Rock Grinder");
        addItem(ModItems.DIAMOND_GRINDER, "Diamond Rock Grinder");

        // Misc Items
        addItem(ModItems.WATER_EXTRACTOR, "Water Extractor");
        addItem(ModItems.CACTUS_FRUIT, "Cactus Fruit");
        addItem(ModItems.HEAVY_SNOWBALL, "Heavy Snowball");
        addItem(ModItems.HEAVY_EXPLOSIVE_SNOWBALL, "Explosive Heavy Snowball");
        addItem(ModItems.FLESHY_SNOW_NUGGET, "Fleshy Snow Nugget");
        addItem(ModItems.BASE_COMPONENT, "Base Component");
        addItem(ModItems.TECH_COMPONENT, "Tech Component");

        // Blocks
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
    }
}
