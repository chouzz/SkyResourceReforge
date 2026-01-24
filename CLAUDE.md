# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SkyResourceReforge is a Minecraft 1.21.1 mod built on NeoForge 21.1.218 using Java 21. It's a reforged version of the classic SkyResources mod, featuring alchemy systems, heat-based mechanics, and multi-stage resource processing.

**Key Properties:**
- Mod ID: `skyresourcereforge`
- Group ID: `com.chouzz.skyresourcereforge`
- Package: `com.chouzz.skyresourcereforge`

## Common Development Commands

```bash
# Build the mod
./gradlew build

# Run client
./gradlew runClient

# Run server
./gradlew runServer

# Run data generators (generates assets/data to src/generated/resources/)
./gradlew runData

# Refresh dependencies
./gradlew --refresh-dependencies

# Clean build
./gradlew clean
```

## Architecture

### Critical Initialization Order (SkyResourceReforge.java)

The main mod class constructor initializes data in a specific order that **must** be preserved:

1. **Static data registration** (before DeferredRegister):
   - `HeatSources.registerDefaults()` - Registers vanilla heat sources
   - `ItemOreAlchDust.init()` - Initializes 25 ore types
   - `DirtyGemItem.initGems()` - Initializes 44 gem variants

2. **Event bus registration** for common setup, capabilities, item helpers

3. **DeferredRegister calls** (all 9 of them):
   - ModBlocks → ModItems → ModDataComponents → ModBlockEntities → ModEntities → ModCreativeTabs → ModRecipeTypes → ModRecipeSerializers → ModMenuTypes

### Multi-Variant Item System

The mod uses **Data Components** (1.21 modern system) for variant items like dirty gems, ore alchemical dust, heat components, and heat providers. These items:
- Store variant index in an Integer data component (e.g., `HEAT_COMPONENT_INDEX`)
- Have dynamic naming: `item.skyresourcereforge.{item_name}.{variant_name}`
- Require JEI subtype registration to show all variants separately
- Use client-side property overrides for model switching

When adding a new variant item:
1. Create item class with data component storage
2. Add static init method that populates variant info
3. Call init in SkyResourceReforge constructor (before DeferredRegister)
4. Add client-side property registration in SkyResourceReforgeClient
5. Register JEI subtypes and extra stacks in SkyResourceJEIPlugin

### Recipe System (ProcessRecipe)

All custom recipes use `ProcessRecipe`, a universal recipe class supporting:
- List of item inputs (`CountedIngredient` - ingredients with count)
- List of item outputs
- List of fluid inputs/outputs
- Float parameter (heat value, time, etc.)
- Strict/non-strict matching mode
- Stack merging option

Recipe types (registered in `ModRecipeTypes`):
- COMBUSTION, WATER_EXTRACTOR_EXTRACT/INSERT, ROCK_GRINDER, CAULDRON_CLEAN, FREEZER, FUSION, INFUSION, CONDENSER, CRUCIBLE

### Heat System

The heat system (`HeatSources`, `HeatVariants`, `IHeatSource`):
- 17 material tiers for heat components/providers
- Registry of heat-providing blocks (Fire: 8, Lava: 6, Magma Block: 9, Torch: 1, Obsidian: 3)
- Machines validate nearby heat sources via `HeatSources.isValidHeatSource()`

### Internationalization

The mod uses an abstract base pattern for scalable i18n:
- `ModLanguageProvider` - Abstract base with shared arrays (ORE_NAMES, GEM_NAMES, HEAT_VARIANT_NAMES)
- `EnglishLanguageProvider`, `ChineseLanguageProvider` - Concrete implementations
- To add a language: Create new provider extending ModLanguageProvider, implement all abstract methods

### Machine Pattern

Machines follow a 4-class pattern:
1. **Block class** extends `BaseEntityBlock` - creates block entity, provides ticker
2. **BlockEntity class** - inventory, recipe processing, data syncing
3. **Menu class** - GUI container for server-client sync
4. **Screen class** (client-only) - renders GUI

Registration happens in `ModBlocks`, `ModBlockEntities`, and `ModMenuTypes`.

### Data Generation

Located in `src/main/java/com/chouzz/skyresourcereforge/datagen/`:
- `DataGenerators.java` - Entry point, registers all providers
- `ModLanguageProvider` subclasses - Generate en_us.json, zh_cn.json
- `ModBlockStateProvider`, `ModItemModelProvider` - Block/item models
- `ModRecipeProvider` - Recipe JSONs

Run `./gradlew runData` to regenerate.

## Registration Patterns

### Adding a simple item (in ModItems.java):
```java
public static final DeferredItem<Item> MY_ITEM = ITEMS.register("my_item",
    () -> new Item(new Item.Properties()));
```

### Adding a block (in ModBlocks.java):
```java
private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
    DeferredBlock<T> toReturn = BLOCKS.register(name, block);
    registerBlockItem(name, toReturn);  // Auto-creates BlockItem
    return toReturn;
}
```

### Adding a recipe type (in ModRecipeTypes.java):
```java
public static final RegistryObject<RecipeType<ProcessRecipe>> MY_TYPE = RECIPE_TYPES.register("my_type",
    () -> new RecipeType<>() {});
```

## Development Context

Always use:
- Use context7 mcp server to check Neoforge documents and apis
- Check the origin 1.12.2 version of Skyresource source code in /Users/chouzz/projects/games/SkyResources
