/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2021, TeamAppliedEnergistics, All rights reserved.
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

package appeng.datagen.providers.advancements;

/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2021, TeamAppliedEnergistics, All rights reserved.
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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.api.util.AEColor;
import appeng.core.AppEng;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.core.stats.AdvancementTriggers;
import appeng.datagen.providers.IAE2DataProvider;
import appeng.datagen.providers.localization.LocalizationProvider;
import appeng.datagen.providers.tags.ConventionTags;
import appeng.loot.NeededPressType;
import appeng.loot.NeedsPressCondition;

public class AdvancementGenerator implements IAE2DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final DataGenerator generator;

    private final LocalizationProvider localization;

    public AdvancementGenerator(DataGenerator generator, LocalizationProvider localization) {
        this.generator = generator;
        this.localization = localization;
    }

    @Override
    public void run(HashCache cache) {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            if (!set.add(advancement.getId())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            } else {
                Path path1 = createPath(path, advancement);

                try {
                    DataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), path1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        };

        generateAdvancements(consumer);
    }

    @SuppressWarnings("unused")
    private void generateAdvancements(Consumer<Advancement> consumer) {

        var root = Advancement.Builder.advancement()
                .display(
                        AEItems.CERTUS_QUARTZ_DUST,
                        localization.component("achievement.ae2_unpowered.Root", "Applied Energistics"),
                        localization.component("achievement.ae2_unpowered.Root.desc", "When a chest is simply not enough."),
                        AppEng.makeId("textures/block/sky_stone_brick.png"),
                        FrameType.TASK,
                        false /* showToast */,
                        false /* announceChat */,
                        false /* hidden */
                )
                .addCriterion("certus", InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.CERTUS_QUARTZ_DUST))
                .save(consumer, "ae2_unpowered:main/root");

        var quartzCrystal = Advancement.Builder.advancement()
                .display(
                        AEItems.CERTUS_QUARTZ_CRYSTAL,
                        localization.component("achievement.ae2_unpowered.QuartzCrystal", "Applied Energistics 2"),
                        localization.component("achievement.ae2_unpowered.QuartzCrystal.desc",
                                "Obtain your first Certus Quartz Crystal."),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(root)
                .addCriterion("certus",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.CERTUS_QUARTZ_CRYSTAL))
                .save(consumer, "ae2_unpowered:main/quartz_crystal");

        var chargedQuartz = Advancement.Builder.advancement()
                .display(
                        AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED,
                        localization.component("achievement.ae2_unpowered.ChargedQuartz", "Shocking"),
                        localization.component("achievement.ae2_unpowered.ChargedQuartz.desc", "Charge Quartz with a Charger"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(quartzCrystal)
                .addCriterion("certus",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED))
                .save(consumer, "ae2_unpowered:main/charged_quartz");

        var charger = Advancement.Builder.advancement()
                .display(
                        AEBlocks.CHARGER,
                        localization.component("achievement.ae2_unpowered.Charger", "Fluix Production"),
                        localization.component("achievement.ae2_unpowered.Charger.desc", "Craft a Charger"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(chargedQuartz)
                .addCriterion("certus", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.CHARGER))
                .save(consumer, "ae2_unpowered:main/charger");

        var compass = Advancement.Builder.advancement()
                .display(
                        AEBlocks.SKY_COMPASS,
                        localization.component("achievement.ae2_unpowered.Compass", "Meteorite Hunter"),
                        localization.component("achievement.ae2_unpowered.Compass.desc", "Craft a Meteorite Compass"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(quartzCrystal)
                .addCriterion("compass", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.SKY_COMPASS))
                .save(consumer, "ae2_unpowered:main/compass");

        var pressesBuilder = Advancement.Builder.advancement()
                .display(
                        AEItems.LOGIC_PROCESSOR_PRESS,
                        localization.component("achievement.ae2_unpowered.Presses", "Unknown Technology"),
                        localization.component("achievement.ae2_unpowered.Presses.desc", "Find all Processor Presses"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(root)
                // This MUST be AND, otherwise the tracking stops after the first acquired press
                .requirements(RequirementsStrategy.AND);
        for (var neededPress : NeededPressType.values()) {
            pressesBuilder.addCriterion(neededPress.getCriterionName(),
                    InventoryChangeTrigger.TriggerInstance.hasItems(neededPress.getItem()));
        }
        var presses = pressesBuilder.save(consumer, NeedsPressCondition.ADVANCEMENT_ID.toString());

        var controller = Advancement.Builder.advancement()
                .display(
                        AEBlocks.CONTROLLER,
                        localization.component("achievement.ae2_unpowered.Controller", "Networking Switchboard"),
                        localization.component("achievement.ae2_unpowered.Controller.desc", "Craft a Controller"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(presses)
                .addCriterion("certus", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.CONTROLLER))
                .save(consumer, "ae2_unpowered:main/controller");

        var storageCell = Advancement.Builder.advancement()
                .display(
                        AEItems.ITEM_CELL_64K,
                        localization.component("achievement.ae2_unpowered.StorageCell", "Better Than Chests"),
                        localization.component("achievement.ae2_unpowered.StorageCell.desc", "Craft a Storage Cell"),
                        null /* background */,
                        FrameType.TASK,
                        false,
                        false,
                        false)
                .parent(controller)
                .addCriterion("c1k", InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.ITEM_CELL_1K))
                .addCriterion("c4k", InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.ITEM_CELL_4K))
                .addCriterion("c16k", InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.ITEM_CELL_16K))
                .addCriterion("c64k", InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.ITEM_CELL_64K))
                .addCriterion("c256k", InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.ITEM_CELL_256K))
                .requirements(RequirementsStrategy.OR)
                .save(consumer, "ae2_unpowered:main/storage_cell");

        var ioport = Advancement.Builder.advancement()
                .display(
                        AEBlocks.IO_PORT,
                        localization.component("achievement.ae2_unpowered.IOPort", "Storage Cell Shuffle"),
                        localization.component("achievement.ae2_unpowered.IOPort.desc", "Craft an IO Port"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(storageCell)
                .addCriterion("certus", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.IO_PORT))
                .save(consumer, "ae2_unpowered:main/ioport");

        var craftingTerminal = Advancement.Builder.advancement()
                .display(
                        AEParts.CRAFTING_TERMINAL,
                        localization.component("achievement.ae2_unpowered.CraftingTerminal", "A (Much) Bigger Table"),
                        localization.component("achievement.ae2_unpowered.CraftingTerminal.desc", "Craft a Crafting Terminal"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(controller)
                .addCriterion("certus", InventoryChangeTrigger.TriggerInstance.hasItems(AEParts.CRAFTING_TERMINAL))
                .save(consumer, "ae2_unpowered:main/crafting_terminal");

        var patternTerminal = Advancement.Builder.advancement()
                .display(
                        AEParts.PATTERN_ENCODING_TERMINAL,
                        localization.component("achievement.ae2_unpowered.PatternTerminal", "Crafting Maestro"),
                        localization.component("achievement.ae2_unpowered.PatternTerminal.desc",
                                "Craft a Pattern Encoding Terminal"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(craftingTerminal)
                .addCriterion("certus",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AEParts.PATTERN_ENCODING_TERMINAL))
                .save(consumer, "ae2_unpowered:main/pattern_encoding_terminal");

        var craftingCpu = Advancement.Builder.advancement()
                .display(
                        AEBlocks.CRAFTING_STORAGE_64K,
                        localization.component("achievement.ae2_unpowered.CraftingCPU", "Next Gen Crafting"),
                        localization.component("achievement.ae2_unpowered.CraftingCPU.desc", "Craft a Crafting Unit"),
                        null /* background */,
                        FrameType.TASK,
                        false,
                        false,
                        false)
                .parent(patternTerminal)
                .addCriterion("c1k", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.CRAFTING_STORAGE_1K))
                .addCriterion("c4k", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.CRAFTING_STORAGE_4K))
                .addCriterion("c16k", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.CRAFTING_STORAGE_16K))
                .addCriterion("c64k", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.CRAFTING_STORAGE_64K))
                .addCriterion("c256k", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.CRAFTING_STORAGE_256K))
                .requirements(RequirementsStrategy.OR)
                .save(consumer, "ae2_unpowered:main/crafting_cpu");

        var fluix = Advancement.Builder.advancement()
                .display(
                        AEItems.FLUIX_DUST,
                        localization.component("achievement.ae2_unpowered.Fluix", "Unnatural"),
                        localization.component("achievement.ae2_unpowered.Fluix.desc", "Create Fluix Dust"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(chargedQuartz)
                .addCriterion("certus", InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.FLUIX_DUST))
                .save(consumer, "ae2_unpowered:main/fluix");

        var glassCable = Advancement.Builder.advancement()
                .display(
                        AEParts.GLASS_CABLE.item(AEColor.TRANSPARENT),
                        localization.component("achievement.ae2_unpowered.GlassCable", "Fluix Energy Connection"),
                        localization.component("achievement.ae2_unpowered.GlassCable.desc", "Craft ME Glass Cable"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(fluix)
                .addCriterion("certus",
                        InventoryChangeTrigger.TriggerInstance
                                .hasItems(ItemPredicate.Builder.item().of(ConventionTags.GLASS_CABLE).build()))
                .save(consumer, "ae2_unpowered:main/glass_cable");

        var facade = Advancement.Builder.advancement()
                .display(
                        AEItems.FACADE.asItem().createFacadeForItemUnchecked(new ItemStack(Items.STONE)),
                        localization.component("achievement.ae2_unpowered.Facade", "Network Aesthetics"),
                        localization.component("achievement.ae2_unpowered.Facade.desc", "Craft a Cable Facade"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(glassCable)
                .addCriterion("facade", InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.FACADE))
                .save(consumer, "ae2_unpowered:main/facade");

        var growthAccelerator = Advancement.Builder.advancement()
                .display(
                        AEBlocks.QUARTZ_GROWTH_ACCELERATOR,
                        localization.component("achievement.ae2_unpowered.CrystalGrowthAccelerator",
                                "Accelerator is an understatement"),
                        localization.component("achievement.ae2_unpowered.CrystalGrowthAccelerator.desc",
                                "Craft a Crystal Growth Accelerator"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(fluix)
                .addCriterion("certus",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.QUARTZ_GROWTH_ACCELERATOR))
                .save(consumer, "ae2_unpowered:main/growth_accelerator");

        var network1 = Advancement.Builder.advancement()
                .display(
                        AEParts.COVERED_CABLE.item(AEColor.TRANSPARENT),
                        localization.component("achievement.ae2_unpowered.Networking1", "Network Apprentice"),
                        localization.component("achievement.ae2_unpowered.Networking1.desc",
                                "Reach 8 channels using devices on a network."),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(glassCable)
                .addCriterion("cable", AdvancementTriggers.NETWORK_APPRENTICE.instance())
                .save(consumer, "ae2_unpowered:main/network1");

        var network2 = Advancement.Builder.advancement()
                .display(
                        AEParts.SMART_CABLE.item(AEColor.TRANSPARENT),
                        localization.component("achievement.ae2_unpowered.Networking2", "Network Engineer"),
                        localization.component("achievement.ae2_unpowered.Networking2.desc",
                                "Reach 128 channels using devices on a network."),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(network1)
                .addCriterion("cable", AdvancementTriggers.NETWORK_ENGINEER.instance())
                .save(consumer, "ae2_unpowered:main/network2");

        var network3 = Advancement.Builder.advancement()
                .display(
                        AEParts.SMART_DENSE_CABLE.item(AEColor.TRANSPARENT),
                        localization.component("achievement.ae2_unpowered.Networking3", "Network Administrator"),
                        localization.component("achievement.ae2_unpowered.Networking3.desc",
                                "Reach 2048 channels using devices on a network."),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(network2)
                .addCriterion("cable", AdvancementTriggers.NETWORK_ADMIN.instance())
                .save(consumer, "ae2_unpowered:main/network3");

        var networkTool = Advancement.Builder.advancement()
                .display(
                        AEItems.NETWORK_TOOL,
                        localization.component("achievement.ae2_unpowered.NetworkTool", "Network Diagnostics"),
                        localization.component("achievement.ae2_unpowered.NetworkTool.desc", "Craft a Network Tool"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(controller)
                .addCriterion("network_tool", InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.NETWORK_TOOL))
                .save(consumer, "ae2_unpowered:main/network_tool");

        var p2p = Advancement.Builder.advancement()
                .display(
                        AEParts.ME_P2P_TUNNEL,
                        localization.component("achievement.ae2_unpowered.P2P", "Point to Point Networking"),
                        localization.component("achievement.ae2_unpowered.P2P.desc", "Craft a P2P Tunnel"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(glassCable)
                .addCriterion("certus", InventoryChangeTrigger.TriggerInstance.hasItems(AEParts.ME_P2P_TUNNEL))
                .save(consumer, "ae2_unpowered:main/p2p");

        var portableCell = Advancement.Builder.advancement()
                .display(
                        AEItems.PORTABLE_ITEM_CELL1K,
                        localization.component("achievement.ae2_unpowered.PortableCell", "Storage Nomad"),
                        localization.component("achievement.ae2_unpowered.PortableCell.desc", "Craft a Portable Cell"),
                        null /* background */,
                        FrameType.TASK,
                        false,
                        false,
                        false)
                .parent(storageCell)
                .addCriterion("pc_1k",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.PORTABLE_ITEM_CELL1K))
                .addCriterion("pc_4k",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.PORTABLE_ITEM_CELL4K))
                .addCriterion("pc_16k",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.PORTABLE_ITEM_CELL16K))
                .addCriterion("pc_64k",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.PORTABLE_ITEM_CELL64K))
                .addCriterion("pc_256k",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AEItems.PORTABLE_ITEM_CELL256K))
                .requirements(RequirementsStrategy.OR)
                .save(consumer, "ae2_unpowered:main/portable_cell");

        var qnb = Advancement.Builder.advancement()
                .display(
                        AEBlocks.QUANTUM_LINK,
                        localization.component("achievement.ae2_unpowered.QNB", "Quantum Tunneling"),
                        localization.component("achievement.ae2_unpowered.QNB.desc", "Craft a Quantum Link"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(p2p)
                .addCriterion("certus", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.QUANTUM_LINK))
                .save(consumer, "ae2_unpowered:main/qnb");

        var spatialIoport = Advancement.Builder.advancement()
                .display(
                        AEBlocks.SPATIAL_IO_PORT,
                        localization.component("achievement.ae2_unpowered.SpatialIO", "Spatial Coordination"),
                        localization.component("achievement.ae2_unpowered.SpatialIO.desc", "Craft a Spatial IO Port"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(ioport)
                .addCriterion("certus", InventoryChangeTrigger.TriggerInstance.hasItems(AEBlocks.SPATIAL_IO_PORT))
                .save(consumer, "ae2_unpowered:main/spatial_ioport");

        var spatialExplorer = Advancement.Builder.advancement()
                .display(
                        AEItems.SPATIAL_128_CELL_COMPONENT,
                        localization.component("achievement.ae2_unpowered.SpatialIOExplorer", "To boldly go"),
                        localization.component("achievement.ae2_unpowered.SpatialIOExplorer.desc",
                                "Get stored in a spatial storage cell"),
                        null /* background */,
                        FrameType.TASK,
                        false,
                        false,
                        false)
                .parent(spatialIoport)
                .addCriterion("explorer", AdvancementTriggers.SPATIAL_EXPLORER.instance())
                .save(consumer, "ae2_unpowered:main/spatial_explorer");

        var storageBus = Advancement.Builder.advancement()
                .display(
                        AEParts.STORAGE_BUS,
                        localization.component("achievement.ae2_unpowered.StorageBus", "Limitless Potential"),
                        localization.component("achievement.ae2_unpowered.StorageBus.desc", "Craft a Storage Bus"),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(glassCable)
                .addCriterion("part", InventoryChangeTrigger.TriggerInstance.hasItems(AEParts.STORAGE_BUS))
                .save(consumer, "ae2_unpowered:main/storage_bus");

        var storageBusOnInterface = Advancement.Builder.advancement()
                .display(
                        AEBlocks.INTERFACE,
                        localization.component("achievement.ae2_unpowered.Recursive", "Recursive Networking"),
                        localization.component("achievement.ae2_unpowered.Recursive.desc",
                                "Place a Storage Bus on an Interface."),
                        null /* background */,
                        FrameType.TASK,
                        true /* showToast */,
                        true /* announceChat */,
                        false /* hidden */
                )
                .parent(storageBus)
                .addCriterion("recursive", AdvancementTriggers.RECURSIVE.instance())
                .save(consumer, "ae2_unpowered:main/recursive");

    }

    private static Path createPath(Path basePath, Advancement advancement) {
        return basePath.resolve("data/" + advancement.getId().getNamespace()
                + "/advancements/" + advancement.getId().getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Advancements";
    }
}
