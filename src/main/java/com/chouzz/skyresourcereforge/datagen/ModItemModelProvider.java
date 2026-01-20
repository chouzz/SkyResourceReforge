package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SkyResourceReforge.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.CACTUS_KNIFE.get());
        basicItem(ModItems.STONE_KNIFE.get());
        basicItem(ModItems.IRON_KNIFE.get());
        basicItem(ModItems.DIAMOND_KNIFE.get());
        basicItem(ModItems.STONE_GRINDER.get());
        basicItem(ModItems.IRON_GRINDER.get());
        basicItem(ModItems.DIAMOND_GRINDER.get());
        basicItem(ModItems.WATER_EXTRACTOR.get());
        basicItem(ModItems.CACTUS_FRUIT.get());
        basicItem(ModItems.BASE_COMPONENT.get());
        basicItem(ModItems.TECH_COMPONENT.get());
        basicItem(ModItems.HEAVY_SNOWBALL.get());
        basicItem(ModItems.HEAVY_EXPLOSIVE_SNOWBALL.get());
        basicItem(ModItems.FLESHY_SNOW_NUGGET.get());
    }
}
