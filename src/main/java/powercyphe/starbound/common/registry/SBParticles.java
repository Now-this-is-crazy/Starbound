package powercyphe.starbound.common.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import powercyphe.starbound.common.Starbound;

public class SBParticles {

    public static SimpleParticleType STARRY_CRIT = register("starry_crit", FabricParticleTypes.simple());
    public static SimpleParticleType STARRY_TRAIL = register("starry_trail", FabricParticleTypes.simple());
    public static SimpleParticleType STARRY_SMOKE = register("starry_smoke", FabricParticleTypes.simple());

    public static void init() {}

    public static SimpleParticleType register(String id, SimpleParticleType particleType) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Starbound.id(id), particleType);
    }
}
