package net.hyper_pigeon.wacky_wheel.block;

import com.mojang.serialization.MapCodec;
import net.hyper_pigeon.wacky_wheel.WheelOfWacky;
import net.hyper_pigeon.wacky_wheel.block.entity.WackyWheelBlockEntity;
import net.hyper_pigeon.wacky_wheel.register.WheelOfWackyGamerules;
import net.hyper_pigeon.wacky_wheel.token.TokenTypeRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WackyWheelBlock extends HorizontalFacingBlock implements BlockEntityProvider {

    public static final MapCodec<WackyWheelBlock> CODEC = createCodec(WackyWheelBlock::new);
//    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public WackyWheelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState().with(FACING, Direction.SOUTH));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WackyWheelBlockEntity wackyWheelBlockEntity) {
                if(!player.isCreative() && world.getGameRules().getBoolean(WheelOfWackyGamerules.PAY_TO_SPIN_WHEEL_OF_WACKY)) {
                    ItemStack mainHandStack = player.getMainHandStack();
                    if(TokenTypeRegistry.ITEM_TO_TOKEN_TYPE.containsKey(mainHandStack.getItem()) && mainHandStack.getCount() >= TokenTypeRegistry.ITEM_TO_TOKEN_TYPE.get(mainHandStack.getItem()).count()) {
                        int count = TokenTypeRegistry.ITEM_TO_TOKEN_TYPE.get(mainHandStack.getItem()).count();
                        mainHandStack.decrement(count);
                        return wackyWheelBlockEntity.spin((ServerPlayerEntity) player);
                    }
                    player.sendMessage(Text.translatable("block.wacky_wheel.not_enough_tokens"), true);
                    return ActionResult.PASS;
                }
                else {
                    return wackyWheelBlockEntity.spin((ServerPlayerEntity) player);
                }
            }
        }

        return ActionResult.PASS;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case NORTH -> VoxelShapes.cuboid(0.25f, 0.25f, 0.3125f, 0.75f, 0.75f, 1f);
            case SOUTH -> VoxelShapes.cuboid(0.25f, 0.25f, 0f, 0.75f, 0.75f, 0.6875f);
            case EAST -> VoxelShapes.cuboid(0f, 0.25f, 0.25f, 0.6875f, 0.75f, 0.75f);
            case WEST -> VoxelShapes.cuboid(0.3125f, 0.25f, 0.25f, 1f, 0.75f, 0.75f);
            default -> VoxelShapes.fullCube();
        };
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WackyWheelBlockEntity(pos,state);
    }

    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> validateTicker(
            BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker
    ) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? validateTicker(type, WheelOfWacky.WACKY_WHEEL_BLOCK_ENTITY, WackyWheelBlockEntity::clientTick) : validateTicker(type, WheelOfWacky.WACKY_WHEEL_BLOCK_ENTITY, WackyWheelBlockEntity::serverTick);
    }
//    protected BlockState rotate(BlockState state, BlockRotation rotation) {
//        return state.with(FACING, rotation.rotate(state.get(FACING)));
//    }
//
//    protected BlockState mirror(BlockState state, BlockMirror mirror) {
//        return state.rotate(mirror.getRotation(state.get(FACING)));
//    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

}
