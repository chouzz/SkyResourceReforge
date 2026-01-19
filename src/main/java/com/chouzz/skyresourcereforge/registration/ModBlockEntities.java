package com.chouzz.skyresourcereforge.registration;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.block.entity.CombustionControllerBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SkyResourceReforge.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CombustionControllerBlockEntity>> COMBUSTION_CONTROLLER =
            BLOCK_ENTITIES.register("combustion_controller", () -> BlockEntityType.Builder.of(CombustionControllerBlockEntity::new, ModBlocks.COMBUSTION_CONTROLLER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
