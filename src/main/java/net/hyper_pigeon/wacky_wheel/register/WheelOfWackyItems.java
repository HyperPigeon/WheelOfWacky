package net.hyper_pigeon.wacky_wheel.register;

import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.item.PropellerHat;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WheelOfWackyItems {
    public static final PropellerHat PROPELLER_HAT = new PropellerHat(new Item.Settings().maxCount(1));
    public static void init() {
        Registry.register(Registries.ITEM, Identifier.of(WheelOfWacky.MOD_ID, "propeller_hat"), PROPELLER_HAT);
        Registry.register(Registries.ITEM, Identifier.of(WheelOfWacky.MOD_ID, "wacky_wheel"), new BlockItem(WheelOfWacky.WACKY_WHEEL_BLOCK, new Item.Settings()));
    }
}
