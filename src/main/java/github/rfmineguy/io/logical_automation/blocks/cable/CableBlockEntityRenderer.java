package github.rfmineguy.io.logical_automation.blocks.cable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import github.rfmineguy.io.logical_automation.LogicalAutomation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import org.apache.commons.lang3.BitField;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.List;

public class CableBlockEntityRenderer implements BlockEntityRenderer<CableBlockEntity> {
    BlockEntityRendererProvider.Context context;
    private final BakedModel cableCore, cableArm;

    public CableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
        ResourceLocation resource = new ResourceLocation(LogicalAutomation.MODID, "block/cable_core");
        ResourceLocation resource1 = new ResourceLocation(LogicalAutomation.MODID, "block/cable_arm");
        this.cableCore = Minecraft.getInstance().getModelManager().getModel(resource);
        this.cableArm = Minecraft.getInstance().getModelManager().getModel(resource1);
    }

    // https://github.com/henkelmax/pipez/blob/master/src/main/java/de/maxhenkel/pipez/blocks/tileentity/render/PipeRenderer.java
    @Override
    public void render(CableBlockEntity cableBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        VertexConsumer b = multiBufferSource.getBuffer(RenderType.cutout());

        List<Direction> test = List.of( Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN );
        List<Direction> directions = cableBlockEntity.getNeighborDirections();

        poseStack.pushPose();
        renderCableCore(cableBlockEntity, poseStack, b, i, i1);
        renderCableArms(poseStack, b, i, i1, test);
        poseStack.popPose();
    }

    private void renderCableCore(CableBlockEntity cableBlockEntity, PoseStack poseStack, VertexConsumer b, int i, int i1) {
        assert Minecraft.getInstance().level != null;
        List<BakedQuad> quadList = cableCore.getQuads(null, null, Minecraft.getInstance().level.random, ModelData.EMPTY, RenderType.translucent());
        for (BakedQuad q : quadList) {
            b.putBulkData(poseStack.last(), q, 1f, 1f, 1f, i, i1);
        }
    }

    private void renderCableArms(PoseStack poseStack, VertexConsumer b, int i, int i1, List<Direction> neighbors) {
        // NORTH
        if (neighbors.contains(Direction.NORTH)) {
            poseStack.pushPose();
            poseStack.translate(6/16.f, 6/16.f, 6/16.f);
            poseStack.mulPose(Axis.YP.rotationDegrees(90f));
            renderCableArm(poseStack, b, i, i1);
            poseStack.popPose();
        }

        // SOUTH
        if (neighbors.contains(Direction.SOUTH)) {
            poseStack.pushPose();
            poseStack.translate(6 / 16.f, 6 / 16.f, 14 / 16.f);
            poseStack.mulPose(Axis.YP.rotationDegrees(90f));
            renderCableArm(poseStack, b, i, i1);
            poseStack.popPose();
        }

        // WEST
        if (neighbors.contains(Direction.WEST)) {
            poseStack.pushPose();
            poseStack.translate(2 / 16.f, 6 / 16.f, 10 / 16.f);
            poseStack.mulPose(Axis.YP.rotationDegrees(90f));
            renderCableArm(poseStack, b, i, i1);
            poseStack.popPose();
        }

        // EAST
        if (neighbors.contains(Direction.EAST)) {
            poseStack.pushPose();
            poseStack.translate(10 / 16.f, 6 / 16.f, 10 / 16.f);
            poseStack.mulPose(Axis.YP.rotationDegrees(90f));
            renderCableArm(poseStack, b, i, i1);
            poseStack.popPose();
        }

        // UP
        if (neighbors.contains(Direction.UP)) {
            poseStack.pushPose();
            poseStack.translate(6 / 16.f, 10 / 16.f, 10 / 16.f);
            poseStack.mulPose(Axis.YP.rotationDegrees(90f));
            renderCableArm(poseStack, b, i, i1);
            poseStack.popPose();
        }

        // DOWN
        if (neighbors.contains(Direction.DOWN)) {
            poseStack.pushPose();
            poseStack.translate(6 / 16.f, 2 / 16.f, 10 / 16.f);
            poseStack.mulPose(Axis.YP.rotationDegrees(90f));
            renderCableArm(poseStack, b, i, i1);
            poseStack.popPose();
        }
    }

    /*
    private void renderCableArms(CableBlockEntity cableBlockEntity, PoseStack poseStack, VertexConsumer b, int i, int i1, List<CableBlockEntity> neighbors) {
        for (CableBlockEntity be : neighbors) {
            Vector3i dir = new Vector3i();
            dir.x = cableBlockEntity.getBlockPos().getX() - be.getBlockPos().getX();
            dir.y = cableBlockEntity.getBlockPos().getY() - be.getBlockPos().getY();
            dir.z = cableBlockEntity.getBlockPos().getZ() - be.getBlockPos().getZ();
            Direction d = Direction.fromDelta(dir.x, dir.y, dir.z);

            poseStack.pushPose();
            poseStack.translate(0f, 0.375f, 0f);
            poseStack.scale(2f, 1, 1);
            poseStack.mulPose(getRotation(d));
            // poseStack.translate(0f, 0f, 0f);
            renderCableArm(cableBlockEntity, poseStack, b, i, i1);
            poseStack.popPose();
        }
    }
     */

    private void renderCableArm(PoseStack poseStack, VertexConsumer b, int i, int i1) {
        assert Minecraft.getInstance().level != null;
        List<BakedQuad> quadList = cableArm.getQuads(null, null, Minecraft.getInstance().level.random, ModelData.EMPTY, RenderType.translucent());
        for (BakedQuad q : quadList) {
            b.putBulkData(poseStack.last(), q, 1f, 1f, 1f, i, i1);
        }
    }

    Quaternionf getRotation(Direction direction) {
        Quaternionf q = new Quaternionf();
        switch (direction) {
            case NORTH ->
                q.mul(Axis.YP.rotationDegrees(180F));
            case SOUTH ->
                q.mul(Axis.YP.rotationDegrees(0));
            case WEST ->
                q.mul(Axis.YP.rotationDegrees(90F));
            case EAST ->
                q.mul(Axis.YP.rotationDegrees(270F));
            case UP ->
                q.mul(Axis.XP.rotationDegrees(90F));
            default ->
                q.mul(Axis.XP.rotationDegrees(270F));
        };
        return q;
    }
}
