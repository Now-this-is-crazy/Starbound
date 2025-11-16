package powercyphe.starbound.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class StarrySmokeParticle extends SingleQuadParticle {
    private final float baseScale;

    public StarrySmokeParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, SpriteSet spriteProvider) {
        super(clientWorld, d, e, f, g, h, i, spriteProvider.first());
        this.friction = 0.97F;

        this.hasPhysics = true;
        this.speedUpWhenYMotionIsBlocked = true;
        this.gravity = 0.05F;

        this.baseScale = 0.6F + RandomSource.create().nextFloat() * 0.4F;
        this.quadSize = this.baseScale;

        this.roll = RandomSource.create().nextIntBetweenInclusive(0, 360);
        this.oRoll = this.roll;

        this.alpha = 0.77F;
        this.lifetime = 60 + RandomSource.create().nextInt(20);
        this.setSprite(spriteProvider.get(RandomSource.create()));
    }

    @Override
    public void tick() {
        super.tick();

        this.oRoll = this.roll;
        this.roll += 30F * (float) ((Math.abs(this.xd) + Math.abs(this.yd) + Math.abs(this.zd)) / 3);

        int scaleAge = this.lifetime - 10;
        if (this.age > scaleAge) {
            float multiplier = 1F - ((float) (this.age - scaleAge) / 10F);

            this.alpha = multiplier * 0.77F;
            this.quadSize = multiplier * this.baseScale;
        }

    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, RandomSource randomSource) {
            return new StarrySmokeParticle(clientLevel, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
