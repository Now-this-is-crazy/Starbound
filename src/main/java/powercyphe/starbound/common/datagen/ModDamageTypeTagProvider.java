package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import powercyphe.starbound.common.registry.ModDamageTypes;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagProvider extends FabricTagProvider<DamageType> {
    public ModDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(DamageTypeTags.NO_IMPACT)
                .addOptional(ModDamageTypes.STARRY_SHARD);
        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_COOLDOWN)
                .addOptional(ModDamageTypes.STARRY_SHARD);
        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
                .addOptional(ModDamageTypes.STARRY_SHARD);
    }
}
