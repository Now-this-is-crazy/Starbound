package powercyphe.starbound.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import powercyphe.starbound.common.Starbound;

public class SBDamageTypes {

    public static ResourceKey<DamageType> STARRY_SHARD = register("starry_shard");

    public static void init() {
    }

    public static ResourceKey<DamageType> register(String id) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Starbound.id(id));
    }

    public static DamageSource create(Level world, ResourceKey<DamageType> damageType, Entity source, Entity attacker) {
        return new DamageSource(world.registryAccess().getOrThrow(damageType), source, attacker);
    }
}
