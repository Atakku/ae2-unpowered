package appeng.init;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;

import appeng.api.behaviors.GenericInternalInventory;
import appeng.api.implementations.blockentities.ICraftingMachine;
import appeng.api.inventories.PartApiLookup;
import appeng.api.storage.IStorageMonitorableAccessor;
import appeng.blockentity.AEBaseInvBlockEntity;
import appeng.blockentity.storage.ChestBlockEntity;
import appeng.core.definitions.AEBlockEntities;
import appeng.helpers.externalstorage.GenericStackItemStorage;
import appeng.parts.crafting.PatternProviderPart;
import appeng.parts.encoding.PatternEncodingTerminalPart;
import appeng.parts.misc.InterfacePart;
import appeng.parts.p2p.ItemP2PTunnelPart;

public final class InitApiLookup {

    private InitApiLookup() {
    }

    public static void init() {

        // Allow forwarding of API lookups to parts for the cable bus
        PartApiLookup.addHostType(AEBlockEntities.CABLE_BUS);

        // Forward to interfaces
        initInterface();
        initPatternProvider();
        initMEChest();
        initMisc();
        initP2P();

        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, direction) -> {
            if (blockEntity instanceof AEBaseInvBlockEntity baseInvBlockEntity) {
                return baseInvBlockEntity.getExposedInventoryForSide(direction).toStorage();
            }
            // Fall back to generic inv
            var genericInv = GenericInternalInventory.SIDED.find(world, pos, state, blockEntity, direction);
            if (genericInv != null) {
                return new GenericStackItemStorage(genericInv);
            }
            return null;
        });
    }

    private static void initP2P() {
        PartApiLookup.register(ItemStorage.SIDED, (part, context) -> part.getExposedApi(), ItemP2PTunnelPart.class);
    }

    private static void initInterface() {
        PartApiLookup.register(GenericInternalInventory.SIDED, (part, context) -> part.getInterfaceLogic().getStorage(),
                InterfacePart.class);
        GenericInternalInventory.SIDED.registerForBlockEntity(
                (blockEntity, context) -> blockEntity.getInterfaceLogic().getStorage(), AEBlockEntities.INTERFACE);

        PartApiLookup.register(IStorageMonitorableAccessor.SIDED,
                (part, context) -> part.getInterfaceLogic().getGridStorageAccessor(), InterfacePart.class);
        IStorageMonitorableAccessor.SIDED.registerForBlockEntity((blockEntity, context) -> {
            return blockEntity.getInterfaceLogic().getGridStorageAccessor();
        }, AEBlockEntities.INTERFACE);
    }

    private static void initPatternProvider() {
        PartApiLookup.register(GenericInternalInventory.SIDED, (part, context) -> part.getLogic().getReturnInv(),
                PatternProviderPart.class);
        GenericInternalInventory.SIDED.registerForBlockEntity(
                (blockEntity, context) -> blockEntity.getLogic().getReturnInv(), AEBlockEntities.PATTERN_PROVIDER);
    }

    private static void initMEChest() {
        FluidStorage.SIDED.registerForBlockEntity(ChestBlockEntity::getFluidHandler, AEBlockEntities.CHEST);
        IStorageMonitorableAccessor.SIDED.registerForBlockEntity(ChestBlockEntity::getMEHandler,
                AEBlockEntities.CHEST);
    }

    private static void initMisc() {
        ICraftingMachine.SIDED.registerSelf(AEBlockEntities.MOLECULAR_ASSEMBLER);
        PartApiLookup.register(ItemStorage.SIDED, (part, direction) -> part.getLogic().getBlankPatternInv().toStorage(),
                PatternEncodingTerminalPart.class);
    }
}
