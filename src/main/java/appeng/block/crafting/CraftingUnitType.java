package appeng.block.crafting;

import net.minecraft.world.item.Item;

import appeng.core.definitions.AEBlocks;

public enum CraftingUnitType implements ICraftingUnitType {
    UNIT(0),
    ACCELERATOR(0),
    STORAGE_1K(1),
    STORAGE_4K(4),
    STORAGE_16K(16),
    STORAGE_64K(64),
    STORAGE_256K(256),
    STORAGE_1M(1024),
    STORAGE_4M(4096),
    STORAGE_16M(16384),
    STORAGE_64M(65536),
    STORAGE_256M(262144),
    STORAGE_1G(1048576),
    STORAGE_4G(4194304),
    STORAGE_16G( 16777216),
    STORAGE_64G(67108864),
    STORAGE_256G(268435456),
    MONITOR(0);

    private final long storageKb;

    CraftingUnitType(long storageKb) {
        this.storageKb = storageKb;
    }

    @Override
    public long getStorageBytes() {
        return 1024 * this.storageKb;
    }

    @Override
    public int getAcceleratorThreads() {
        return this == ACCELERATOR ? 1 : 0;
    }

    @Override
    public Item getItemFromType() {
        var definition = switch (this) {
            case UNIT -> AEBlocks.CRAFTING_UNIT;
            case ACCELERATOR -> AEBlocks.CRAFTING_ACCELERATOR;
            case STORAGE_1K -> AEBlocks.CRAFTING_STORAGE_1K;
            case STORAGE_4K -> AEBlocks.CRAFTING_STORAGE_4K;
            case STORAGE_16K -> AEBlocks.CRAFTING_STORAGE_16K;
            case STORAGE_64K -> AEBlocks.CRAFTING_STORAGE_64K;
            case STORAGE_256K -> AEBlocks.CRAFTING_STORAGE_256K;
            case STORAGE_1M -> AEBlocks.CRAFTING_STORAGE_1M;
            case STORAGE_4M -> AEBlocks.CRAFTING_STORAGE_4M;
            case STORAGE_16M -> AEBlocks.CRAFTING_STORAGE_16M;
            case STORAGE_64M -> AEBlocks.CRAFTING_STORAGE_64M;
            case STORAGE_256M -> AEBlocks.CRAFTING_STORAGE_256M;
            case STORAGE_1G -> AEBlocks.CRAFTING_STORAGE_1G;
            case STORAGE_4G -> AEBlocks.CRAFTING_STORAGE_4G;
            case STORAGE_16G -> AEBlocks.CRAFTING_STORAGE_16G;
            case STORAGE_64G -> AEBlocks.CRAFTING_STORAGE_64G;
            case STORAGE_256G -> AEBlocks.CRAFTING_STORAGE_256G;
            case MONITOR -> AEBlocks.CRAFTING_MONITOR;
        };
        return definition.asItem();
    }
}
