package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.block.CombustionControllerBlock;
import com.chouzz.skyresourcereforge.registration.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SkyResourceReforge.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.COMPRESSED_COAL_BLOCK.get(), cubeAll(ModBlocks.COMPRESSED_COAL_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.CASING.get(), cubeAll(ModBlocks.CASING.get()));
        simpleBlockWithItem(ModBlocks.SANDY_NETHERRACK.get(), cubeAll(ModBlocks.SANDY_NETHERRACK.get()));
        simpleBlockWithItem(ModBlocks.COAL_INFUSED_BLOCK.get(), cubeAll(ModBlocks.COAL_INFUSED_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.DARK_MATTER_BLOCK.get(), cubeAll(ModBlocks.DARK_MATTER_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.LIGHT_MATTER_BLOCK.get(), cubeAll(ModBlocks.LIGHT_MATTER_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.BLAZE_POWDER_BLOCK.get(), cubeAll(ModBlocks.BLAZE_POWDER_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.MAGMAFIED_STONE.get(), cubeAll(ModBlocks.MAGMAFIED_STONE.get()));
        simpleBlockWithItem(ModBlocks.DRY_CACTUS.get(), cubeAll(ModBlocks.DRY_CACTUS.get()));
        simpleBlockWithItem(ModBlocks.CACTUS_FRUIT_NEEDLE.get(), cubeAll(ModBlocks.CACTUS_FRUIT_NEEDLE.get()));
        
        horizontalBlock(ModBlocks.COMBUSTION_CONTROLLER.get(), 
                models().orientable("combustion_controller", 
                        modLoc("block/combustion_controller_side"), 
                        modLoc("block/combustion_controller_front"), 
                        modLoc("block/combustion_controller_top")));
        simpleBlockItem(ModBlocks.COMBUSTION_CONTROLLER.get(), 
                models().orientable("combustion_controller", 
                        modLoc("block/combustion_controller_side"), 
                        modLoc("block/combustion_controller_front"), 
                        modLoc("block/combustion_controller_top")));
        simpleBlockWithItem(ModBlocks.COMBUSTION_COLLECTOR.get(), cubeAll(ModBlocks.COMBUSTION_COLLECTOR.get()));
        horizontalBlock(ModBlocks.ROCK_CRUSHER.get(), cubeAll(ModBlocks.ROCK_CRUSHER.get()));
        simpleBlockItem(ModBlocks.ROCK_CRUSHER.get(), cubeAll(ModBlocks.ROCK_CRUSHER.get()));
        horizontalBlock(ModBlocks.ROCK_CLEANER.get(), cubeAll(ModBlocks.ROCK_CLEANER.get()));
        simpleBlockItem(ModBlocks.ROCK_CLEANER.get(), cubeAll(ModBlocks.ROCK_CLEANER.get()));
        horizontalBlock(ModBlocks.DIRT_FURNACE.get(), cubeAll(ModBlocks.DIRT_FURNACE.get()));
        simpleBlockItem(ModBlocks.DIRT_FURNACE.get(), cubeAll(ModBlocks.DIRT_FURNACE.get()));
        horizontalBlock(ModBlocks.MINI_FREEZER.get(), cubeAll(ModBlocks.MINI_FREEZER.get()));
        simpleBlockItem(ModBlocks.MINI_FREEZER.get(), cubeAll(ModBlocks.MINI_FREEZER.get()));
        horizontalBlock(ModBlocks.IRON_FREEZER.get(), cubeAll(ModBlocks.IRON_FREEZER.get()));
        simpleBlockItem(ModBlocks.IRON_FREEZER.get(), cubeAll(ModBlocks.IRON_FREEZER.get()));
        horizontalBlock(ModBlocks.LIGHT_FREEZER.get(), cubeAll(ModBlocks.LIGHT_FREEZER.get()));
        simpleBlockItem(ModBlocks.LIGHT_FREEZER.get(), cubeAll(ModBlocks.LIGHT_FREEZER.get()));
    }
}
