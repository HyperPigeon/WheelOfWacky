package net.hyper_pigeon.wacky_wheel.block;

import net.hyper_pigeon.wacky_wheel.block.entity.WackyWheelBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class WackyWheelBlock extends Block implements BlockEntityProvider {
    public WackyWheelBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            player.sendMessage(Text.literal("Hello, world!"), false);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Stream.of(
                Stream.of(
                        Block.createCuboidShape(1, -1.11269837220809, -14, 7, 17.11269837220809, -13),
                        Block.createCuboidShape(1, -1.11269837220809, -14, 7, 17.11269837220809, -13),
                        Block.createCuboidShape(1, -1.11269837220809, 29, 7, 17.11269837220809, 30),
                        Block.createCuboidShape(1, -1.11269837220809, 29, 7, 17.11269837220809, 30),
                        Block.createCuboidShape(1, -14, -1.11269837220809, 7, -13, 17.11269837220809),
                        Block.createCuboidShape(1, -14, -1.11269837220809, 7, -13, 17.11269837220809),
                        Block.createCuboidShape(1, 29, -1.11269837220809, 7, 30, 17.11269837220809),
                        Block.createCuboidShape(1, 29, -1.11269837220809, 7, 30, 17.11269837220809)
                ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get(),
        Stream.of(
                Block.createCuboidShape(3, -0.6984848098349943, -13, 5, 16.698484809834994, 29),
                Block.createCuboidShape(3, -0.6984848098349943, -13, 5, 16.698484809834994, 29),
                Block.createCuboidShape(3, -13, -0.6984848098349943, 5, 29, 16.698484809834994),
                Block.createCuboidShape(3, -13, -0.6984848098349943, 5, 29, 16.698484809834994)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get(),
        Stream.of(
                Block.createCuboidShape(0, 7.403262897861026, 5, 6, 8.596737102138974, 11),
                Block.createCuboidShape(0, 7.403262897861026, 5, 6, 8.596737102138974, 11),
                Block.createCuboidShape(0, 7.403262897861026, 5, 6, 8.596737102138974, 11),
                Block.createCuboidShape(0, 7.403262897861026, 5, 6, 8.596737102138974, 11),
                Block.createCuboidShape(0, 7.403262897861026, 5, 6, 8.596737102138974, 11),
                Block.createCuboidShape(0, 5, 7.403262897861026, 6, 11, 8.596737102138974),
                Block.createCuboidShape(0, 5, 7.403262897861026, 6, 11, 8.596737102138974),
                Block.createCuboidShape(0, 5, 7.403262897861026, 6, 11, 8.596737102138974)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get()
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WackyWheelBlockEntity(pos,state);
    }
}
