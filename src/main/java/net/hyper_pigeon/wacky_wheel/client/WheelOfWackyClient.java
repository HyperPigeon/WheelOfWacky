package net.hyper_pigeon.wacky_wheel.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.client.model.armor.PropellerHatModel;
import net.hyper_pigeon.wacky_wheel.client.renderer.armor.PropellerHatArmorRenderer;
import net.hyper_pigeon.wacky_wheel.client.renderer.block.WackyWheelBlockEntityRenderer;
import net.hyper_pigeon.wacky_wheel.register.WheelOfWackyItems;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import static net.hyper_pigeon.wacky_wheel.WheelOfWacky.WACKY_WHEEL_BLOCK_ENTITY;

public class WheelOfWackyClient implements ClientModInitializer {

    public static final EntityModelLayer PROPELLER_HAT_MODEL_LAYER = new EntityModelLayer(Identifier.of(WheelOfWacky.MOD_ID, "propeller_hat"), "main");
    public static final EntityModelLayer WACKY_WHEEL_MODEL_LAYER = new EntityModelLayer(Identifier.of(WheelOfWacky.MOD_ID, "wacky_wheel"), "main");
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(PROPELLER_HAT_MODEL_LAYER, PropellerHatModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WACKY_WHEEL_MODEL_LAYER, WackyWheelBlockEntityRenderer::getTexturedModelData);
        BlockEntityRendererRegistry.register(WACKY_WHEEL_BLOCK_ENTITY, WackyWheelBlockEntityRenderer::new);
        ArmorRenderer.register(new PropellerHatArmorRenderer(), WheelOfWackyItems.PROPELLER_HAT);
    }
}
