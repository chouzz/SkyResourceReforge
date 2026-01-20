package com.chouzz.skyresourcereforge.registration;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SkyResourceReforge.MODID);

    public static final DeferredBlock<Block> COMPRESSED_COAL_BLOCK = registerBlock("compressed_coal_block",
            () -> new BaseBlock(BlockBehaviour.Properties.of()
                    .strength(6.0f, 6.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> SANDY_NETHERRACK = registerBlock("sandy_netherrack",
            () -> new BaseBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f, 2.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> COAL_INFUSED_BLOCK = registerBlock("coal_infused_block",
            () -> new BaseBlock(BlockBehaviour.Properties.of()
                    .strength(6.0f, 6.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DARK_MATTER_BLOCK = registerBlock("dark_matter_block",
            () -> new BaseBlock(BlockBehaviour.Properties.of()
                    .strength(10.0f, 12.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> LIGHT_MATTER_BLOCK = registerBlock("light_matter_block",
            () -> new BaseBlock(BlockBehaviour.Properties.of()
                    .strength(10.0f, 12.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> BLAZE_POWDER_BLOCK = registerBlock("blaze_powder_block",
            () -> new BlazePowderBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .sound(SoundType.GRAVEL)
                    .randomTicks()));

    public static final DeferredBlock<Block> MAGMAFIED_STONE = registerBlock("magmafied_stone",
            () -> new MagmafiedStoneBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f, 1.0f)
                    .sound(SoundType.STONE)
                    .randomTicks()));

    public static final DeferredBlock<Block> DRY_CACTUS = registerBlock("dry_cactus",
            () -> new DryCactusBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .sound(SoundType.GRASS)
                    .noOcclusion()));

    public static final DeferredBlock<Block> CACTUS_FRUIT_NEEDLE = registerBlock("cactus_fruit_needle",
            () -> new com.chouzz.skyresourcereforge.block.CactusFruitNeedleBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .sound(SoundType.GRASS)
                    .noOcclusion()));

    public static final DeferredBlock<CombustionControllerBlock> COMBUSTION_CONTROLLER = registerBlock("combustion_controller",
            () -> new CombustionControllerBlock(BlockBehaviour.Properties.of()
                    .strength(6.0f, 12.0f)
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> COMBUSTION_COLLECTOR = registerBlock("combustion_collector",
            () -> new CombustionCollectorBlock(BlockBehaviour.Properties.of()
                    .strength(6.0f, 12.0f)
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> ROCK_CRUSHER = registerBlock("rock_crusher",
            () -> new com.chouzz.skyresourcereforge.block.RockCrusherBlock(BlockBehaviour.Properties.of()
                    .strength(6.0f, 12.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> ROCK_CLEANER = registerBlock("rock_cleaner",
            () -> new com.chouzz.skyresourcereforge.block.RockCleanerBlock(BlockBehaviour.Properties.of()
                    .strength(6.0f, 12.0f)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DIRT_FURNACE = registerBlock("dirt_furnace",
            () -> new com.chouzz.skyresourcereforge.block.DirtFurnaceBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .sound(SoundType.GRAVEL)
                    .lightLevel(state -> state.getValue(com.chouzz.skyresourcereforge.block.DirtFurnaceBlock.LIT) ? 13 : 0)));

    public static final DeferredBlock<Block> MINI_FREEZER = registerBlock("mini_freezer",
            () -> new com.chouzz.skyresourcereforge.block.FreezerBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .sound(SoundType.METAL), 0.25f));

    public static final DeferredBlock<Block> IRON_FREEZER = registerBlock("iron_freezer",
            () -> new com.chouzz.skyresourcereforge.block.FreezerBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f, 2.0f)
                    .sound(SoundType.METAL), 1.0f));

    public static final DeferredBlock<Block> LIGHT_FREEZER = registerBlock("light_freezer",
            () -> new com.chouzz.skyresourcereforge.block.FreezerBlock(BlockBehaviour.Properties.of()
                    .strength(8.0f, 12.0f)
                    .sound(SoundType.METAL), 100.0f));

    public static final DeferredBlock<Block> AQUEOUS_CONCENTRATOR = registerBlock("aqueous_concentrator",
            () -> new com.chouzz.skyresourcereforge.block.AqueousConcentratorBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f, 2.0f)
                    .sound(SoundType.STONE), true));

    public static final DeferredBlock<Block> AQUEOUS_DECONCENTRATOR = registerBlock("aqueous_deconcentrator",
            () -> new com.chouzz.skyresourcereforge.block.AqueousConcentratorBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f, 2.0f)
                    .sound(SoundType.STONE), false));

    public static final DeferredBlock<Block> HEAVY_SNOW = registerBlock("heavy_snow",
            () -> new com.chouzz.skyresourcereforge.block.HeavySnowBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .sound(SoundType.SNOW)));

    public static final DeferredBlock<Block> PETRIFIED_WOOD = registerBlock("petrified_wood",
            () -> new BaseBlock(BlockBehaviour.Properties.of()
                    .strength(1.5f, 0.5f)
                    .sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> PETRIFIED_PLANKS = registerBlock("petrified_planks",
            () -> new BaseBlock(BlockBehaviour.Properties.of()
                    .strength(1.5f, 0.5f)
                    .sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> SILVERFISH_DISRUPTOR = registerBlock("silverfish_disruptor",
            () -> new com.chouzz.skyresourcereforge.block.SilverfishDisruptorBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f, 0.5f)
                    .sound(SoundType.GLASS)
                    .noOcclusion()));

    public static final DeferredBlock<Block> CASING = registerBlock("casing",
            () -> new CasingBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f, 12.0f)
                    .sound(SoundType.METAL)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
