package powercyphe.starbound.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import powercyphe.starbound.common.registry.SBBlocks;

public class LayeredStarryGelBlock extends StarryGelBlock {
    public static int MAX_LAYERS = 4;
    public static IntegerProperty LAYERS = IntegerProperty.create("layers", 1, MAX_LAYERS);

    public LayeredStarryGelBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(LAYERS, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape collisionShape = StarryGelBlock.box(0, state.getValue(LAYERS) * 4 - 1, 0, 16,state.getValue(LAYERS) * 4, 16);
        if (context instanceof EntityCollisionContext entityShapeContext && canWalkOnTop(entityShapeContext, collisionShape, pos)) {
            return collisionShape;
        }
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0, 0, 16, state.getValue(LAYERS) * 4, 16);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        if (direction.getAxis() != Direction.Axis.Y) {
            return super.skipRendering(state, stateFrom, direction) || (stateFrom.getBlock() instanceof LayeredStarryGelBlock && stateFrom.getValue(LAYERS) >= state.getValue(LAYERS));
        }
        return super.skipRendering(state, stateFrom, direction);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        int layers = state.getValue(LAYERS);
        if (context.getItemInHand().is(this.asItem()) && layers < MAX_LAYERS) {
            if (context.replacingClickedOnBlock()) {
                return context.getClickedFace() == Direction.UP;
            } else {
                return true;
            }
        }
        return super.canBeReplaced(state, context);

    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState lowerState = world.getBlockState(pos.below());
        return Block.isFaceFull(lowerState.getCollisionShape(world, pos.below()), Direction.UP) ||
                (lowerState.is(this) && lowerState.getValue(LAYERS) == MAX_LAYERS) || lowerState.is(SBBlocks.STARRY_GEL_BLOCK);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (blockState.is(this)) {
            int i = blockState.getValue(LAYERS);
            return blockState.setValue(LAYERS, Math.min(MAX_LAYERS, i + 1));
        } else {
            return super.getStateForPlacement(ctx);
        }
    }

    @Override
    public boolean isFullBlock(BlockState state) {
        return state.getValue(LAYERS) == MAX_LAYERS;
    }
}
