/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
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

package appeng.parts.reporting;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import appeng.api.config.Settings;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.TypeFilter;
import appeng.api.config.ViewItems;
import appeng.api.implementations.blockentities.IViewCellStorage;
import appeng.api.inventories.InternalInventory;
import appeng.api.parts.IPartItem;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.MEStorage;
import appeng.api.util.IConfigManager;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.menu.me.common.MEStorageMenu;
import appeng.util.ConfigManager;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.InternalInventoryHost;

/**
 * Anything resembling an network terminal with view cells can reuse this.
 * <p>
 * Note this applies only to terminals like the ME Terminal. It does not apply for more specialized terminals like the
 * Interface Terminal.
 *
 * @author AlgorithmX2
 * @author yueh
 * @version rv3
 * @since rv3
 */
public abstract class AbstractTerminalPart extends AbstractDisplayPart
        implements ITerminalHost, IViewCellStorage, InternalInventoryHost {

    private final IConfigManager cm = new ConfigManager(this::saveChanges);
    private final AppEngInternalInventory viewCell = new AppEngInternalInventory(this, 5);

    public AbstractTerminalPart(IPartItem<?> partItem) {
        super(partItem, true);

        this.cm.registerSetting(Settings.SORT_BY, SortOrder.NAME);
        this.cm.registerSetting(Settings.VIEW_MODE, ViewItems.ALL);
        this.cm.registerSetting(Settings.TYPE_FILTER, TypeFilter.ALL);
        this.cm.registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING);
    }

    @Override
    public void addAdditionalDrops(List<ItemStack> drops, boolean wrenched) {
        for (var is : this.viewCell) {
            if (!is.isEmpty()) {
                drops.add(is);
            }
        }
    }

    @Override
    public IConfigManager getConfigManager() {
        return this.cm;
    }

    @Override
    public void saveChanges() {
        this.getHost().markForSave();
    }

    @Override
    public void readFromNBT(CompoundTag data) {
        super.readFromNBT(data);
        this.cm.readFromNBT(data);
        this.viewCell.readFromNBT(data, "viewCell");
    }

    @Override
    public void writeToNBT(CompoundTag data) {
        super.writeToNBT(data);
        this.cm.writeToNBT(data);
        this.viewCell.writeToNBT(data, "viewCell");
    }

    @Override
    public boolean onPartActivate(Player player, InteractionHand hand, Vec3 pos) {
        if (!super.onPartActivate(player, hand, pos) && !player.level.isClientSide) {
            MenuOpener.open(getMenuType(player), player, MenuLocators.forPart(this));
        }
        return true;
    }

    @Override
    public void returnToMainMenu(Player player, ISubMenu subMenu) {
        MenuOpener.open(getMenuType(player), player, subMenu.getLocator());
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return new ItemStack(getPartItem());
    }

    public MenuType<?> getMenuType(Player player) {
        return MEStorageMenu.TYPE;
    }

    @Override
    public MEStorage getInventory() {
        var grid = getMainNode().getGrid();
        if (grid != null) {
            return grid.getStorageService().getInventory();
        }
        return null;
    }

    @Override
    public InternalInventory getViewCellStorage() {
        return this.viewCell;
    }

    @Override
    public void onChangeInventory(InternalInventory inv, int slot) {
        this.getHost().markForSave();
    }
}
