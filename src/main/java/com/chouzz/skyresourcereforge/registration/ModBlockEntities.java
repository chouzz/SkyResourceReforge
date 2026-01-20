package com.chouzz.skyresourcereforge.registration;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.block.entity.CasingBlockEntity;
import com.chouzz.skyresourcereforge.block.entity.CombustionCollectorBlockEntity;
import com.chouzz.skyresourcereforge.block.entity.CombustionControllerBlockEntity;
import com.chouzz.skyresourcereforge.block.entity.RockCleanerBlockEntity;
import com.chouzz.skyresourcereforge.block.entity.RockCrusherBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SkyResourceReforge.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CombustionControllerBlockEntity>> COMBUSTION_CONTROLLER =
            BLOCK_ENTITIES.register("combustion_controller", () -> BlockEntityType.Builder.of(CombustionControllerBlockEntity::new, ModBlocks.COMBUSTION_CONTROLLER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CombustionCollectorBlockEntity>> COMBUSTION_COLLECTOR =
            BLOCK_ENTITIES.register("combustion_collector", () -> BlockEntityType.Builder.of(CombustionCollectorBlockEntity::new, ModBlocks.COMBUSTION_COLLECTOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RockCrusherBlockEntity>> ROCK_CRUSHER =
            BLOCK_ENTITIES.register("rock_crusher", () -> BlockEntityType.Builder.of(RockCrusherBlockEntity::new, ModBlocks.ROCK_CRUSHER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RockCleanerBlockEntity>> ROCK_CLEANER =
            BLOCK_ENTITIES.register("rock_cleaner", () -> BlockEntityType.Builder.of(RockCleanerBlockEntity::new, ModBlocks.ROCK_CLEANER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.chouzz.skyresourcereforge.block.entity.DirtFurnaceBlockEntity>> DIRT_FURNACE =
            BLOCK_ENTITIES.register("dirt_furnace", () -> BlockEntityType.Builder.of(com.chouzz.skyresourcereforge.block.entity.DirtFurnaceBlockEntity::new, ModBlocks.DIRT_FURNACE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.chouzz.skyresourcereforge.block.entity.FreezerBlockEntity>> MINI_FREEZER =
            BLOCK_ENTITIES.register("mini_freezer", () -> BlockEntityType.Builder.of(
                    (pos, state) -> {
                        if (state.getBlock() instanceof com.chouzz.skyresourcereforge.block.FreezerBlock freezerBlock) {
                            return new com.chouzz.skyresourcereforge.block.entity.FreezerBlockEntity(pos, state, freezerBlock.getSpeed());
                        }
                        return new com.chouzz.skyresourcereforge.block.entity.FreezerBlockEntity(pos, state, 0.25f);
                    },
                    ModBlocks.MINI_FREEZER.get(), ModBlocks.IRON_FREEZER.get(), ModBlocks.LIGHT_FREEZER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.chouzz.skyresourcereforge.block.entity.AqueousConcentratorBlockEntity>> AQUEOUS_CONCENTRATOR =
            BLOCK_ENTITIES.register("aqueous_concentrator", () -> BlockEntityType.Builder.of(
                    (pos, state) -> new com.chouzz.skyresourcereforge.block.entity.AqueousConcentratorBlockEntity(pos, state, true),
                    ModBlocks.AQUEOUS_CONCENTRATOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<com.chouzz.skyresourcereforge.block.entity.AqueousConcentratorBlockEntity>> AQUEOUS_DECONCENTRATOR =
            BLOCK_ENTITIES.register("aqueous_deconcentrator", () -> BlockEntityType.Builder.of(
                    (pos, state) -> new com.chouzz.skyresourcereforge.block.entity.AqueousConcentratorBlockEntity(pos, state, false),
                    ModBlocks.AQUEOUS_DECONCENTRATOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CasingBlockEntity>> CASING =
            BLOCK_ENTITIES.register("casing", () -> BlockEntityType.Builder.of(CasingBlockEntity::new, ModBlocks.CASING.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
