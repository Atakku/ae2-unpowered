package appeng.server.testworld;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import appeng.api.config.Actionable;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEItemKey;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.me.helpers.BaseActionSource;
import appeng.menu.AutoCraftingMenu;

public final class AutoCraftingTestPlot {
    private AutoCraftingTestPlot() {
    }

    public static void create(PlotBuilder plot) {
        // Cable to CPU / Access / Storage
        plot.cable("4 0 [1,5]");
        plot.cable("[3,6] 0 1");
        plot.block("[4,5] [0,1] [4,5]", AEBlocks.CONTROLLER);

        // Crafting cube
        craftingCube(plot.offset(1, 0, 1));

        // Stack of pattern providers with 6 assemblers each
        plot.cable("[6,8] 0 5");
        var assemblerStack = plot.offset(8, 1, 5);
        for (var i = 0; i < 8; i++) {
            assemblerFlower(assemblerStack.offset(0, i * 3, 0));
        }

        // Storage and access
        plot.blockEntity("7 0 1", AEBlocks.DRIVE, drive -> {
            drive.getInternalInventory().addItems(AEItems.ITEM_CELL_64K.stack());
            drive.getInternalInventory().addItems(AEItems.ITEM_CELL_64K.stack());
        });
        plot.part("6 0 1", Direction.NORTH, AEParts.PATTERN_ENCODING_TERMINAL, term -> {
            var inv = term.getLogic().getBlankPatternInv();
            inv.addItems(AEItems.BLANK_PATTERN.stack(64));
        });
        plot.part("5 0 1", Direction.NORTH, AEParts.PATTERN_ACCESS_TERMINAL);
        plot.part("4 0 1", Direction.NORTH, AEParts.TERMINAL);
        plot.part("3 0 1", Direction.NORTH, AEParts.CRAFTING_TERMINAL);

        // Subsystem to export crafted items
        buildChestCraftingExport(plot.offset(5, 0, 7));

        // Connect subsystems with dense cable
        plot.cable("4 0 [6,9]", AEParts.SMART_DENSE_CABLE);

        // Add post-processing action once the grid is up and running
        plot.afterGridInitAt("4 0 4", (grid, gridNode) -> {
            var level = gridNode.getLevel();
            var patterns = new ArrayList<ItemStack>();
            // Crafting pattern table with substitutions enabled and some items that are NOT in storage
            patterns.add(encodeCraftingPattern(
                    level,
                    new Object[] {
                            Items.OAK_PLANKS, Items.OAK_PLANKS, null,
                            Items.OAK_PLANKS, Items.CRIMSON_PLANKS, null,
                            null, null, null
                    },
                    true,
                    false));
            // Crafting pattern table with substitutions enabled and some items that are NOT in storage,
            // and an actual sparse pattern
            patterns.add(encodeCraftingPattern(
                    level,
                    new Object[] {
                            Items.OAK_PLANKS, Items.OAK_PLANKS, Items.OAK_PLANKS,
                            Items.OAK_PLANKS, null, Items.OAK_PLANKS,
                            Items.CRIMSON_PLANKS, Items.OAK_PLANKS, Items.OAK_PLANKS
                    },
                    true,
                    false));

            // Add ingredients to network storage
            var networkInv = grid.getStorageService().getInventory();
            networkInv.insert(AEItemKey.of(Items.OAK_PLANKS), 83, Actionable.MODULATE, new BaseActionSource());

            // Distribute patterns across the pattern providers in the grid
            for (var provider : grid.getMachines(PatternProviderBlockEntity.class)) {
                while (!patterns.isEmpty()) {
                    var pattern = patterns.get(0);
                    if (provider.getLogic().getPatternInv().addItems(pattern).isEmpty()) {
                        patterns.remove(0);
                    } else {
                        break; // Try the next pattern provider
                    }
                }
            }
        });
    }

    private static void buildChestCraftingExport(PlotBuilder plot) {
        // Subsystem to export chests via crafting card export bus
        plot.cable("0 0 0").part(Direction.SOUTH, AEParts.EXPORT_BUS, eb -> {
            eb.getUpgrades().addItems(new ItemStack(AEItems.CRAFTING_CARD));
            eb.getConfig().insert(0, AEItemKey.of(Items.CHEST), 1, Actionable.MODULATE);
        });
        plot.block("0 0 1", Blocks.CHEST);
    }

    private static ItemStack encodeCraftingPattern(ServerLevel level,
            Object[] ingredients,
            boolean allowSubstitutions,
            boolean allowFluidSubstitutions) {

        // Allow a mixed input of items or item stacks as ingredients
        var stacks = Arrays.stream(ingredients)
                .map(in -> {
                    if (in instanceof ItemLike itemLike) {
                        return new ItemStack(itemLike);
                    } else if (in instanceof ItemStack itemStack) {
                        return itemStack;
                    } else if (in == null) {
                        return ItemStack.EMPTY;
                    } else {
                        throw new IllegalArgumentException("Unsupported argument: " + in);
                    }
                })
                .toArray(ItemStack[]::new);

        var c = new CraftingContainer(new AutoCraftingMenu(), 3, 3);
        for (int i = 0; i < stacks.length; i++) {
            c.setItem(i, stacks[i]);
        }

        var recipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, c, level).orElseThrow();

        var result = recipe.assemble(c);

        return PatternDetailsHelper.encodeCraftingPattern(
                recipe,
                stacks,
                result,
                allowSubstitutions,
                allowFluidSubstitutions);
    }

    private static void craftingCube(PlotBuilder plot) {
        plot.block("[-1,1] [0,2] [-1,1]", AEBlocks.CRAFTING_STORAGE_64K);
        plot.block("-1 2 -1", AEBlocks.CRAFTING_STORAGE_16K);
        plot.block("1 2 -1", AEBlocks.CRAFTING_STORAGE_4K);
        plot.block("-1 2 1", AEBlocks.CRAFTING_STORAGE_1K);
        plot.block("[-1,1] 0 [-1,1]", AEBlocks.CRAFTING_ACCELERATOR);
        plot.block("0 1 -1", AEBlocks.CRAFTING_MONITOR);
    }

    private static void assemblerFlower(PlotBuilder plot) {
        plot.block("0 0 0", AEBlocks.PATTERN_PROVIDER);
        plot.block("-1 0 0", AEBlocks.MOLECULAR_ASSEMBLER);
        plot.block("1 0 0", AEBlocks.MOLECULAR_ASSEMBLER);
        plot.block("0 0 1", AEBlocks.MOLECULAR_ASSEMBLER);
        plot.block("0 0 -1", AEBlocks.MOLECULAR_ASSEMBLER);
        plot.block("0 -1 0", AEBlocks.MOLECULAR_ASSEMBLER);
        plot.block("0 1 0", AEBlocks.MOLECULAR_ASSEMBLER);
    }
}
