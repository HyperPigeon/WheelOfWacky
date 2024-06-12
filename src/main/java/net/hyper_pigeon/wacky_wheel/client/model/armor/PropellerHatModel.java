package net.hyper_pigeon.wacky_wheel.client.model.armor;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class PropellerHatModel extends EntityModel<Entity> {
    public final ModelPart hat;
    public final ModelPart visor;
    public final ModelPart crown;
    public final ModelPart propeller;
    public final ModelPart blades;
    public PropellerHatModel(ModelPart root) {
        this.hat = root.getChild("hat");
        this.visor = hat.getChild("visor");
        this.crown = hat.getChild("crown");
        this.propeller = crown.getChild("propeller");
        this.blades = propeller.getChild("blades");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData hat = modelPartData.addChild("hat", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData visor = hat.addChild("visor", ModelPartBuilder.create().uv(18, 18).cuboid(-13.0F, -2.0F, 0.0F, 10.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 0.0F, -8.0F));

        ModelPartData crown = hat.addChild("crown", ModelPartBuilder.create().uv(11, 0).mirrored().cuboid(-13.5F, -5.0F, 7.0F, 11.0F, 5.0F, 11.0F, new Dilation(0.0F)).mirrored(false)
                .uv(18, 0).mirrored().cuboid(-12.5F, -6.0F, 8.0F, 9.0F, 2.0F, 9.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(8.0F, 0.0F, -8.0F));

        ModelPartData propeller = crown.addChild("propeller", ModelPartBuilder.create().uv(20, 26).mirrored().cuboid(-9.0F, -7.8F, 11.5F, 2.0F, 2.8F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData blades = propeller.addChild("blades", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, -7.025F, 12.5F));

        ModelPartData blade_two_r1 = blades.addChild("blade_two_r1", ModelPartBuilder.create().uv(2, 29).cuboid(-5.0F, -0.15F, -1.0F, 4.0F, 0.25F, 2.0F, new Dilation(0.0F))
                .uv(2, 29).mirrored().cuboid(1.0F, -0.15F, -1.0F, 4.0F, 0.25F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.025F, 0.0F, 0.0F, 0.7854F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    public void setupAnim(BipedEntityModel<LivingEntity> humanoidModel) {
        hat.setAngles(humanoidModel.hat.pitch, humanoidModel.hat.yaw, humanoidModel.hat.roll);
        hat.setPivot(humanoidModel.hat.pivotX, humanoidModel.hat.pivotY - 5.0F, humanoidModel.hat.pivotZ);
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        hat.render(matrices, vertexConsumer, light, overlay, color);
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
}
