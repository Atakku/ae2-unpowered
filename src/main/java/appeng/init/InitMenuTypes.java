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

package appeng.init;

import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import appeng.menu.implementations.CellWorkbenchMenu;
import appeng.menu.implementations.ChestMenu;
import appeng.menu.implementations.DriveMenu;
import appeng.menu.implementations.IOBusMenu;
import appeng.menu.implementations.IOPortMenu;
import appeng.menu.implementations.InscriberMenu;
import appeng.menu.implementations.InterfaceMenu;
import appeng.menu.implementations.InterfaceTerminalMenu;
import appeng.menu.implementations.MolecularAssemblerMenu;
import appeng.menu.implementations.PatternProviderMenu;
import appeng.menu.implementations.PriorityMenu;
import appeng.menu.implementations.QuartzKnifeMenu;
import appeng.menu.implementations.SecurityStationMenu;
import appeng.menu.implementations.SetStockAmountMenu;
import appeng.menu.implementations.SkyChestMenu;
import appeng.menu.implementations.StorageBusMenu;
import appeng.menu.implementations.StorageLevelEmitterMenu;
import appeng.menu.me.crafting.CraftAmountMenu;
import appeng.menu.me.crafting.CraftConfirmMenu;
import appeng.menu.me.crafting.CraftingCPUMenu;
import appeng.menu.me.crafting.CraftingStatusMenu;
import appeng.menu.me.items.*;

public final class InitMenuTypes {

    private InitMenuTypes() {
    }

    public static void init(Registry<MenuType<?>> registry) {
        registerAll(registry,
                CellWorkbenchMenu.TYPE,
                ChestMenu.TYPE,
                CraftAmountMenu.TYPE,
                CraftConfirmMenu.TYPE,
                CraftingCPUMenu.TYPE,
                CraftingStatusMenu.TYPE,
                CraftingTermMenu.TYPE,
                DriveMenu.TYPE,
                IOBusMenu.EXPORT_TYPE,
                IOBusMenu.IMPORT_TYPE,
                IOPortMenu.TYPE,
                InscriberMenu.TYPE,
                InterfaceMenu.TYPE,
                InterfaceTerminalMenu.TYPE,
                MolecularAssemblerMenu.TYPE,
                PatternProviderMenu.TYPE,
                PatternEncodingTermMenu.TYPE,
                PriorityMenu.TYPE,
                QuartzKnifeMenu.TYPE,
                SecurityStationMenu.TYPE,
                SetStockAmountMenu.TYPE,
                SkyChestMenu.TYPE,
                StorageBusMenu.TYPE,
                StorageLevelEmitterMenu.TYPE,
                WirelessCraftingTermMenu.TYPE);
    }

    private static void registerAll(Registry<MenuType<?>> registry, MenuType<?>... types) {
        // Fabric registers the container types at creation time, we just do this
        // to ensure all static CTORs are called in a predictable manner
        for (var type : types) {
            if (registry.getResourceKey(type).isEmpty()) {
                throw new IllegalStateException("Menu Type " + type + " is not registered");
            }
        }
    }

}
