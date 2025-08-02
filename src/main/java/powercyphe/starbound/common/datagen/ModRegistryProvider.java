package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import powercyphe.starbound.common.Starbound;

import java.util.concurrent.CompletableFuture;

public class ModRegistryProvider extends FabricDynamicRegistryProvider {

    public ModRegistryProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        entries.addAll(wrapperLookup.getOrThrow(RegistryKeys.TRIM_MATERIAL));
    }

    @Override
    public String getName() {
        return Starbound.MOD_ID + "RegistryProvider";
    }
}
