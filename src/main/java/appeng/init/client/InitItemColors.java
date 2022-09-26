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

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import appeng.api.util.AEColor;
import appeng.client.render.StaticItemColor;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import appeng.items.parts.ColoredPartItem;
import appeng.items.parts.PartItem;
import appeng.items.storage.BasicStorageCell;

public final class InitItemColors {
    private InitItemColors() {
    }

    public static void init(Registry itemColors) {
        itemColors.register(new StaticItemColor(AEColor.TRANSPARENT), AEBlocks.SECURITY_STATION.asItem());
        // I checked, the ME chest doesn't keep its color in item form
        itemColors.register(new StaticItemColor(AEColor.TRANSPARENT), AEBlocks.CHEST.asItem());

        itemColors.register(BasicStorageCell::getColor, AEItems.ITEM_CELL_1K,
                AEItems.ITEM_CELL_4K,
                AEItems.ITEM_CELL_16K,
                AEItems.ITEM_CELL_64K,
                AEItems.ITEM_CELL_256K);

        // Automatically register colors for certain items we register
        for (ItemDefinition<?> definition : AEItems.getItems()) {
            Item item = definition.asItem();
            if (item instanceof PartItem) {
                AEColor color = AEColor.TRANSPARENT;
                if (item instanceof ColoredPartItem) {
                    color = ((ColoredPartItem<?>) item).getColor();
                }
                itemColors.register(new StaticItemColor(color), item);
            }
        }
    }

    @FunctionalInterface
    public interface Registry {
        void register(ItemColor itemColor, ItemLike... itemLikes);
    }
}
