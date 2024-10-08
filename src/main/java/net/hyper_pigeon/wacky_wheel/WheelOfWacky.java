package net.hyper_pigeon.wacky_wheel;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.hyper_pigeon.wacky_wheel.block.WackyWheelBlock;
import net.hyper_pigeon.wacky_wheel.block.entity.WackyWheelBlockEntity;
import net.hyper_pigeon.wacky_wheel.config.WackyWheelConfig;
import net.hyper_pigeon.wacky_wheel.register.WheelOfWackyCommands;
import net.hyper_pigeon.wacky_wheel.register.WheelOfWackyData;
import net.hyper_pigeon.wacky_wheel.register.WheelOfWackyItems;
import net.hyper_pigeon.wacky_wheel.spell.SpellManager;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WheelOfWacky implements ModInitializer {

    public static final String MOD_ID = "wacky_wheel";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);

    public static final Block WACKY_WHEEL_BLOCK = new WackyWheelBlock(Block.Settings.create().strength(1.5F, 1200.0F));
    public static final BlockEntityType<WackyWheelBlockEntity> WACKY_WHEEL_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(MOD_ID, "wacky_wheel_block_entity"),
            FabricBlockEntityTypeBuilder.create(WackyWheelBlockEntity::new, WACKY_WHEEL_BLOCK).build()
    );

    public static final WackyWheelConfig CONFIG = WackyWheelConfig.createToml(FabricLoader.getInstance().getConfigDir(), "", MOD_ID, WackyWheelConfig.class);

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "wacky_wheel"), WACKY_WHEEL_BLOCK);
        WheelOfWackyData.init();
        WheelOfWackyItems.init();
        WheelOfWackyCommands.init();
        SpellManager.init();
    }
}
