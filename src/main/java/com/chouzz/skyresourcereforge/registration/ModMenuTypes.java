package com.chouzz.skyresourcereforge.registration;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import com.chouzz.skyresourcereforge.menu.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, SkyResourceReforge.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<DirtFurnaceMenu>> DIRT_FURNACE = MENU_TYPES.register(
            "dirt_furnace",
            () -> IMenuTypeExtension.create(DirtFurnaceMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<FreezerMenu>> FREEZER = MENU_TYPES.register(
            "freezer",
            () -> IMenuTypeExtension.create(FreezerMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<AqueousConcentratorMenu>> AQUEOUS_CONCENTRATOR = MENU_TYPES.register(
            "aqueous_concentrator",
            () -> IMenuTypeExtension.create(AqueousConcentratorMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<RockCrusherMenu>> ROCK_CRUSHER = MENU_TYPES.register(
            "rock_crusher",
            () -> IMenuTypeExtension.create(RockCrusherMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<RockCleanerMenu>> ROCK_CLEANER = MENU_TYPES.register(
            "rock_cleaner",
            () -> IMenuTypeExtension.create(RockCleanerMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<CombustionCollectorMenu>> COMBUSTION_COLLECTOR = MENU_TYPES.register(
            "combustion_collector",
            () -> IMenuTypeExtension.create(CombustionCollectorMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<CasingMenu>> CASING = MENU_TYPES.register(
            "casing",
            () -> IMenuTypeExtension.create(CasingMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<CrucibleInserterMenu>> CRUCIBLE_INSERTER = MENU_TYPES.register(
            "crucible_inserter",
            () -> IMenuTypeExtension.create(CrucibleInserterMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<WildlifeAttractorMenu>> WILDLIFE_ATTRACTOR = MENU_TYPES.register(
            "wildlife_attractor",
            () -> IMenuTypeExtension.create(WildlifeAttractorMenu::new)
    );

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
