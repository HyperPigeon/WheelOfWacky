package net.hyper_pigeon.wacky_wheel.register;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.hyper_pigeon.wacky_wheel.spell.SpellManager;
import net.hyper_pigeon.wacky_wheel.spell.SpellType;
import net.hyper_pigeon.wacky_wheel.spell.SpellTypeRegistry;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WheelOfWackyCommands {

    private static final SuggestionProvider<ServerCommandSource> SPELL_SUGGESTIONS = (context, builder) ->
            CommandSource.suggestMatching(() -> {
                ServerWorld world = context.getSource().getWorld();
                return SpellTypeRegistry.identifierStream().map(identifier -> identifier.toString().split("wacky_wheel:")[1]).sorted().iterator();
            }, builder);

    public static void init(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("wheel")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.argument("spell_type", StringArgumentType.string())
                            .suggests(SPELL_SUGGESTIONS)
                            .executes(context -> {
                                String spell_name = StringArgumentType.getString(context, "spell_type");

                                try {
                                    SpellType spellType = SpellTypeRegistry.get(Identifier.of("wacky_wheel:" + spell_name));
                                    ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayerOrThrow();

                                    SpellManager.addSpell(spellType,serverPlayerEntity);

                                    context.getSource()
                                            .sendFeedback(
                                                    () -> Text.literal(
                                                            "Casted spell = %s".formatted(spell_name)),
                                                    false);
                                }
                                catch(Error e) {
                                    context.getSource()
                                            .sendError(
                                                    Text.literal(
                                                            "Failed to cast spell = %s.".formatted(spell_name) + "Error: " + e));
                                }


                                return 1;
                            })));
        });
    }
}
