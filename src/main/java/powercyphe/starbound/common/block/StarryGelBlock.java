package powercyphe.starbound.common.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import powercyphe.starbound.common.registry.ModBlocks;
import powercyphe.starbound.common.registry.ModParticles;
import powercyphe.starbound.common.util.StarboundUtil;

public class StarryGelBlock extends Block {
    public StarryGelBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape collisionShape = StarryGelBlock.createCuboidShape(0, 15, 0, 16,16, 16);
        if (context instanceof EntityShapeContext entityShapeContext && canWalkOnTop(entityShapeContext, collisionShape, pos)) {
            return collisionShape;
        }
        return VoxelShapes.empty();
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (direction == Direction.UP) {
            return stateFrom.getBlock() instanceof LayeredStarryGelBlock && this.isFullBlock(state);
        }
        return (stateFrom.getBlock() instanceof StarryGelBlock starryGelBlock && starryGelBlock.isFullBlock(stateFrom));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos blockPos, Random random) {
        DefaultedList<BlockPos> affectedBlocks = DefaultedList.of();

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = blockPos.offset(direction);
            BlockState neighborState = world.getBlockState(neighborPos);

            if (neighborState.isOf(Blocks.HONEY_BLOCK)) {
                affectedBlocks.add(neighborPos);
            }
        }
        if (!affectedBlocks.isEmpty()) {
            BlockPos affectedBlock = affectedBlocks.get(random.nextBetween(0, affectedBlocks.size()-1));
            world.setBlockState(affectedBlock, ModBlocks.STARRY_HONEY_BLOCK.getDefaultState());
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos blockPos, Random random) {
        if (random.nextInt(20) == 0) {
            Direction direction = Direction.random(random);
            BlockPos neighborPos = blockPos.offset(direction);
            BlockState neighborState = world.getBlockState(neighborPos);
            if (!state.isOpaque() || !neighborState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
                double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5 + (double)direction.getOffsetX() * 0.6;
                double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5 + (double)direction.getOffsetY() * 0.6;
                double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5 + (double)direction.getOffsetZ() * 0.6;
                world.addParticleClient(ModParticles.STARRY_CRIT, blockPos.getX() + d, blockPos.getY() + e, blockPos.getZ() + f, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    public boolean canWalkOnTop(EntityShapeContext entityShapeContext, VoxelShape collisionShape, BlockPos blockPos) {
        Entity entity = entityShapeContext.getEntity();
        return entity instanceof LivingEntity livingEntity && !StarboundUtil.isInStarryGel(entity) &&
                livingEntity.getEquippedStack(EquipmentSlot.FEET).isIn(ItemTags.FOOT_ARMOR)
                && entityShapeContext.isAbove(collisionShape, blockPos, true);
    }

    public boolean isFullBlock(BlockState state) {
        return true;
    }
}
