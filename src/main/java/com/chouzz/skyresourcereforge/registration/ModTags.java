package com.chouzz.skyresourcereforge.registration;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModTags {
    public static final TagKey<Item> STEEL_POWER_COMPONENT_MATERIALS = itemTag("steel_power_component_materials");
    public static final TagKey<Item> BASIC_CIRCUIT = itemTag("basic_circuit");
    public static final TagKey<Item> COAL_DUST = itemTag("coal_dust");

    private ModTags() {
    }

    public static TagKey<Item> itemTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, path));
    }

    public static TagKey<Item> commonItemTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
