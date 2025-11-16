package powercyphe.starbound.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class StarryTrailParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    private final float baseScale;
    private final float angleMultiplier;

    public StarryTrailParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, SpriteSet spriteProvider) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.friction = 0.7F;
        this.gravity = 0.5F;
        this.xd *= 0.10000000149011612;
        this.yd *= 0.10000000149011612;
        this.zd *= 0.10000000149011612;
        this.xd += g * 0.4;
        this.yd += h * 0.4;
        this.zd += i * 0.4;

        this.quadSize *= 0.6F;
        this.baseScale = this.quadSize;

        this.roll = RandomSource.create().nextIntBetweenInclusive(0, 360);
        this.oRoll = this.roll;
        this.angleMultiplier = this.random.nextFloat() * (this.random.nextBoolean() ? 1 : -1);

        this.lifetime = Math.max((int)(21.0 / (Math.random() * 0.8 + 0.6)), 1);
        this.hasPhysics = false;
        this.spriteProvider = spriteProvider;
        this.tick();
    }

    @Override
    public void tick() {
        super.tick();

        this.roll = this.oRoll + this.angleMultiplier * (float) (Math.sin(this.age * 14) * 3);
        if (this.age++ < this.lifetime) {
            int faintAge = this.lifetime / 3;
            if (this.age >= faintAge) {
                float scaleAge = this.age - faintAge - 0.75F;
                float scaleAgeMax = this.lifetime - faintAge;
                this.quadSize = Math.max(this.baseScale * ((scaleAgeMax - scaleAge) / scaleAgeMax), 0.01F);
            }
        }
        this.setSpriteFromAge(this.spriteProvider);
    }

    @Override
    protected int getLightColor(float tint) {
        return 15728640;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float tickDelta) {
        return this.quadSize * Mth.clamp(((float)this.age + tickDelta) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            StarryTrailParticle fancyCritParticle = new StarryTrailParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            fancyCritParticle.pickSprite(this.spriteProvider);
            return fancyCritParticle;
        }
    }

}
