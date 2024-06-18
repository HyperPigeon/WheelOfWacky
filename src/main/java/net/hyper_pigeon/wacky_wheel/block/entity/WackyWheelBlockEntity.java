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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
    private final float friction = 0.975F;
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
        if(nbt.contains("wedgeSpells")) {
            nbt.getList("wedgeSpells",10).forEach(nbtElement -> {
                SpellType.CODEC.parse(NbtOps.INSTANCE, nbtElement)
                        .resultOrPartial(string -> WheelOfWacky.LOG.error("Failed to parse wedge spells: '{}'", string))
                        .ifPresent(wedgeSpells::add);
            });
        }
        else {
            initWedgeSpells();
        }
        this.roll = nbt.getFloat("roll");
        this.speed = nbt.getFloat("speed");
        this.spellFlag = nbt.getBoolean("spellFlag");
//
//        if(!world.isClient() && nbt.contains("spinningPlayerUUID"))
//            this.spinningPlayer = (ServerPlayerEntity) world.getPlayerByUuid(nbt.getUuid("spinningPlayerUUID"));
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
        nbt.putFloat("speed", speed);
        nbt.putBoolean("spellFlag",spellFlag);
//
//        if(spinningPlayer != null) {
//            nbt.putUuid("spinningPlayerUUID",spinningPlayer.getUuid());
//        }

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
        this.roll = MathHelper.wrapDegrees(roll);
    }

    public float getFriction(){
        return friction;
    }

    public boolean isSpinning(){
        return speed > 0.001F;
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
        super.markDirty();
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
    }


    public static void clientTick(World world, BlockPos blockPos, BlockState blockState, WackyWheelBlockEntity wackyWheelBlockEntity) {
        if(wackyWheelBlockEntity.isSpinning() && wackyWheelBlockEntity.spellFlag) {
            Direction direction = blockState.get(WackyWheelBlock.FACING);

            float maxDistance = Math.clamp(wackyWheelBlockEntity.getSpeed() > 5 ? wackyWheelBlockEntity.getSpeed()/3F :wackyWheelBlockEntity.getSpeed()/2F , 0.1F, 8F);
            int particleNum = random.nextInt(11) + 1;

            for(int i = 0; i <= particleNum; i++) {
                double x = (double)blockPos.getX() + 0.55 - (double)((random.nextFloat() * 2 - 1) * maxDistance);
                double y = (double)blockPos.getY() + 0.55 - (double)((random.nextFloat() * 2 - 1) * maxDistance);
                double z = (double)blockPos.getZ() + 0.55 - (double)((random.nextFloat() * 2 - 1) * maxDistance);
                double g = (double)(0.4F - (random.nextFloat() + random.nextFloat()) * 0.4F);

//            Vec3d vec3d = new Vec3d(blockPos.getX() - x ,blockPos.getY() - y ,  blockPos.getZ() - z).normalize();
//            double magnitude = 0.1F + 0.1F/Math.clamp(wackyWheelBlockEntity.getSpeed(), 1,30);

                if (random.nextInt(5) == 0) {
                    world.addParticle(
                            random.nextInt(2) == 1 ?  ParticleTypes.END_ROD :ParticleTypes.ENCHANT,
                            x + direction.getOffsetX()*g,
                            y + direction.getOffsetY()*g,
                            z + direction.getOffsetZ()*g,
                            random.nextGaussian() * 0.005,
                            random.nextGaussian() * 0.005,
                            random.nextGaussian() * 0.005
                    );
                }
            }
        }
    }

    public static void serverTick(World world, BlockPos blockPos, BlockState blockState, WackyWheelBlockEntity wackyWheelBlockEntity) {
        if(wackyWheelBlockEntity.getSpinningPlayer() != null) {
            if(wackyWheelBlockEntity.isSpinning()) {
                wackyWheelBlockEntity.setRoll(wackyWheelBlockEntity.getRoll() + wackyWheelBlockEntity.getSpeed());
                wackyWheelBlockEntity.setSpeed(wackyWheelBlockEntity.getSpeed()* wackyWheelBlockEntity.getFriction());
                wackyWheelBlockEntity.markDirty();
            }
            else if(!wackyWheelBlockEntity.isSpinning() && wackyWheelBlockEntity.spellFlag) {
                wackyWheelBlockEntity.setSpeed(0F);
                wackyWheelBlockEntity.spellFlag = false;
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
            float startSpeed = random.nextFloat(15F) + 20F;
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
        return (int) Math.ceil(this.roll/360);
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

}
