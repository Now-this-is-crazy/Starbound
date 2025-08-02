package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import powercyphe.starbound.common.registry.ModBlocks;
import powercyphe.starbound.common.registry.ModItems;
import powercyphe.starbound.common.registry.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS)
                .add(ModItems.STARRY_INGOT);

        getOrCreateTagBuilder(ModTags.Items.ENCHANTABLE_STARRY_GOLIATH)
                .add(ModItems.STARRY_GOLIATH);

        getOrCreateTagBuilder(ItemTags.AXES)
                .add(ModItems.STARRY_GOLIATH);

        getOrCreateTagBuilder(ModTags.Items.ALWAYS_HAS_STARRY_EMISSIVITY)
                .add(ModItems.STARRY_GEL, ModItems.STARRY_INGOT, ModItems.STARRY_TOTEM, ModItems.STARRY_GOLIATH,
                        ModBlocks.STARRY_GEL_BLOCK.asItem());

        getOrCreateTagBuilder(ModTags.Items.CANNOT_HAVE_STARRY_EMISSIVITY)
                .add(Items.ENCHANTED_BOOK, Items.NETHER_STAR)
                .addTag(ModTags.Items.ALWAYS_HAS_STARRY_EMISSIVITY);
    }
}
