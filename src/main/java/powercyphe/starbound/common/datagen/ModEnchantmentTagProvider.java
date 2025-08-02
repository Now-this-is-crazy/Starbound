package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import powercyphe.starbound.common.registry.ModEnchantments;
import powercyphe.starbound.common.registry.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {
    public ModEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(EnchantmentTags.IN_ENCHANTING_TABLE)
                .addOptional(ModEnchantments.FLAIL);
        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
                .addOptional(ModEnchantments.FLAIL);

        getOrCreateTagBuilder(ModTags.Enchantments.EXCLUSIVE_SET_STARRY_GOLIATH)
                .addOptional(ModEnchantments.FLAIL);
    }
}
