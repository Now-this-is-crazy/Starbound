package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import powercyphe.starbound.common.registry.SBBlocks;
import powercyphe.starbound.common.registry.SBTags;

import java.util.concurrent.CompletableFuture;

public class SBBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public SBBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        getOrCreateTagBuilder(SBTags.Blocks.STARSTONE_CONVERTABLE)
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
                .add(SBBlocks.STARSTONE_STAIRS, SBBlocks.POLISHED_STARSTONE_STAIRS, SBBlocks.STARSTONE_TILE_STAIRS);

        getOrCreateTagBuilder(BlockTags.SLABS)
                .add(SBBlocks.STARSTONE_SLAB, SBBlocks.POLISHED_STARSTONE_SLAB, SBBlocks.STARSTONE_TILE_SLAB);

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(SBBlocks.STARSTONE_WALL, SBBlocks.POLISHED_STARSTONE_WALL, SBBlocks.STARSTONE_TILE_WALL);

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        SBBlocks.STARSTONE,
                        SBBlocks.STARSTONE_STAIRS,
                        SBBlocks.STARSTONE_SLAB,
                        SBBlocks.STARSTONE_WALL,

                        SBBlocks.POLISHED_STARSTONE,
                        SBBlocks.POLISHED_STARSTONE_STAIRS,
                        SBBlocks.POLISHED_STARSTONE_SLAB,
                        SBBlocks.POLISHED_STARSTONE_WALL,

                        SBBlocks.STARSTONE_TILES,
                        SBBlocks.STARSTONE_TILE_STAIRS,
                        SBBlocks.STARSTONE_TILE_SLAB,
                        SBBlocks.STARSTONE_TILE_WALL
                );

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_HOE)
                .add(SBBlocks.STARRY_GEL_BLOCK, SBBlocks.STARRY_GEL_LAYER, SBBlocks.STARRY_HONEY_BLOCK);

        getOrCreateTagBuilder(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)
                .add(SBBlocks.STARRY_GEL_LAYER, SBBlocks.STARRY_GEL_BLOCK);

        getOrCreateTagBuilder(BlockTags.FALL_DAMAGE_RESETTING)
                .add(SBBlocks.STARRY_GEL_LAYER, SBBlocks.STARRY_GEL_BLOCK);
    }
}
