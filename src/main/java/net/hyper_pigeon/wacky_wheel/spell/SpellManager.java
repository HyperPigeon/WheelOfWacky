package net.hyper_pigeon.wacky_wheel.spell;

import com.mojang.brigadier.ParseResults;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class SpellManager {

    private static List<Spell> inProgressSpells = new ArrayList<>();

    public static void addSpell(SpellType spellType, ServerPlayerEntity serverPlayerEntity) {
        inProgressSpells.add(new Spell(spellType,serverPlayerEntity));
        if(spellType.flavorText().isPresent()) {
            String parsedCommand = String.format("execute as %s run title @s subtitle {\"text\":\"%s\",\"color\":\"gray\",\"italic\":true}", serverPlayerEntity.getUuidAsString(), spellType.flavorText().get());
            ServerCommandSource commandSource = serverPlayerEntity.getServer().getCommandSource().withSilent().withMaxLevel(2);
            ParseResults<ServerCommandSource> parseResults = commandSource.getDispatcher().parse(parsedCommand, commandSource);
            serverPlayerEntity.getServer().getCommandManager().execute(parseResults, parsedCommand);
        }
        String parsedCommand = String.format("execute as %s run title @s title {\"text\":\"%s\",\"bold\":true}", serverPlayerEntity.getUuidAsString(), spellType.name());
        ServerCommandSource commandSource = serverPlayerEntity.getServer().getCommandSource().withSilent().withMaxLevel(2);
        ParseResults<ServerCommandSource> parseResults = commandSource.getDispatcher().parse(parsedCommand, commandSource);
        serverPlayerEntity.getServer().getCommandManager().execute(parseResults, parsedCommand);
    }

    public static void castSpell(SpellType spellType, ServerPlayerEntity serverPlayerEntity){
        String parsedCommand = "execute as " + serverPlayerEntity.getUuidAsString() + " run function " + "wacky_wheel:" + spellType.onCastFunction();
        ServerCommandSource commandSource = serverPlayerEntity.getServer().getCommandSource().withSilent().withMaxLevel(2);
        ParseResults<ServerCommandSource> parseResults = commandSource.getDispatcher().parse(parsedCommand, commandSource);
        serverPlayerEntity.getServer().getCommandManager().execute(parseResults, parsedCommand);
    }

    public static void onSpellEnd(SpellType spellType, ServerPlayerEntity serverPlayerEntity){
        if(spellType.onEndFunction().isPresent()) {
            String parsedCommand = "execute as " + serverPlayerEntity.getUuidAsString() + " run function " + "wacky_wheel:" + spellType.onEndFunction();
            ServerCommandSource commandSource = serverPlayerEntity.getServer().getCommandSource();
            ParseResults<ServerCommandSource> parseResults = commandSource.getDispatcher().parse(parsedCommand, commandSource);
            serverPlayerEntity.getServer().getCommandManager().execute(parseResults, parsedCommand);
        }
    }

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
            for(Spell spell : inProgressSpells) {
                if (serverWorld.getTime() >= spell.getStartTime()) {
                    castSpell(spell.getSpellType(), spell.getPlayer());
                }
                if (serverWorld.getTime() >= spell.getEndTime()) {
                    onSpellEnd(spell.getSpellType(), spell.getPlayer());
                }
            }
            inProgressSpells.removeIf(spell -> serverWorld.getTime() >= spell.getEndTime());
        });
    }
}
