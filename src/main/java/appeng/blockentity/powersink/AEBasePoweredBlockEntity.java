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

package appeng.blockentity.powersink;

import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.networking.events.GridPowerStorageStateChanged.PowerEventType;
import appeng.blockentity.AEBaseInvBlockEntity;

public abstract class AEBasePoweredBlockEntity extends AEBaseInvBlockEntity
        implements IAEPowerStorage, IExternalPowerSink {

    // values that determine general function, are set by inheriting classes if
    // needed. These should generally remain static.
    private double internalMaxPower = 10000;
    private boolean internalPublicPowerStorage = false;
    private AccessRestriction internalPowerFlow = AccessRestriction.READ_WRITE;
    // the current power buffer.
    private double internalCurrentPower = 0;
    private static final Set<Direction> ALL_SIDES = ImmutableSet.copyOf(EnumSet.allOf(Direction.class));
    private Set<Direction> internalPowerSides = ALL_SIDES;
    // Cache the optional to not continuously re-allocate it or the supplier

    public AEBasePoweredBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    protected final Set<Direction> getPowerSides() {
        return this.internalPowerSides;
    }

    protected void setPowerSides(Set<Direction> sides) {
        this.internalPowerSides = ImmutableSet.copyOf(sides);
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        data.putDouble("internalCurrentPower", this.getInternalCurrentPower());
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);
        this.setInternalCurrentPower(data.getDouble("internalCurrentPower"));
    }

    protected double getFunnelPowerDemand(double maxRequired) {
        return this.getInternalMaxPower() - this.getInternalCurrentPower();
    }

    @Override
    public final double injectAEPower(double amt, Actionable mode) {
        if (amt < 0.000001) {
            return 0;
        }

        final double required = this.getAEMaxPower() - this.getAECurrentPower();
        final double insertable = Math.min(required, amt);

        if (mode == Actionable.MODULATE) {
            if (this.getInternalCurrentPower() < 0.01 && insertable > 0.01) {
                this.PowerEvent(PowerEventType.PROVIDE_POWER);
            }

            this.setInternalCurrentPower(this.getInternalCurrentPower() + insertable);
        }

        return amt - insertable;
    }

    protected void PowerEvent(PowerEventType x) {
        // nothing.
    }

    @Override
    public final double getAEMaxPower() {
        return this.getInternalMaxPower();
    }

    @Override
    public final double getAECurrentPower() {
        return this.getInternalCurrentPower();
    }

    @Override
    public final boolean isAEPublicPowerStorage() {
        return this.isInternalPublicPowerStorage();
    }

    @Override
    public final AccessRestriction getPowerFlow() {
        return this.getInternalPowerFlow();
    }

    public double getInternalCurrentPower() {
        return this.internalCurrentPower;
    }

    public void setInternalCurrentPower(double internalCurrentPower) {
        this.internalCurrentPower = internalCurrentPower;
    }

    public double getInternalMaxPower() {
        return this.internalMaxPower;
    }

    public void setInternalMaxPower(double internalMaxPower) {
        this.internalMaxPower = internalMaxPower;
    }

    private boolean isInternalPublicPowerStorage() {
        return this.internalPublicPowerStorage;
    }

    public void setInternalPublicPowerStorage(boolean internalPublicPowerStorage) {
        this.internalPublicPowerStorage = internalPublicPowerStorage;
    }

    private AccessRestriction getInternalPowerFlow() {
        return this.internalPowerFlow;
    }

    public void setInternalPowerFlow(AccessRestriction internalPowerFlow) {
        this.internalPowerFlow = internalPowerFlow;
    }
}
