package appeng.siteexport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import appeng.api.util.AEColor;
import appeng.block.crafting.CraftingUnitBlock;
import appeng.core.AppEng;
import appeng.core.definitions.AEBlockEntities;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.core.definitions.BlockDefinition;
import appeng.server.testworld.Plot;
import appeng.server.testworld.PlotBuilder;
import appeng.server.testworld.TestPlots;

final class SiteExportScenes {
    private SiteExportScenes() {
    }

    public static List<Scene> createScenes() {
        List<Scene> scenes = new ArrayList<>();

        Function<BlockState, BlockState> craftingStorageState = (BlockState s) -> s
                .setValue(CraftingUnitBlock.FORMED, true);
        Collections.addAll(
                scenes,
                singleBlock(AEBlocks.CONTROLLER),
                singleBlock(AEBlocks.CRAFTING_STORAGE_1K, craftingStorageState),
                singleBlock(AEBlocks.CRAFTING_STORAGE_4K, craftingStorageState),
                singleBlock(AEBlocks.CRAFTING_STORAGE_16K, craftingStorageState),
                singleBlock(AEBlocks.CRAFTING_STORAGE_64K, craftingStorageState),
                singleBlock(AEBlocks.CRAFTING_STORAGE_256K, craftingStorageState),
                singleBlock(AEBlocks.CELL_WORKBENCH),
                singleBlock(AEBlocks.QUARTZ_ORE),
                singleBlock(AEBlocks.QUARTZ_BLOCK),
                singleBlock(AEBlocks.CHISELED_QUARTZ_BLOCK),
                singleBlock(AEBlocks.FLUIX_BLOCK),
                createInscriberScene(),
                singleBlock(AEBlocks.INTERFACE),
                singleBlock(AEBlocks.IO_PORT),
                singleBlock(AEBlocks.CHEST),
                singleBlock(AEBlocks.DRIVE),
                singleBlock(AEBlocks.QUARTZ_GLASS),
                singleBlock(AEBlocks.SECURITY_STATION),
                singleBlock(AEBlocks.SKY_STONE_BLOCK),
                singleBlock(AEBlocks.SMOOTH_SKY_STONE_CHEST),
                singleBlock(AEBlocks.SKY_STONE_BRICK),
                singleBlock(AEBlocks.SKY_STONE_SMALL_BRICK));

        scenes.add(createColoredCablesScene());
        scenes.add(plotScene("inscriber_hoppers", TestPlots::inscriber));

        return scenes;
    }

    private static Scene createInscriberScene() {
        var scene = singleBlock(AEBlocks.INSCRIBER);
        scene.postSetup = serverLevel -> {
            serverLevel.getBlockEntity(BlockPos.ZERO, AEBlockEntities.INSCRIBER).ifPresent(be -> {
                be.getInternalInventory().setItemDirect(1, AEItems.LOGIC_PROCESSOR_PRESS.stack());
            });
        };
        scene.waitTicks = 2;
        return scene;
    }

    private static Scene createColoredCablesScene() {
        Scene coloredCables = new Scene(blockArea(), "large/colored_cables.png");
        for (var x = 0; x < 4; x++) {
            var item = AEParts.COVERED_CABLE.item(switch (x) {
                default -> AEColor.PURPLE;
                case 1 -> AEColor.BLACK;
                case 2 -> AEColor.ORANGE;
                case 3 -> AEColor.CYAN;
            });
            for (var z = 0; z < 3; z++) {
                coloredCables.putCable(new BlockPos(x, 0, z), item);
            }
            coloredCables.putCable(new BlockPos(x, 0, 0), AEParts.COVERED_CABLE.item(AEColor.TRANSPARENT));
        }
        coloredCables.waitTicks = 3;
        coloredCables.centerOn = new Vector3f(2.5f, 0, 1.5f);
        return coloredCables;
    }

    private static Scene singleBlock(BlockDefinition<?> block) {
        return singleBlock(block.id().getPath(), block.block(), Function.identity());
    }

    private static Scene singleBlock(BlockDefinition<?> block, Function<BlockState, BlockState> stateCustomizer) {
        return singleBlock(block.id().getPath(), block.block(), stateCustomizer);
    }

    private static Scene singleBlock(String filename,
            Block block,
            Function<BlockState, BlockState> stateCustomizer) {
        String fullPath = "large/" + filename + ".png";
        var scene = new Scene(bigBlockSettings(), fullPath);
        var state = block.defaultBlockState();
        state = stateCustomizer.apply(state);
        scene.blocks.put(BlockPos.ZERO, state);
        return scene;
    }

    private static Scene plotScene(String filename, Consumer<PlotBuilder> plotFactory) {
        var plot = new Plot(AppEng.makeId(filename));
        plotFactory.accept(plot);
        return plotScene(filename, plot);
    }

    private static Scene plotScene(String filename, Plot plot) {
        String fullPath = "large/" + filename + ".png";
        var renderSettings = blockArea();
        var scene = new PlotScene(renderSettings, fullPath, plot);
        scene.waitTicks = 22;
        return scene;
    }

    private static SceneRenderSettings bigBlockSettings() {
        var settings = new SceneRenderSettings();
        settings.ortographic = false;
        settings.width = 512;
        settings.height = 512;
        return settings;
    }

    private static SceneRenderSettings blockArea() {
        var settings = new SceneRenderSettings();
        settings.ortographic = true;
        settings.width = 512;
        settings.height = 512;
        return settings;
    }

}
