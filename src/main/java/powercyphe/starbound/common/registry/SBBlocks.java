package powercyphe.starbound.common.registry;

import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.block.*;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public class SBBlocks {

    public static SoundType STARRY_GEL = new SoundType(1f, 1f,
            SoundEvents.HONEY_BLOCK_BREAK, SBSounds.STARRY_GEL_STEP, SoundEvents.HONEY_BLOCK_PLACE,
            SoundEvents.HONEY_BLOCK_HIT, SoundEvents.HONEY_BLOCK_FALL);

    public static Block STARRY_HONEY_BLOCK = register("starry_honey_block", StarryHoneyBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.HONEY_BLOCK)
            .lightLevel(state -> (state.getValue(StarryHoneyBlock.LEVEL) >= StarryHoneyBlock.LEVEL_MAX ? 1 : 0)).randomTicks());

    public static Block STARRY_GEL_BLOCK = register("starry_gel_block", StarryGelBlock::new, BlockBehaviour.Properties.of().forceSolidOn().randomTicks()
            .strength(0.5F).lightLevel(state -> 1).emissiveRendering((state, world, pos) -> true)
            .jumpFactor(1.25F).friction(0.66F).sound(STARRY_GEL).noOcclusion());
    public static Block STARRY_GEL_LAYER = registerWithoutItem("starry_gel_layer", LayeredStarryGelBlock::new, BlockBehaviour.Properties.ofFullCopy(STARRY_GEL_BLOCK)
            .pushReaction(PushReaction.DESTROY));

    public static Block STARSTONE = register("starstone", StarryBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE));
    public static Block STARSTONE_STAIRS = register("starstone_stairs", settings -> new StarryStairsBlock(STARSTONE.defaultBlockState(), settings), BlockBehaviour.Properties.ofFullCopy(STARSTONE));
    public static Block STARSTONE_SLAB = register("starstone_slab", StarrySlabBlock::new, BlockBehaviour.Properties.ofFullCopy(STARSTONE));
    public static Block STARSTONE_WALL = register("starstone_wall", StarryWallBlock::new, BlockBehaviour.Properties.ofFullCopy(STARSTONE));

    public static Block POLISHED_STARSTONE = register("polished_starstone", StarryBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE));
    public static Block POLISHED_STARSTONE_STAIRS = register("polished_starstone_stairs", settings -> new StarryStairsBlock(POLISHED_STARSTONE.defaultBlockState(), settings), BlockBehaviour.Properties.ofFullCopy(POLISHED_STARSTONE));
    public static Block POLISHED_STARSTONE_SLAB = register("polished_starstone_slab", StarrySlabBlock::new, BlockBehaviour.Properties.ofFullCopy(POLISHED_STARSTONE));
    public static Block POLISHED_STARSTONE_WALL = register("polished_starstone_wall", StarryWallBlock::new, BlockBehaviour.Properties.ofFullCopy(POLISHED_STARSTONE));

    public static Block STARSTONE_TILES = register("starstone_tiles", StarryBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_TILES));
    public static Block STARSTONE_TILE_STAIRS = register("starstone_tile_stairs", settings -> new StarryStairsBlock(STARSTONE_TILES.defaultBlockState(), settings), BlockBehaviour.Properties.ofFullCopy(STARSTONE_TILES));
    public static Block STARSTONE_TILE_SLAB = register("starstone_tile_slab", StarrySlabBlock::new, BlockBehaviour.Properties.ofFullCopy(STARSTONE_TILES));
    public static Block STARSTONE_TILE_WALL = register("starstone_tile_wall", StarryWallBlock::new, BlockBehaviour.Properties.ofFullCopy(STARSTONE_TILES));

    public static void init() {}

    public static Block register(String id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Starbound.id(id));
        Block block = registerWithoutItem(blockKey, blockFactory, settings);
        SBItems.registerBlockItem(block, BlockItem::new, new Item.Properties());
        return block;
    }

    public static Block registerWithoutItem(String id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Starbound.id(id));
        return registerWithoutItem(blockKey, blockFactory, settings);
    }

    public static Block registerWithoutItem(ResourceKey<Block> blockKey, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings) {
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, blockFactory.apply(settings.setId(blockKey)));
    }
}
