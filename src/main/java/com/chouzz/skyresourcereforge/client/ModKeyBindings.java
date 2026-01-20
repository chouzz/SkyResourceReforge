package com.chouzz.skyresourcereforge.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Key bindings for SkyResourceReforge.
 * Ported from SkyResources ModKeyBindings.
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModKeyBindings {
    // TODO: Add key bindings when needed
    // Example: public static KeyMapping OPEN_GUIDE;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        // Register key mappings here
        // Example: event.register(OPEN_GUIDE);
    }
}
