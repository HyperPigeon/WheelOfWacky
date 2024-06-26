package net.hyper_pigeon.wacky_wheel.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.TextColor;

import java.util.Optional;

public record SpellType(String name,
                        Optional<String> flavorText,
                        Item item, int castingTime,
                        String onCastFunction,
                        Optional<Integer> duration,
                        Optional<String> onEndFunction,
                        Optional<TextColor> titleColor,
                        Optional<TextColor> flavorTextColor,
                        Optional<Boolean> executeOnCastFunctionAtPlayer,
                        Optional<Boolean> executeOnEndFunctionAtPlayer,
                        Optional<String> onTickFunction,
                        Optional<Boolean> executeOnTickFunctionAtPlayer,
                        Optional<String> onCastTargetSelector,
                        Optional<String> onTickTargetSelector,
                        Optional<String> onEndTargetSelector
                        ) {
    public static final Codec<SpellType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(SpellType::name),
            Codec.STRING.optionalFieldOf("flavorText").forGetter(SpellType::flavorText),
            Registries.ITEM.getCodec().fieldOf("itemID").forGetter(SpellType::item),
            Codec.INT.fieldOf("castingTime").forGetter(SpellType::castingTime),
            Codec.STRING.fieldOf("onCastFunction").forGetter(SpellType::onCastFunction),
            Codec.INT.optionalFieldOf("duration").forGetter(SpellType::duration),
            Codec.STRING.optionalFieldOf("onEndFunction").forGetter(SpellType::onEndFunction),
            TextColor.CODEC.optionalFieldOf("titleColor").forGetter(SpellType::titleColor),
            TextColor.CODEC.optionalFieldOf("flavorTextColor").forGetter(SpellType::flavorTextColor),
            Codec.BOOL.optionalFieldOf("executeOnCastFunctionAtPlayer").forGetter(SpellType::executeOnCastFunctionAtPlayer),
            Codec.BOOL.optionalFieldOf("executeOnEndFunctionAtPlayer").forGetter(SpellType::executeOnEndFunctionAtPlayer),
            Codec.STRING.optionalFieldOf("onTickFunction").forGetter(SpellType::onTickFunction),
            Codec.BOOL.optionalFieldOf("executeOnTickFunctionAtPlayer").forGetter(SpellType::executeOnTickFunctionAtPlayer),
            Codec.STRING.optionalFieldOf("onCastTargetSelector").forGetter(SpellType::onCastTargetSelector),
            Codec.STRING.optionalFieldOf("onTickTargetSelector").forGetter(SpellType::onTickTargetSelector),
            Codec.STRING.optionalFieldOf("onEndTargetSelector").forGetter(SpellType::onCastTargetSelector)
    ).apply(instance, SpellType::new));

}
