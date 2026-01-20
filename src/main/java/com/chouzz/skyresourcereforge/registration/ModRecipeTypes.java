package com.chouzz.skyresourcereforge.registration;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.recipe.ProcessRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, SkyResourceReforge.MODID);

    // We can define specific types if needed, or a generic one if machines share the format
    public static final DeferredHolder<RecipeType<?>, RecipeType<ProcessRecipe>> COMBUSTION = RECIPE_TYPES.register("combustion", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "combustion";
        }
    });

    public static final DeferredHolder<RecipeType<?>, RecipeType<ProcessRecipe>> WATER_EXTRACTOR_EXTRACT = RECIPE_TYPES.register("water_extractor_extract", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "water_extractor_extract";
        }
    });

    public static final DeferredHolder<RecipeType<?>, RecipeType<ProcessRecipe>> WATER_EXTRACTOR_INSERT = RECIPE_TYPES.register("water_extractor_insert", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "water_extractor_insert";
        }
    });

    public static final DeferredHolder<RecipeType<?>, RecipeType<ProcessRecipe>> ROCK_GRINDER = RECIPE_TYPES.register("rock_grinder", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "rock_grinder";
        }
    });

    public static final DeferredHolder<RecipeType<?>, RecipeType<ProcessRecipe>> CAULDRON_CLEAN = RECIPE_TYPES.register("cauldron_clean", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "cauldron_clean";
        }
    });

    public static final DeferredHolder<RecipeType<?>, RecipeType<ProcessRecipe>> FREEZER = RECIPE_TYPES.register("freezer", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "freezer";
        }
    });

    // Phase 5: Additional recipe types
    public static final DeferredHolder<RecipeType<?>, RecipeType<ProcessRecipe>> FUSION = RECIPE_TYPES.register("fusion", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "fusion";
        }
    });

    public static final DeferredHolder<RecipeType<?>, RecipeType<ProcessRecipe>> INFUSION = RECIPE_TYPES.register("infusion", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "infusion";
        }
    });

    public static final DeferredHolder<RecipeType<?>, RecipeType<ProcessRecipe>> CONDENSER = RECIPE_TYPES.register("condenser", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "condenser";
        }
    });

    public static final DeferredHolder<RecipeType<?>, RecipeType<ProcessRecipe>> CRUCIBLE = RECIPE_TYPES.register("crucible", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "crucible";
        }
    });

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}
