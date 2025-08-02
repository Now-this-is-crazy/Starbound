package powercyphe.starbound.common.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import powercyphe.starbound.common.block.LayeredStarryGelBlock;
import powercyphe.starbound.common.registry.ModBlocks;

public class StarryCloudFeature extends Feature<DefaultFeatureConfig> {
    public StarryCloudFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext context) {
        Random random = context.getRandom();
        float size = random.nextFloat() + 1;

        StructureWorldAccess world = context.getWorld();
        BlockPos originPos = context.getOrigin();

        PerlinNoiseSampler sampler = new PerlinNoiseSampler(random);
        int minOffset = (int) Math.min(-1, -4 * size);
        int maxOffset = (int) Math.max(1, 4 * size);

        BlockPos range1 = originPos.add(minOffset, 0, minOffset);
        BlockPos range2 = originPos.add(maxOffset, 0, maxOffset);

        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int x = range1.getX(); x <= range2.getX(); ++x) {
            for (int z = range1.getZ(); z <= range2.getZ(); ++z) {
                mutable.set(x, originPos.getY(), z);

                double distance = Math.sqrt(originPos.getSquaredDistance(mutable));
                double noise = getNoise(sampler, originPos, mutable, distance);

                BlockPos maxPos = mutable.add(0, (int) Math.clamp(noise, 0, maxOffset), 0);
                BlockPos minPos = mutable.add(0, (int) Math.clamp(-noise, minOffset, 0), 0);

                for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                    mutable.set(mutable.getX(), y, mutable.getZ());
                    BlockState state = world.getBlockState(mutable);

                    if (canReplace(state) && distance < maxOffset) {
                        if (noise >= 0.25) {
                            int layers = y == maxPos.getY() ? (int) Math.clamp((noise / (maxPos.getY() - minPos.getY() + 1)) * 4, 1, 4) : 4;

                            BlockState gelState = ModBlocks.STARRY_GEL_LAYER.getDefaultState().withIfExists(LayeredStarryGelBlock.LAYERS, layers);
                            world.setBlockState(mutable, gelState, 0);
                        }
                    }
                }
            }
        }

        return true;

    }

    private double getNoise(PerlinNoiseSampler sampler, BlockPos originPos, BlockPos blockPos, double distance) {
        double noiseTotal = 0;
        for (int x2 = blockPos.getX()-2; x2 <= blockPos.getX()+2; x2++) {
            for (int z2 = blockPos.getZ()-2; z2 <= blockPos.getZ()+2; z2++) {
                noiseTotal += Math.abs(sampler.sample(x2, originPos.getY(), z2));
            }
        }

        return Math.clamp(Math.abs((noiseTotal / 25) * 5) / Math.max(1, distance / 2), 0, 4);
    }

    private boolean canReplace(BlockState state) {
        return !state.isIn(BlockTags.FEATURES_CANNOT_REPLACE) && !state.isOf(Blocks.END_STONE);
    }
}
