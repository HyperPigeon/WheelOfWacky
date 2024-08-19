package net.hyper_pigeon.wacky_wheel.client.renderer.armor;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.hyper_pigeon.wacky_wheel.client.model.armor.PropellerHatModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class PropellerHatArmorRenderer implements ArmorRenderer {
    private static final Identifier TEXTURE = Identifier.of("textures/models/armor/propeller_hat.png");
    private PropellerHatModel propellerHatModel;
    public PropellerHatArmorRenderer() {
        propellerHatModel = new PropellerHatModel(PropellerHatModel.getTexturedModelData().createModel());
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        propellerHatModel.setupAnim(contextModel);
        matrices.push();
        matrices.translate(0.0,-0.20F,-0.25);
        matrices.scale(0.75F,0.75F,0.75F);
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, propellerHatModel, TEXTURE);
        matrices.pop();
    }
}
