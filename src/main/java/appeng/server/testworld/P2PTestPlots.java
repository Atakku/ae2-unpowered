package appeng.server.testworld;

import java.util.Objects;

import org.apache.commons.lang3.mutable.MutableInt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEParts;
import appeng.core.definitions.ItemDefinition;
import appeng.items.parts.PartItem;
import appeng.me.service.P2PService;
import appeng.parts.AEBasePart;
import appeng.parts.p2p.P2PTunnelPart;
import appeng.util.SettingsFrom;

public class P2PTestPlots {
    public static void me(PlotBuilder plot) {
        var origin = BlockPos.ZERO;
        placeTunnel(plot, AEParts.ME_P2P_TUNNEL);

        // Import bus to import from a chest and place it into storage bus.
        // this tests that the import bus on one end can see the storage bus
        // on the other of the P2P tunnel
        plot.cable(origin.west().west())
                .part(Direction.WEST, AEParts.IMPORT_BUS);
        plot.chest(origin.west().west().west(),
                new ItemStack(Items.BEDROCK));

        // Storage bus for the import bus on the P2P
        plot.cable(origin.east().east())
                .part(Direction.EAST, AEParts.STORAGE_BUS);
        plot.chest(origin.east().east().east());

        // High-priority storage bus on the main network to make sure the import bus
        // cannot see the carrier network
        plot.part(origin, Direction.UP, AEParts.STORAGE_BUS, storageBus -> {
            storageBus.setPriority(1);
        });
        plot.chest(origin.above());

        // Energy connection between the P2P-net and the carrier net
        plot.cable(origin.east().above());

        plot.test(helper -> {
            helper.succeedWhen(() -> {
                helper.assertContainerEmpty(origin.west().west().west());
                helper.assertContainerContains(origin.east().east().east(), Items.BEDROCK);
            });
        });
    }

    public static void item(PlotBuilder plot) {
        var origin = BlockPos.ZERO;
        placeTunnel(plot, AEParts.ITEM_P2P_TUNNEL);

        // Hopper pointing into the input P2P
        plot.hopper(origin.west().west(), Direction.EAST, new ItemStack(Items.BEDROCK));
        // Chest adjacent to output
        var chestPos = origin.east().east();
        plot.chest(chestPos);

        plot.test(helper -> {
            helper.succeedWhen(() -> {
                helper.assertContainerContains(chestPos, Items.BEDROCK);
            });
        });
    }

    public static void light(PlotBuilder plot) {
        var origin = BlockPos.ZERO;
        placeTunnel(plot, AEParts.LIGHT_P2P_TUNNEL);
        plot.block(origin.west().west(), Blocks.REDSTONE_LAMP);
        var leverPos = origin.west().west().above();
        plot.block(leverPos, Blocks.LEVER);
        var outputPos = origin.east().east();
        plot.test(helper -> {
            MutableInt lightLevel = new MutableInt(0);
            helper.startSequence()
                    .thenIdle(20)
                    .thenExecute(() -> {
                        lightLevel.setValue(
                                helper.getLevel().getBrightness(LightLayer.BLOCK, helper.absolutePos(outputPos)));
                        helper.pullLever(leverPos);
                    })
                    .thenWaitUntil(() -> {
                        var newLightLevel = helper.getLevel().getBrightness(LightLayer.BLOCK,
                                helper.absolutePos(outputPos));
                        helper.check(
                                newLightLevel > lightLevel.getValue(),
                                "Light-Level didn't increase");
                    })
                    .thenExecute(() -> helper.pullLever(leverPos))
                    .thenWaitUntil(() -> {
                        var newLightLevel = helper.getLevel().getBrightness(LightLayer.BLOCK,
                                helper.absolutePos(outputPos));
                        helper.check(
                                newLightLevel <= lightLevel.getValue(),
                                "Light-Level didn't reset");
                    })
                    .thenSucceed();
        });
    }

    private static <T extends P2PTunnelPart<?>> void placeTunnel(PlotBuilder plot, ItemDefinition<PartItem<T>> tunnel) {
        var origin = BlockPos.ZERO;
        plot.cable(origin);
        plot.cable(origin.west()).part(Direction.WEST, tunnel);
        plot.cable(origin.east()).part(Direction.EAST, tunnel);
        plot.afterGridInitAt(origin, (grid, gridNode) -> {
            BlockPos absOrigin = ((AEBasePart) gridNode.getOwner()).getBlockEntity().getBlockPos();

            var p2p = P2PService.get(grid);
            T inputTunnel = null;
            T outputTunnel = null;
            for (T p2pPart : grid.getMachines(tunnel.asItem().getPartClass())) {
                if (p2pPart.getBlockEntity().getBlockPos().equals(absOrigin.west())) {
                    inputTunnel = p2pPart;
                } else if (p2pPart.getBlockEntity().getBlockPos().equals(absOrigin.east())) {
                    outputTunnel = p2pPart;
                }
            }

            Objects.requireNonNull(inputTunnel, "inputTunnel");
            Objects.requireNonNull(outputTunnel, "outputTunnel");

            inputTunnel.setFrequency(p2p.newFrequency());
            p2p.updateFreq(inputTunnel, inputTunnel.getFrequency());

            // Link to output
            var settings = new CompoundTag();
            inputTunnel.exportSettings(SettingsFrom.MEMORY_CARD, settings);
            outputTunnel.importSettings(SettingsFrom.MEMORY_CARD, settings, null);
        });
    }
}
