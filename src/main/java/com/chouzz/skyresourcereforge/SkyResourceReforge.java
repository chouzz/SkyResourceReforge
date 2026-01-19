package com.chouzz.skyresourcereforge;

import com.chouzz.skyresourcereforge.datagen.ModBlockStateProvider;
import com.chouzz.skyresourcereforge.datagen.ModItemModelProvider;
import com.chouzz.skyresourcereforge.registration.ModBlockEntities;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModCreativeTabs;
import com.chouzz.skyresourcereforge.registration.ModItems;
import com.chouzz.skyresourcereforge.registration.ModRecipeTypes;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

@Mod(SkyResourceReforge.MODID)
public class SkyResourceReforge {
    public static final String MODID = "skyresourcereforge";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SkyResourceReforge(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModRecipeTypes.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("SkyResource Reforge Initializing...");
    }

    private void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
    }
}
