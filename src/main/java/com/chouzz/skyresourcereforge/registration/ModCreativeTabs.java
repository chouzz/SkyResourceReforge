package com.chouzz.skyresourcereforge.registration;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SkyResourceReforge.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.skyresourcereforge.main"))
            .icon(() -> new ItemStack(ModItems.CACTUS_KNIFE.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.CACTUS_KNIFE.get());
                output.accept(ModItems.STONE_KNIFE.get());
                output.accept(ModItems.IRON_KNIFE.get());
                output.accept(ModItems.DIAMOND_KNIFE.get());
                output.accept(ModItems.STONE_GRINDER.get());
                output.accept(ModItems.IRON_GRINDER.get());
                output.accept(ModItems.DIAMOND_GRINDER.get());
                output.accept(ModItems.WATER_EXTRACTOR.get());
                output.accept(ModItems.CACTUS_FRUIT.get());
                output.accept(ModItems.BASE_COMPONENT.get());
                output.accept(ModItems.TECH_COMPONENT.get());
                output.accept(ModBlocks.COMPRESSED_COAL_BLOCK.get());
                output.accept(ModBlocks.SANDY_NETHERRACK.get());
                output.accept(ModBlocks.COAL_INFUSED_BLOCK.get());
                output.accept(ModBlocks.DARK_MATTER_BLOCK.get());
                output.accept(ModBlocks.LIGHT_MATTER_BLOCK.get());
                output.accept(ModBlocks.BLAZE_POWDER_BLOCK.get());
                output.accept(ModBlocks.MAGMAFIED_STONE.get());
                output.accept(ModBlocks.DRY_CACTUS.get());
                output.accept(ModBlocks.CACTUS_FRUIT_NEEDLE.get());
                output.accept(ModBlocks.COMBUSTION_CONTROLLER.get());
                output.accept(ModBlocks.COMBUSTION_COLLECTOR.get());
                output.accept(ModBlocks.ROCK_CRUSHER.get());
                output.accept(ModBlocks.ROCK_CLEANER.get());
                output.accept(ModBlocks.DIRT_FURNACE.get());
                output.accept(ModBlocks.MINI_FREEZER.get());
                output.accept(ModBlocks.IRON_FREEZER.get());
                output.accept(ModBlocks.LIGHT_FREEZER.get());
                output.accept(ModBlocks.AQUEOUS_CONCENTRATOR.get());
                output.accept(ModBlocks.AQUEOUS_DECONCENTRATOR.get());
                output.accept(ModBlocks.HEAVY_SNOW.get());
                output.accept(ModItems.HEAVY_SNOWBALL.get());
                output.accept(ModItems.HEAVY_EXPLOSIVE_SNOWBALL.get());
                output.accept(ModBlocks.CASING.get());
            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
