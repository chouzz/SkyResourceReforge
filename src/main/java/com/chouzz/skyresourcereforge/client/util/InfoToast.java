package com.chouzz.skyresourcereforge.client.util;

import net.minecraft.network.chat.Component;

/**
 * Custom toast notification utilities for SkyResourceReforge.
 * Ported from SkyResources InfoToast.
 *
 * Note: The modern toast system requires more complex client-side setup.
 * This class provides factory methods for future toast implementation.
 */
public class InfoToast {
    private static final long DEFAULT_DISPLAY_TIME = 5000L; // 5 seconds

    private InfoToast() {
        // Private constructor - use factory methods
    }

    /**
     * Show an info toast to the player.
     * @param title The title component
     * @param subtitle The subtitle component (optional)
     */
    public static void show(Component title, Component subtitle) {
        // TODO: Implement toast display when client-side GUI system is set up
        // Modern NeoForge uses ToastComponent through Minecraft.getInstance().getToasts()
        // This requires proper client-side initialization
    }

    /**
     * Show an info toast to the player with custom display time.
     * @param title The title component
     * @param subtitle The subtitle component (optional)
     * @param displayTimeMs Display time in milliseconds
     */
    public static void show(Component title, Component subtitle, long displayTimeMs) {
        // TODO: Implement toast display when client-side GUI system is set up
    }
}
