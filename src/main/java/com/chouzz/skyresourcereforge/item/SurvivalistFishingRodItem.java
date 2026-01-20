package com.chouzz.skyresourcereforge.item;

import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;

/**
 * Survivalist fishing rod for SkyResourceReforge.
 * Ported from SkyResources ItemSurvivalFishingRod.
 *
 * Note: This uses vanilla fishing mechanics for now.
 * The custom SurvivalistHookEntity will be implemented in a future phase.
 */
public class SurvivalistFishingRodItem extends FishingRodItem {
    public SurvivalistFishingRodItem() {
        super(new Item.Properties());
    }
}
