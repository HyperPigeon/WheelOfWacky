package net.hyper_pigeon.wacky_wheel.register;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class WheelOfWackyGamerules {
    public static final GameRules.Key<GameRules.BooleanRule> PAY_TO_SPIN_WHEEL_OF_WACKY =
            GameRuleRegistry.register("payToSpinWheelOfWacky", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    public static void init(){

    }
}
