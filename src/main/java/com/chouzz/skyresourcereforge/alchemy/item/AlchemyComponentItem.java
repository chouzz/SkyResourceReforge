package com.chouzz.skyresourcereforge.alchemy.item;

import net.minecraft.world.item.Item;

public class AlchemyComponentItem extends Item {
    private final int componentType;

    public AlchemyComponentItem(int componentType, Properties properties) {
        super(properties);
        this.componentType = componentType;
    }

    public int getComponentType() {
        return componentType;
    }
}
