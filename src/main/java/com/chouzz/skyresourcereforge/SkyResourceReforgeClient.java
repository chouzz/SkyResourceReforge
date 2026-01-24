package com.chouzz.skyresourcereforge;

import com.chouzz.skyresourcereforge.alchemy.item.DirtyGemItem;
import com.chouzz.skyresourcereforge.client.screen.*;
import com.chouzz.skyresourcereforge.registration.ModEntities;
import com.chouzz.skyresourcereforge.registration.ModMenuTypes;
import com.chouzz.skyresourcereforge.registration.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = SkyResourceReforge.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = SkyResourceReforge.MODID, value = Dist.CLIENT)
public class SkyResourceReforgeClient {
    public SkyResourceReforgeClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        SkyResourceReforge.LOGGER.info("HELLO FROM CLIENT SETUP");
        SkyResourceReforge.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        ItemProperties.register(ModItems.HEAT_COMPONENT.get(),
                ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "heat_variant"),
                (stack, level, entity, seed) -> com.chouzz.skyresourcereforge.item.HeatComponentItem.getVariantIndex(stack) + 1);

        ItemProperties.register(ModItems.HEAT_PROVIDER.get(),
                ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "heat_variant"),
                (stack, level, entity, seed) -> com.chouzz.skyresourcereforge.item.HeatProviderItem.getVariantIndex(stack) + 1);
    }

    @SubscribeEvent
    static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.DIRT_FURNACE.get(), DirtFurnaceScreen::new);
        event.register(ModMenuTypes.FREEZER.get(), FreezerScreen::new);
        event.register(ModMenuTypes.AQUEOUS_CONCENTRATOR.get(), AqueousConcentratorScreen::new);
        event.register(ModMenuTypes.ROCK_CRUSHER.get(), RockCrusherScreen::new);
        event.register(ModMenuTypes.ROCK_CLEANER.get(), RockCleanerScreen::new);
        event.register(ModMenuTypes.COMBUSTION_COLLECTOR.get(), CombustionCollectorScreen::new);
        event.register(ModMenuTypes.CASING.get(), CasingScreen::new);
    }

    @SubscribeEvent
    static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HEAVY_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.HEAVY_EXPLOSIVE_SNOWBALL.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        // Register dirty gem color handler - dynamic color tinting based on damage value
        event.register((stack, tintIndex) -> {
            int index = DirtyGemItem.getGemIndex(stack);
            if (index < 0 || index >= DirtyGemItem.gemInfos.size()) {
                return -1; // No tint for invalid damage values
            }
            return DirtyGemItem.gemInfos.get(index).color;
        }, ModItems.DIRTY_GEM.get());

        // Register ore alchemical dust color handler - dynamic color tinting based on damage value
        event.register((stack, tintIndex) -> {
            int index = com.chouzz.skyresourcereforge.alchemy.item.ItemOreAlchDust.getDustIndex(stack);
            if (index < 0 || index >= com.chouzz.skyresourcereforge.alchemy.item.ItemOreAlchDust.oreInfos.size()) {
                return -1; // No tint for invalid damage values
            }
            return com.chouzz.skyresourcereforge.alchemy.item.ItemOreAlchDust.oreInfos.get(index).color;
        }, ModItems.ORE_ALCH_DUST.get());
    }
}
