package powercyphe.starbound.common.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.block.*;

import java.util.function.Function;

public class ModBlocks {

    public static BlockSoundGroup STARRY_GEL = new BlockSoundGroup(1f, 1f,
            SoundEvents.BLOCK_HONEY_BLOCK_BREAK, ModSounds.STARRY_GEL_STEP, SoundEvents.BLOCK_HONEY_BLOCK_PLACE,
            SoundEvents.BLOCK_HONEY_BLOCK_HIT, SoundEvents.BLOCK_HONEY_BLOCK_FALL);

    public static Block STARRY_HONEY_BLOCK = register("starry_honey_block", StarryHoneyBlock::new, AbstractBlock.Settings.copy(Blocks.HONEY_BLOCK)
            .luminance(state -> (state.get(StarryHoneyBlock.LEVEL) >= StarryHoneyBlock.LEVEL_MAX ? 1 : 0)).ticksRandomly());

    public static Block STARRY_GEL_BLOCK = register("starry_gel_block", StarryGelBlock::new, AbstractBlock.Settings.create().solid().ticksRandomly()
            .strength(0.5F).luminance(state -> 1).emissiveLighting((state, world, pos) -> true)
            .jumpVelocityMultiplier(1.25F).slipperiness(0.66F).sounds(STARRY_GEL).nonOpaque());
    public static Block STARRY_GEL_LAYER = registerWithoutItem("starry_gel_layer", LayeredStarryGelBlock::new, AbstractBlock.Settings.copy(STARRY_GEL_BLOCK)
            .pistonBehavior(PistonBehavior.DESTROY));

    public static Block STARSTONE = register("starstone", StarryBlock::new, AbstractBlock.Settings.copy(Blocks.DEEPSLATE));
    public static Block STARSTONE_STAIRS = register("starstone_stairs", settings -> new StarryStairsBlock(STARSTONE.getDefaultState(), settings), AbstractBlock.Settings.copy(STARSTONE));
    public static Block STARSTONE_SLAB = register("starstone_slab", StarrySlabBlock::new, AbstractBlock.Settings.copy(STARSTONE));
    public static Block STARSTONE_WALL = register("starstone_wall", StarryWallBlock::new, AbstractBlock.Settings.copy(STARSTONE));

    public static Block POLISHED_STARSTONE = register("polished_starstone", StarryBlock::new, AbstractBlock.Settings.copy(Blocks.POLISHED_DEEPSLATE));
    public static Block POLISHED_STARSTONE_STAIRS = register("polished_starstone_stairs", settings -> new StarryStairsBlock(POLISHED_STARSTONE.getDefaultState(), settings), AbstractBlock.Settings.copy(POLISHED_STARSTONE));
    public static Block POLISHED_STARSTONE_SLAB = register("polished_starstone_slab", StarrySlabBlock::new, AbstractBlock.Settings.copy(POLISHED_STARSTONE));
    public static Block POLISHED_STARSTONE_WALL = register("polished_starstone_wall", StarryWallBlock::new, AbstractBlock.Settings.copy(POLISHED_STARSTONE));

    public static Block STARSTONE_TILES = register("starstone_tiles", StarryBlock::new, AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES));
    public static Block STARSTONE_TILE_STAIRS = register("starstone_tile_stairs", settings -> new StarryStairsBlock(STARSTONE_TILES.getDefaultState(), settings), AbstractBlock.Settings.copy(STARSTONE_TILES));
    public static Block STARSTONE_TILE_SLAB = register("starstone_tile_slab", StarrySlabBlock::new, AbstractBlock.Settings.copy(STARSTONE_TILES));
    public static Block STARSTONE_TILE_WALL = register("starstone_tile_wall", StarryWallBlock::new, AbstractBlock.Settings.copy(STARSTONE_TILES));

    public static void init() {}

    public static Block register(String id, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Starbound.id(id));
        Block block = registerWithoutItem(blockKey, blockFactory, settings);
        ModItems.registerBlockItem(block, BlockItem::new, new Item.Settings());
        return block;
    }

    public static Block registerWithoutItem(String id, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Starbound.id(id));
        return registerWithoutItem(blockKey, blockFactory, settings);
    }

    public static Block registerWithoutItem(RegistryKey<Block> blockKey, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
        return Registry.register(Registries.BLOCK, blockKey, blockFactory.apply(settings.registryKey(blockKey)));
    }
}
