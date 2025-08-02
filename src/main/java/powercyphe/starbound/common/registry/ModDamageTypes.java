package powercyphe.starbound.common.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import powercyphe.starbound.common.Starbound;

public class ModDamageTypes {

    public static RegistryKey<DamageType> STARRY_SHARD = register("starry_shard");

    public static void init() {
    }

    public static RegistryKey<DamageType> register(String id) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Starbound.id(id));
    }

    public static DamageSource create(World world, RegistryKey<DamageType> damageType, Entity source, Entity attacker) {
        return new DamageSource(world.getRegistryManager().getEntryOrThrow(damageType), source, attacker);
    }
}
