package appeng.util;

import javax.annotation.Nullable;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;

// Consider moving to API?
public interface IVariantConversion<V extends TransferVariant<?>> {
    IVariantConversion<ItemVariant> ITEM = new Item();

    AEKeyType getKeyType();

    V getVariant(@Nullable AEKey key);

    @Nullable
    AEKey getKey(V variant);

    default boolean variantMatches(AEKey key, V variant) {
        return getVariant(key).equals(variant);
    }

    long getBaseSlotSize(V variant);

    class Item implements IVariantConversion<ItemVariant> {
        @Override
        public AEKeyType getKeyType() {
            return AEKeyType.items();
        }

        @Override
        public ItemVariant getVariant(AEKey key) {
            return key instanceof AEItemKey itemKey ? itemKey.toVariant() : ItemVariant.blank();
        }

        @org.jetbrains.annotations.Nullable
        @Override
        public AEItemKey getKey(ItemVariant variant) {
            return AEItemKey.of(variant);
        }

        @Override
        public long getBaseSlotSize(ItemVariant variant) {
            return Math.min(64, variant.getItem().getMaxStackSize());
        }
    }
}
