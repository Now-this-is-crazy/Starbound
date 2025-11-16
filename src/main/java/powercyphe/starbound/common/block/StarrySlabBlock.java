package powercyphe.starbound.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;

public class StarrySlabBlock extends SlabBlock {
    public static BooleanProperty LIT = BlockStateProperties.LIT;

    public StarrySlabBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        super.createBlockStateDefinition(builder);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        tick(state, world, pos, random);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos blockPos, RandomSource random) {
        boolean lit = state.getValue(LIT);

        if (shouldNotify(world, state)) {
            AABB box = AABB.ofSize(blockPos.getCenter(), 2, 2, 2);

            BlockPos blockPos1 = BlockPos.containing(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7);
            BlockPos blockPos2 = BlockPos.containing(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7);

            if (world.hasChunksAt(blockPos1, blockPos2)) {
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

                for(int i = blockPos.getX(); i <= blockPos2.getX(); ++i) {
                    for(int j = blockPos.getY(); j <= blockPos2.getY(); ++j) {
                        for(int k = blockPos.getZ(); k <= blockPos2.getZ(); ++k) {
                            mutable.set(i, j, k);

                            BlockState checkState = world.getBlockState(mutable);
                            if (checkState.getBlock() instanceof StarryBlock) {
                                world.scheduleTick(mutable, checkState.getBlock(), 20 + RandomSource.create().nextInt(80));
                            }

                        }
                    }
                }
            }
        } else {
            world.setBlockAndUpdate(blockPos, state.setValue(LIT, !lit));
            world.scheduleTick(blockPos, state.getBlock(), 1);
        }
    }

    public boolean shouldNotify(Level world, BlockState state) {
        return world.isDarkOutside() == state.getValue(LIT);
    }
}
