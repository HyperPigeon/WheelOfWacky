package net.hyper_pigeon.wacky_wheel.spell;

import com.mojang.brigadier.ParseResults;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class SpellManager {

    private static List<Spell> inProgressSpells = new ArrayList<>();

    public static void addSpell(SpellType spellType, ServerPlayerEntity serverPlayerEntity) {
        inProgressSpells.add(new Spell(spellType,serverPlayerEntity));
        if(spellType.flavorText().isPresent()) {
            String flavorTextColor = spellType.flavorTextColor().isPresent() ? spellType.flavorTextColor().get().getHexCode() : "gray";
            String parsedCommand = String.format("execute as %s run title @s subtitle {\"text\":\"%s\",\"color\":\"%s\",\"italic\":true}", serverPlayerEntity.getUuidAsString(), spellType.flavorText().get(), flavorTextColor);
            ServerCommandSource commandSource = serverPlayerEntity.getServer().getCommandSource().withSilent().withMaxLevel(2);
            ParseResults<ServerCommandSource> parseResults = commandSource.getDispatcher().parse(parsedCommand, commandSource);
            serverPlayerEntity.getServer().getCommandManager().execute(parseResults, parsedCommand);
        }
        String titleColor = spellType.titleColor().isPresent() ? spellType.titleColor().get().getHexCode() : "gray";
        String parsedCommand = String.format("execute as %s run title @s title {\"text\":\"%s\",\"bold\":true,\"color\":\"%s\"}", serverPlayerEntity.getUuidAsString(), spellType.name(), titleColor);
        ServerCommandSource commandSource = serverPlayerEntity.getServer().getCommandSource().withSilent().withMaxLevel(2);
        ParseResults<ServerCommandSource> parseResults = commandSource.getDispatcher().parse(parsedCommand, commandSource);
        serverPlayerEntity.getServer().getCommandManager().execute(parseResults, parsedCommand);
    }

    public static void castSpell(SpellType spellType, ServerPlayerEntity serverPlayerEntity){
        String executeModifier = spellType.executeOnCastFunctionAtPlayer().isPresent() && spellType.executeOnCastFunctionAtPlayer().get() ? "at" : "as";
        String targetSelector = spellType.onCastTargetSelector().isPresent() ? spellType.onCastTargetSelector().get() : serverPlayerEntity.getUuidAsString();

        String parsedCommand = "execute " + executeModifier  + " " + targetSelector + " run function " + "wacky_wheel:" + spellType.onCastFunction();
        ServerCommandSource commandSource = serverPlayerEntity.getServer().getCommandSource().withSilent().withMaxLevel(2);
        ParseResults<ServerCommandSource> parseResults = commandSource.getDispatcher().parse(parsedCommand, commandSource);
        serverPlayerEntity.getServer().getCommandManager().execute(parseResults, parsedCommand);
    }

    public static void onSpellTick(SpellType spellType, ServerPlayerEntity serverPlayerEntity) {
        if(spellType.onTickFunction().isPresent()) {
            String executeModifier = spellType.executeOnTickFunctionAtPlayer().isPresent() && spellType.executeOnTickFunctionAtPlayer().get() ? "at" : "as";
            String targetSelector = spellType.onTickTargetSelector().isPresent() ? spellType.onTickTargetSelector().get() : serverPlayerEntity.getUuidAsString();

            String parsedCommand = "execute " + executeModifier  + " " + targetSelector + " run function " + "wacky_wheel:" + spellType.onTickFunction().get();
            ServerCommandSource commandSource = serverPlayerEntity.getServer().getCommandSource().withSilent().withMaxLevel(2);
            ParseResults<ServerCommandSource> parseResults = commandSource.getDispatcher().parse(parsedCommand, commandSource);
            serverPlayerEntity.getServer().getCommandManager().execute(parseResults, parsedCommand);
        }
    }

    public static void onSpellEnd(SpellType spellType, ServerPlayerEntity serverPlayerEntity){
        if(spellType.onEndFunction().isPresent()) {
            String executeModifier = spellType.executeOnEndFunctionAtPlayer().isPresent() && spellType.executeOnEndFunctionAtPlayer().get() ? "at" : "as";
            String targetSelector = spellType.onEndTargetSelector().isPresent() ? spellType.onEndTargetSelector().get() : serverPlayerEntity.getUuidAsString();

            String parsedCommand = "execute " + executeModifier  + " " + targetSelector + " run function " + "wacky_wheel:" + spellType.onEndFunction().get();
            ServerCommandSource commandSource = serverPlayerEntity.getServer().getCommandSource().withSilent().withMaxLevel(2);
            ParseResults<ServerCommandSource> parseResults = commandSource.getDispatcher().parse(parsedCommand, commandSource);
            serverPlayerEntity.getServer().getCommandManager().execute(parseResults, parsedCommand);
        }
    }

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
            for(Spell spell : inProgressSpells) {
                if (serverWorld.getTime() >= spell.getStartTime() && !spell.hasBeenCasted()) {
                    castSpell(spell.getSpellType(), spell.getPlayer());
                    spell.setHasBeenCasted(true);
                }
                else if (serverWorld.getTime() >= spell.getEndTime()) {
                    onSpellEnd(spell.getSpellType(), spell.getPlayer());
                }
                else {
                    onSpellTick(spell.getSpellType(), spell.getPlayer());
                }
            }
            inProgressSpells.removeIf(spell -> serverWorld.getTime() >= spell.getEndTime());
        });
    }
}
