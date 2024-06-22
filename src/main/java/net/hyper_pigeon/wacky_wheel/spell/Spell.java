package net.hyper_pigeon.wacky_wheel.spell;

import net.minecraft.server.network.ServerPlayerEntity;

public class Spell {
    private SpellType spellType;
    private ServerPlayerEntity player;
    private long startTime;
    private long endTime;

    public Spell(SpellType spellType, ServerPlayerEntity player){
        this.spellType = spellType;
        this.player = player;
        this.startTime = player.getServerWorld().getTime() + spellType.castingTime();
        this.endTime = spellType.duration().isPresent() ? player.getServerWorld().getTime() + spellType.castingTime() +  spellType.duration().get() : startTime;
    }

    public long getStartTime(){
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public SpellType getSpellType() {
        return spellType;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }


}
