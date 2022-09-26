package appeng.hooks;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.parts.IPartItem;
import appeng.core.AEConfig;
import appeng.parts.BusCollisionHelper;
import appeng.parts.PartPlacement;

public class RenderBlockOutlineHook {
    private RenderBlockOutlineHook() {
    }

    public static void install() {
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((WorldRenderContext context, @Nullable HitResult hitResult) -> {
            var level = context.world();
            var poseStack = context.matrixStack();
            var buffers = context.consumers();
            var camera = context.camera();
            if (level == null || buffers == null) {
                return true;
            }

            if (!(hitResult instanceof BlockHitResult blockHitResult)
                    || blockHitResult.getType() != HitResult.Type.BLOCK) {
                return true;
            }

            return !replaceBlockOutline(level, poseStack, buffers, camera, blockHitResult);
        });
    }

    /*
     * Changes block outline rendering such that it renders only for individual parts, not for the entire part host.
     */
    private static boolean replaceBlockOutline(ClientLevel level,
            PoseStack poseStack,
            MultiBufferSource buffers,
            Camera camera,
            BlockHitResult hitResult) {

        var player = Minecraft.getInstance().player;
        if (player == null) {
            return false;
        }

        if (AEConfig.instance().isPlacementPreviewEnabled()) {
            var itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
            showPartPlacementPreview(player, poseStack, buffers, camera, hitResult, itemInHand);
        }

        // Hit test against all attached parts to highlight the part that is relevant
        var pos = hitResult.getBlockPos();
        if (level.getBlockEntity(pos) instanceof IPartHost partHost) {
            var selectedPart = partHost.selectPartWorld(hitResult.getLocation());
            if (selectedPart.part != null) {
                renderPart(poseStack, buffers, camera, pos, selectedPart.part, selectedPart.side, false);
                return true;
            }
        }

        return false;
    }

    private static void showPartPlacementPreview(
            Player player,
            PoseStack poseStack,
            MultiBufferSource buffers,
            Camera camera,
            BlockHitResult blockHitResult,
            ItemStack itemInHand) {
        if (itemInHand.getItem() instanceof IPartItem<?>partItem) {
            var placement = PartPlacement.getPartPlacement(player,
                    player.level,
                    itemInHand,
                    blockHitResult.getBlockPos(),
                    blockHitResult.getDirection());

            if (placement != null) {
                var part = partItem.createPart();
                renderPart(poseStack, buffers, camera, placement.pos(), part, placement.side(), true);
            }
        }
    }

    private static void renderPart(PoseStack poseStack,
            MultiBufferSource buffers,
            Camera camera,
            BlockPos pos,
            IPart part,
            Direction side,
            boolean preview) {
        var boxes = new ArrayList<AABB>();
        var helper = new BusCollisionHelper(boxes, side, true);
        part.getBoxes(helper);
        renderBoxes(poseStack, buffers, camera, pos, boxes, preview);
    }

    private static void renderBoxes(PoseStack poseStack,
            MultiBufferSource buffers,
            Camera camera,
            BlockPos pos,
            List<AABB> boxes,
            boolean preview) {
        for (var box : boxes) {
            var shape = Shapes.create(box);

            LevelRenderer.renderShape(
                    poseStack,
                    buffers.getBuffer(RenderType.lines()),
                    shape,
                    pos.getX() - camera.getPosition().x,
                    pos.getY() - camera.getPosition().y,
                    pos.getZ() - camera.getPosition().z,
                    preview ? 1 : 0,
                    preview ? 1 : 0,
                    preview ? 1 : 0,
                    0.4f);
        }
    }
}
