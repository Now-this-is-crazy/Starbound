package powercyphe.starbound.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import powercyphe.starbound.common.registry.SBEnchantments;
import powercyphe.starbound.common.registry.SBTags;

import java.util.concurrent.CompletableFuture;

public class SBEnchantmentTagProvider extends EnchantmentTagsProvider {
    public SBEnchantmentTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        tag(EnchantmentTags.IN_ENCHANTING_TABLE)
                .addOptional(SBEnchantments.FLAIL);
        tag(EnchantmentTags.NON_TREASURE)
                .addOptional(SBEnchantments.FLAIL);

        tag(SBTags.Enchantments.EXCLUSIVE_SET_STARRY_GOLIATH)
                .addOptional(SBEnchantments.FLAIL);
    }
}
