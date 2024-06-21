package net.hyper_pigeon.wacky_wheel.client.renderer.block;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.block.WackyWheelBlock;
import net.hyper_pigeon.wacky_wheel.block.entity.WackyWheelBlockEntity;
import net.hyper_pigeon.wacky_wheel.client.WheelOfWackyClient;
import net.minecraft.block.BedBlock;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

@Environment(EnvType.CLIENT)
public class WackyWheelBlockEntityRenderer implements BlockEntityRenderer<WackyWheelBlockEntity> {

    public static final SpriteIdentifier JUNE_WHEEL_TEXTURE = new SpriteIdentifier(
            SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(WheelOfWacky.MOD_ID, "block/wheel_pride"));

    public static final SpriteIdentifier WHEEL_TEXTURE = new SpriteIdentifier(
            SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier.of(WheelOfWacky.MOD_ID, "block/wheel"));

    private final ModelPart wheel;
    private final ModelPart main_wheel;
    private final ModelPart eye;

    private final TextRenderer textRenderer;
    private final ItemRenderer itemRenderer;

    public WackyWheelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart root = ctx.getLayerModelPart(WheelOfWackyClient.WACKY_WHEEL_MODEL_LAYER);
        this.wheel = root.getChild("wheel");
        this.main_wheel = wheel.getChild("main_wheel");
        this.eye = wheel.getChild("eye");
        this.textRenderer = ctx.getTextRenderer();
        this.itemRenderer = ctx.getItemRenderer();
    }
    @Override
    public void render(WackyWheelBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        VertexConsumer vertexConsumer = getTexture().getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        Text text = Text.literal(entity.getCurrentWedgeName());
        int textColor = entity.getCurrentWedgeSpell().titleColor().isPresent() ? entity.getCurrentWedgeSpell().titleColor().get().getRgb(): 553648127;
        Direction blockDirection = (Direction)entity.getCachedState().get(WackyWheelBlock.FACING);
        float degreeOffset = (blockDirection.equals(Direction.WEST) || blockDirection.equals(Direction.EAST)) ? -90F : 90F;
        matrices.push();
        matrices.translate(0.5,-0.5,0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(degreeOffset  + blockDirection.asRotation()));
        renderText(text, matrices, vertexConsumers, lightAbove, textColor);

        if(entity.isSpinning()) {
            this.main_wheel.pitch = (float) ((Math.PI/180F) * clerp(tickDelta,entity.getPreviousRoll(), entity.getRoll()));
        }
        else {
            this.main_wheel.pitch = (float) ((Math.PI/180F) * entity.getRoll());
        }

        for(int i = 0; i < entity.getWedgeSpells().size(); i++) {
            float pitch = i * 22.5F;
            matrices.push();
            matrices.translate(0,1.0,0);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(clerp(tickDelta,entity.getPreviousRoll(), entity.getRoll())));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90F));
            matrices.translate(0F, 1.1F, -0.10F);
            matrices.scale(0.3F, 0.3F, 0.3F);
            this.itemRenderer.renderItem(entity.getWedgeSpells().get(i).item().getDefaultStack(), ModelTransformationMode.NONE, lightAbove, overlay, matrices, vertexConsumers,entity.getWorld(),0);
            matrices.pop();
        }
        this.wheel.render(matrices,vertexConsumer,lightAbove,overlay);
        matrices.pop();
    }

    void renderText(Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int color) {
        matrices.push();
        RenderSystem.disableCull();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270F));
        matrices.scale(0.035F, -0.035F, 0.035F);

        float x = (float)(-this.textRenderer.getWidth(text) / 2);

        this.textRenderer.draw(
                text,
                x,
                -85.0F,
                color,
                false,
                matrices.peek().getPositionMatrix(),
                vertexConsumers,
                TextRenderer.TextLayerType.POLYGON_OFFSET,
               0,
                light
        );
        RenderSystem.enableCull();
        matrices.pop();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData wheel = modelPartData.addChild("wheel", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData main_wheel = wheel.addChild("main_wheel", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, -23.5F, -24.0F, 6.0F, 48.0F, 48.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-0.5F, -0.5F, 0.0F));

        ModelPartData frame = main_wheel.addChild("frame", ModelPartBuilder.create().uv(74, 0).mirrored().cuboid(-1.5F, -4.8734F, -24.5F, 8.0F, 9.7467F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(78, 0).mirrored().cuboid(-1.5F, -4.8734F, 22.5F, 8.0F, 9.7467F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(66, 0).mirrored().cuboid(-1.5F, -24.5F, -4.8734F, 8.0F, 2.0F, 9.7467F, new Dilation(0.0F)).mirrored(false)
                .uv(69, 0).mirrored().cuboid(-1.5F, 22.5F, -4.8734F, 8.0F, 2.0F, 9.7467F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.5F, 0.0F));

        ModelPartData hexadecagon_r1 = frame.addChild("hexadecagon_r1", ModelPartBuilder.create().uv(67, 0).mirrored().cuboid(-2.0F, 22.5F, -4.8734F, 8.0F, 2.0F, 9.7467F, new Dilation(0.0F)).mirrored(false)
                .uv(70, 0).mirrored().cuboid(-2.0F, -24.5F, -4.8734F, 8.0F, 2.0F, 9.7467F, new Dilation(0.0F)).mirrored(false)
                .uv(78, 0).mirrored().cuboid(-2.0F, -4.8734F, 22.5F, 8.0F, 9.7467F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(66, 0).mirrored().cuboid(-2.0F, -4.8734F, -24.5F, 8.0F, 9.7467F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.5F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        ModelPartData hexadecagon_r2 = frame.addChild("hexadecagon_r2", ModelPartBuilder.create().uv(69, 0).mirrored().cuboid(-2.0F, 22.5F, -4.8734F, 8.0F, 2.0F, 9.7467F, new Dilation(0.0F)).mirrored(false)
                .uv(68, 0).mirrored().cuboid(-2.0F, -24.5F, -4.8734F, 8.0F, 2.0F, 9.7467F, new Dilation(0.0F)).mirrored(false)
                .uv(75, 0).mirrored().cuboid(-2.0F, -4.8734F, 22.5F, 8.0F, 9.7467F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(73, 0).mirrored().cuboid(-2.0F, -4.8734F, -24.5F, 8.0F, 9.7467F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.5F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        ModelPartData hexadecagon_r3 = frame.addChild("hexadecagon_r3", ModelPartBuilder.create().uv(76, 0).mirrored().cuboid(-2.0F, -4.8734F, 22.5F, 8.0F, 9.7467F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(68, 0).mirrored().cuboid(-2.0F, -4.8734F, -24.5F, 8.0F, 9.7467F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.5F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData hexadecagon_r4 = frame.addChild("hexadecagon_r4", ModelPartBuilder.create().uv(78, 0).mirrored().cuboid(-2.0F, -4.8734F, 22.5F, 8.0F, 9.7467F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(71, 0).mirrored().cuboid(-2.0F, -4.8734F, -24.5F, 8.0F, 10.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData eye = wheel.addChild("eye", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-5.5F, -4.5F, -4.5F, 11.0F, 8.0F, 8.0F, new Dilation(0.0F)).mirrored(false)
                .uv(98, 14).mirrored().cuboid(-4.5F, 3.0F, -2.0F, 2.0F, 10.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.5F, 0.5F, 0.5F));

        ModelPartData cube_r1 = eye.addChild("cube_r1", ModelPartBuilder.create().uv(73, 3).mirrored().cuboid(8.5F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-13.0F, 9.0F, 2.0F, -0.48F, 0.0F, 0.0F));

        ModelPartData cube_r2 = eye.addChild("cube_r2", ModelPartBuilder.create().uv(73, 3).mirrored().cuboid(8.5F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-13.0F, 9.0F, -3.0F, 0.48F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    private static boolean isJune() {
        LocalDate localDate = LocalDate.now();
        int m = localDate.get(ChronoField.MONTH_OF_YEAR);
        return m == 6;
    }

    public SpriteIdentifier getTexture() {
        return isJune() ? JUNE_WHEEL_TEXTURE : WHEEL_TEXTURE;
    }

    public float clerp(float delta, float startingAngle, float endAngle) {
        if (endAngle < startingAngle) {
            endAngle += 360;
        }
        float interpolatedAngle = startingAngle + delta * (endAngle - startingAngle);


        interpolatedAngle %= 360;
        return interpolatedAngle;
    }
}
