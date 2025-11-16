package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.registry.SBBlocks;
import powercyphe.starbound.common.registry.SBItems;

import java.util.concurrent.CompletableFuture;

public class SBRecipeProvider extends FabricRecipeProvider {
    public SBRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
        return new RecipeProvider(wrapperLookup, exporter) {
            @Override
            public void buildRecipes() {
                // Starry Gel

                shaped(RecipeCategory.BUILDING_BLOCKS, SBBlocks.STARRY_GEL_BLOCK, 1)
                        .pattern("GG")
                        .pattern("GG")
                        .define('G', SBItems.STARRY_GEL)
                        .unlockedBy("has_starry_gel", has(SBItems.STARRY_GEL))
                        .save(output);

                shapeless(RecipeCategory.BUILDING_BLOCKS, SBItems.STARRY_GEL, 4)
                        .requires(SBBlocks.STARRY_GEL_BLOCK)
                        .unlockedBy("has_starry_gel", has(SBItems.STARRY_GEL))
                        .save(output);

                // Starstone Blocks

                createBlockFamily(SBBlocks.STARSTONE, SBBlocks.STARSTONE_STAIRS, SBBlocks.STARSTONE_SLAB, SBBlocks.STARSTONE_WALL);
                createBlockFamily(SBBlocks.POLISHED_STARSTONE, SBBlocks.POLISHED_STARSTONE_STAIRS, SBBlocks.POLISHED_STARSTONE_SLAB, SBBlocks.POLISHED_STARSTONE_WALL);
                createBlockFamily(SBBlocks.STARSTONE_TILES, SBBlocks.STARSTONE_TILE_STAIRS, SBBlocks.STARSTONE_TILE_SLAB, SBBlocks.STARSTONE_TILE_WALL);

                // Starry Ingot & Goliath

                shaped(RecipeCategory.MISC, SBItems.STARRY_INGOT, 1)
                        .pattern("GGG")
                        .pattern("GIG")
                        .pattern("GGG")
                        .define('G', SBItems.STARRY_GEL)
                        .define('I', Items.IRON_INGOT)
                        .unlockedBy("has_starry_gel", has(SBItems.STARRY_GEL))
                        .save(output);

                shaped(RecipeCategory.COMBAT, SBItems.STARRY_TOTEM)
                        .pattern("III")
                        .pattern("ITI")
                        .pattern("III")
                        .define('I', SBItems.STARRY_INGOT)
                        .define('T', Items.TOTEM_OF_UNDYING)
                        .unlockedBy("has_starry_gel", has(SBItems.STARRY_GEL))
                        .save(output);

                shaped(RecipeCategory.COMBAT, SBItems.STARRY_SPYGLASS)
                        .pattern(" I ")
                        .pattern("ISI")
                        .pattern(" I ")
                        .define('I', SBItems.STARRY_INGOT)
                        .define('S', Items.SPYGLASS)
                        .unlockedBy("has_starry_gel", has(SBItems.STARRY_GEL))
                        .save(output);

                shaped(RecipeCategory.COMBAT, SBItems.STARRY_GOLIATH)
                        .pattern("II ")
                        .pattern("IN ")
                        .pattern("S  ")
                        .define('I', SBItems.STARRY_INGOT)
                        .define('S', Items.STICK)
                        .define('N', Items.NETHERITE_AXE)
                        .unlockedBy("has_starry_gel", has(SBItems.STARRY_GEL))
                        .save(output);
            }

            public void createBlockFamily(Block baseBlock, Block stairBlock, Block slabBlock, Block wallBlock) {
                stairBuilder(stairBlock, Ingredient.of(baseBlock))
                        .unlockedBy("has_" + baseBlock.getDescriptionId(), has(baseBlock))
                        .save(output);

                slabBuilder(RecipeCategory.BUILDING_BLOCKS, slabBlock, Ingredient.of(baseBlock))
                        .unlockedBy("has_" + baseBlock.getDescriptionId(), has(baseBlock))
                        .save(output);

                wall(RecipeCategory.BUILDING_BLOCKS, wallBlock, baseBlock);

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, stairBlock, baseBlock);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, slabBlock, baseBlock, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, wallBlock, baseBlock);
            }
        };
    }

    @Override
    public String getName() {
        return Starbound.MOD_ID + "RecipeProvider";
    }
}
