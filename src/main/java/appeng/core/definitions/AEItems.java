/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2015, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.core.definitions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import appeng.api.ids.AEItemIds;
import appeng.api.stacks.AEKeyType;
import appeng.api.upgrades.Upgrades;
import appeng.api.util.AEColor;
import appeng.core.AEConfig;
import appeng.core.AppEng;
import appeng.core.CreativeTab;
import appeng.crafting.pattern.CraftingPatternItem;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.items.materials.CustomEntityItem;
import appeng.items.materials.MaterialItem;
import appeng.items.materials.StorageComponentItem;
import appeng.items.misc.CrystalSeedItem;
import appeng.items.misc.WrappedGenericStack;
import appeng.items.storage.BasicStorageCell;
import appeng.items.storage.ViewCellItem;
import appeng.items.tools.BiometricCardItem;
import appeng.items.tools.MemoryCardItem;
import appeng.items.tools.powered.WirelessCraftingTerminalItem;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.items.tools.quartz.QuartzCuttingKnifeItem;
import appeng.items.tools.quartz.QuartzToolType;
import appeng.menu.me.common.MEStorageMenu;

/**
 * Internal implementation for the API items
 */
@SuppressWarnings("unused")
public final class AEItems {

    // spotless:off
    private static final List<ItemDefinition<?>> ITEMS = new ArrayList<>();

    ///
    /// CERTUS QUARTZ TOOLS
    ///

    public static final ItemDefinition<QuartzCuttingKnifeItem> CERTUS_QUARTZ_KNIFE = item("Certus Quartz Cutting Knife", AEItemIds.CERTUS_QUARTZ_KNIFE, p -> new QuartzCuttingKnifeItem(p.durability(50), QuartzToolType.CERTUS), CreativeModeTab.TAB_TOOLS);

    ///
    /// VARIOUS POWERED TOOLS
    ///

    public static final ItemDefinition<WirelessTerminalItem> WIRELESS_TERMINAL = item("Wireless Terminal", AEItemIds.WIRELESS_TERMINAL, p -> new WirelessTerminalItem(p.stacksTo(1)));
    public static final ItemDefinition<WirelessTerminalItem> WIRELESS_CRAFTING_TERMINAL = item("Wireless Crafting Terminal", AEItemIds.WIRELESS_CRAFTING_TERMINAL, p -> new WirelessCraftingTerminalItem(p.stacksTo(1)));

    ///
    /// NETWORK RELATED TOOLS
    ///

    public static final ItemDefinition<BiometricCardItem> BIOMETRIC_CARD = item("Biometric Card", AEItemIds.BIOMETRIC_CARD, p -> new BiometricCardItem(p.stacksTo(1)));
    public static final ItemDefinition<MemoryCardItem> MEMORY_CARD = item("Memory Card",AEItemIds.MEMORY_CARD, p -> new MemoryCardItem(p.stacksTo(1)));


    public static final ItemDefinition<MaterialItem> BLANK_PATTERN = item("Blank Pattern", AEItemIds.BLANK_PATTERN, MaterialItem::new);
    public static final ItemDefinition<CraftingPatternItem> CRAFTING_PATTERN = item("Crafting Pattern", AEItemIds.CRAFTING_PATTERN, p -> new CraftingPatternItem(p.stacksTo(1)));
    public static final ItemDefinition<ProcessingPatternItem> PROCESSING_PATTERN = item("Processing Pattern", AEItemIds.PROCESSING_PATTERN, p -> new ProcessingPatternItem(p.stacksTo(1)));

    ///
    /// MATERIALS
    ///

    public static final ItemDefinition<MaterialItem> CERTUS_QUARTZ_CRYSTAL = item("Certus Quartz Crystal", AEItemIds.CERTUS_QUARTZ_CRYSTAL, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> CERTUS_QUARTZ_DUST = item("Certus Quartz Dust", AEItemIds.CERTUS_QUARTZ_DUST, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> SILICON = item("Silicon", AEItemIds.SILICON, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> FLUIX_CRYSTAL = item("Fluix Crystal", AEItemIds.FLUIX_CRYSTAL, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> FLUIX_DUST = item("Fluix Dust", AEItemIds.FLUIX_DUST, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> FLUIX_PEARL = item("Fluix Pearl", AEItemIds.FLUIX_PEARL, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> CALCULATION_PROCESSOR_PRESS = item("Inscriber Calculation Press", AEItemIds.CALCULATION_PROCESSOR_PRESS, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> ENGINEERING_PROCESSOR_PRESS = item("Inscriber Engineering Press", AEItemIds.ENGINEERING_PROCESSOR_PRESS, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> LOGIC_PROCESSOR_PRESS = item("Inscriber Logic Press", AEItemIds.LOGIC_PROCESSOR_PRESS, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> CALCULATION_PROCESSOR_PRINT = item("Printed Calculation Circuit", AEItemIds.CALCULATION_PROCESSOR_PRINT, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> ENGINEERING_PROCESSOR_PRINT = item("Printed Engineering Circuit", AEItemIds.ENGINEERING_PROCESSOR_PRINT, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> LOGIC_PROCESSOR_PRINT = item("Printed Logic Circuit", AEItemIds.LOGIC_PROCESSOR_PRINT, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> SILICON_PRESS = item("Inscriber Silicon Press", AEItemIds.SILICON_PRESS, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> SILICON_PRINT = item("Printed Silicon", AEItemIds.SILICON_PRINT, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> LOGIC_PROCESSOR = item("Logic Processor", AEItemIds.LOGIC_PROCESSOR, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> CALCULATION_PROCESSOR = item("Calculation Processor", AEItemIds.CALCULATION_PROCESSOR, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> ENGINEERING_PROCESSOR = item("Engineering Processor", AEItemIds.ENGINEERING_PROCESSOR, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> BASIC_CARD = item("Basic Card", AEItemIds.BASIC_CARD, MaterialItem::new);
    public static final ItemDefinition<Item> REDSTONE_CARD = item("Redstone Card", AEItemIds.REDSTONE_CARD, Upgrades::createUpgradeCardItem);
    public static final ItemDefinition<Item> CAPACITY_CARD = item("Capacity Card", AEItemIds.CAPACITY_CARD, Upgrades::createUpgradeCardItem);
    public static final ItemDefinition<Item> VOID_CARD = item("Overflow Destruction Card", AEItemIds.VOID_CARD, Upgrades::createUpgradeCardItem);
    public static final ItemDefinition<MaterialItem> ADVANCED_CARD = item("Advanced Card", AEItemIds.ADVANCED_CARD, MaterialItem::new);
    public static final ItemDefinition<Item> FUZZY_CARD = item("Fuzzy Card", AEItemIds.FUZZY_CARD, Upgrades::createUpgradeCardItem);
    public static final ItemDefinition<Item> SPEED_CARD = item("Acceleration Card", AEItemIds.SPEED_CARD, Upgrades::createUpgradeCardItem);
    public static final ItemDefinition<Item> INVERTER_CARD = item("Inverter Card", AEItemIds.INVERTER_CARD, Upgrades::createUpgradeCardItem);
    public static final ItemDefinition<Item> CRAFTING_CARD = item("Crafting Card", AEItemIds.CRAFTING_CARD, Upgrades::createUpgradeCardItem);
    public static final ItemDefinition<Item> EQUAL_DISTRIBUTION_CARD = item("Equal Distribution Card", AEItemIds.EQUAL_DISTRIBUTION_CARD, Upgrades::createUpgradeCardItem);
    public static final ItemDefinition<StorageComponentItem> CELL_COMPONENT_1K = item("1k ME Storage Component", AEItemIds.CELL_COMPONENT_1K, p -> new StorageComponentItem(p, 1));
    public static final ItemDefinition<StorageComponentItem> CELL_COMPONENT_4K = item("4k ME Storage Component", AEItemIds.CELL_COMPONENT_4K, p -> new StorageComponentItem(p, 4));
    public static final ItemDefinition<StorageComponentItem> CELL_COMPONENT_16K = item("16k ME Storage Component", AEItemIds.CELL_COMPONENT_16K, p -> new StorageComponentItem(p, 16));
    public static final ItemDefinition<StorageComponentItem> CELL_COMPONENT_64K = item("64k ME Storage Component", AEItemIds.CELL_COMPONENT_64K, p -> new StorageComponentItem(p, 64));
    public static final ItemDefinition<StorageComponentItem> CELL_COMPONENT_256K = item("256k ME Storage Component", AEItemIds.CELL_COMPONENT_256K, p -> new StorageComponentItem(p, 256));
    public static final ItemDefinition<MaterialItem> ITEM_CELL_HOUSING = item("ME Item Cell Housing", AEItemIds.ITEM_CELL_HOUSING, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> WIRELESS_RECEIVER = item("Wireless Receiver", AEItemIds.WIRELESS_RECEIVER, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> FORMATION_CORE = item("Formation Core", AEItemIds.FORMATION_CORE, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> ANNIHILATION_CORE = item("Annihilation Core", AEItemIds.ANNIHILATION_CORE, MaterialItem::new);
    public static final ItemDefinition<MaterialItem> SKY_DUST = item("Sky Stone Dust", AEItemIds.SKY_DUST, MaterialItem::new);

    ///
    /// SEEDS
    ///

    public static final ItemDefinition<CrystalSeedItem> CERTUS_CRYSTAL_SEED = item("Certus Quartz Seed", AEItemIds.CERTUS_CRYSTAL_SEED,
            p -> new CrystalSeedItem(p, CERTUS_QUARTZ_CRYSTAL.asItem()));
    public static final ItemDefinition<CrystalSeedItem> FLUIX_CRYSTAL_SEED = item("Fluix Seed", AEItemIds.FLUIX_CRYSTAL_SEED,
            p -> new CrystalSeedItem(p, FLUIX_CRYSTAL.asItem()));

    ///
    /// CELLS
    ///
    public static final ItemDefinition<ViewCellItem> VIEW_CELL = item("View Cell", AEItemIds.VIEW_CELL, p -> new ViewCellItem(p.stacksTo(1)));

    public static final ItemDefinition<BasicStorageCell> ITEM_CELL_1K = item("1k ME Item Storage Cell", AEItemIds.ITEM_CELL_1K, p -> new BasicStorageCell(p.stacksTo(1), CELL_COMPONENT_1K, ITEM_CELL_HOUSING, 0.5f, 1, 8, 63, AEKeyType.items()));
    public static final ItemDefinition<BasicStorageCell> ITEM_CELL_4K = item("4k ME Item Storage Cell", AEItemIds.ITEM_CELL_4K, p -> new BasicStorageCell(p.stacksTo(1), CELL_COMPONENT_4K, ITEM_CELL_HOUSING, 1.0f, 4, 32, 63, AEKeyType.items()));
    public static final ItemDefinition<BasicStorageCell> ITEM_CELL_16K = item("16k ME Item Storage Cell", AEItemIds.ITEM_CELL_16K, p -> new BasicStorageCell(p.stacksTo(1), CELL_COMPONENT_16K, ITEM_CELL_HOUSING, 1.5f, 16, 128, 63, AEKeyType.items()));
    public static final ItemDefinition<BasicStorageCell> ITEM_CELL_64K = item("64k ME Item Storage Cell", AEItemIds.ITEM_CELL_64K, p -> new BasicStorageCell(p.stacksTo(1), CELL_COMPONENT_64K, ITEM_CELL_HOUSING, 2.0f, 64, 512, 63, AEKeyType.items()));
    public static final ItemDefinition<BasicStorageCell> ITEM_CELL_256K = item("256k ME Item Storage Cell", AEItemIds.ITEM_CELL_256K, p -> new BasicStorageCell(p.stacksTo(1), CELL_COMPONENT_256K, ITEM_CELL_HOUSING, 2.5f, 256, 2048, 63, AEKeyType.items()));

    ///
    /// UNSUPPORTED DEV TOOLS
    ///

    public static final ItemDefinition<WrappedGenericStack> WRAPPED_GENERIC_STACK = item("Wrapped Generic Stack", AEItemIds.WRAPPED_GENERIC_STACK, WrappedGenericStack::new);

    // spotless:on

    public static List<ItemDefinition<?>> getItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    private static <T extends Item> ColoredItemDefinition<T> createColoredItems(String name,
            Map<AEColor, ResourceLocation> ids,
            BiFunction<FabricItemSettings, AEColor, T> factory) {
        var colors = new ColoredItemDefinition<T>();
        for (var entry : ids.entrySet()) {
            String fullName;
            if (entry.getKey() == AEColor.TRANSPARENT) {
                fullName = name;
            } else {
                fullName = entry.getKey().getEnglishName() + " " + name;
            }
            colors.add(entry.getKey(), entry.getValue(),
                    item(fullName, entry.getValue(), p -> factory.apply(p, entry.getKey())));
        }
        return colors;
    }

    static <T extends Item> ItemDefinition<T> item(String name, ResourceLocation id,
            Function<FabricItemSettings, T> factory) {
        return item(name, id, factory, CreativeTab.INSTANCE);
    }

    static <T extends Item> ItemDefinition<T> item(String name, ResourceLocation id,
            Function<FabricItemSettings, T> factory,
            CreativeModeTab group) {

        FabricItemSettings p = new FabricItemSettings().group(group);

        T item = factory.apply(p);

        ItemDefinition<T> definition = new ItemDefinition<>(name, id, item);

        if (group == CreativeTab.INSTANCE) {
            CreativeTab.add(definition);
        }

        ITEMS.add(definition);

        return definition;
    }

    // Used to control in which order static constructors are called
    public static void init() {
    }

}
