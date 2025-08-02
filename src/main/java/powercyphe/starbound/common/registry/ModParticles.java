package powercyphe.starbound.common.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import powercyphe.starbound.common.Starbound;

public class ModParticles {

    public static SimpleParticleType STARRY_CRIT = register("starry_crit", FabricParticleTypes.simple());
    public static SimpleParticleType STARRY_TRAIL = register("starry_trail", FabricParticleTypes.simple());
    public static SimpleParticleType STARRY_SMOKE = register("starry_smoke", FabricParticleTypes.simple());

    public static void init() {}

    public static SimpleParticleType register(String id, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, Starbound.id(id), particleType);
    }
}
