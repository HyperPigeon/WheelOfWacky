package net.hyper_pigeon.wacky_wheel.block.entity;

import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.block.WackyWheelBlock;
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
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WackyWheelBlockEntity extends BlockEntity {

    private static final Random random = new Random();
    private final List<SpellType> wedgeSpells = new ArrayList<>();
    private ServerPlayerEntity spinningPlayer;
    private float speed = 0.0F;
    private float roll = 0.0F;

    private float previousRoll = 0.0F;

    private boolean spellFlag = false;
    private float particleDistance = 7.5F;


    public WackyWheelBlockEntity(BlockPos pos, BlockState state) {
        super(WheelOfWacky.WACKY_WHEEL_BLOCK_ENTITY, pos, state);
        initWedgeSpells();
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        getWedgeSpells().clear();
        if(nbt.contains("wedgeSpells")) {
            nbt.getList("wedgeSpells",10).forEach(nbtElement -> {
                SpellType.CODEC.parse(NbtOps.INSTANCE, nbtElement)
                        .resultOrPartial(string -> WheelOfWacky.LOG.error("Failed to parse wedge spells: '{}'", string))
                        .ifPresent(wedgeSpells::add);
            });
        }
        this.roll = nbt.getFloat("roll");
        this.previousRoll = nbt.getFloat("previousRoll");
        this.speed = nbt.getFloat("speed");
        this.spellFlag = nbt.getBoolean("spellFlag");

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
        nbt.putFloat("previousRoll", roll);
        nbt.putFloat("speed", speed);
        nbt.putBoolean("spellFlag",spellFlag);
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
        roll = roll % 360;
        if (roll < 0) {
            roll += 360;
        }
        this.roll = roll;
    }

    public float getFriction(){
        float friction = 0.05F;
        return friction;
    }

    public boolean isSpinning(){
        return speed > 0.01F;
    }

    public void initWedgeSpells(){
        for (int i = 0; i < 16; i++) {
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
        super.markDirty();
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
    }


    public static void clientTick(World world, BlockPos blockPos, BlockState blockState, WackyWheelBlockEntity wackyWheelBlockEntity) {
        if(wackyWheelBlockEntity.isSpinning() && wackyWheelBlockEntity.getSpellFlag()) {
            Direction direction = blockState.get(WackyWheelBlock.FACING);

            wackyWheelBlockEntity.particleDistance = (float) MathHelper.lerp(0.05 * Math.clamp(wackyWheelBlockEntity.getSpeed()/20,0.10,1), wackyWheelBlockEntity.particleDistance, 0.3);
            int particleNum = random.nextInt(2) + 2;

            for(int i = 0; i <= particleNum; i++) {
                double x = (double)blockPos.getX() + 0.55 - (double)((random.nextFloat() * 2 - 1) *  wackyWheelBlockEntity.particleDistance);
                double y = (double)blockPos.getY() + 0.55 - (double)((random.nextFloat() * 2 - 1) *  wackyWheelBlockEntity.particleDistance);
                double z = (double)blockPos.getZ() + 0.55 - (double)((random.nextFloat() * 2 - 1) *  wackyWheelBlockEntity.particleDistance);
                double g = 0.4F - (random.nextFloat() + random.nextFloat()) * 0.4F;

            Vec3d vec3d = new Vec3d(blockPos.getX() - x ,blockPos.getY() - y ,  blockPos.getZ() - z).normalize();
            double magnitude = 0.005F;

                world.addParticle(
                        new DustParticleEffect(new Vector3f(160,32,240), 1.2F),
                        x + direction.getOffsetX()*g,
                        y + direction.getOffsetY()*g,
                        z + direction.getOffsetZ()*g,
                        vec3d.getX() * magnitude,
                        vec3d.getY() * magnitude,
                        vec3d.getZ() * magnitude);
            }
        }
        else {
            wackyWheelBlockEntity.particleDistance = 7.5F;
        }
    }

    public static void serverTick(World world, BlockPos blockPos, BlockState blockState, WackyWheelBlockEntity wackyWheelBlockEntity) {
        if(wackyWheelBlockEntity.getSpinningPlayer() != null) {
            if(wackyWheelBlockEntity.isSpinning()) {
                wackyWheelBlockEntity.setPreviousRoll(wackyWheelBlockEntity.getRoll());
                wackyWheelBlockEntity.setRoll(wackyWheelBlockEntity.getRoll() + wackyWheelBlockEntity.getSpeed());
                wackyWheelBlockEntity.setSpeed(wackyWheelBlockEntity.getSpeed() - wackyWheelBlockEntity.getFriction());
                wackyWheelBlockEntity.markDirty();
            }
            else if(!wackyWheelBlockEntity.isSpinning() && wackyWheelBlockEntity.getSpellFlag()) {
                wackyWheelBlockEntity.setSpeed(0F);
                wackyWheelBlockEntity.setSpellFlag(false);
                int wedgeIndex = wackyWheelBlockEntity.getWedgeIndexFromRoll();
                SpellManager.addSpell(wackyWheelBlockEntity.getWedgeSpells().get(wedgeIndex),wackyWheelBlockEntity.getSpinningPlayer());
                wackyWheelBlockEntity.setSpinningPlayer(null);
                wackyWheelBlockEntity.markDirty();
            }
        }
        else if(wackyWheelBlockEntity.isSpinning()) {
            wackyWheelBlockEntity.setSpeed(0);
            wackyWheelBlockEntity.spellFlag = false;
            wackyWheelBlockEntity.markDirty();
        }

    }

    public void spin(ServerPlayerEntity serverPlayerEntity){
        if(!isSpinning() && !spellFlag) {
            setSpinningPlayer(serverPlayerEntity);
            float startSpeed = random.nextFloat(10F) + 10F;
            setSpeed(startSpeed);
            spellFlag = true;
            markDirty();
        }
    }

    public ServerPlayerEntity getSpinningPlayer() {
        return spinningPlayer;
    }

    public void setSpinningPlayer(ServerPlayerEntity spinningPlayer) {
        this.spinningPlayer = spinningPlayer;
    }

    public int getWedgeIndexFromRoll(){
        int index = (int)(this.getRoll()/22.5F);
        return MathHelper.clamp(index, 0, 15);
    }

    public String getCurrentWedgeName(){
        int index = getWedgeIndexFromRoll();
        SpellType spellType = getWedgeSpells().get(index);
        return spellType.name();
    }

    public SpellType getCurrentWedgeSpell(){
        int index = getWedgeIndexFromRoll();
        SpellType spellType = getWedgeSpells().get(index);
        return spellType;
    }

    public void setSpellFlag(boolean bl) {
        this.spellFlag = bl;
    }

    public boolean getSpellFlag() {
        return this.spellFlag;
    }

    public float getPreviousRoll(){
        return this.previousRoll;
    }


    public void setPreviousRoll(float previousRoll) {
        this.previousRoll = previousRoll;
    }
}
