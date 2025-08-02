package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;
import powercyphe.starbound.common.block.LayeredStarryGelBlock;
import powercyphe.starbound.common.registry.ModBlocks;
import powercyphe.starbound.common.registry.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    protected ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.STARRY_HONEY_BLOCK);
        addDrop(ModBlocks.STARRY_GEL_BLOCK);

        addDrop(ModBlocks.STARSTONE);
        addDrop(ModBlocks.STARSTONE_STAIRS);
        addDrop(ModBlocks.STARSTONE_SLAB, slabDrops(ModBlocks.POLISHED_STARSTONE_SLAB));
        addDrop(ModBlocks.STARSTONE_WALL);

        addDrop(ModBlocks.POLISHED_STARSTONE);
        addDrop(ModBlocks.POLISHED_STARSTONE_STAIRS);
        addDrop(ModBlocks.POLISHED_STARSTONE_SLAB, slabDrops(ModBlocks.POLISHED_STARSTONE_SLAB));
        addDrop(ModBlocks.POLISHED_STARSTONE_WALL);

        addDrop(ModBlocks.STARSTONE_TILES);
        addDrop(ModBlocks.STARSTONE_TILE_STAIRS);
        addDrop(ModBlocks.STARSTONE_TILE_SLAB, slabDrops(ModBlocks.STARSTONE_TILE_SLAB));
        addDrop(ModBlocks.STARSTONE_TILE_WALL);

        addDrop(ModBlocks.STARRY_GEL_LAYER, layerDrops(ModBlocks.STARRY_GEL_LAYER));
    }

    public LootTable.Builder layerDrops(Block drop) {
        LootPool.Builder pool = LootPool.builder();
        pool.with(ItemEntry.builder(drop));
        pool.rolls(ConstantLootNumberProvider.create(1f));
        pool.bonusRolls(ConstantLootNumberProvider.create(0f));

        pool.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(4F))
                .conditionally(new BlockStatePropertyLootCondition.Builder(drop).properties(StatePredicate.Builder.create().exactMatch(LayeredStarryGelBlock.LAYERS, 4))));
        pool.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(3F))
                .conditionally(new BlockStatePropertyLootCondition.Builder(drop).properties(StatePredicate.Builder.create().exactMatch(LayeredStarryGelBlock.LAYERS, 3))));
        pool.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2F))
                .conditionally(new BlockStatePropertyLootCondition.Builder(drop).properties(StatePredicate.Builder.create().exactMatch(LayeredStarryGelBlock.LAYERS, 2))));
        pool.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1F))
                .conditionally(new BlockStatePropertyLootCondition.Builder(drop).properties(StatePredicate.Builder.create().exactMatch(LayeredStarryGelBlock.LAYERS, 1))));

        return LootTable.builder().pool(pool);
    }
}
