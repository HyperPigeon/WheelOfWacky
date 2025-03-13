package net.hyper_pigeon.wacky_wheel.token;

import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.spell.SpellType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class TokenTypeRegistry {
    public static final Map<Identifier, TokenType> LOCATION_TO_TOKEN_TYPE = new HashMap<>();
    public static final Map<Item, TokenType> ITEM_TO_TOKEN_TYPE = new HashMap<>();

    public static Optional<TokenType> register(Identifier location, TokenType value) {
        if (LOCATION_TO_TOKEN_TYPE.containsKey(location)) {
            WheelOfWacky.LOG.warn("Tried registering TokenType at location '{}'. Of which has already been registered. (Skipping).", location);
            return Optional.empty();
        }
        ITEM_TO_TOKEN_TYPE.put(value.item(), value);
        return Optional.ofNullable(LOCATION_TO_TOKEN_TYPE.put(location, value));
    }

    public static Optional<TokenType> update(Identifier location, TokenType value) {
        LOCATION_TO_TOKEN_TYPE.remove(location, value);
        if (ITEM_TO_TOKEN_TYPE.containsKey(value.item())) {
            ITEM_TO_TOKEN_TYPE.remove(value.item(),value);
        }
        return register(location, value);
    }



    public static boolean containsKey(Identifier location) {
        return LOCATION_TO_TOKEN_TYPE.containsKey(location);
    }

    public static TokenType get(Identifier location) {
        return LOCATION_TO_TOKEN_TYPE.get(location);
    }

    public static int size() {
        return LOCATION_TO_TOKEN_TYPE.size();
    }

    public static Stream<TokenType> valueStream() {
        return LOCATION_TO_TOKEN_TYPE.values().stream();
    }


    public static Stream<Map.Entry<Identifier, TokenType>> asStream() {
        return LOCATION_TO_TOKEN_TYPE.entrySet().stream();
    }

    public static void clear() {
        LOCATION_TO_TOKEN_TYPE.clear();
        ITEM_TO_TOKEN_TYPE.clear();
    }
}
