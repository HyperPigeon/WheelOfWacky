package net.hyper_pigeon.wacky_wheel.block.entity;

import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class WackyWheelBlockEntity extends BlockEntity {

    private float speed = 0.0F;
    private float friction = 0.8F;

    public WackyWheelBlockEntity(BlockPos pos, BlockState state) {
        super(WheelOfWacky.WACKY_WHEEL_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
    }

    public float getSpeed(){
        return speed;
    }

    public float getFriction(){
        return friction;
    }

    public boolean isSpinning(){
        return speed > 0.0F;
    }
}
