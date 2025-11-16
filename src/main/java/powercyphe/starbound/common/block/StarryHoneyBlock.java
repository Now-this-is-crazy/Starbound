package powercyphe.starbound.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import powercyphe.starbound.common.registry.SBBlocks;
import powercyphe.starbound.common.registry.SBParticles;
import powercyphe.starbound.common.util.StarboundUtil;

public class StarryHoneyBlock extends HoneyBlock {
    public static int LEVEL_MAX = 3;
    public static IntegerProperty LEVEL = IntegerProperty.create("level", 1, LEVEL_MAX);

    public StarryHoneyBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(LEVEL, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos blockPos, RandomSource random) {
        int level = state.getValue(LEVEL);
        Vec3 particlePos = blockPos.getCenter();

        if (level < LEVEL_MAX && StarboundUtil.hasNearbyStarryGel(world, blockPos)) {
            world.setBlockAndUpdate(blockPos, state.setValue(LEVEL, level+1));
            world.sendParticles(SBParticles.STARRY_TRAIL, particlePos.x(), particlePos.y(), particlePos.z(), 14, 0.5, 0.5, 0.5, 0.5);
        } else {
            if (StarboundUtil.hasNearbyStarryGel(world, blockPos)) {
                world.setBlockAndUpdate(blockPos, SBBlocks.STARRY_GEL_BLOCK.defaultBlockState());
                world.sendParticles(SBParticles.STARRY_CRIT, particlePos.x(), particlePos.y(), particlePos.z(), 14, 0.5, 0.5, 0.5, 0);
            } else {
                BlockState newState = level > 1 ? state.setValue(LEVEL, level-1) : Blocks.HONEY_BLOCK.defaultBlockState();
                world.setBlockAndUpdate(blockPos, newState);

                BlockParticleOption par = new BlockParticleOption(ParticleTypes.BLOCK, newState);
                world.sendParticles(par, particlePos.x(), particlePos.y(), particlePos.z(), 14, 0.5, 0.5, 0.5, 0.5);
            }
        }
    }
}
