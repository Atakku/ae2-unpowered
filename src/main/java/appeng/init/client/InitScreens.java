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

package appeng.init.client;

import java.util.IdentityHashMap;
import java.util.Map;

import com.google.common.annotations.VisibleForTesting;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.implementations.CellWorkbenchScreen;
import appeng.client.gui.implementations.ChestScreen;
import appeng.client.gui.implementations.DriveScreen;
import appeng.client.gui.implementations.IOBusScreen;
import appeng.client.gui.implementations.IOPortScreen;
import appeng.client.gui.implementations.InscriberScreen;
import appeng.client.gui.implementations.InterfaceScreen;
import appeng.client.gui.implementations.MolecularAssemblerScreen;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.implementations.PriorityScreen;
import appeng.client.gui.implementations.QuartzKnifeScreen;
import appeng.client.gui.implementations.SecurityStationScreen;
import appeng.client.gui.implementations.SkyChestScreen;
import appeng.client.gui.implementations.StorageBusScreen;
import appeng.client.gui.implementations.StorageLevelEmitterScreen;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.crafting.CraftAmountScreen;
import appeng.client.gui.me.crafting.CraftConfirmScreen;
import appeng.client.gui.me.crafting.CraftingCPUScreen;
import appeng.client.gui.me.crafting.CraftingStatusScreen;
import appeng.client.gui.me.crafting.SetStockAmountScreen;
import appeng.client.gui.me.interfaceterminal.InterfaceTerminalScreen;
import appeng.client.gui.me.items.CraftingTermScreen;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.style.StyleManager;
import appeng.menu.AEBaseMenu;
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
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.crafting.CraftAmountMenu;
import appeng.menu.me.crafting.CraftConfirmMenu;
import appeng.menu.me.crafting.CraftingStatusMenu;
import appeng.menu.me.items.*;

/**
 * The server sends the client a menu identifier, which the client then maps onto a screen using {@link MenuScreens}.
 * This class registers our screens.
 */
public final class InitScreens {

    @VisibleForTesting
    static final Map<MenuType<?>, String> MENU_STYLES = new IdentityHashMap<>();

    private InitScreens() {
    }

    public static void init() {
        register(SkyChestMenu.TYPE, SkyChestScreen::new, "/screens/sky_chest.json");
        register(ChestMenu.TYPE, ChestScreen::new, "/screens/chest.json");
        register(QuartzKnifeMenu.TYPE, QuartzKnifeScreen::new, "/screens/quartz_knife.json");
        register(DriveMenu.TYPE, DriveScreen::new, "/screens/drive.json");
        register(InterfaceMenu.TYPE, InterfaceScreen::new, "/screens/interface.json");
        register(IOBusMenu.EXPORT_TYPE, IOBusScreen::new, "/screens/export_bus.json");
        register(IOBusMenu.IMPORT_TYPE, IOBusScreen::new, "/screens/import_bus.json");
        register(IOPortMenu.TYPE, IOPortScreen::new, "/screens/io_port.json");
        register(StorageBusMenu.TYPE, StorageBusScreen::new, "/screens/storage_bus.json");
        register(SetStockAmountMenu.TYPE, SetStockAmountScreen::new, "/screens/set_stock_amount.json");
        register(PriorityMenu.TYPE, PriorityScreen::new, "/screens/priority.json");
        register(StorageLevelEmitterMenu.TYPE, StorageLevelEmitterScreen::new, "/screens/level_emitter.json");
        register(InscriberMenu.TYPE, InscriberScreen::new, "/screens/inscriber.json");
        register(CellWorkbenchMenu.TYPE, CellWorkbenchScreen::new, "/screens/cell_workbench.json");
        register(PatternProviderMenu.TYPE, PatternProviderScreen::new, "/screens/pattern_provider.json");
        register(MolecularAssemblerMenu.TYPE, MolecularAssemblerScreen::new, "/screens/molecular_assembler.json");
        register(CraftAmountMenu.TYPE, CraftAmountScreen::new, "/screens/craft_amount.json");
        register(CraftConfirmMenu.TYPE, CraftConfirmScreen::new, "/screens/craft_confirm.json");
        InitScreens.<InterfaceTerminalMenu, InterfaceTerminalScreen<InterfaceTerminalMenu>>register(
                InterfaceTerminalMenu.TYPE, InterfaceTerminalScreen::new, "/screens/pattern_access_terminal.json");
        register(CraftingStatusMenu.TYPE, CraftingStatusScreen::new, "/screens/crafting_status.json");

        // Terminals
        InitScreens.<MEStorageMenu, MEStorageScreen<MEStorageMenu>>register(
                MEStorageMenu.TYPE,
                MEStorageScreen::new,
                "/screens/terminals/terminal.json");
        InitScreens.<MEStorageMenu, MEStorageScreen<MEStorageMenu>>register(
                MEStorageMenu.WIRELESS_TYPE,
                MEStorageScreen::new,
                "/screens/terminals/wireless_terminal.json");
        register(SecurityStationMenu.TYPE,
                SecurityStationScreen::new,
                "/screens/terminals/security_station.json");
        InitScreens.<CraftingTermMenu, CraftingTermScreen<CraftingTermMenu>>register(
                CraftingTermMenu.TYPE,
                CraftingTermScreen::new,
                "/screens/terminals/crafting_terminal.json");
        InitScreens.<WirelessCraftingTermMenu, CraftingTermScreen<WirelessCraftingTermMenu>>register(
                WirelessCraftingTermMenu.TYPE,
                CraftingTermScreen::new,
                "/screens/terminals/crafting_terminal.json");
        InitScreens.<PatternEncodingTermMenu, PatternEncodingTermScreen<PatternEncodingTermMenu>>register(
                PatternEncodingTermMenu.TYPE,
                PatternEncodingTermScreen::new,
                "/screens/terminals/pattern_encoding_terminal.json");
    }

    /**
     * Registers a screen for a given menu and ensures the given style is applied after opening the screen.
     */
    public static <M extends AEBaseMenu, U extends AEBaseScreen<M>> void register(MenuType<M> type,
            StyledScreenFactory<M, U> factory,
            String stylePath) {
        MENU_STYLES.put(type, stylePath);
        ScreenRegistry.<M, U>register(type, (menu, playerInv, title) -> {
            var style = StyleManager.loadStyleDoc(stylePath);

            return factory.create(menu, playerInv, title, style);
        });
    }

    /**
     * A type definition that matches the constructors of our screens, which take an additional {@link ScreenStyle}
     * argument.
     */
    @FunctionalInterface
    public interface StyledScreenFactory<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
        U create(T t, Inventory pi, Component title, ScreenStyle style);
    }

}
