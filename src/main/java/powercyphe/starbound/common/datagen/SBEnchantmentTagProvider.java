package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EnchantmentTags;
import powercyphe.starbound.common.registry.SBEnchantments;
import powercyphe.starbound.common.registry.SBTags;

import java.util.concurrent.CompletableFuture;

public class SBEnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {
    public SBEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        getOrCreateTagBuilder(EnchantmentTags.IN_ENCHANTING_TABLE)
                .addOptional(SBEnchantments.FLAIL);
        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
                .addOptional(SBEnchantments.FLAIL);

        getOrCreateTagBuilder(SBTags.Enchantments.EXCLUSIVE_SET_STARRY_GOLIATH)
                .addOptional(SBEnchantments.FLAIL);
    }
}
