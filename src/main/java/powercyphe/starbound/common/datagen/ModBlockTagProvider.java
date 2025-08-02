package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import powercyphe.starbound.common.registry.ModBlocks;
import powercyphe.starbound.common.registry.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Blocks.STARSTONE_CONVERTABLE)
                .add(
                        Blocks.STONE,
                        Blocks.COBBLESTONE,
                        Blocks.DEEPSLATE,
                        Blocks.COBBLED_DEEPSLATE,
                        Blocks.TUFF,
                        Blocks.ANDESITE,
                        Blocks.DIORITE,
                        Blocks.GRANITE,
                        Blocks.END_STONE
                );

        getOrCreateTagBuilder(BlockTags.STAIRS)
                .add(ModBlocks.STARSTONE_STAIRS, ModBlocks.POLISHED_STARSTONE_STAIRS, ModBlocks.STARSTONE_TILE_STAIRS);

        getOrCreateTagBuilder(BlockTags.SLABS)
                .add(ModBlocks.STARSTONE_SLAB, ModBlocks.POLISHED_STARSTONE_SLAB, ModBlocks.STARSTONE_TILE_SLAB);

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(ModBlocks.STARSTONE_WALL, ModBlocks.POLISHED_STARSTONE_WALL, ModBlocks.STARSTONE_TILE_WALL);

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(
                        ModBlocks.STARSTONE,
                        ModBlocks.STARSTONE_STAIRS,
                        ModBlocks.STARSTONE_SLAB,
                        ModBlocks.STARSTONE_WALL,

                        ModBlocks.POLISHED_STARSTONE,
                        ModBlocks.POLISHED_STARSTONE_STAIRS,
                        ModBlocks.POLISHED_STARSTONE_SLAB,
                        ModBlocks.POLISHED_STARSTONE_WALL,

                        ModBlocks.STARSTONE_TILES,
                        ModBlocks.STARSTONE_TILE_STAIRS,
                        ModBlocks.STARSTONE_TILE_SLAB,
                        ModBlocks.STARSTONE_TILE_WALL
                );

        getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
                .add(ModBlocks.STARRY_GEL_BLOCK, ModBlocks.STARRY_GEL_LAYER, ModBlocks.STARRY_HONEY_BLOCK);

        getOrCreateTagBuilder(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)
                .add(ModBlocks.STARRY_GEL_LAYER, ModBlocks.STARRY_GEL_BLOCK);

        getOrCreateTagBuilder(BlockTags.FALL_DAMAGE_RESETTING)
                .add(ModBlocks.STARRY_GEL_LAYER, ModBlocks.STARRY_GEL_BLOCK);
    }
}
