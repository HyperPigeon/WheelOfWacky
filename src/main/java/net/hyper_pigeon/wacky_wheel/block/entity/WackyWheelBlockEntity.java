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
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
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

    private int previousIndex = 0;

    public WackyWheelBlockEntity(BlockPos pos, BlockState state) {
        super(WheelOfWacky.WACKY_WHEEL_BLOCK_ENTITY, pos, state);
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
        if(world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }


    public static void clientTick(World world, BlockPos blockPos, BlockState blockState, WackyWheelBlockEntity wackyWheelBlockEntity) {
        if(wackyWheelBlockEntity.isSpinning() && wackyWheelBlockEntity.getSpellFlag()) {
            Direction direction = blockState.get(WackyWheelBlock.FACING);

            int particleNum = random.nextInt(2) + 2;

            for(int i = 0; i <= particleNum; i++) {
                double x = (double)blockPos.getX() + 0.55 - (double)((random.nextFloat() * 2 - 1) *  3.5F);
                double y = (double)blockPos.getY() + 0.55 - (double)((random.nextFloat() * 2 - 1) *  3.5F);
                double z = (double)blockPos.getZ() + 0.55 - (double)((random.nextFloat() * 2 - 1) *  3.5F);
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

            int index = wackyWheelBlockEntity.getWedgeIndexFromRoll(wackyWheelBlockEntity.getRoll());

            if(wackyWheelBlockEntity.previousIndex != index) {
                world.playSoundAtBlockCenter(blockPos, Registries.SOUND_EVENT.get(SoundEvents.BLOCK_NOTE_BLOCK_HAT.registryKey()), SoundCategory.RECORDS, 1F, 1F, true);
                wackyWheelBlockEntity.previousIndex = index;
            }
        }
    }

    public static void serverTick(World world, BlockPos blockPos, BlockState blockState, WackyWheelBlockEntity wackyWheelBlockEntity) {
        if(wackyWheelBlockEntity.getWedgeSpells().isEmpty()) {
            wackyWheelBlockEntity.initWedgeSpells();
            wackyWheelBlockEntity.markDirty();
        }

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
                int wedgeIndex = wackyWheelBlockEntity.getWedgeIndexFromRoll(wackyWheelBlockEntity.getRoll());
                SpellManager.addSpell(wackyWheelBlockEntity.getWedgeSpells().get(wedgeIndex),wackyWheelBlockEntity.getSpinningPlayer());
                wackyWheelBlockEntity.setSpinningPlayer(null);
                wackyWheelBlockEntity.removeAndReplaceSpell(wedgeIndex,blockPos);
                wackyWheelBlockEntity.markDirty();
            }
        }
        else if(wackyWheelBlockEntity.isSpinning()) {
            wackyWheelBlockEntity.setSpeed(0);
            wackyWheelBlockEntity.spellFlag = false;
            wackyWheelBlockEntity.markDirty();
        }

    }

    public ActionResult spin(ServerPlayerEntity serverPlayerEntity){
        if(!isSpinning() && !spellFlag) {
            setSpinningPlayer(serverPlayerEntity);
            float startSpeed = random.nextFloat(10F) + 10F;
            setSpeed(startSpeed);
            spellFlag = true;
            markDirty();
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public ServerPlayerEntity getSpinningPlayer() {
        return spinningPlayer;
    }

    public void setSpinningPlayer(ServerPlayerEntity spinningPlayer) {
        this.spinningPlayer = spinningPlayer;
    }

    public int getWedgeIndexFromRoll(float roll){
        int index = Math.round(roll/22.5F);
        return MathHelper.clamp(index, 0, 15);
    }

    public String getCurrentWedgeName(){
        int index = getWedgeIndexFromRoll(this.getRoll());
        SpellType spellType = getWedgeSpells().get(index);
        return spellType.name();
    }

    public SpellType getCurrentWedgeSpell(){
        int index = getWedgeIndexFromRoll(this.getRoll());
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

    public void removeAndReplaceSpell(int index, BlockPos blockPos){
        getWedgeSpells().set(index, (SpellType) SpellTypeRegistry.valueStream().toArray()[random.nextInt(SpellTypeRegistry.size())]);
        world.playSound(null,blockPos,SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM,SoundCategory.BLOCKS,1F,1);
    }

}
