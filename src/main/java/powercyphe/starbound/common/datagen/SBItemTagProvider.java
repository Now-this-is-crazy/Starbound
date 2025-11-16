package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import powercyphe.starbound.common.registry.SBBlocks;
import powercyphe.starbound.common.registry.SBItems;
import powercyphe.starbound.common.registry.SBTags;

import java.util.concurrent.CompletableFuture;

public class SBItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public SBItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS)
                .add(SBItems.STARRY_INGOT);

        getOrCreateTagBuilder(SBTags.Items.ENCHANTABLE_STARRY_GOLIATH)
                .add(SBItems.STARRY_GOLIATH);

        getOrCreateTagBuilder(ItemTags.AXES)
                .add(SBItems.STARRY_GOLIATH);

        getOrCreateTagBuilder(SBTags.Items.ALWAYS_HAS_STARRY_EMISSIVITY)
                .add(SBItems.STARRY_GEL, SBItems.STARRY_INGOT, SBItems.STARRY_TOTEM, SBItems.STARRY_GOLIATH,
                        SBBlocks.STARRY_GEL_BLOCK.asItem());

        getOrCreateTagBuilder(SBTags.Items.CANNOT_HAVE_STARRY_EMISSIVITY)
                .add(Items.ENCHANTED_BOOK, Items.NETHER_STAR)
                .addTag(SBTags.Items.ALWAYS_HAS_STARRY_EMISSIVITY);
    }
}
