package com.chouzz.skyresourcereforge;

import com.chouzz.skyresourcereforge.registration.*;
import com.chouzz.skyresourcereforge.gametest.RecipeGameTests;
import com.chouzz.skyresourcereforge.util.ItemHelper;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import org.slf4j.Logger;

@Mod(SkyResourceReforge.MODID)
public class SkyResourceReforge {
    public static final String MODID = "skyresourcereforge";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SkyResourceReforge(IEventBus modEventBus, ModContainer modContainer) {
        // Initialize data BEFORE registering creative tabs
        com.chouzz.skyresourcereforge.heat.HeatSources.registerDefaults();
        com.chouzz.skyresourcereforge.alchemy.item.ItemOreAlchDust.init();
        com.chouzz.skyresourcereforge.alchemy.item.DirtyGemItem.initGems();
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerItemHelpers);
        modEventBus.addListener(this::registerGameTests);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        // ModFluids.register(modEventBus); // TODO: Implement in future phase

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("SkyResource Reforge Initializing...");
        event.enqueueWork(() -> {
            ItemHelper.init();
            // JEI plugin uses @JeiPlugin annotation for auto-registration
        });
    }

    private void registerCapabilities(net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent event) {
        event.registerItem(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.ITEM,
                (stack, context) -> new net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack(ModDataComponents.FLUID_CONTENT, stack, com.chouzz.skyresourcereforge.item.WaterExtractorItem.CAPACITY),
                ModItems.WATER_EXTRACTOR.get());
    }

    private void registerItemHelpers(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Already initialized in constructor
        });
    }

    private void registerGameTests(RegisterGameTestsEvent event) {
        LOGGER.info("Registering SkyResourceReforge GameTests");
        event.register(RecipeGameTests.class);
    }
}
