package powercyphe.starbound.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

public class StarrySmokeParticle extends SpriteBillboardParticle {
    private final float baseScale;

    public StarrySmokeParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, h, i);
        this.velocityMultiplier = 0.97F;

        this.collidesWithWorld = true;
        this.ascending = true;
        this.gravityStrength = 0.05F;

        this.baseScale = 0.6F + Random.create().nextFloat() * 0.4F;
        this.scale = this.baseScale;

        this.angle = Random.create().nextBetween(0, 360);
        this.lastAngle = this.angle;

        this.alpha = 0.77F;
        this.maxAge = 60 + Random.create().nextInt(20);
        this.setSprite(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();

        this.lastAngle = this.angle;
        this.angle += 30F * (float) ((Math.abs(this.velocityX) + Math.abs(this.velocityY) + Math.abs(this.velocityZ)) / 3);

        int scaleAge = this.maxAge - 10;
        if (this.age > scaleAge) {
            float multiplier = 1F - ((float) (this.age - scaleAge) / 10F);

            this.alpha = multiplier * 0.77F;
            this.scale = multiplier * this.baseScale;
        }

    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            StarrySmokeParticle starrySmokeParticle = new StarrySmokeParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            starrySmokeParticle.setSprite(this.spriteProvider);
            return starrySmokeParticle;
        }
    }
}
