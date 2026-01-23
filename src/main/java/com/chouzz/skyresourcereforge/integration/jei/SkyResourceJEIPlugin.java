package com.chouzz.skyresourcereforge.integration.jei;

import com.chouzz.skyresourcereforge.SkyResourceReforge;

/**
 * JEI Plugin for SkyResourceReforge
 *
 * This class is a stub for future JEI integration. For now, multi-subtype items
 * like ore alchemical dust should work correctly with JEI due to proper
 * getDescriptionId implementation and damage value handling.
 *
 * When JEI is added as a dependency, this can be expanded to include:
 * - Recipe categories for various crafting processes
 * - Custom ingredient renderers
 * - Advanced subtype handling
 */
public class SkyResourceJEIPlugin {

    /**
     * Initialize JEI integration if JEI is loaded
     * Currently just logs that JEI integration is ready for future implementation
     */
    public static void init() {
        // Check if JEI is loaded and log status
        try {
            Class.forName("mezz.jei.api.JeiPlugin");
            SkyResourceReforge.LOGGER.info("JEI detected - multi-subtype items should display correctly");
        } catch (ClassNotFoundException e) {
            SkyResourceReforge.LOGGER.info("JEI not detected - items will still work normally");
        }
    }
}
