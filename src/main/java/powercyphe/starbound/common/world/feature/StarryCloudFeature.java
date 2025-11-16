package powercyphe.starbound.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import powercyphe.starbound.common.block.LayeredStarryGelBlock;
import powercyphe.starbound.common.registry.SBBlocks;

public class StarryCloudFeature extends Feature<NoneFeatureConfiguration> {
    public StarryCloudFeature(Codec<NoneFeatureConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext context) {
        RandomSource random = context.random();
        float size = random.nextFloat() + 1;

        WorldGenLevel world = context.level();
        BlockPos originPos = context.origin();

        ImprovedNoise sampler = new ImprovedNoise(random);
        int minOffset = (int) Math.min(-1, -4 * size);
        int maxOffset = (int) Math.max(1, 4 * size);

        BlockPos range1 = originPos.offset(minOffset, 0, minOffset);
        BlockPos range2 = originPos.offset(maxOffset, 0, maxOffset);

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = range1.getX(); x <= range2.getX(); ++x) {
            for (int z = range1.getZ(); z <= range2.getZ(); ++z) {
                mutable.set(x, originPos.getY(), z);

                double distance = Math.sqrt(originPos.distSqr(mutable));
                double noise = getNoise(sampler, originPos, mutable, distance);

                BlockPos maxPos = mutable.offset(0, (int) Math.clamp(noise, 0, maxOffset), 0);
                BlockPos minPos = mutable.offset(0, (int) Math.clamp(-noise, minOffset, 0), 0);

                for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                    mutable.set(mutable.getX(), y, mutable.getZ());
                    BlockState state = world.getBlockState(mutable);

                    if (canReplace(state) && distance < maxOffset) {
                        if (noise >= 0.25) {
                            int layers = y == maxPos.getY() ? (int) Math.clamp((noise / (maxPos.getY() - minPos.getY() + 1)) * 4, 1, 4) : 4;

                            BlockState gelState = SBBlocks.STARRY_GEL_LAYER.defaultBlockState().trySetValue(LayeredStarryGelBlock.LAYERS, layers);
                            world.setBlock(mutable, gelState, 0);
                        }
                    }
                }
            }
        }

        return true;

    }

    private double getNoise(ImprovedNoise sampler, BlockPos originPos, BlockPos blockPos, double distance) {
        double noiseTotal = 0;
        for (int x2 = blockPos.getX()-2; x2 <= blockPos.getX()+2; x2++) {
            for (int z2 = blockPos.getZ()-2; z2 <= blockPos.getZ()+2; z2++) {
                noiseTotal += Math.abs(sampler.noise(x2, originPos.getY(), z2));
            }
        }

        return Math.clamp(Math.abs((noiseTotal / 25) * 5) / Math.max(1, distance / 2), 0, 4);
    }

    private boolean canReplace(BlockState state) {
        return !state.is(BlockTags.FEATURES_CANNOT_REPLACE) && !state.is(Blocks.END_STONE);
    }
}
