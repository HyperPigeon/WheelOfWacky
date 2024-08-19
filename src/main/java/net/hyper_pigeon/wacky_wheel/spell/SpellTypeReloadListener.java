package net.hyper_pigeon.wacky_wheel.spell;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;

import java.util.Map;

public class SpellTypeReloadListener extends JsonDataLoader  implements IdentifiableResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogUtils.getLogger();

    public SpellTypeReloadListener() {
        super(GSON, "spell_type");
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(WheelOfWacky.MOD_ID, "spell_type");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        SpellTypeRegistry.clear();
        prepared.forEach((location, element) -> {
            try {
                var dataResult = SpellType.CODEC.parse(JsonOps.INSTANCE,element);
                var spellType = dataResult.resultOrPartial((s -> {}));

                if (dataResult.error().isPresent()) {
                    if (spellType.isPresent())
                        WheelOfWacky.LOG.warn("Error loading Spell Type '{}'. Spell Type will only be partially loaded. {}", location, dataResult.error().get().message());
                    else
                        WheelOfWacky.LOG.warn("Error loading Spell Type '{}'. (Skipping). {}", location, dataResult.error().get().message());
                }


                if (spellType.isEmpty()) return;
                if (SpellTypeRegistry.containsKey(location))
                    SpellTypeRegistry.update(location, spellType.get());
                else
                    SpellTypeRegistry.register(location, spellType.get());
            }
            catch(Exception e) {
                WheelOfWacky.LOG.error("Could not load spell type at location '{}'. (Skipping). {}", location, e);
            }


        });

    }

}
