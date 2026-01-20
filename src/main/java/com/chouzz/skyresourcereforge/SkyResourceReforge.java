package com.chouzz.skyresourcereforge;

import com.chouzz.skyresourcereforge.registration.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(SkyResourceReforge.MODID)
public class SkyResourceReforge {
    public static final String MODID = "skyresourcereforge";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SkyResourceReforge(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("SkyResource Reforge Initializing...");
        event.enqueueWork(com.chouzz.skyresourcereforge.heat.HeatSources::registerDefaults);
    }

    private void registerCapabilities(net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent event) {
        event.registerItem(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.ITEM,
                (stack, context) -> new net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack(ModDataComponents.FLUID_CONTENT, stack, com.chouzz.skyresourcereforge.item.WaterExtractorItem.CAPACITY),
                ModItems.WATER_EXTRACTOR.get());
    }
}
