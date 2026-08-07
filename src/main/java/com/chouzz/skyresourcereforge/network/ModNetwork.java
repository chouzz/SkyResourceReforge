package com.chouzz.skyresourcereforge.network;

/**
 * Modern NeoForge networking system for SkyResourceReforge
 *
 * This class handles packet registration and provides a channel for
 * client-to-server communication.
 */
public class ModNetwork {
    /**
     * Register all network packets with NeoForge's modern networking system.
     * Call this during FMLCommonSetupEvent.
     */
    public static void register() {
        // Packets are registered via CustomPacketPayload.Type
        // Server-bound packets are handled by IPayloadHandler
        // Registration is done in SkyResourceReforge.java
    }
}
