package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import powercyphe.starbound.common.registry.SBDamageTypes;

import java.util.concurrent.CompletableFuture;

public class SBDamageTypeTagProvider extends FabricTagProvider<DamageType> {
    public SBDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(DamageTypeTags.NO_IMPACT)
                .addOptional(SBDamageTypes.STARRY_SHARD);
        builder(DamageTypeTags.BYPASSES_COOLDOWN)
                .addOptional(SBDamageTypes.STARRY_SHARD);
        builder(DamageTypeTags.BYPASSES_ARMOR)
                .addOptional(SBDamageTypes.STARRY_SHARD);
    }
}
