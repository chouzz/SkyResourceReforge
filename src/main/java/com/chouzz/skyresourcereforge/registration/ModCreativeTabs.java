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
                output.accept(ModBlocks.COMPRESSED_COAL_BLOCK.get());
                output.accept(ModBlocks.SANDY_NETHERRACK.get());
                output.accept(ModBlocks.COAL_INFUSED_BLOCK.get());
                output.accept(ModBlocks.DARK_MATTER_BLOCK.get());
                output.accept(ModBlocks.LIGHT_MATTER_BLOCK.get());
                output.accept(ModBlocks.BLAZE_POWDER_BLOCK.get());
                output.accept(ModBlocks.MAGMAFIED_STONE.get());
                output.accept(ModBlocks.DRY_CACTUS.get());
                output.accept(ModBlocks.COMBUSTION_CONTROLLER.get());
                output.accept(ModBlocks.CASING.get());
            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
