package com.chouzz.skyresourcereforge;

import com.chouzz.skyresourcereforge.registration.*;
import com.chouzz.skyresourcereforge.gametest.RecipeGameTests;
import com.chouzz.skyresourcereforge.util.ItemHelper;
import com.chouzz.skyresourcereforge.item.WaterExtractorItem;
import com.chouzz.skyresourcereforge.block.entity.AqueousConcentratorBlockEntity;
import com.chouzz.skyresourcereforge.block.entity.RockCleanerBlockEntity;
import com.chouzz.skyresourcereforge.block.entity.FluidDropperBlockEntity;
import com.chouzz.skyresourcereforge.alchemy.block.entity.CrucibleBlockEntity;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
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

    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("SkyResource Reforge Initializing...");
        event.enqueueWork(() -> {
            ItemHelper.init();
            // JEI plugin uses @JeiPlugin annotation for auto-registration
        });
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM,
                (stack, context) -> new FluidHandlerItemStack(ModDataComponents.FLUID_CONTENT, stack, WaterExtractorItem.CAPACITY),
                ModItems.WATER_EXTRACTOR.get());

        event.registerBlock(Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, side) -> be instanceof AqueousConcentratorBlockEntity ace ? ace : null,
                ModBlocks.AQUEOUS_CONCENTRATOR.get(),
                ModBlocks.AQUEOUS_DECONCENTRATOR.get());

        event.registerBlock(Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, side) -> be instanceof RockCleanerBlockEntity rce ? rce.getTank() : null,
                ModBlocks.ROCK_CLEANER.get());

        event.registerBlock(Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, side) -> be instanceof FluidDropperBlockEntity fde ? fde.getTank() : null,
                ModBlocks.FLUID_DROPPER.get());

        event.registerBlock(Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, side) -> be instanceof CrucibleBlockEntity cbe ? cbe.getTank() : null,
                ModBlocks.CRUCIBLE.get());

        event.registerBlock(Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) -> be instanceof com.chouzz.skyresourcereforge.block.entity.CrucibleInserterBlockEntity cie ? cie.getInventory() : null,
                ModBlocks.CRUCIBLE_INSERTER.get());

        event.registerBlock(Capabilities.FluidHandler.BLOCK,
                (level, pos, state, be, side) -> be instanceof com.chouzz.skyresourcereforge.block.entity.WildlifeAttractorBlockEntity wae ? wae.getTank() : null,
                ModBlocks.WILDLIFE_ATTRACTOR.get());

        event.registerBlock(Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) -> be instanceof com.chouzz.skyresourcereforge.block.entity.WildlifeAttractorBlockEntity wae ? wae.getInventory() : null,
                ModBlocks.WILDLIFE_ATTRACTOR.get());
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
