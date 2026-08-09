package com.chouzz.skyresourcereforge.heat;

import java.util.List;

public final class HeatVariants {
    public static final List<String> NAMES = List.of(
            "wood",
            "stone",
            "bronze",
            "iron",
            "steel",
            "electrum",
            "netherbrick",
            "lead",
            "manyullyn",
            "signalum",
            "endstone",
            "enderium",
            "darkmatter",
            "lightmatter",
            "osmium",
            "refinedobsidian"
    );

    /** Named index constants — single source of truth for variant positions. */
    public static final int WOOD = 0;
    public static final int STONE = 1;
    public static final int BRONZE = 2;
    public static final int IRON = 3;
    public static final int STEEL = 4;
    public static final int ELECTRUM = 5;
    public static final int NETHER_BRICK = 6;
    public static final int LEAD = 7;
    public static final int MANYULLYN = 8;
    public static final int SIGNALUM = 9;
    public static final int END_STONE = 10;
    public static final int ENDERIUM = 11;
    public static final int DARK_MATTER = 12;
    public static final int LIGHT_MATTER = 13;
    public static final int OSMIUM = 14;
    public static final int REFINED_OBSIDIAN = 15;

    public static final List<Integer> HEAT_VALUES = List.of(
            100, 600, 950, 1538, 1370, 1878, 3072, 328,
            2324, 1362, 2164, 3166, 4042, 1566, 3033, 3768
    );

    static {
        if (NAMES.size() != HEAT_VALUES.size()) {
            throw new IllegalStateException(
                "HeatVariants NAMES and HEAT_VALUES lists must be the same size: "
                + NAMES.size() + " names vs " + HEAT_VALUES.size() + " values");
        }
    }

    private HeatVariants() {
    }

    public static int size() {
        return NAMES.size();
    }

    public static String getName(int index) {
        if (index < 0 || index >= NAMES.size()) {
            return "unknown";
        }
        return NAMES.get(index);
    }

    public static int getHeat(int index) {
        if (index < 0 || index >= HEAT_VALUES.size()) {
            throw new IndexOutOfBoundsException(
                "Heat variant index out of range: " + index + " (size: " + HEAT_VALUES.size() + ")");
        }
        return HEAT_VALUES.get(index);
    }
}
