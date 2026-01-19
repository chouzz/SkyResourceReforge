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
        simpleBlock(ModBlocks.COMPRESSED_COAL_BLOCK.get());
        simpleBlock(ModBlocks.CASING.get());
        
        horizontalBlock(ModBlocks.COMBUSTION_CONTROLLER.get(), 
                models().orientable("combustion_controller", 
                        modLoc("block/combustion_controller_side"), 
                        modLoc("block/combustion_controller_front"), 
                        modLoc("block/combustion_controller_top")));
    }
}
