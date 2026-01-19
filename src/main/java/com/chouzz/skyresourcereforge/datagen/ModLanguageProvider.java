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
        
        addBlock(ModBlocks.COMPRESSED_COAL_BLOCK, "Compressed Coal Block");
        addBlock(ModBlocks.COMBUSTION_CONTROLLER, "Combustion Controller");
        addBlock(ModBlocks.CASING, "Casing");
    }
}
