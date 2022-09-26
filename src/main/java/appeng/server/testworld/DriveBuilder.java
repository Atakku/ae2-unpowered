package appeng.server.testworld;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import appeng.core.definitions.AEItems;
import appeng.me.cells.BasicCellInventory;
import appeng.me.helpers.BaseActionSource;

/**
 * A helper class for quickly and succintly building the contents of a drive for test plots and tests.
 */
public class DriveBuilder {
    private final List<ItemStack> cells;

    DriveBuilder(List<ItemStack> cells) {
        this.cells = cells;
    }

    public ItemCellBuilder addItemCell64k() {
        var cell = AEItems.ITEM_CELL_64K.stack();
        var cellInv = BasicCellInventory.createInventory(cell, null);
        cells.add(cell);
        return new ItemCellBuilder(cellInv);
    }

    public class CellBuilder {
        protected final MEStorage inv;

        public CellBuilder(MEStorage inv) {
            this.inv = inv;
        }

        public void add(GenericStack stack) {
            add(stack.what(), stack.amount());
        }

        public void add(AEKey what, long amount) {
            if (inv.insert(what, amount, Actionable.MODULATE, new BaseActionSource()) != amount) {
                throw new IllegalArgumentException("Couldn't insert " + amount + " of " + what);
            }
        }

        public DriveBuilder and() {
            return DriveBuilder.this;
        }
    }

    public class ItemCellBuilder extends CellBuilder {
        public ItemCellBuilder(BasicCellInventory inv) {
            super(inv);
        }

        public void add(ItemLike what, long amount) {
            add(AEItemKey.of(what.asItem()), amount);
        }
    }
}
