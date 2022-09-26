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

package appeng.helpers;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import appeng.api.features.Locatables;
import appeng.api.implementations.blockentities.IAcessPoint;
import appeng.api.implementations.menuobjects.IPortableTerminal;
import appeng.api.implementations.menuobjects.ItemMenuHost;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.storage.IStorageService;
import appeng.api.storage.MEStorage;
import appeng.api.util.IConfigManager;
import appeng.blockentity.misc.SecurityStationBlockEntity;
import appeng.core.localization.PlayerMessages;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.menu.ISubMenu;

public class WirelessTerminalMenuHost extends ItemMenuHost implements IPortableTerminal, IActionHost {

    private final WirelessTerminalItem terminal;
    private final BiConsumer<Player, ISubMenu> returnToMainMenu;
    private IGrid targetGrid;
    private IStorageService sg;
    private IAcessPoint ap;

    public WirelessTerminalMenuHost(Player player, @Nullable Integer slot, ItemStack itemStack,
            BiConsumer<Player, ISubMenu> returnToMainMenu) {
        super(player, slot, itemStack);
        if (!(itemStack.getItem() instanceof WirelessTerminalItem wirelessTerminalItem)) {
            throw new IllegalArgumentException("Can only use this class with subclasses of WirelessTerminalItem");
        }
        this.terminal = wirelessTerminalItem;
        this.returnToMainMenu = returnToMainMenu;

        var gridKey = terminal.getGridKey(itemStack);
        if (gridKey.isEmpty()) {
            return;
        }

        var actionHost = Locatables.securityStations().get(player.level, gridKey.getAsLong());
        if (actionHost != null) {
            var n = actionHost.getActionableNode();
            if (n != null) {
                this.targetGrid = n.getGrid();
                this.sg = this.targetGrid.getStorageService();
            }
        }
    }

    @Override
    public MEStorage getInventory() {
        return this.sg != null ? this.sg.getInventory() : null;
    }

    @Override
    public IConfigManager getConfigManager() {
        return this.terminal.getConfigManager(getItemStack());
    }

    @Override
    public IGridNode getActionableNode() {
        this.checkAP();
        if (this.ap != null) {
            return this.ap.getActionableNode();
        }
        return null;
    }

    public boolean checkAP() {
        if (this.targetGrid != null) {
            if (this.ap != null) {
                return this.ap.getActionableNode().getGrid() == this.targetGrid && this.testAP(this.ap);
            }

            for (var ap : this.targetGrid.getMachines(SecurityStationBlockEntity.class)) {
                if (this.testAP(ap)) {
                    this.ap = ap;
                }
            }

            return this.ap != null;
        }
        return false;
    }

    protected boolean testAP(IAcessPoint ap) {
        return ap.isActive();
    }

    @Override
    public boolean onBroadcastChanges(AbstractContainerMenu menu) {
        return super.onBroadcastChanges(menu)
                && checkWirelessRange(menu);
    }

    /**
     * Can only be used with a host that extends {@link WirelessTerminalMenuHost}
     */
    private boolean checkWirelessRange(AbstractContainerMenu menu) {
        if (!checkAP()) {
            if (!isClientSide()) {
                getPlayer().sendMessage(PlayerMessages.OutOfRange.text(), Util.NIL_UUID);
            }
            return false;
        }
        return true;
    }

    @Override
    public void returnToMainMenu(Player player, ISubMenu subMenu) {
        returnToMainMenu.accept(player, subMenu);
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return getItemStack();
    }
}
