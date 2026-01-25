package com.chouzz.skyresourcereforge.datagen;

import com.chouzz.skyresourcereforge.registration.ModBlocks;
import com.chouzz.skyresourcereforge.registration.ModItems;
import net.minecraft.data.PackOutput;

/**
 * Chinese (Simplified) language provider.
 * Contains all Chinese translations for SkyResource Reforge.
 */
public class ChineseLanguageProvider extends ModLanguageProvider {

    public ChineseLanguageProvider(PackOutput output) {
        super(output, "zh_cn");
    }

    @Override
    protected void addCreativeTab() {
        add("itemGroup.skyresourcereforge.main", "天际资源：重铸版");
    }

    @Override
    protected void addKnives() {
        addItem(ModItems.CACTUS_KNIFE, "仙人掌切割刀");
        addItem(ModItems.STONE_KNIFE, "石制切割刀");
        addItem(ModItems.IRON_KNIFE, "铁制切割刀");
        addItem(ModItems.DIAMOND_KNIFE, "钻石切割刀");
    }

    @Override
    protected void addGrinders() {
        addItem(ModItems.STONE_GRINDER, "石制碎石杵");
        addItem(ModItems.IRON_GRINDER, "铁制碎石杵");
        addItem(ModItems.DIAMOND_GRINDER, "钻石碎石杵");
    }

    @Override
    protected void addMiscItems() {
        addItem(ModItems.WATER_EXTRACTOR, "抽水器");
        addItem(ModItems.CACTUS_FRUIT, "仙人掌果");
        addItem(ModItems.HEAVY_SNOWBALL, "重型雪球");
        addItem(ModItems.HEAVY_EXPLOSIVE_SNOWBALL, "爆炸性重型雪球");
        addItem(ModItems.FLESHY_SNOW_NUGGET, "沾满血肉的雪球");
        addItem(ModItems.BASE_COMPONENT, "基础组件");
        addItem(ModItems.DARK_MATTER, "暗物质");
        addItem(ModItems.LIGHT_MATTER, "亮物质");
        addItem(ModItems.TECH_COMPONENT, "科技组件");
        addItem(ModItems.HEAT_COMPONENT, "加热组件");
        addItem(ModItems.HEAT_PROVIDER, "热量供应器");
    }

    @Override
    protected void addBlocks() {
        addBlock(ModBlocks.COMPRESSED_COAL_BLOCK, "硬化煤炭块");
        addBlock(ModBlocks.COMBUSTION_CONTROLLER, "智能氧化控制器");
        addBlock(ModBlocks.COMBUSTION_COLLECTOR, "氧化产物收集器");
        addBlock(ModBlocks.ROCK_CRUSHER, "碎石机");
        addBlock(ModBlocks.ROCK_CLEANER, "洗矿机");
        addBlock(ModBlocks.CASING, "框架");
        addBlock(ModBlocks.SANDY_NETHERRACK, "沙化地狱岩");
        addBlock(ModBlocks.COAL_INFUSED_BLOCK, "赫耳墨斯煤炭块");
        addBlock(ModBlocks.DARK_MATTER_BLOCK, "暗物质块");
        addBlock(ModBlocks.LIGHT_MATTER_BLOCK, "亮物质块");
        addBlock(ModBlocks.BLAZE_POWDER_BLOCK, "烈焰粉块");
        addBlock(ModBlocks.MAGMAFIED_STONE, "岩浆石");
        addBlock(ModBlocks.DRY_CACTUS, "脱水仙人掌");
        addBlock(ModBlocks.CACTUS_FRUIT_NEEDLE, "扎在刺上的仙人掌果");
        addBlock(ModBlocks.DIRT_FURNACE, "泥炉");
        addBlock(ModBlocks.MINI_FREEZER, "迷你冰箱");
        addBlock(ModBlocks.IRON_FREEZER, "铁制冰箱");
        addBlock(ModBlocks.LIGHT_FREEZER, "亮物质冰箱");
        addBlock(ModBlocks.AQUEOUS_CONCENTRATOR, "自动注水器");
        addBlock(ModBlocks.AQUEOUS_DECONCENTRATOR, "自动抽水器");
        addBlock(ModBlocks.HEAVY_SNOW, "重型雪");
        addBlock(ModBlocks.PETRIFIED_WOOD, "石化木");
        addBlock(ModBlocks.PETRIFIED_PLANKS, "石化木板");
        addBlock(ModBlocks.SILVERFISH_DISRUPTOR, "蠹虫传送干扰器");
        addBlock(ModBlocks.CRUCIBLE, "坩埚");
    }

    @Override
    protected void addOreAlchemicalDusts() {
        String[] oreCnNames = {
            "铁", "金", "铜", "锡", "银", "锌", "镍", "铂", "铝", "铅",
            "钴", "阿迪特", "锇", "龙", "钛", "钨", "铬", "铱",
            "硼", "锂", "镁", "秘银", "黄铀", "铀", "钍"
        };

        for (int i = 0; i < ORE_NAMES.length; i++) {
            add("item.skyresourcereforge.ore_alch_dust." + ORE_NAMES[i], oreCnNames[i] + "炼金矿尘");
        }
    }

    @Override
    protected void addAlchemyItems() {
        addItem(ModItems.ALCHEMY_COMPONENT, "炼金组件");
        addItem(ModItems.HEALTH_GEM, "生命宝石");
        addItem(ModItems.ORE_ALCH_DUST, "炼金矿尘");
        addItem(ModItems.DIRTY_GEM, "污浊的宝石");
    }

    @Override
    protected void addDirtyGems() {
        String[] gemCnNames = {
            "绿宝石", "钻石", "红宝石", "蓝宝石", "橄榄石",
            "红石榴石", "黄石榴石", "磷灰石", "琥珀", "缟玛瑙",
            "玛瑙", "欧泊", "紫晶", "海蓝宝石", "日长石",
            "摩根石", "绿柱石", "蓝碧玺", "石榴石", "黄玉",
            "堇青石", "混沌精华", "暗黑宝石", "青金石", "焦黑石英", "赛特斯石英",
            "锂云母", "孔雀石", "捷克陨石", "碧玉", "绿松石",
            "月长石", "红玉髓", "金绿柱石", "黄水晶", "紫黄晶",
            "坦桑石", "紫色蓝宝石", "紫翠玉", "蓝黄玉",
            "尖晶石", "黑钻", "石英", "末影精华"
        };

        for (int i = 0; i < GEM_NAMES.length; i++) {
            add("item.skyresourcereforge.dirty_gem." + GEM_NAMES[i], "污浊的" + gemCnNames[i]);
        }
    }

    @Override
    protected void addHeatVariants() {
        String[] variantCnNames = {
            "木质", "石质", "青铜", "铁质", "钢制", "琥珀金", "地狱砖", "铅质",
            "玛玉灵", "信素", "末地石", "末影合金", "暗物质", "亮物质",
            "锇", "精炼黑曜石"
        };

        for (int i = 0; i < HEAT_VARIANT_NAMES.length; i++) {
            add("item.skyresourcereforge.heat_component." + HEAT_VARIANT_NAMES[i],
                variantCnNames[i] + "加热组件");
            add("item.skyresourcereforge.heat_provider." + HEAT_VARIANT_NAMES[i],
                variantCnNames[i] + "热量供应器");
        }
    }

    @Override
    protected void addTooltips() {
        add("tooltip.skyresourcereforge.ore_alch_dust.rarity", "稀有度: %s");
        add("tooltip.skyresourcereforge.dirty_gem.rarity", "稀有度: %s");
    }

    @Override
    protected void addJEIDescriptions() {
        add("jei.skyresourcereforge.description.cactus_knife",
            "通过按住 Shift 键右击仙人掌获得。\n\n 提示：这个过程中你会受到伤害。");
        add("jei.skyresourcereforge.description.blaze_powder_block",
            "在一个热源上方加热将其变为岩浆。");
    }

    @Override
    protected void addJEIRecipeCategories() {
        add("jei.skyresourcereforge.recipe.combustion", "氧化");
        add("jei.skyresourcereforge.recipe.water_extractor_extract", "抽水器（抽取）");
        add("jei.skyresourcereforge.recipe.water_extractor_insert", "抽水器（注入）");
        add("jei.skyresourcereforge.recipe.rock_grinder", "碎石机");
        add("jei.skyresourcereforge.recipe.cauldron_clean", "洗矿");
        add("jei.skyresourcereforge.recipe.freezer", "冰箱");
        add("jei.skyresourcereforge.recipe.fusion", "融合");
        add("jei.skyresourcereforge.recipe.infusion", "注魔");
        add("jei.skyresourcereforge.recipe.condenser", "冷凝器");
        add("jei.skyresourcereforge.recipe.crucible", "坩埚");
        add("jei.skyresourcereforge.recipe.knife", "切割刀");
        add("jei.skyresourcereforge.recipe.handheld_rock_grinder", "手持碎石杵");
    }
}
