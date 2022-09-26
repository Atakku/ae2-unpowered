/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2021, TeamAppliedEnergistics, All rights reserved.
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

package appeng.init.internal;

import java.util.List;

import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.core.localization.GuiText;

public final class InitUpgrades {

    private InitUpgrades() {
    }

    public static void init() {
        // Block and part interface have different translation keys, but support the
        // same upgrades
        String interfaceGroup = GuiText.Interface.getTranslationKey();
        String itemIoBusGroup = GuiText.IOBuses.getTranslationKey();
        String storageCellGroup = GuiText.StorageCells.getTranslationKey();

        // Interface
        Upgrades.add(AEItems.CRAFTING_CARD, AEParts.INTERFACE, 1, interfaceGroup);
        Upgrades.add(AEItems.CRAFTING_CARD, AEBlocks.INTERFACE, 1, interfaceGroup);
        Upgrades.add(AEItems.FUZZY_CARD, AEParts.INTERFACE, 1, interfaceGroup);
        Upgrades.add(AEItems.FUZZY_CARD, AEBlocks.INTERFACE, 1, interfaceGroup);

        // IO Port!
        Upgrades.add(AEItems.SPEED_CARD, AEBlocks.IO_PORT, 3);
        Upgrades.add(AEItems.REDSTONE_CARD, AEBlocks.IO_PORT, 1);

        // Level Emitter!
        Upgrades.add(AEItems.FUZZY_CARD, AEParts.LEVEL_EMITTER, 1);
        Upgrades.add(AEItems.CRAFTING_CARD, AEParts.LEVEL_EMITTER, 1);

        // Import Bus
        Upgrades.add(AEItems.FUZZY_CARD, AEParts.IMPORT_BUS, 1, itemIoBusGroup);
        Upgrades.add(AEItems.REDSTONE_CARD, AEParts.IMPORT_BUS, 1, itemIoBusGroup);
        Upgrades.add(AEItems.CAPACITY_CARD, AEParts.IMPORT_BUS, 2, itemIoBusGroup);
        Upgrades.add(AEItems.SPEED_CARD, AEParts.IMPORT_BUS, 4, itemIoBusGroup);
        Upgrades.add(AEItems.INVERTER_CARD, AEParts.IMPORT_BUS, 1, itemIoBusGroup);

        // Export Bus
        Upgrades.add(AEItems.FUZZY_CARD, AEParts.EXPORT_BUS, 1, itemIoBusGroup);
        Upgrades.add(AEItems.REDSTONE_CARD, AEParts.EXPORT_BUS, 1, itemIoBusGroup);
        Upgrades.add(AEItems.CAPACITY_CARD, AEParts.EXPORT_BUS, 2, itemIoBusGroup);
        Upgrades.add(AEItems.SPEED_CARD, AEParts.EXPORT_BUS, 4, itemIoBusGroup);
        Upgrades.add(AEItems.CRAFTING_CARD, AEParts.EXPORT_BUS, 1, itemIoBusGroup);

        // Storage Cells
        var itemCells = List.of(
                AEItems.ITEM_CELL_1K, AEItems.ITEM_CELL_4K, AEItems.ITEM_CELL_16K, AEItems.ITEM_CELL_64K,
                AEItems.ITEM_CELL_256K);
        for (var itemCell : itemCells) {
            Upgrades.add(AEItems.FUZZY_CARD, itemCell, 1, storageCellGroup);
            Upgrades.add(AEItems.INVERTER_CARD, itemCell, 1, storageCellGroup);
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, itemCell, 1, storageCellGroup);
            Upgrades.add(AEItems.VOID_CARD, itemCell, 1, storageCellGroup);
        }

        Upgrades.add(AEItems.FUZZY_CARD, AEItems.VIEW_CELL, 1);
        Upgrades.add(AEItems.INVERTER_CARD, AEItems.VIEW_CELL, 1);

        // Storage Bus
        Upgrades.add(AEItems.FUZZY_CARD, AEParts.STORAGE_BUS, 1);
        Upgrades.add(AEItems.INVERTER_CARD, AEParts.STORAGE_BUS, 1);
        Upgrades.add(AEItems.CAPACITY_CARD, AEParts.STORAGE_BUS, 5);

        // Molecular Assembler
        Upgrades.add(AEItems.SPEED_CARD, AEBlocks.MOLECULAR_ASSEMBLER, 5);

        // Inscriber
        Upgrades.add(AEItems.SPEED_CARD, AEBlocks.INSCRIBER, 3);
    }

}
