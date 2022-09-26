package appeng.datagen.providers.models;

import static appeng.core.AppEng.makeId;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import appeng.api.util.AEColor;
import appeng.client.render.model.BiometricCardModel;
import appeng.client.render.model.MemoryCardModel;
import appeng.core.AppEng;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import appeng.datagen.providers.IAE2DataProvider;
import appeng.init.client.InitItemModelsProperties;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider
        implements IAE2DataProvider {
    public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, AppEng.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        flatSingleLayer(BiometricCardModel.MODEL_BASE, "item/biometric_card");
        builtInItemModel("biometric_card");
        flatSingleLayer(MemoryCardModel.MODEL_BASE, "item/memory_card");
        builtInItemModel("memory_card");

        builtInItemModel("sky_compass");

        crystalSeed(AEItems.CERTUS_CRYSTAL_SEED,
                "item/crystal_seed_certus",
                "item/crystal_seed_certus2",
                "item/crystal_seed_certus3");
        crystalSeed(AEItems.FLUIX_CRYSTAL_SEED,
                "item/crystal_seed_fluix",
                "item/crystal_seed_fluix2",
                "item/crystal_seed_fluix3");

        flatSingleLayer(AEItems.ADVANCED_CARD, "item/advanced_card");
        flatSingleLayer(AEItems.VOID_CARD, "item/card_void");
        flatSingleLayer(AEItems.ANNIHILATION_CORE, "item/annihilation_core");
        flatSingleLayer(AEItems.BASIC_CARD, "item/basic_card");
        flatSingleLayer(AEItems.BLANK_PATTERN, "item/blank_pattern");
        flatSingleLayer(AEItems.CALCULATION_PROCESSOR, "item/calculation_processor");
        flatSingleLayer(AEItems.CALCULATION_PROCESSOR_PRESS, "item/calculation_processor_press");
        flatSingleLayer(AEItems.CALCULATION_PROCESSOR_PRINT, "item/printed_calculation_processor");
        flatSingleLayer(AEItems.CAPACITY_CARD, "item/card_capacity");
        storageCell(AEItems.ITEM_CELL_1K, "item/item_storage_cell_1k");
        storageCell(AEItems.ITEM_CELL_4K, "item/item_storage_cell_4k");
        storageCell(AEItems.ITEM_CELL_16K, "item/item_storage_cell_16k");
        storageCell(AEItems.ITEM_CELL_64K, "item/item_storage_cell_64k");
        storageCell(AEItems.ITEM_CELL_256K, "item/item_storage_cell_256k");
        flatSingleLayer(AEItems.CERTUS_QUARTZ_CRYSTAL, "item/certus_quartz_crystal");
        flatSingleLayer(AEItems.CERTUS_QUARTZ_DUST, "item/certus_quartz_dust");
        flatSingleLayer(AEItems.CERTUS_QUARTZ_KNIFE, "item/certus_quartz_cutting_knife");
        flatSingleLayer(AEItems.CRAFTING_CARD, "item/card_crafting");
        flatSingleLayer(AEItems.CRAFTING_PATTERN, "item/crafting_pattern");
        flatSingleLayer(AEItems.ENGINEERING_PROCESSOR, "item/engineering_processor");
        flatSingleLayer(AEItems.ENGINEERING_PROCESSOR_PRESS, "item/engineering_processor_press");
        flatSingleLayer(AEItems.ENGINEERING_PROCESSOR_PRINT, "item/printed_engineering_processor");
        flatSingleLayer(AEItems.EQUAL_DISTRIBUTION_CARD, "item/card_equal_distribution");
        flatSingleLayer(AEItems.FLUIX_CRYSTAL, "item/fluix_crystal");
        flatSingleLayer(AEItems.FLUIX_DUST, "item/fluix_dust");
        flatSingleLayer(AEItems.FLUIX_PEARL, "item/fluix_pearl");
        flatSingleLayer(AEItems.FORMATION_CORE, "item/formation_core");
        flatSingleLayer(AEItems.FUZZY_CARD, "item/card_fuzzy");
        flatSingleLayer(AEItems.INVERTER_CARD, "item/card_inverter");
        flatSingleLayer(AEItems.CELL_COMPONENT_16K, "item/cell_component_16k");
        flatSingleLayer(AEItems.CELL_COMPONENT_1K, "item/cell_component_1k");
        flatSingleLayer(AEItems.CELL_COMPONENT_4K, "item/cell_component_4k");
        flatSingleLayer(AEItems.CELL_COMPONENT_64K, "item/cell_component_64k");
        flatSingleLayer(AEItems.CELL_COMPONENT_256K, "item/cell_component_256k");
        flatSingleLayer(AEItems.ITEM_CELL_HOUSING, "item/item_cell_housing");
        flatSingleLayer(AEItems.LOGIC_PROCESSOR, "item/logic_processor");
        flatSingleLayer(AEItems.LOGIC_PROCESSOR_PRESS, "item/logic_processor_press");
        flatSingleLayer(AEItems.LOGIC_PROCESSOR_PRINT, "item/printed_logic_processor");
        flatSingleLayer(AEItems.PROCESSING_PATTERN, "item/processing_pattern");
        flatSingleLayer(AEItems.REDSTONE_CARD, "item/card_redstone");
        flatSingleLayer(AEItems.SILICON, "item/silicon");
        flatSingleLayer(AEItems.SILICON_PRESS, "item/silicon_press");
        flatSingleLayer(AEItems.SILICON_PRINT, "item/printed_silicon");
        flatSingleLayer(AEItems.SKY_DUST, "item/sky_dust");
        flatSingleLayer(AEItems.SPEED_CARD, "item/card_speed");
        flatSingleLayer(AEItems.VIEW_CELL, "item/view_cell");
        flatSingleLayer(AEItems.WIRELESS_CRAFTING_TERMINAL, "item/wireless_crafting_terminal");
        flatSingleLayer(AEItems.WIRELESS_RECEIVER, "item/wireless_receiver");
        flatSingleLayer(AEItems.WIRELESS_TERMINAL, "item/wireless_terminal");
        registerEmptyModel(AEItems.WRAPPED_GENERIC_STACK);
        registerEmptyModel(AEBlocks.CABLE_BUS);
        registerHandheld();
    }

    private void storageCell(ItemDefinition<?> item, String background) {
        String id = item.id().getPath();
        singleTexture(
                id,
                mcLoc("item/generated"),
                "layer0",
                makeId(background))
                        .texture("layer1", "item/storage_cell_led");
    }

    /**
     * Define a crystal seed item model with three growth stages shown by three textures. The fully grown crystal is not
     * part of this model.
     */
    private void crystalSeed(ItemDefinition<?> seed,
            String texture0,
            String texture1,
            String texture2) {

        var baseId = seed.id().getPath();

        var model1 = flatSingleLayer(makeId(baseId + "_1"), texture1);
        var model2 = flatSingleLayer(makeId(baseId + "_2"), texture2);

        withExistingParent(baseId, "item/generated")
                .texture("layer0", makeId(texture0))
                // 2nd growth stage
                .override()
                .predicate(InitItemModelsProperties.GROWTH_PREDICATE_ID, 0.333f)
                .model(model1)
                .end()
                // 3rd growth stage
                .override()
                .predicate(InitItemModelsProperties.GROWTH_PREDICATE_ID, 0.666f)
                .model(model2)
                .end();
    }

    private void registerHandheld() {
        handheld(AEItems.CERTUS_QUARTZ_KNIFE);
    }

    private void handheld(ItemDefinition<?> item) {
        singleTexture(
                item.id().getPath(),
                new ResourceLocation("item/handheld"),
                "layer0",
                makeId("item/" + item.id().getPath()));
    }

    private void registerEmptyModel(ItemDefinition<?> item) {
        this.getBuilder(item.id().getPath());
    }

    private ItemModelBuilder flatSingleLayer(ItemDefinition<?> item, String texture) {
        String id = item.id().getPath();
        return singleTexture(
                id,
                mcLoc("item/generated"),
                "layer0",
                makeId(texture));
    }

    private ItemModelBuilder flatSingleLayer(ResourceLocation id, String texture) {
        return singleTexture(
                id.getPath(),
                mcLoc("item/generated"),
                "layer0",
                makeId(texture));
    }

    private ItemModelBuilder builtInItemModel(String name) {
        var model = getBuilder("item/" + name);
        var loaderId = AppEng.makeId("item/" + name);
        model.customLoader((bmb, efh) -> new CustomLoaderBuilder<ItemModelBuilder>(loaderId, bmb, efh) {
        });
        return model;
    }
}
