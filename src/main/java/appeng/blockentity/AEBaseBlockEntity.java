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

package appeng.blockentity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.google.common.collect.Lists;

import io.netty.buffer.Unpooled;

import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import appeng.api.inventories.ISegmentedInventory;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.GridHelper;
import appeng.api.util.IConfigurableObject;
import appeng.api.util.IOrientable;
import appeng.block.AEBaseBlock;
import appeng.block.AEBaseEntityBlock;
import appeng.client.render.model.AEModelData;
import appeng.core.AELog;
import appeng.helpers.IConfigInvHost;
import appeng.helpers.ICustomNameObject;
import appeng.helpers.IPriorityHost;
import appeng.hooks.ticking.TickHandler;
import appeng.util.CustomNameUtil;
import appeng.util.Platform;
import appeng.util.SettingsFrom;
import appeng.util.helpers.ItemComparisonHelper;

public class AEBaseBlockEntity extends BlockEntity
        implements IOrientable, ICustomNameObject, ISegmentedInventory,
        RenderAttachmentBlockEntity {

    static {
        DeferredBlockEntityUnloader.register();
    }

    protected void onChunkUnloaded() {
    }

    private static final Map<BlockEntityType<?>, Item> REPRESENTATIVE_ITEMS = new HashMap<>();
    @Nullable
    private String customName;
    private Direction forward = Direction.NORTH;
    private Direction up = Direction.UP;
    private boolean setChangedQueued = false;
    /**
     * For diagnosing issues with the delayed block entity initialization, this tracks how often this BE has been queued
     * for defered initializiation using {@link appeng.api.networking.GridHelper#onFirstTick}.
     */
    private byte queuedForReady = 0;
    /**
     * Tracks how often {@link #onReady()} has been called. This should always be less than {@link #queuedForReady}, and
     * subsequently be equal.
     */
    private byte readyInvoked = 0;

    public AEBaseBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    public static void registerBlockEntityItem(BlockEntityType<?> type, Item wat) {
        REPRESENTATIVE_ITEMS.put(type, wat);
    }

    public boolean notLoaded() {
        return !this.level.hasChunkAt(this.worldPosition);
    }

    public BlockEntity getBlockEntity() {
        return this;
    }

    protected Item getItemFromBlockEntity() {
        return REPRESENTATIVE_ITEMS.getOrDefault(getType(), Items.AIR);
    }

    @Override
    public final void load(CompoundTag tag) {
        // On the client, this can either be data received as part of an initial chunk update,
        // or as part of a sole block entity data update.
        if (tag.contains("#upd", Tag.TAG_BYTE_ARRAY) && tag.size() == 1) {
            var updateData = tag.getByteArray("#upd");
            if (readUpdateData(new FriendlyByteBuf(Unpooled.wrappedBuffer(updateData)))) {
                // Triggers a chunk re-render if the level is already loaded
                if (level != null) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
                }
            }
            return;
        }

        super.load(tag);
        loadTag(tag);
    }

    public void loadTag(CompoundTag data) {
        if (data.contains("customName")) {
            this.customName = data.getString("customName");
        } else {
            this.customName = null;
        }

        try {
            if (this.canBeRotated()) {
                this.forward = Direction.valueOf(data.getString("forward"));
                this.up = Direction.valueOf(data.getString("up"));
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);

        if (this.canBeRotated()) {
            data.putString("forward", this.getForward().name());
            data.putString("up", this.getUp().name());
        }

        if (this.customName != null) {
            data.putString("customName", this.customName);
        }
    }

    /**
     * Deferred initialization when block entities actually start first ticking in a chunk. The block entity needs to
     * override {@link #clearRemoved()} and call {@link #scheduleInit()} to make this work.
     */
    @OverridingMethodsMustInvokeSuper
    public void onReady() {
        readyInvoked++;
    }

    protected void scheduleInit() {
        queuedForReady++;
        GridHelper.onFirstTick(this, AEBaseBlockEntity::onReady);
    }

    /**
     * This builds a tag with the actual data that should be sent to the client for update syncs. If the block entity
     * doesn't need update syncs, it returns null.
     */
    @Override
    public CompoundTag getUpdateTag() {
        var data = new CompoundTag();

        var stream = new FriendlyByteBuf(Unpooled.buffer());
        this.writeToStream(stream);

        stream.capacity(stream.readableBytes());
        data.putByteArray("#upd", stream.array());
        return data;
    }

    private boolean readUpdateData(FriendlyByteBuf stream) {
        boolean output = false;

        try {
            output = this.readFromStream(stream);
        } catch (Throwable t) {
            AELog.warn(t);
        }

        return output;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected boolean readFromStream(FriendlyByteBuf data) {
        if (this.canBeRotated()) {
            final Direction old_Forward = this.forward;
            final Direction old_Up = this.up;

            final byte orientation = data.readByte();
            this.forward = Direction.values()[orientation & 0x7];
            this.up = Direction.values()[orientation >> 3];

            return this.forward != old_Forward || this.up != old_Up;
        }
        return false;
    }

    protected void writeToStream(FriendlyByteBuf data) {
        if (this.canBeRotated()) {
            final byte orientation = (byte) (this.up.ordinal() << 3 | this.forward.ordinal());
            data.writeByte(orientation);
        }
    }

    public void markForUpdate() {
        // TODO: Optimize Network Load
        if (this.level != null && !this.isRemoved() && !notLoaded()) {

            boolean alreadyUpdated = false;
            // Let the block update its own state with our internal state changes
            BlockState currentState = getBlockState();
            if (currentState.getBlock() instanceof AEBaseEntityBlock<?>block) {
                BlockState newState = block.getBlockEntityBlockState(currentState, this);
                if (currentState != newState) {
                    AELog.blockUpdate(this.worldPosition, currentState, newState, this);
                    this.level.setBlockAndUpdate(worldPosition, newState);
                    alreadyUpdated = true;
                }
            }

            if (!alreadyUpdated) {
                this.level.sendBlockUpdated(this.worldPosition, currentState, currentState, Block.UPDATE_NEIGHBORS);
            }
        }
    }

    /**
     * By default all blocks can have orientation, this handles saving, and loading, as well as synchronization.
     *
     * @return true if block entity can be rotated
     */
    @Override
    public boolean canBeRotated() {
        return true;
    }

    @Override
    public Direction getForward() {
        return this.forward;
    }

    @Override
    public Direction getUp() {
        return this.up;
    }

    @Override
    public void setOrientation(Direction inForward, Direction inUp) {
        this.forward = inForward;
        this.up = inUp;
        this.markForUpdate();
        Platform.notifyBlocksOfNeighbors(this.level, this.worldPosition);
        this.saveChanges();
    }

    /**
     * null means nothing to store...
     *
     * @param mode   source of settings
     * @param player The (optional) player, who is exporting the settings
     */
    @OverridingMethodsMustInvokeSuper
    public void exportSettings(SettingsFrom mode, CompoundTag output, @Nullable Player player) {
        CustomNameUtil.setCustomName(output, customName);

        if (mode == SettingsFrom.MEMORY_CARD) {
            if (this instanceof IConfigurableObject configurableObject) {
                configurableObject.getConfigManager().writeToNBT(output);
            }

            if (this instanceof IPriorityHost pHost) {
                output.putInt("priority", pHost.getPriority());
            }

            if (this instanceof IConfigInvHost configInvHost) {
                configInvHost.getConfig().writeToChildTag(output, "config");
            }
        }
    }

    /**
     * Depending on the mode, different settings will be accepted.
     *
     * @param input  source of settings
     * @param player The (optional) player, who is importing the settings
     */
    @OverridingMethodsMustInvokeSuper
    public void importSettings(SettingsFrom mode, CompoundTag input, @Nullable Player player) {
        if (this instanceof IConfigurableObject configurableObject) {
            configurableObject.getConfigManager().readFromNBT(input);
        }

        if (this instanceof IPriorityHost pHost) {
            pHost.setPriority(input.getInt("priority"));
        }

        if (this instanceof IConfigInvHost configInvHost) {
            configInvHost.getConfig().readFromChildTag(input, "config");
        }
    }

    /**
     * returns the contents of the block entity but not the block itself, to drop into the world
     *
     * @param level level
     * @param pos   block position
     * @param drops drops of block entity
     */
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
    }

    @Override
    public Component getCustomInventoryName() {
        return new TextComponent(
                this.hasCustomInventoryName() ? this.customName : this.getClass().getSimpleName());
    }

    @Override
    public boolean hasCustomInventoryName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void securityBreak() {
        this.level.destroyBlock(this.worldPosition, true);
    }

    /**
     * Checks if this block entity is remote (we are running on the logical client side).
     */
    public boolean isClientSide() {
        Level level = getLevel();
        return level == null || level.isClientSide();
    }

    public void saveChanges() {
        if (this.level == null) {
            return;
        }

        // Clientside is marked immediately as dirty as there is no queue processing
        // Serverside is only queued once per tick to avoid costly operations
        // TODO: Evaluate if this is still necessary
        if (this.level.isClientSide) {
            this.setChanged();
        } else {
            this.level.blockEntityChanged(this.worldPosition);
            if (!this.setChangedQueued) {
                TickHandler.instance().addCallable(null, this::setChangedAtEndOfTick);
                this.setChangedQueued = true;
            }
        }
    }

    private Object setChangedAtEndOfTick(Level level) {
        this.setChanged();
        this.setChangedQueued = false;
        return null;
    }

    public void setName(String name) {
        this.customName = name;
    }

    @Override
    @Nullable
    @OverridingMethodsMustInvokeSuper
    public InternalInventory getSubInventory(ResourceLocation id) {
        return null;
    }

    @Nullable
    @Override
    public Object getRenderAttachmentData() {
        return new AEModelData(up, forward);
    }

    /**
     * Called when a player uses a wrench on this block entity to disassemble it.
     */
    public InteractionResult disassembleWithWrench(Player player, Level level, BlockHitResult hitResult) {
        var pos = hitResult.getBlockPos();
        final BlockState blockState = level.getBlockState(pos);
        final Block block = blockState.getBlock();

        var itemDropCandidates = Platform.getBlockDrops(level, pos);
        var op = new ItemStack(getBlockState().getBlock());

        for (var ol : itemDropCandidates) {
            if (ItemComparisonHelper.isEqualItemType(ol, op)) {
                var tag = new CompoundTag();
                exportSettings(SettingsFrom.DISMANTLE_ITEM, tag, player);
                if (!tag.isEmpty()) {
                    ol.setTag(tag);
                }
            }
        }

        block.playerWillDestroy(level, pos, blockState, player);
        level.removeBlock(pos, false);
        block.destroy(level, pos, getBlockState());

        var itemsToDrop = Lists.newArrayList(itemDropCandidates);
        Platform.spawnDrops(level, pos, itemsToDrop);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    /**
     * Called when a player uses a wrench on this block entity to rotate it.
     */
    public InteractionResult rotateWithWrench(Player player, Level level, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();

        var block = getBlockState().getBlock();
        if (block instanceof AEBaseBlock aeBlock) {
            if (aeBlock.rotateAroundFaceAxis(level, pos, hitResult.getDirection())) {
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.PASS;
    }

    public byte getQueuedForReady() {
        return queuedForReady;
    }

    public byte getReadyInvoked() {
        return readyInvoked;
    }
}
