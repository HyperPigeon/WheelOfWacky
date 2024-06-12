package net.hyper_pigeon.wacky_wheel.client.renderer.armor;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.client.model.armor.PropellerHatModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class PropellerHatArmorRenderer implements ArmorRenderer {
    private static final Identifier TEXTURE = Identifier.of(WheelOfWacky.MOD_ID, "textures/item/propeller_hat");
    private PropellerHatModel propellerHatModel;
    public PropellerHatArmorRenderer() {
        propellerHatModel = new PropellerHatModel(PropellerHatModel.getTexturedModelData().createModel());
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        propellerHatModel.setupAnim(contextModel);
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, propellerHatModel, TEXTURE);
    }
}
