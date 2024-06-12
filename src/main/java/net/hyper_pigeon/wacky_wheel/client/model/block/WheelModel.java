package net.hyper_pigeon.wacky_wheel.client.model.block;

import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class WheelModel extends Model {
    public WheelModel(Optional<Identifier> parent, Optional<String> variant, TextureKey... requiredTextureKeys) {
        super(parent, variant, requiredTextureKeys);
    }
}
