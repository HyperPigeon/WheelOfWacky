package net.hyper_pigeon.wacky_wheel.token;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public record TokenType(
        Item item,
        int count
) {
    public static final Codec<TokenType> CODEC =  RecordCodecBuilder.create(instance ->
            instance.group(
                Registries.ITEM.getCodec().fieldOf("id").forGetter(TokenType::item),
                Codec.INT.fieldOf("count").forGetter(TokenType::count)
            ).apply(instance, TokenType::new));
}
