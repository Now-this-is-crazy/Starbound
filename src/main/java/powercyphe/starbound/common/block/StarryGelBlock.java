package powercyphe.starbound.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import powercyphe.starbound.common.registry.SBBlocks;
import powercyphe.starbound.common.registry.SBParticles;
import powercyphe.starbound.common.util.StarboundUtil;

public class StarryGelBlock extends Block {
    public StarryGelBlock(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape collisionShape = StarryGelBlock.box(0, 15, 0, 16,16, 16);
        if (context instanceof EntityCollisionContext entityShapeContext && canWalkOnTop(entityShapeContext, collisionShape, pos)) {
            return collisionShape;
        }
        return Shapes.empty();
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        if (direction == Direction.UP) {
            return stateFrom.getBlock() instanceof StarryGelBlock && this.isFullBlock(state);
        }
        return (stateFrom.getBlock() instanceof StarryGelBlock starryGelBlock && starryGelBlock.isFullBlock(stateFrom));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos blockPos, RandomSource random) {
        NonNullList<BlockPos> affectedBlocks = NonNullList.create();

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = blockPos.relative(direction);
            BlockState neighborState = world.getBlockState(neighborPos);

            if (neighborState.is(Blocks.HONEY_BLOCK)) {
                affectedBlocks.add(neighborPos);
            }
        }
        if (!affectedBlocks.isEmpty()) {
            BlockPos affectedBlock = affectedBlocks.get(random.nextIntBetweenInclusive(0, affectedBlocks.size()-1));
            world.setBlockAndUpdate(affectedBlock, SBBlocks.STARRY_HONEY_BLOCK.defaultBlockState());
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos blockPos, RandomSource random) {
        if (random.nextInt(20) == 0) {
            Direction direction = Direction.getRandom(random);
            BlockPos neighborPos = blockPos.relative(direction);
            BlockState neighborState = world.getBlockState(neighborPos);
            if (!state.canOcclude() || !neighborState.isFaceSturdy(world, blockPos, direction.getOpposite())) {
                double d = direction.getStepX() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepX() * 0.6;
                double e = direction.getStepY() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepY() * 0.6;
                double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepZ() * 0.6;
                world.addParticle(SBParticles.STARRY_CRIT, blockPos.getX() + d, blockPos.getY() + e, blockPos.getZ() + f, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public boolean canWalkOnTop(EntityCollisionContext entityShapeContext, VoxelShape collisionShape, BlockPos blockPos) {
        Entity entity = entityShapeContext.getEntity();
        return entity instanceof LivingEntity livingEntity && !StarboundUtil.isInStarryGel(entity) &&
                livingEntity.getItemBySlot(EquipmentSlot.FEET).is(ItemTags.FOOT_ARMOR)
                && entityShapeContext.isAbove(collisionShape, blockPos, true);
    }

    public boolean isFullBlock(BlockState state) {
        return true;
    }
}
