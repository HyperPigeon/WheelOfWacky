package net.hyper_pigeon.wacky_wheel.block.entity;

import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.spell.SpellManager;
import net.hyper_pigeon.wacky_wheel.spell.SpellType;
import net.hyper_pigeon.wacky_wheel.spell.SpellTypeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WackyWheelBlockEntity extends BlockEntity {

    private static final Random random = new Random();

    private final List<SpellType> wedgeSpells = new ArrayList<>();

    private ServerPlayerEntity spinningPlayer;

    private float speed = 0.0F;
    private float friction = 0.975F;

    private float roll = 0.0F;

    private boolean spellFlag = false;

    public WackyWheelBlockEntity(BlockPos pos, BlockState state) {
        super(WheelOfWacky.WACKY_WHEEL_BLOCK_ENTITY, pos, state);
        initWedgeSpells();
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        getWedgeSpells().clear();
        nbt.getList("wedgeSpells",10).forEach(nbtElement -> {
            SpellType.CODEC.parse(NbtOps.INSTANCE, nbtElement)
                    .resultOrPartial(string -> WheelOfWacky.LOG.error("Failed to parse wedge spells: '{}'", string))
                    .ifPresent(wedgeSpells::add);
        });
        this.roll = nbt.getFloat("roll");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        NbtList nbtList = new NbtList();
        getWedgeSpells().forEach(spellType -> {
            nbtList.add(SpellType.CODEC.encodeStart(NbtOps.INSTANCE,spellType).getPartialOrThrow());
        });
        nbt.put("wedgeSpells",nbtList);
        nbt.putFloat("roll",roll);
    }

    public float getSpeed(){
        return speed;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public float getRoll(){
        return roll;
    }

    public void setRoll(float roll){
        if(roll <= 360F) {
            this.roll = roll;
        }
        else {
            this.roll = roll - 360F;
        }
    }

    public float getFriction(){
        return friction;
    }

    public boolean isSpinning(){
        return speed > 0.5F;
    }

    public void initWedgeSpells(){
        for (int i = 0; i < 8; i++) {
            wedgeSpells.add((SpellType) SpellTypeRegistry.valueStream().toArray()[random.nextInt(SpellTypeRegistry.size())]);
        }
    }

    public List<SpellType> getWedgeSpells() {
        return wedgeSpells;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    public void markDirty() {
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        super.markDirty();
    }


    public static void clientTick(World world, BlockPos blockPos, BlockState blockState, WackyWheelBlockEntity wackyWheelBlockEntity) {

    }

    public static void serverTick(World world, BlockPos blockPos, BlockState blockState, WackyWheelBlockEntity wackyWheelBlockEntity) {
        if(wackyWheelBlockEntity.isSpinning() && wackyWheelBlockEntity.getSpinningPlayer() != null) {
            wackyWheelBlockEntity.setRoll(wackyWheelBlockEntity.getRoll() + wackyWheelBlockEntity.getSpeed());
            wackyWheelBlockEntity.setSpeed(wackyWheelBlockEntity.getSpeed()* wackyWheelBlockEntity.getFriction());
            wackyWheelBlockEntity.markDirty();
            wackyWheelBlockEntity.getSpinningPlayer().sendMessage(Text.literal("roll: " + wackyWheelBlockEntity.getRoll()), false);
        }
        else if(!wackyWheelBlockEntity.isSpinning() && wackyWheelBlockEntity.spellFlag) {
            wackyWheelBlockEntity.setSpeed(0F);
            wackyWheelBlockEntity.spellFlag = false;
            int wedgeIndex = wackyWheelBlockEntity.getWedgeIndexFromRoll();
            SpellManager.addSpell(wackyWheelBlockEntity.getWedgeSpells().get(wedgeIndex),wackyWheelBlockEntity.getSpinningPlayer());
            wackyWheelBlockEntity.setSpinningPlayer(null);
        }
    }

    public void spin(ServerPlayerEntity serverPlayerEntity){
        if(!isSpinning() && !spellFlag) {
            setSpinningPlayer(serverPlayerEntity);
            float startSpeed = random.nextFloat(2.5F) + 5F;
            setSpeed(startSpeed);
            spellFlag = true;
        }
    }

    public ServerPlayerEntity getSpinningPlayer() {
        return spinningPlayer;
    }

    public void setSpinningPlayer(ServerPlayerEntity spinningPlayer) {
        this.spinningPlayer = spinningPlayer;
    }

    public int getWedgeIndexFromRoll(){
        return (int) Math.ceil(this.roll/360);
    }
}
