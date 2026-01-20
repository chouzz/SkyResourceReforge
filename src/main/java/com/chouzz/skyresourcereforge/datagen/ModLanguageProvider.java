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
        
        addItem(ModItems.CACTUS_KNIFE, "Cactus Knife");
        addItem(ModItems.STONE_KNIFE, "Stone Knife");
        addItem(ModItems.IRON_KNIFE, "Iron Knife");
        addItem(ModItems.DIAMOND_KNIFE, "Diamond Knife");
        addItem(ModItems.STONE_GRINDER, "Stone Grinder");
        addItem(ModItems.IRON_GRINDER, "Iron Grinder");
        addItem(ModItems.DIAMOND_GRINDER, "Diamond Grinder");
        addItem(ModItems.WATER_EXTRACTOR, "Water Extractor");
        
        addBlock(ModBlocks.COMPRESSED_COAL_BLOCK, "Compressed Coal Block");
        addBlock(ModBlocks.COMBUSTION_CONTROLLER, "Combustion Controller");
        addBlock(ModBlocks.CASING, "Casing");
        addBlock(ModBlocks.SANDY_NETHERRACK, "Sandy Netherrack");
        addBlock(ModBlocks.COAL_INFUSED_BLOCK, "Coal Infused Block");
        addBlock(ModBlocks.DARK_MATTER_BLOCK, "Dark Matter Block");
        addBlock(ModBlocks.LIGHT_MATTER_BLOCK, "Light Matter Block");
        addBlock(ModBlocks.BLAZE_POWDER_BLOCK, "Blaze Powder Block");
        addBlock(ModBlocks.MAGMAFIED_STONE, "Magmafied Stone");
        addBlock(ModBlocks.DRY_CACTUS, "Dry Cactus");
        addBlock(ModBlocks.CACTUS_FRUIT_NEEDLE, "Cactus Fruit Needle");
        addItem(ModItems.CACTUS_FRUIT, "Cactus Fruit");
    }
}
