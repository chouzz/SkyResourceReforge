package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/**
 * Abstract base class for language providers.
 * Contains shared data structures and defines template for language-specific implementations.
 */
public abstract class ModLanguageProvider extends LanguageProvider {

    // Shared data: ore names
    protected static final String[] ORE_NAMES = {
        "iron", "gold", "copper", "tin", "silver", "zinc", "nickel", "platinum", "aluminum", "lead",
        "cobalt", "ardite", "osmium", "draconium", "titanium", "tungsten", "chrome", "iridium",
        "boron", "lithium", "magnesium", "mithril", "yellorium", "uranium", "thorium"
    };

    // Shared data: gem names
    protected static final String[] GEM_NAMES = {
        "emerald", "diamond", "ruby", "sapphire", "peridot",
        "red_garnet", "yellow_garnet", "apatite", "amber", "onyx",
        "agate", "opal", "amethyst", "aquamarine", "heliodor",
        "morganite", "beryl", "indicolite", "garnet", "topaz",
        "iolite", "chaos", "dark", "lapis", "quartz_black", "certus_quartz",
        "lepidolite", "malachite", "moldavite", "jasper", "turquoise",
        "moonstone", "carnelian", "golden_beryl", "citrine", "ametrine",
        "tanzanite", "violet_sapphire", "alexandrite", "blue_topaz",
        "spinel", "black_diamond", "quartz", "ender_essence"
    };

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, SkyResourceReforge.MODID, locale);
    }

    @Override
    protected final void addTranslations() {
        // Creative Tab
        addCreativeTab();

        // Knives
        addKnives();

        // Grinders
        addGrinders();

        // Misc Items
        addMiscItems();

        // Blocks
        addBlocks();

        // Ore Alchemical Dusts
        addOreAlchemicalDusts();

        // Alchemy Items
        addAlchemyItems();

        // Dirty Gems
        addDirtyGems();

        // Tooltips
        addTooltips();

        // JEI Descriptions
        addJEIDescriptions();
    }

    // Abstract methods - each language must implement these
    protected abstract void addCreativeTab();
    protected abstract void addKnives();
    protected abstract void addGrinders();
    protected abstract void addMiscItems();
    protected abstract void addBlocks();
    protected abstract void addOreAlchemicalDusts();
    protected abstract void addAlchemyItems();
    protected abstract void addDirtyGems();
    protected abstract void addTooltips();
    protected abstract void addJEIDescriptions();

    // Helper methods for common patterns
    protected String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
