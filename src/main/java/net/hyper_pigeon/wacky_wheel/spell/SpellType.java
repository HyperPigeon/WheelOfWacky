package net.hyper_pigeon.wacky_wheel.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.Optional;

public record SpellType(String name, String playerName, boolean showTitle, Optional<String> flavorText, Item item, String mcfunctionName) {
    public static final Codec<SpellType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(SpellType::name),
            Codec.STRING.fieldOf("playerName").forGetter(SpellType::playerName),
            Codec.BOOL.fieldOf("showTitle").forGetter(SpellType::showTitle),
            Codec.STRING.optionalFieldOf("flavorText").forGetter(SpellType::flavorText),
            Registries.ITEM.getCodec().fieldOf("itemID").forGetter(SpellType::item),
            Codec.STRING.fieldOf("mcfunctionName").forGetter(SpellType::mcfunctionName)
    ).apply(instance, SpellType::new));

}
