package powercyphe.starbound.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class StarryBlock extends Block {
    public static BooleanProperty LIT = Properties.LIT;

    public StarryBlock(Settings settings) {
        super(settings.ticksRandomly().luminance(Blocks.createLightLevelFromLitBlockState(7)));
        this.setDefaultState(this.getDefaultState().with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        super.appendProperties(builder);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        scheduledTick(state, world, pos, random);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos blockPos, Random random) {
        boolean lit = state.get(LIT);

        if (shouldNotify(world, state)) {
            Box box = Box.of(blockPos.toCenterPos(), 2, 2, 2);

            BlockPos blockPos1 = BlockPos.ofFloored(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7);
            BlockPos blockPos2 = BlockPos.ofFloored(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7);

            if (world.isRegionLoaded(blockPos1, blockPos2)) {
                BlockPos.Mutable mutable = new BlockPos.Mutable();

                for(int i = blockPos.getX(); i <= blockPos2.getX(); ++i) {
                    for(int j = blockPos.getY(); j <= blockPos2.getY(); ++j) {
                        for(int k = blockPos.getZ(); k <= blockPos2.getZ(); ++k) {
                            mutable.set(i, j, k);

                            BlockState checkState = world.getBlockState(mutable);
                            if (checkState.getBlock() instanceof StarryBlock) {
                                world.scheduleBlockTick(mutable, checkState.getBlock(), 20 + Random.create().nextInt(80));
                            }

                        }
                    }
                }
            }
        } else {
            world.setBlockState(blockPos, state.with(LIT, !lit));
            world.scheduleBlockTick(blockPos, state.getBlock(), 1);
        }
    }

    public boolean shouldNotify(World world, BlockState state) {
        return world.isNight() == state.get(LIT);
    }
}
