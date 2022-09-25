package appeng.datagen.providers.models;

import static appeng.core.AppEng.makeId;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.block.spatial.SpatialAnchorBlock;
import appeng.block.spatial.SpatialIOPortBlock;
import appeng.block.storage.IOPortBlock;
import appeng.core.AppEng;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.BlockDefinition;

public class BlockModelProvider extends AE2BlockStateProvider {
    public BlockModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, AppEng.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        emptyModel(AEBlocks.MATRIX_FRAME);

        // These models will be overwritten in code
        builtInModel(AEBlocks.QUARTZ_GLASS, true);
        builtInModel(AEBlocks.CABLE_BUS);
        builtInModel(AEBlocks.PAINT);
        builtInModel(AEBlocks.SKY_COMPASS, true);
        builtInBlockModel("drive");
        builtInBlockModel("spatial_pylon");
        builtInBlockModel("qnb/qnb_formed");
        builtInBlockModel("crafting/monitor_formed");
        builtInBlockModel("crafting/unit_formed");
        builtInBlockModel("crafting/accelerator_formed");
        builtInBlockModel("crafting/1k_storage_formed");
        builtInBlockModel("crafting/4k_storage_formed");
        builtInBlockModel("crafting/16k_storage_formed");
        builtInBlockModel("crafting/64k_storage_formed");
        builtInBlockModel("crafting/256k_storage_formed");
        builtInBlockModel("crafting/1m_storage_formed");
        builtInBlockModel("crafting/4m_storage_formed");
        builtInBlockModel("crafting/16m_storage_formed");
        builtInBlockModel("crafting/64m_storage_formed");
        builtInBlockModel("crafting/256m_storage_formed");
        builtInBlockModel("crafting/1g_storage_formed");
        builtInBlockModel("crafting/4g_storage_formed");
        builtInBlockModel("crafting/16g_storage_formed");
        builtInBlockModel("crafting/64g_storage_formed");
        builtInBlockModel("crafting/256g_storage_formed");

        // Spatial pylon uses a normal model for the item, special model for block
        simpleBlock(AEBlocks.SPATIAL_PYLON.block(), models().getBuilder(modelPath(AEBlocks.SPATIAL_PYLON)));
        itemModels().cubeAll(modelPath(AEBlocks.SPATIAL_PYLON), makeId("item/spatial_pylon"));

        generateOreBlock(AEBlocks.QUARTZ_ORE);
        generateOreBlock(AEBlocks.DEEPSLATE_QUARTZ_ORE);

        simpleBlockAndItem(AEBlocks.CONDENSER);
        simpleBlockAndItem(AEBlocks.INTERFACE);

        simpleBlockAndItem(AEBlocks.DEBUG_ITEM_GEN, "block/debug/item_gen");
        simpleBlockAndItem(AEBlocks.DEBUG_CHUNK_LOADER, "block/debug/chunk_loader");
        simpleBlockAndItem(AEBlocks.DEBUG_PHANTOM_NODE, "block/debug/phantom_node");
        simpleBlockAndItem(AEBlocks.DEBUG_CUBE_GEN, "block/debug/cube_gen");

        craftingModel(AEBlocks.CRAFTING_ACCELERATOR, "accelerator");
        craftingModel(AEBlocks.CRAFTING_UNIT, "unit");
        craftingModel(AEBlocks.CRAFTING_STORAGE_1K, "1k_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_4K, "4k_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_16K, "16k_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_64K, "64k_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_256K, "256k_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_1M, "1m_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_4M, "4m_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_16M, "16m_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_64M, "64m_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_256M, "256m_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_1G, "1g_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_4G, "4g_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_16G, "16g_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_64G, "64g_storage");
        craftingModel(AEBlocks.CRAFTING_STORAGE_256G, "256g_storage");

        simpleBlockAndItem(AEBlocks.CELL_WORKBENCH, models().cubeBottomTop(
                modelPath(AEBlocks.CELL_WORKBENCH),
                makeId("block/cell_workbench_side"),
                makeId("block/cell_workbench_bottom"),
                makeId("block/cell_workbench")));

        spatialAnchor();
        patternProvider();
        ioPorts();
    }

    private void spatialAnchor() {
        var offModel = models().cubeBottomTop(
                modelPath(AEBlocks.SPATIAL_ANCHOR),
                makeId("block/spatial_anchor_side_off"),
                makeId("block/spatial_anchor_bottom"),
                makeId("block/spatial_anchor_top_off"));
        var onModel = models().cubeBottomTop(
                modelPath(AEBlocks.SPATIAL_ANCHOR) + "_on",
                makeId("block/spatial_anchor_side"),
                makeId("block/spatial_anchor_bottom"),
                makeId("block/spatial_anchor_top"));

        getVariantBuilder(AEBlocks.SPATIAL_ANCHOR.block())
                .partialState().with(SpatialAnchorBlock.POWERED, true).setModels(new ConfiguredModel(onModel))
                .partialState().with(SpatialAnchorBlock.POWERED, false).setModels(new ConfiguredModel(offModel));
        itemModels().withExistingParent(modelPath(AEBlocks.SPATIAL_ANCHOR), offModel.getLocation());
    }

    private void patternProvider() {
        var def = AEBlocks.PATTERN_PROVIDER;
        var normalModel = cubeAll(def.block());
        simpleBlockItem(def.block(), normalModel);
        // the block state and the oriented model are in manually written json files
    }

    private void ioPorts() {
        var ioOff = models().cubeBottomTop(
                modelPath(AEBlocks.IO_PORT),
                makeId("block/io_port_side_off"),
                makeId("block/io_port_bottom"),
                makeId("block/io_port_top_off"));
        var ioOn = models().cubeBottomTop(
                modelPath(AEBlocks.IO_PORT) + "_on",
                makeId("block/io_port_side"),
                makeId("block/io_port_bottom"),
                makeId("block/io_port_top"));
        var spatialOff = models().cubeBottomTop(
                modelPath(AEBlocks.SPATIAL_IO_PORT),
                makeId("block/spatial_io_port_side_off"),
                makeId("block/spatial_io_port_bottom"),
                makeId("block/spatial_io_port_top_off"));
        var spatialOn = models().cubeBottomTop(
                modelPath(AEBlocks.SPATIAL_IO_PORT) + "_on",
                makeId("block/spatial_io_port_side"),
                makeId("block/spatial_io_port_bottom"),
                makeId("block/spatial_io_port_top"));

        getVariantBuilder(AEBlocks.IO_PORT.block())
                .partialState().with(IOPortBlock.POWERED, true).setModels(new ConfiguredModel(ioOn))
                .partialState().with(IOPortBlock.POWERED, false).setModels(new ConfiguredModel(ioOff));
        getVariantBuilder(AEBlocks.SPATIAL_IO_PORT.block())
                .partialState().with(SpatialIOPortBlock.POWERED, true).setModels(new ConfiguredModel(spatialOn))
                .partialState().with(SpatialIOPortBlock.POWERED, false).setModels(new ConfiguredModel(spatialOff));
        itemModels().withExistingParent(modelPath(AEBlocks.IO_PORT), ioOff.getLocation());
        itemModels().withExistingParent(modelPath(AEBlocks.SPATIAL_IO_PORT), spatialOff.getLocation());
    }

    private String modelPath(BlockDefinition<?> block) {
        return block.id().getPath();
    }

    private void emptyModel(BlockDefinition<?> block) {
        var model = models().getBuilder(block.id().getPath());
        simpleBlockAndItem(block, model);
    }

    private void builtInModel(BlockDefinition<?> block) {
        builtInModel(block, false);
    }

    private void builtInModel(BlockDefinition<?> block, boolean skipItem) {
        var model = builtInBlockModel(block.id().getPath());
        getVariantBuilder(block.block()).partialState().setModels(new ConfiguredModel(model));

        if (!skipItem) {
            // The item model should not reference the block model since that will be replaced in-code
            itemModels().getBuilder(block.id().getPath());
        }
    }

    private BlockModelBuilder builtInBlockModel(String name) {
        var model = models().getBuilder("block/" + name);
        var loaderId = AppEng.makeId("block/" + name);
        model.customLoader((bmb, efh) -> new CustomLoaderBuilder<BlockModelBuilder>(loaderId, bmb, efh) {
        });
        return model;
    }

    private void craftingModel(BlockDefinition<?> block, String name) {
        var blockModel = models().cubeAll("block/crafting/" + name, makeId("block/crafting/" + name));
        getVariantBuilder(block.block())
                .partialState().with(AbstractCraftingUnitBlock.FORMED, false).setModels(
                        new ConfiguredModel(blockModel))
                .partialState().with(AbstractCraftingUnitBlock.FORMED, true).setModels(
                        // Empty model, will be replaced dynamically
                        new ConfiguredModel(models().getBuilder("block/crafting/" + name + "_formed")));
        simpleBlockItem(block.block(), blockModel);
    }

    /**
     * Generate an ore block with 4 variants (0 to 3, inclusive).
     */
    private void generateOreBlock(BlockDefinition<?> block) {
        String name = block.id().getPath();
        BlockModelBuilder primaryModel = models().cubeAll(
                name + "_0",
                makeId("block/" + name + "_0"));

        simpleBlock(
                block.block(),
                ConfiguredModel.builder()
                        .modelFile(primaryModel)
                        .nextModel()
                        .modelFile(models().cubeAll(
                                name + "_1",
                                makeId("block/" + name + "_1")))
                        .nextModel()
                        .modelFile(models().cubeAll(
                                name + "_2",
                                makeId("block/" + name + "_2")))
                        .nextModel()
                        .modelFile(models().cubeAll(
                                name + "_3",
                                makeId("block/" + name + "_3")))
                        .build());
        simpleBlockItem(block.block(), primaryModel);
    }
}
