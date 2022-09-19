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

package appeng.items.tools.powered.powersink;


import java.util.function.DoubleSupplier;

import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import appeng.items.AEBaseItem;

public abstract class AEBasePoweredItem extends AEBaseItem {
    // Any energy capacity below this threshold will be clamped to zero
    private static final double MIN_POWER = 0.0001;
    private static final String CURRENT_POWER_NBT_KEY = "internalCurrentPower";
    private static final String MAX_POWER_NBT_KEY = "internalMaxPower";
    private final DoubleSupplier powerCapacity;

    public AEBasePoweredItem(DoubleSupplier powerCapacity, Item.Properties props) {
        super(props);
        this.powerCapacity = powerCapacity;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        super.fillItemCategory(group, items);

        if (this.allowdedIn(group)) {
            final ItemStack charged = new ItemStack(this, 1);
            items.add(charged);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        double filled = 1;
        return Mth.clamp((int) (filled * 13), 0, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        // This is the standard green color of full durability bars
        return Mth.hsvToRgb(1 / 3.0F, 1.0F, 1.0F);
    }

    /**
     * Allows items to change the max power of their stacks without incurring heavy deserialization cost every time it's
     * accessed.
     */
    protected final void setAEMaxPower(ItemStack stack, double maxPower) {
        var defaultCapacity = powerCapacity.getAsDouble();
        if (Math.abs(maxPower - defaultCapacity) < MIN_POWER) {
            stack.removeTagKey(MAX_POWER_NBT_KEY);
            maxPower = defaultCapacity;
        } else {
            stack.getOrCreateTag().putDouble(MAX_POWER_NBT_KEY, maxPower);
        }
    }

    /**
     * Changes the maximum power of the chargeable item based on a multiplier for the configured default power. The
     * multiplier is clamped to [1,100]
     */
    protected final void setAEMaxPowerMultiplier(ItemStack stack, int multiplier) {
        multiplier = Mth.clamp(multiplier, 1, 100);
        if (multiplier == 1) {
            resetAEMaxPower(stack);
        } else {
            setAEMaxPower(stack, multiplier * powerCapacity.getAsDouble());
        }
    }

    /**
     * Clears any custom maximum power from the given stack.
     */
    protected final void resetAEMaxPower(ItemStack stack) {
        stack.removeTagKey(MAX_POWER_NBT_KEY);
    }

    protected final void setAECurrentPower(ItemStack stack, double power) {
        if (power < MIN_POWER) {
            stack.removeTagKey(CURRENT_POWER_NBT_KEY);
        } else {
            stack.getOrCreateTag().putDouble(CURRENT_POWER_NBT_KEY, power);
        }
    }
}
