package net.hyper_pigeon.wacky_wheel.spell;

import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class SpellTypeRegistry {
    private static final Map<Identifier, SpellType> LOCATION_TO_SPELL_TYPE = new HashMap<>();
    private static final Map<SpellType, Identifier> SPELL_TYPE_TO_LOCATION = new HashMap<>();
    public static Optional<SpellType> register(Identifier location, SpellType value) {
        if (LOCATION_TO_SPELL_TYPE.containsKey(location)) {
            WheelOfWacky.LOG.warn("Tried registering SpellType at location '{}'. Of which has already been registered. (Skipping).", location);
            return Optional.empty();
        }
        SPELL_TYPE_TO_LOCATION.put(value, location);
        return Optional.ofNullable(LOCATION_TO_SPELL_TYPE.put(location, value));
    }

    public static Optional<SpellType> update(Identifier location, SpellType value) {
        LOCATION_TO_SPELL_TYPE.remove(location, value);
        if (SPELL_TYPE_TO_LOCATION.containsValue(location)) {
            SPELL_TYPE_TO_LOCATION.forEach((spellType, spellLocation) -> {
                if (spellLocation == location) {
                    SPELL_TYPE_TO_LOCATION.remove(spellType, location);
                }
            });
        }
        return register(location, value);
    }

    public static Identifier getKey(SpellType spellType) {
        return SPELL_TYPE_TO_LOCATION.get(spellType);
    }

    public static boolean containsKey(Identifier location) {
        return LOCATION_TO_SPELL_TYPE.containsKey(location);
    }

    public static SpellType get(Identifier location) {
        return LOCATION_TO_SPELL_TYPE.get(location);
    }

    public static int size() {
        return LOCATION_TO_SPELL_TYPE.size();
    }

    public static Stream<SpellType> valueStream() {
        return LOCATION_TO_SPELL_TYPE.values().stream();
    }

    public static Stream<Identifier> identifierStream() {
        return SPELL_TYPE_TO_LOCATION.values().stream();
    }

    public static Stream<Map.Entry<Identifier, SpellType>> asStream() {
        return LOCATION_TO_SPELL_TYPE.entrySet().stream();
    }

    public static void clear() {
        LOCATION_TO_SPELL_TYPE.clear();
        SPELL_TYPE_TO_LOCATION.clear();
    }

}
