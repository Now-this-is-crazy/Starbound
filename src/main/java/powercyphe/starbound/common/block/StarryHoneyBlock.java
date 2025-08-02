package powercyphe.starbound.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HoneyBlock;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.registry.ModBlocks;
import powercyphe.starbound.common.registry.ModParticles;
import powercyphe.starbound.common.util.StarboundUtil;

public class StarryHoneyBlock extends HoneyBlock {
    public static int LEVEL_MAX = 3;
    public static IntProperty LEVEL = IntProperty.of("level", 1, LEVEL_MAX);

    public StarryHoneyBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(LEVEL, 1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
        super.appendProperties(builder);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos blockPos, Random random) {
        int level = state.get(LEVEL);
        Vec3d particlePos = blockPos.toCenterPos();

        if (level < LEVEL_MAX && StarboundUtil.hasNearbyStarryGel(world, blockPos)) {
            world.setBlockState(blockPos, state.with(LEVEL, level+1));
            world.spawnParticles(ModParticles.STARRY_TRAIL, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 14, 0.5, 0.5, 0.5, 0.5);
        } else {
            if (StarboundUtil.hasNearbyStarryGel(world, blockPos)) {
                world.setBlockState(blockPos, ModBlocks.STARRY_GEL_BLOCK.getDefaultState());
                world.spawnParticles(ModParticles.STARRY_CRIT, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 14, 0.5, 0.5, 0.5, 0);
            } else {
                BlockState newState = level > 1 ? state.with(LEVEL, level-1) : Blocks.HONEY_BLOCK.getDefaultState();
                world.setBlockState(blockPos, newState);

                BlockStateParticleEffect par = new BlockStateParticleEffect(ParticleTypes.BLOCK, newState);
                world.spawnParticles(par, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 14, 0.5, 0.5, 0.5, 0.5);
            }
        }
    }
}
