package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import powercyphe.starbound.common.Starbound;

import java.util.concurrent.CompletableFuture;

public class SBRegistryProvider extends FabricDynamicRegistryProvider {

    public SBRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider wrapperLookup, Entries entries) {
        entries.addAll(wrapperLookup.lookupOrThrow(Registries.TRIM_MATERIAL));
    }

    @Override
    public String getName() {
        return Starbound.MOD_ID + "RegistryProvider";
    }
}
