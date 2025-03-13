package net.hyper_pigeon.wacky_wheel.token;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;

public class TokenTypeReloadListener extends JsonDataLoader implements IdentifiableResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public TokenTypeReloadListener() {
        super(GSON, "token");
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(WheelOfWacky.MOD_ID, "token");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        TokenTypeRegistry.clear();
        prepared.forEach((location, element) -> {
            try {
                var dataResult = TokenType.CODEC.parse(JsonOps.INSTANCE,element);
                var tokenType = dataResult.resultOrPartial((s -> {}));

                if (dataResult.error().isPresent()) {
                    if (tokenType.isPresent())
                        WheelOfWacky.LOG.warn("Error loading Token Type '{}'. Token Type will only be partially loaded. {}", location, dataResult.error().get().message());
                    else
                        WheelOfWacky.LOG.warn("Error loading Token Type '{}'. (Skipping). {}", location, dataResult.error().get().message());
                }


                if (tokenType.isEmpty()) return;
                if (TokenTypeRegistry.containsKey(location))
                    TokenTypeRegistry.update(location, tokenType.get());
                else {
                    WheelOfWacky.CONFIG.enabledTokens.putIfAbsent(location.toString(), true);
                    if (WheelOfWacky.CONFIG.enabledTokens.getOrDefault(location.toString(), false)) {
                        TokenTypeRegistry.register(location, tokenType.get());
                    }
                }
            }
            catch(Exception e) {
                WheelOfWacky.LOG.error("Could not load token type at location '{}'. (Skipping). {}", location, e);
            }


        });

    }
}
