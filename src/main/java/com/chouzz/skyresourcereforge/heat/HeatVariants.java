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
}
