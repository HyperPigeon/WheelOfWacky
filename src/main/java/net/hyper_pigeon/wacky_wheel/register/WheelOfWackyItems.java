package net.hyper_pigeon.wacky_wheel.register;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.item.PropellerHat;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WheelOfWackyItems {
    public static final PropellerHat PROPELLER_HAT = new PropellerHat(new Item.Settings().maxCount(1));
    public static final BlockItem WACKY_WHEEL_ITEM = new BlockItem(WheelOfWacky.WACKY_WHEEL_BLOCK, new Item.Settings());

    private static final ItemGroup WACKY_WHEEL_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(WACKY_WHEEL_ITEM))
            .displayName(Text.translatable("itemGroup.wacky_wheel.wheel_of_wacky"))
            .entries((context, entries) -> {
                entries.add(WACKY_WHEEL_ITEM);
            }).build();

    public static void init() {
        Registry.register(Registries.ITEM, Identifier.of(WheelOfWacky.MOD_ID, "propeller_hat"), PROPELLER_HAT);
        Registry.register(Registries.ITEM, Identifier.of(WheelOfWacky.MOD_ID, "wacky_wheel"), WACKY_WHEEL_ITEM);

        Registry.register(Registries.ITEM_GROUP, Identifier.of("wacky_wheel", "wacky_wheel_group"), WACKY_WHEEL_ITEM_GROUP);



    }
}
