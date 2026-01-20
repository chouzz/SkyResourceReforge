package com.chouzz.skyresourcereforge.registration;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.entity.HeavyExplosiveSnowballEntity;
import com.chouzz.skyresourcereforge.entity.HeavySnowballEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, SkyResourceReforge.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<HeavySnowballEntity>> HEAVY_SNOWBALL =
            ENTITIES.register("heavy_snowball", () -> EntityType.Builder.<HeavySnowballEntity>of(HeavySnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("heavy_snowball"));

    public static final DeferredHolder<EntityType<?>, EntityType<HeavyExplosiveSnowballEntity>> HEAVY_EXPLOSIVE_SNOWBALL =
            ENTITIES.register("heavy_explosive_snowball", () -> EntityType.Builder.<HeavyExplosiveSnowballEntity>of(HeavyExplosiveSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("heavy_explosive_snowball"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
