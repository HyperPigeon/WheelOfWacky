package net.hyper_pigeon.wacky_wheel.client.renderer.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.block.entity.WackyWheelBlockEntity;
import net.hyper_pigeon.wacky_wheel.client.WheelOfWackyClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class WackyWheelBlockEntityRenderer implements BlockEntityRenderer<WackyWheelBlockEntity> {

    public static final SpriteIdentifier WHEEL_TEXTURE = new SpriteIdentifier(
            SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(WheelOfWacky.MOD_ID, "block/wacky_wheel"));

    private final ModelPart wacky_wheel;

    public WackyWheelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = ctx.getLayerModelPart(WheelOfWackyClient.WACKY_WHEEL_MODEL_LAYER);
        this.wacky_wheel = modelPart.getChild("wacky_wheel");
    }
    @Override
    public void render(WackyWheelBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
//        VertexConsumer vertexConsumer = WHEEL_TEXTURE.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
//        this.wacky_wheel.render(matrices, vertexConsumer, light, overlay);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData wacky_wheel = modelPartData.addChild("wacky_wheel", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData frame = wacky_wheel.addChild("frame", ModelPartBuilder.create().uv(86, 20).cuboid(1.0F, -9.1127F, -22.0F, 6.0F, 18.2254F, 1.0F, new Dilation(0.0F))
                .uv(80, 24).mirrored().cuboid(1.0F, -9.1127F, 21.0F, 6.0F, 18.2254F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(88, 77).cuboid(1.0F, 21.0F, -9.1127F, 6.0F, 1.0F, 18.2254F, new Dilation(0.0F))
                .uv(88, 77).cuboid(1.0F, -22.0F, -9.1127F, 6.0F, 1.0F, 18.2254F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData octagon_r1 = frame.addChild("octagon_r1", ModelPartBuilder.create().uv(88, 77).cuboid(-3.0F, -22.0F, -9.1127F, 6.0F, 1.0F, 18.2254F, new Dilation(0.0F))
                .uv(88, 35).cuboid(-3.0F, 21.0F, -9.1127F, 6.0F, 1.0F, 18.2254F, new Dilation(0.0F))
                .uv(79, 22).mirrored().cuboid(-3.0F, -9.1127F, 21.0F, 6.0F, 18.2254F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(80, 18).cuboid(-3.0F, -9.1127F, -22.0F, 6.0F, 18.2254F, 1.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData wheel = wacky_wheel.addChild("wheel", ModelPartBuilder.create().uv(0, 51).cuboid(3.0F, -8.6985F, -21.0F, 2.0F, 17.397F, 42.0F, new Dilation(0.0F))
                .uv(17, 17).cuboid(3.0F, -21.0F, -8.6985F, 2.0F, 42.0F, 17.397F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData octagon_r2 = wheel.addChild("octagon_r2", ModelPartBuilder.create().uv(17, 51).mirrored().cuboid(-1.0F, -21.0F, -8.6985F, 2.0F, 42.0F, 17.397F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 51).cuboid(-1.0F, -8.6985F, -21.0F, 2.0F, 17.397F, 42.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData eye_center = wacky_wheel.addChild("eye_center", ModelPartBuilder.create().uv(88, 75).cuboid(2.0F, -0.5967F, -3.0F, 6.0F, 1.1935F, 6.0F, new Dilation(0.0F))
                .uv(82, 46).cuboid(2.0F, -3.0F, -0.5967F, 6.0F, 6.0F, 1.1935F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData hexadecagon_r1 = eye_center.addChild("hexadecagon_r1", ModelPartBuilder.create().uv(84, 60).cuboid(-2.0F, -3.0F, -0.5967F, 6.0F, 6.0F, 1.1935F, new Dilation(0.0F))
                .uv(88, 0).cuboid(-2.0F, -0.5967F, -3.0F, 6.0F, 1.1935F, 6.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        ModelPartData hexadecagon_r2 = eye_center.addChild("hexadecagon_r2", ModelPartBuilder.create().uv(76, 44).cuboid(-2.0F, -3.0F, -0.5967F, 6.0F, 6.0F, 1.1935F, new Dilation(0.0F))
                .uv(88, 63).cuboid(-2.0F, -0.5967F, -3.0F, 6.0F, 1.1935F, 6.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        ModelPartData hexadecagon_r3 = eye_center.addChild("hexadecagon_r3", ModelPartBuilder.create().uv(88, 12).cuboid(-2.0F, -0.5967F, -3.0F, 6.0F, 1.1935F, 6.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData hexadecagon_r4 = eye_center.addChild("hexadecagon_r4", ModelPartBuilder.create().uv(88, 51).cuboid(-2.0F, -0.5967F, -3.0F, 6.0F, 1.1935F, 6.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}
