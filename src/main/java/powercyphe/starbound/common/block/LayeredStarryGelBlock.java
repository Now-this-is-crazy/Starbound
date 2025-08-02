package powercyphe.starbound.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import powercyphe.starbound.common.registry.ModBlocks;

public class LayeredStarryGelBlock extends StarryGelBlock {
    public static int MAX_LAYERS = 4;
    public static IntProperty LAYERS = IntProperty.of("layers", 1, MAX_LAYERS);

    public LayeredStarryGelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(LAYERS, 1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
        super.appendProperties(builder);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape collisionShape = StarryGelBlock.createCuboidShape(0, state.get(LAYERS) * 4 - 1, 0, 16,state.get(LAYERS) * 4, 16);
        if (context instanceof EntityShapeContext entityShapeContext && canWalkOnTop(entityShapeContext, collisionShape, pos)) {
            return collisionShape;
        }
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0, 0, 0, 16, state.get(LAYERS) * 4, 16);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (direction.getAxis() != Direction.Axis.Y) {
            return super.isSideInvisible(state, stateFrom, direction) || (stateFrom.getBlock() instanceof LayeredStarryGelBlock && stateFrom.get(LAYERS) >= state.get(LAYERS));
        }
        return super.isSideInvisible(state, stateFrom, direction);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        int layers = state.get(LAYERS);
        if (context.getStack().isOf(this.asItem()) && layers < MAX_LAYERS) {
            if (context.canReplaceExisting()) {
                return context.getSide() == Direction.UP;
            } else {
                return true;
            }
        }
        return super.canReplace(state, context);

    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState lowerState = world.getBlockState(pos.down());
        return Block.isFaceFullSquare(lowerState.getCollisionShape(world, pos.down()), Direction.UP) ||
                (lowerState.isOf(this) && lowerState.get(LAYERS) == MAX_LAYERS) || lowerState.isOf(ModBlocks.STARRY_GEL_BLOCK);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            int i = blockState.get(LAYERS);
            return blockState.with(LAYERS, Math.min(MAX_LAYERS, i + 1));
        } else {
            return super.getPlacementState(ctx);
        }
    }

    @Override
    public boolean isFullBlock(BlockState state) {
        return state.get(LAYERS) == MAX_LAYERS;
    }
}
