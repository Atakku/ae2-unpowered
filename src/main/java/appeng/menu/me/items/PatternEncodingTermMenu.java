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

package appeng.menu.me.items;

import java.util.Arrays;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;

import appeng.api.config.SecurityPermissions;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.inventories.InternalInventory;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.Icon;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.core.definitions.AEItems;
import appeng.helpers.IMenuCraftingPacket;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.menu.SlotSemantics;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.slot.FakeSlot;
import appeng.menu.slot.PatternTermSlot;
import appeng.menu.slot.RestrictedInputSlot;
import appeng.parts.encoding.EncodingMode;
import appeng.parts.encoding.PatternEncodingLogic;
import appeng.util.ConfigInventory;

/**
 * Can only be used with a host that implements {@link PatternEncodingLogic}.
 *
 * @see PatternEncodingTermScreen
 */
public class PatternEncodingTermMenu extends MEStorageMenu implements IMenuCraftingPacket {

    private static final int CRAFTING_GRID_WIDTH = 3;
    private static final int CRAFTING_GRID_HEIGHT = 3;
    private static final int CRAFTING_GRID_SLOTS = CRAFTING_GRID_WIDTH * CRAFTING_GRID_HEIGHT;

    private static final String ACTION_SET_MODE = "setMode";
    private static final String ACTION_ENCODE = "encode";
    private static final String ACTION_CLEAR = "clear";
    private static final String ACTION_SET_SUBSTITUTION = "setSubstitution";
    private static final String ACTION_SET_FLUID_SUBSTITUTION = "setFluidSubstitution";
    private static final String ACTION_CYCLE_PROCESSING_OUTPUT = "cycleProcessingOutput";

    public static MenuType<PatternEncodingTermMenu> TYPE = MenuTypeBuilder
            .create(PatternEncodingTermMenu::new, IPatternTerminalMenuHost.class)
            .requirePermission(SecurityPermissions.CRAFT)
            .build("patternterm");

    private final PatternEncodingLogic encodingLogic;
    private final FakeSlot[] craftingGridSlots = new FakeSlot[9];
    private final FakeSlot[] processingInputSlots = new FakeSlot[18];
    private final FakeSlot[] processingOutputSlots = new FakeSlot[6];
    private final PatternTermSlot craftOutputSlot;
    private final RestrictedInputSlot blankPatternSlot;
    private final RestrictedInputSlot encodedPatternSlot;
    // 9x9 inventory wrapper to feed into the crafting mode slots

    private final ConfigInventory encodedInputsInv;
    private final ConfigInventory encodedOutputsInv;

    private CraftingRecipe currentRecipe;
    // The current mode is essentially the last-known client-side version of mode
    private EncodingMode currentMode;

    @GuiSync(97)
    public EncodingMode mode = EncodingMode.CRAFTING;
    @GuiSync(96)
    public boolean substitute = false;
    @GuiSync(95)
    public boolean substituteFluids = true;

    /**
     * Whether fluids can be substituted or not depends on the recipe. This set contains the slots of the crafting
     * matrix that support such substitution.
     */
    public IntSet slotsSupportingFluidSubstitution = new IntArraySet();

    public PatternEncodingTermMenu(int id, Inventory ip, IPatternTerminalMenuHost host) {
        this(TYPE, id, ip, host, true);
    }

    public PatternEncodingTermMenu(MenuType<?> menuType, int id, Inventory ip, IPatternTerminalMenuHost host,
            boolean bindInventory) {
        super(menuType, id, ip, host, bindInventory);
        this.encodingLogic = host.getLogic();
        this.encodedInputsInv = encodingLogic.getEncodedInputInv();
        this.encodedOutputsInv = encodingLogic.getEncodedOutputInv();

        // Wrappers for use with slots
        var encodedInputs = encodedInputsInv.createMenuWrapper();
        var encodedOutputs = encodedOutputsInv.createMenuWrapper();

        // Create the 3x3 crafting input grid for crafting mode
        for (int i = 0; i < CRAFTING_GRID_SLOTS; i++) {
            var slot = new FakeSlot(encodedInputs, i);
            slot.setHideAmount(true);
            this.addSlot(this.craftingGridSlots[i] = slot, SlotSemantics.CRAFTING_GRID);
        }
        // Create the output slot used for crafting mode patterns
        this.addSlot(this.craftOutputSlot = new PatternTermSlot(ip.player, this.getActionSource(),
                host.getInventory(), encodedInputs, this),
                SlotSemantics.CRAFTING_RESULT);
        this.craftOutputSlot.setIcon(null);

        // Create as many slots as needed for processing inputs and outputs
        for (int i = 0; i < processingInputSlots.length; i++) {
            this.addSlot(this.processingInputSlots[i] = new FakeSlot(encodedInputs, i),
                    SlotSemantics.PROCESSING_INPUTS);
        }
        for (int i = 0; i < this.processingOutputSlots.length; i++) {
            this.addSlot(this.processingOutputSlots[i] = new FakeSlot(encodedOutputs, i),
                    SlotSemantics.PROCESSING_OUTPUTS);
        }
        this.processingOutputSlots[0].setIcon(Icon.BACKGROUND_PRIMARY_OUTPUT);

        this.addSlot(this.blankPatternSlot = new RestrictedInputSlot(RestrictedInputSlot.PlacableItemType.BLANK_PATTERN,
                encodingLogic.getBlankPatternInv(), 0), SlotSemantics.BLANK_PATTERN);
        this.addSlot(
                this.encodedPatternSlot = new RestrictedInputSlot(RestrictedInputSlot.PlacableItemType.ENCODED_PATTERN,
                        encodingLogic.getEncodedPatternInv(), 0),
                SlotSemantics.ENCODED_PATTERN);

        this.encodedPatternSlot.setStackLimit(1);

        registerClientAction(ACTION_ENCODE, this::encode);
        registerClientAction(ACTION_CLEAR, this::clear);
        registerClientAction(ACTION_SET_MODE, EncodingMode.class, encodingLogic::setMode);
        registerClientAction(ACTION_SET_SUBSTITUTION, Boolean.class, encodingLogic::setSubstitution);
        registerClientAction(ACTION_SET_FLUID_SUBSTITUTION, Boolean.class, encodingLogic::setFluidSubstitution);
        registerClientAction(ACTION_CYCLE_PROCESSING_OUTPUT, this::cycleProcessingOutput);
    }

    @Override
    public void setItem(int slotID, int stateId, ItemStack stack) {
        super.setItem(slotID, stateId, stack);
        this.getAndUpdateOutput();
    }

    private ItemStack getAndUpdateOutput() {
        var level = this.getPlayerInventory().player.level;
        var ic = new CraftingContainer(this, CRAFTING_GRID_WIDTH, CRAFTING_GRID_HEIGHT);

        boolean invalidIngredients = false;
        for (int x = 0; x < ic.getContainerSize(); x++) {
            var stack = getEncodedCraftingIngredient(x);
            if (stack != null) {
                ic.setItem(x, stack);
            } else {
                invalidIngredients = true;
            }
        }

        if (this.currentRecipe == null || !this.currentRecipe.matches(ic, level)) {
            if (invalidIngredients) {
                this.currentRecipe = null;
            } else {
                this.currentRecipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, ic, level).orElse(null);
            }
            this.currentMode = this.mode;
        }

        final ItemStack is;

        if (this.currentRecipe == null) {
            is = ItemStack.EMPTY;
        } else {
            is = this.currentRecipe.assemble(ic);
        }

        this.craftOutputSlot.setDisplayedCraftingOutput(is);
        return is;
    }

    public void encode() {
        if (isClientSide()) {
            sendClientAction(ACTION_ENCODE);
            return;
        }

        ItemStack encodedPattern = encodePattern();
        if (encodedPattern != null) {
            var encodeOutput = this.encodedPatternSlot.getItem();

            // first check the output slots, should either be null, or a pattern (encoded or otherwise)
            if (!encodeOutput.isEmpty()
                    && !PatternDetailsHelper.isEncodedPattern(encodeOutput)
                    && !AEItems.BLANK_PATTERN.isSameAs(encodeOutput)) {
                return;
            } // if nothing is there we should snag a new pattern.
            else if (encodeOutput.isEmpty()) {
                var blankPattern = this.blankPatternSlot.getItem();
                if (!isPattern(blankPattern)) {
                    return; // no blanks.
                }

                // remove one, and clear the input slot.
                blankPattern.shrink(1);
                if (blankPattern.getCount() <= 0) {
                    this.blankPatternSlot.set(ItemStack.EMPTY);
                }
            }

            this.encodedPatternSlot.set(encodedPattern);
        } else {
            clearPattern();
        }
    }

    /**
     * Clears the pattern in the encoded pattern slot.
     */
    private void clearPattern() {
        var encodedPattern = this.encodedPatternSlot.getItem();
        if (PatternDetailsHelper.isEncodedPattern(encodedPattern)) {
            this.encodedPatternSlot.set(
                    AEItems.BLANK_PATTERN.stack(encodedPattern.getCount()));
        }
    }

    @Nullable
    private ItemStack encodePattern() {
        if (this.mode == EncodingMode.CRAFTING) {
            return encodeCraftingPattern();
        } else {
            return encodeProcessingPattern();
        }
    }

    @Nullable
    private ItemStack encodeCraftingPattern() {
        var ingredients = new ItemStack[CRAFTING_GRID_SLOTS];
        boolean valid = false;
        for (int x = 0; x < ingredients.length; x++) {
            ingredients[x] = getEncodedCraftingIngredient(x);
            if (ingredients[x] == null) {
                return null; // Invalid item
            } else if (!ingredients[x].isEmpty()) {
                // At least one input must be set, but it doesn't matter which one
                valid = true;
            }
        }
        if (!valid) {
            return null;
        }

        var result = this.getAndUpdateOutput();
        if (result.isEmpty() || currentRecipe == null) {
            return null;
        }

        return PatternDetailsHelper.encodeCraftingPattern(this.currentRecipe, ingredients, result, isSubstitute(),
                isSubstituteFluids());
    }

    @Nullable
    private ItemStack encodeProcessingPattern() {
        var inputs = new GenericStack[encodedInputsInv.size()];
        boolean valid = false;
        for (int slot = 0; slot < encodedInputsInv.size(); slot++) {
            inputs[slot] = encodedInputsInv.getStack(slot);
            if (inputs[slot] != null) {
                // At least one input must be set, but it doesn't matter which one
                valid = true;
            }
        }
        if (!valid) {
            return null;
        }

        var outputs = new GenericStack[encodedOutputsInv.size()];
        for (int slot = 0; slot < encodedOutputsInv.size(); slot++) {
            outputs[slot] = encodedOutputsInv.getStack(slot);
        }
        if (outputs[0] == null) {
            // The first output slot is required
            return null;
        }

        return PatternDetailsHelper.encodeProcessingPattern(inputs, outputs);
    }

    /**
     * Get potential crafting ingredient encoded in given slot, return null if something is encoded in the slot, but
     * it's not an item.
     */
    @Nullable
    private ItemStack getEncodedCraftingIngredient(int slot) {
        var what = encodedInputsInv.getKey(slot);
        if (what == null) {
            return ItemStack.EMPTY;
        } else if (what instanceof AEItemKey itemKey) {
            return itemKey.toStack(1);
        } else {
            return null; // There's something in this slot that's not an item
        }
    }

    private boolean isPattern(ItemStack output) {
        if (output.isEmpty()) {
            return false;
        }

        return AEItems.BLANK_PATTERN.isSameAs(output);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (isServerSide()) {
            if (this.mode != encodingLogic.getMode()) {
                this.setMode(encodingLogic.getMode());
            }

            this.substitute = encodingLogic.isSubstitution();
            this.substituteFluids = encodingLogic.isFluidSubstitution();
        }
    }

    @Override
    public void onServerDataSync() {
        super.onServerDataSync();

        // Update slot visibility
        for (var slot : craftingGridSlots) {
            slot.setActive(mode == EncodingMode.CRAFTING);
        }
        craftOutputSlot.setActive(mode == EncodingMode.CRAFTING);
        for (var slot : processingInputSlots) {
            slot.setActive(mode == EncodingMode.PROCESSING);
        }
        for (var slot : processingOutputSlots) {
            slot.setActive(mode == EncodingMode.PROCESSING);
        }

        if (this.currentMode != this.mode) {
            this.encodingLogic.setMode(this.mode);
            this.getAndUpdateOutput();
        }
    }

    @Override
    public void onSlotChange(Slot s) {
        if (s == this.encodedPatternSlot && isServerSide()) {
            this.broadcastChanges();
        }

        if (s == this.craftOutputSlot && isClientSide()) {
            this.getAndUpdateOutput();
        }
    }

    public void clear() {
        if (isClientSide()) {
            sendClientAction(ACTION_CLEAR);
            return;
        }

        encodedInputsInv.clear();
        encodedOutputsInv.clear();

        this.broadcastChanges();
        this.getAndUpdateOutput();
    }

    @Override
    public InternalInventory getCraftingMatrix() {
        return encodedInputsInv.createMenuWrapper().getSubInventory(0, CRAFTING_GRID_SLOTS);
    }

    @Override
    public boolean useRealItems() {
        return false;
    }

    public EncodingMode getMode() {
        return this.mode;
    }

    public void setMode(EncodingMode mode) {
        if (isClientSide()) {
            sendClientAction(ACTION_SET_MODE, mode);
        } else {
            this.mode = mode;
        }
    }

    public boolean isSubstitute() {
        return this.substitute;
    }

    public void setSubstitute(boolean substitute) {
        if (isClientSide()) {
            sendClientAction(ACTION_SET_SUBSTITUTION, substitute);
        } else {
            this.substitute = substitute;
        }
    }

    public boolean isSubstituteFluids() {
        return this.substituteFluids;
    }

    public void setSubstituteFluids(boolean substituteFluids) {
        if (isClientSide()) {
            sendClientAction(ACTION_SET_FLUID_SUBSTITUTION, substituteFluids);
        } else {
            this.substituteFluids = substituteFluids;
        }
    }

    @Override
    protected ItemStack transferStackToMenu(ItemStack input) {
        // try refilling the blank pattern slot
        if (blankPatternSlot.mayPlace(input)) {
            input = blankPatternSlot.safeInsert(input);
            if (input.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        // try refilling the encoded pattern slot
        if (encodedPatternSlot.mayPlace(input)) {
            input = encodedPatternSlot.safeInsert(input);
            if (input.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        return super.transferStackToMenu(input);
    }

    @Contract("null -> false")
    public boolean canModifyAmountForSlot(@Nullable Slot slot) {
        return isProcessingPatternSlot(slot) && slot.hasItem();
    }

    @Contract("null -> false")
    public boolean isProcessingPatternSlot(@Nullable Slot slot) {
        if (slot == null || mode != EncodingMode.PROCESSING) {
            return false;
        }

        for (var processingOutputSlot : processingOutputSlots) {
            if (processingOutputSlot == slot) {
                return true;
            }
        }

        for (var craftingSlot : processingInputSlots) {
            if (craftingSlot == slot) {
                return true;
            }
        }
        return false;
    }

    public FakeSlot[] getCraftingGridSlots() {
        return craftingGridSlots;
    }

    public FakeSlot[] getProcessingInputSlots() {
        return processingInputSlots;
    }

    public FakeSlot[] getProcessingOutputSlots() {
        return processingOutputSlots;
    }

    /**
     * Cycles the defined processing outputs around in case recipe transfer didn't put what the player considers the
     * primary output into the right slot.
     */
    public void cycleProcessingOutput() {
        if (isClientSide()) {
            sendClientAction(ACTION_CYCLE_PROCESSING_OUTPUT);
        } else {
            if (mode != EncodingMode.PROCESSING) {
                return;
            }

            var newOutputs = new ItemStack[getProcessingOutputSlots().length];
            for (int i = 0; i < processingOutputSlots.length; i++) {
                newOutputs[i] = ItemStack.EMPTY;
                if (!processingOutputSlots[i].getItem().isEmpty()) {
                    // Search for the next, skipping empty slots
                    for (int j = 1; j < processingOutputSlots.length; j++) {
                        var nextItem = processingOutputSlots[(i + j) % processingOutputSlots.length].getItem();
                        if (!nextItem.isEmpty()) {
                            newOutputs[i] = nextItem;
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < newOutputs.length; i++) {
                processingOutputSlots[i].set(newOutputs[i]);
            }
        }
    }

    // Can cycle if there is more than 1 processing output encoded
    public boolean canCycleProcessingOutputs() {
        return mode == EncodingMode.PROCESSING
                && Arrays.stream(processingOutputSlots).filter(s -> !s.getItem().isEmpty()).count() > 1;
    }
}
