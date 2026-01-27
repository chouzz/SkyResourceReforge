package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.registration.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output,
                               CompletableFuture<HolderLookup.Provider> registries,
                               CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
                               ExistingFileHelper existingFileHelper) {
        super(output, registries, blockTags, SkyResourceReforge.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.STEEL_POWER_COMPONENT_MATERIALS)
            .add(Items.IRON_INGOT)
            .addOptionalTag(cLocation("ingots/steel"))
            .addOptionalTag(cLocation("ingots/electrical_steel"));

        tag(ModTags.BASIC_CIRCUIT)
            .add(Items.REDSTONE_BLOCK)
            .addOptionalTag(cLocation("circuits/basic"));

        tag(ModTags.COAL_DUST)
            .add(Items.REDSTONE)
            .addOptionalTag(cLocation("dusts/coal"))
            .addOptionalTag(cLocation("dusts/redstone"));
    }

    private static ResourceLocation cLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
}
