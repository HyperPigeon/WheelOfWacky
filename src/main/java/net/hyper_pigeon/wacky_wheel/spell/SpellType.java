package net.hyper_pigeon.wacky_wheel.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.TextColor;

import java.util.Optional;

public record SpellType(String name, boolean showTitle, Optional<String> flavorText, Item item, int castingTime, String onCastFunction, Optional<Integer> duration, Optional<String> onEndFunction, Optional<TextColor> color, Optional<TextColor> flavorTextColor) {
    public static final Codec<SpellType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(SpellType::name),
            Codec.BOOL.fieldOf("showTitle").forGetter(SpellType::showTitle),
            Codec.STRING.optionalFieldOf("flavorText").forGetter(SpellType::flavorText),
            Registries.ITEM.getCodec().fieldOf("itemID").forGetter(SpellType::item),
            Codec.INT.fieldOf("castingTime").forGetter(SpellType::castingTime),
            Codec.STRING.fieldOf("onCastFunction").forGetter(SpellType::onCastFunction),
            Codec.INT.optionalFieldOf("duration").forGetter(SpellType::duration),
            Codec.STRING.optionalFieldOf("onEndFunction").forGetter(SpellType::onEndFunction),
            TextColor.CODEC.optionalFieldOf("titleColor").forGetter(SpellType::color),
            TextColor.CODEC.optionalFieldOf("flavorTextColor").forGetter(SpellType::flavorTextColor)
    ).apply(instance, SpellType::new));

}
