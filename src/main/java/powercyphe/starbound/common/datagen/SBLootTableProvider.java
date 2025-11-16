package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import powercyphe.starbound.common.block.LayeredStarryGelBlock;
import powercyphe.starbound.common.registry.SBBlocks;

import java.util.concurrent.CompletableFuture;

public class SBLootTableProvider extends FabricBlockLootTableProvider {
    protected SBLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(SBBlocks.STARRY_HONEY_BLOCK);
        dropSelf(SBBlocks.STARRY_GEL_BLOCK);

        dropSelf(SBBlocks.STARSTONE);
        dropSelf(SBBlocks.STARSTONE_STAIRS);
        add(SBBlocks.STARSTONE_SLAB, createSlabItemTable(SBBlocks.POLISHED_STARSTONE_SLAB));
        dropSelf(SBBlocks.STARSTONE_WALL);

        dropSelf(SBBlocks.POLISHED_STARSTONE);
        dropSelf(SBBlocks.POLISHED_STARSTONE_STAIRS);
        add(SBBlocks.POLISHED_STARSTONE_SLAB, createSlabItemTable(SBBlocks.POLISHED_STARSTONE_SLAB));
        dropSelf(SBBlocks.POLISHED_STARSTONE_WALL);

        dropSelf(SBBlocks.STARSTONE_TILES);
        dropSelf(SBBlocks.STARSTONE_TILE_STAIRS);
        add(SBBlocks.STARSTONE_TILE_SLAB, createSlabItemTable(SBBlocks.STARSTONE_TILE_SLAB));
        dropSelf(SBBlocks.STARSTONE_TILE_WALL);

        add(SBBlocks.STARRY_GEL_LAYER, layerDrops(SBBlocks.STARRY_GEL_LAYER));
    }

    public LootTable.Builder layerDrops(Block drop) {
        LootPool.Builder pool = LootPool.lootPool();
        pool.add(LootItem.lootTableItem(drop));
        pool.setRolls(ConstantValue.exactly(1f));
        pool.setBonusRolls(ConstantValue.exactly(0f));

        pool.apply(SetItemCountFunction.setCount(ConstantValue.exactly(4F))
                .when(new LootItemBlockStatePropertyCondition.Builder(drop).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(LayeredStarryGelBlock.LAYERS, 4))));
        pool.apply(SetItemCountFunction.setCount(ConstantValue.exactly(3F))
                .when(new LootItemBlockStatePropertyCondition.Builder(drop).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(LayeredStarryGelBlock.LAYERS, 3))));
        pool.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2F))
                .when(new LootItemBlockStatePropertyCondition.Builder(drop).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(LayeredStarryGelBlock.LAYERS, 2))));
        pool.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F))
                .when(new LootItemBlockStatePropertyCondition.Builder(drop).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(LayeredStarryGelBlock.LAYERS, 1))));

        return LootTable.lootTable().withPool(pool);
    }
}
