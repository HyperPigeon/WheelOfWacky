package net.hyper_pigeon.wacky_wheel.register;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.hyper_pigeon.wacky_wheel.spell.SpellTypeReloadListener;
import net.minecraft.resource.ResourceType;

public class WheelOfWackyData {
    public static void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SpellTypeReloadListener());
    }
}
