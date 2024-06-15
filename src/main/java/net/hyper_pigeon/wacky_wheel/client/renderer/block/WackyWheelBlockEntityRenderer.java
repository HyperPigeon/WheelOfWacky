package net.hyper_pigeon.wacky_wheel.client.renderer.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.block.entity.WackyWheelBlockEntity;
import net.hyper_pigeon.wacky_wheel.client.WheelOfWackyClient;
import net.minecraft.block.BedBlock;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class WackyWheelBlockEntityRenderer implements BlockEntityRenderer<WackyWheelBlockEntity> {

    public static final SpriteIdentifier WHEEL_TEXTURE = new SpriteIdentifier(
            SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(WheelOfWacky.MOD_ID, "block/wacky_wheel"));

    private final ModelPart wheel;
    private final ModelPart main_wheel;
    private final ModelPart eye;
    private final ModelPart arrow;

    public WackyWheelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart root = ctx.getLayerModelPart(WheelOfWackyClient.WACKY_WHEEL_MODEL_LAYER);
        this.wheel = root.getChild("wheel");
        this.main_wheel = wheel.getChild("main_wheel");
        this.eye = wheel.getChild("eye");
        this.arrow = wheel.getChild("arrow");
    }
    @Override
    public void render(WackyWheelBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        VertexConsumer vertexConsumer = WHEEL_TEXTURE.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        Direction blockDirection = (Direction)entity.getCachedState().get(BedBlock.FACING);
        matrices.push();
        matrices.translate(0.5,-0.5,0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(blockDirection.asRotation()));

        if(entity.isSpinning()) {
            this.main_wheel.pitch = MathHelper.lerpAngleDegrees(0.10F, this.main_wheel.pitch, entity.getRoll());
        }
        else {
            this.main_wheel.pitch = entity.getRoll();
        }

        this.wheel.render(matrices, vertexConsumer, lightAbove, overlay);
        matrices.pop();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData wheel = modelPartData.addChild("wheel", ModelPartBuilder.create(), ModelTransform.pivot(-4.0F, 16.0F, 0.0F));

        ModelPartData main_wheel = wheel.addChild("main_wheel", ModelPartBuilder.create().uv(0, 0).cuboid(3.0F, -21.0F, -21.0F, 2.0F, 42.0F, 42.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData frame = main_wheel.addChild("frame", ModelPartBuilder.create().uv(0, 19).cuboid(1.0F, -9.1127F, -22.0F, 6.0F, 18.2254F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(1.0F, -9.1127F, 21.0F, 6.0F, 18.2254F, 1.0F, new Dilation(0.0F))
                .uv(70, 66).cuboid(1.0F, 21.0F, -9.1127F, 6.0F, 1.0F, 18.2254F, new Dilation(0.0F))
                .uv(46, 0).cuboid(1.0F, -22.0F, -9.1127F, 6.0F, 1.0F, 18.2254F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData octagon_r1 = frame.addChild("octagon_r1", ModelPartBuilder.create().uv(46, 19).cuboid(-3.0F, -22.0F, -9.1127F, 6.0F, 1.0F, 18.2254F, new Dilation(0.0F))
                .uv(76, 1).cuboid(-3.0F, 21.0F, -9.1127F, 6.0F, 1.0F, 18.2254F, new Dilation(0.0F))
                .uv(14, 0).cuboid(-3.0F, -9.1127F, 21.0F, 6.0F, 18.2254F, 1.0F, new Dilation(0.0F))
                .uv(14, 19).cuboid(-3.0F, -9.1127F, -22.0F, 6.0F, 18.2254F, 1.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData eye = wheel.addChild("eye", ModelPartBuilder.create().uv(24, 34).cuboid(2.0F, -3.0F, -2.0F, 10.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData arrow = wheel.addChild("arrow", ModelPartBuilder.create().uv(1, 0).cuboid(2.0F, -6.0F, -1.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}
