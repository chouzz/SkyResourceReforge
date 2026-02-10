package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.heat.HeatVariants;
import com.chouzz.skyresourcereforge.item.BaseComponentItem;
import com.chouzz.skyresourcereforge.item.TechComponentItem;
import com.chouzz.skyresourcereforge.alchemy.item.AlchemyComponentItem;
import com.chouzz.skyresourcereforge.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SkyResourceReforge.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.CACTUS_KNIFE.get());
        basicItem(ModItems.STONE_KNIFE.get());
        basicItem(ModItems.IRON_KNIFE.get());
        basicItem(ModItems.DIAMOND_KNIFE.get());
        basicItem(ModItems.STONE_GRINDER.get());
        basicItem(ModItems.IRON_GRINDER.get());
        basicItem(ModItems.DIAMOND_GRINDER.get());
        basicItem(ModItems.WATER_EXTRACTOR.get());
        withExistingParent("nether_brick_condenser", modLoc("block/nether_brick_condenser"));
        withExistingParent("nether_brick_combustion_heater", modLoc("block/nether_brick_combustion_heater"));
        basicItem(ModItems.CACTUS_FRUIT.get());
        getBuilder("dark_matter")
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/dark_matter"));
        getBuilder("light_matter")
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/light_matter"));
        basicItem(ModItems.HEAVY_SNOWBALL.get());
        basicItem(ModItems.HEAVY_EXPLOSIVE_SNOWBALL.get());
        basicItem(ModItems.FLESHY_SNOW_NUGGET.get());

        // Ore Alchemical Dust - single model for all subtypes
        basicItem(ModItems.ORE_ALCH_DUST.get());
        basicItem(ModItems.DIRTY_GEM.get());

        registerBaseComponentModels();
        registerTechComponentModels();
        registerAlchemyComponentModels();
        registerHeatComponentModels();
        registerHeatProviderModels();
        registerAlchemyMachineModels();
    }

    private void registerBaseComponentModels() {
        String baseVariant = BaseComponentItem.getNames().get(0);
        ItemModelBuilder base = getBuilder("base_component")
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/base_component_" + baseVariant));

        for (int i = 0; i < BaseComponentItem.getNames().size(); i++) {
            String variant = BaseComponentItem.getNames().get(i);
            ItemModelBuilder variantModel = getBuilder("base_component_" + variant)
                    .parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/base_component_" + variant));

            base.override()
                    .predicate(modLoc("base_component_variant"), i + 1)
                    .model(variantModel)
                    .end();
        }
    }

    private void registerTechComponentModels() {
        String baseVariant = TechComponentItem.getNames().get(0);
        ItemModelBuilder base = getBuilder("tech_component")
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/tech_component_" + baseVariant));

        for (int i = 0; i < TechComponentItem.getNames().size(); i++) {
            String variant = TechComponentItem.getNames().get(i);
            ItemModelBuilder variantModel = getBuilder("tech_component_" + variant)
                    .parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/tech_component_" + variant));

            base.override()
                    .predicate(modLoc("tech_component_variant"), i + 1)
                    .model(variantModel)
                    .end();
        }
    }

    private void registerAlchemyComponentModels() {
        String baseVariant = AlchemyComponentItem.getNames().get(0);
        ItemModelBuilder base = getBuilder("alchemy_component")
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/alchemy_component_" + baseVariant));

        for (int i = 0; i < AlchemyComponentItem.getNames().size(); i++) {
            String variant = AlchemyComponentItem.getNames().get(i);
            ItemModelBuilder variantModel = getBuilder("alchemy_component_" + variant)
                    .parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/alchemy_component_" + variant));

            base.override()
                    .predicate(modLoc("alchemy_component_variant"), i + 1)
                    .model(variantModel)
                    .end();
        }
    }

    private void registerHeatComponentModels() {
        ItemModelBuilder base = getBuilder("heat_component")
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", heatBaseTexture(HeatVariants.getName(0), false))
                .texture("layer1", modLoc("item/heat_component"));

        for (int i = 0; i < HeatVariants.size(); i++) {
            String variant = HeatVariants.getName(i);
            ItemModelBuilder variantModel = getBuilder("heat_component_" + variant)
                    .parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", heatBaseTexture(variant, false))
                    .texture("layer1", modLoc("item/heat_component"));

            base.override()
                    .predicate(modLoc("heat_variant"), i + 1)
                    .model(variantModel)
                    .end();
        }
    }

    private void registerHeatProviderModels() {
        ItemModelBuilder base = getBuilder("heat_provider")
                .parent(getExistingFile(modLoc("item/heat_provider_base")))
                .texture("all", heatBaseTexture(HeatVariants.getName(0), true))
                .texture("part", modLoc("item/heat_provider"));

        for (int i = 0; i < HeatVariants.size(); i++) {
            String variant = HeatVariants.getName(i);
            ItemModelBuilder variantModel = getBuilder("heat_provider_" + variant)
                    .parent(getExistingFile(modLoc("item/heat_provider_base")))
                    .texture("all", heatBaseTexture(variant, true))
                    .texture("part", modLoc("item/heat_provider"));

            base.override()
                    .predicate(modLoc("heat_variant"), i + 1)
                    .model(variantModel)
                    .end();
        }
    }

    private void registerAlchemyMachineModels() {
        ItemModelBuilder base = getBuilder("alchemy")
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", heatBaseTexture(HeatVariants.getName(0), false))
                .texture("layer1", modLoc("item/alchemy_component_crystal_shard"));

        for (int i = 0; i < HeatVariants.size(); i++) {
            String variant = HeatVariants.getName(i);
            ItemModelBuilder variantModel = getBuilder("alchemy_" + variant)
                    .parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", heatBaseTexture(variant, false))
                    .texture("layer1", modLoc("item/alchemy_component_crystal_shard"));

            base.override()
                    .predicate(modLoc("alchemy_variant"), i + 1)
                    .model(variantModel)
                    .end();
        }
    }

    private ResourceLocation heatBaseTexture(String variant, boolean useLogForWood) {
        return switch (variant) {
            case "wood" -> mcLoc(useLogForWood ? "block/oak_log" : "block/oak_planks");
            case "stone" -> mcLoc("block/stone");
            case "bronze" -> modLoc("block/bronzemachine");
            case "iron" -> modLoc("block/ironmachine");
            case "steel" -> modLoc("block/steelmachine");
            case "electrum" -> modLoc("block/electrummachine");
            case "netherbrick" -> mcLoc("block/nether_bricks");
            case "lead" -> modLoc("block/leadmachine");
            case "manyullyn" -> modLoc("block/manyullynmachine");
            case "signalum" -> modLoc("block/signalummachine");
            case "endstone" -> mcLoc("block/end_stone");
            case "enderium" -> modLoc("block/enderiummachine");
            case "darkmatter" -> modLoc("block/darkmatter");
            case "lightmatter" -> modLoc("block/lightmatter");
            case "osmium" -> modLoc("block/osmiummachine");
            case "refinedobsidian" -> modLoc("block/refinedobsidianmachine");
            default -> mcLoc("block/oak_planks");
        };
    }
}
