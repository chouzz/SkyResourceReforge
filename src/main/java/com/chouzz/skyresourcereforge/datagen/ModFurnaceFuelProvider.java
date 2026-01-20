package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.registration.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ModFurnaceFuelProvider extends DataMapProvider {
    public ModFurnaceFuelProvider(PackOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void gather(net.minecraft.core.HolderLookup.Provider provider) {
        this.builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(ModBlocks.PETRIFIED_WOOD.getId(), new FurnaceFuel(2400), false)
                .add(ModBlocks.PETRIFIED_PLANKS.getId(), new FurnaceFuel(600), false);
    }
}
