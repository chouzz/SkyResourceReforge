package com.chouzz.skyresourcereforge.registration;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.item.ItemKnife;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SkyResourceReforge.MODID);

    public static final DeferredItem<Item> CACTUS_KNIFE = ITEMS.register("cactus_knife",
            () -> new ItemKnife(ModTiers.CACTUS, BlockTags.MINEABLE_WITH_AXE, new Item.Properties()));

    public static final DeferredItem<Item> WATER_EXTRACTOR = ITEMS.register("water_extractor",
            () -> new com.chouzz.skyresourcereforge.item.WaterExtractorItem(new Item.Properties().component(ModDataComponents.FLUID_CONTENT.get(), SimpleFluidContent.EMPTY)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
