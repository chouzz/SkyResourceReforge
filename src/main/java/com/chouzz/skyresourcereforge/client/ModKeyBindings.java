package com.chouzz.skyresourcereforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

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
