package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.registry.ModBlocks;
import powercyphe.starbound.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter exporter) {
        return new RecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void generate() {
                // Starry Gel

                createShaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STARRY_GEL_BLOCK, 1)
                        .pattern("GG")
                        .pattern("GG")
                        .input('G', ModItems.STARRY_GEL)
                        .criterion("has_starry_gel", conditionsFromItem(ModItems.STARRY_GEL))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.STARRY_GEL, 4)
                        .input(ModBlocks.STARRY_GEL_BLOCK)
                        .criterion("has_starry_gel", conditionsFromItem(ModItems.STARRY_GEL))
                        .offerTo(exporter);

                // Starstone Blocks

                createBlockFamily(ModBlocks.STARSTONE, ModBlocks.STARSTONE_STAIRS, ModBlocks.STARSTONE_SLAB, ModBlocks.STARSTONE_WALL);
                createBlockFamily(ModBlocks.POLISHED_STARSTONE, ModBlocks.POLISHED_STARSTONE_STAIRS, ModBlocks.POLISHED_STARSTONE_SLAB, ModBlocks.POLISHED_STARSTONE_WALL);
                createBlockFamily(ModBlocks.STARSTONE_TILES, ModBlocks.STARSTONE_TILE_STAIRS, ModBlocks.STARSTONE_TILE_SLAB, ModBlocks.STARSTONE_TILE_WALL);

                // Starry Ingot & Goliath

                createShaped(RecipeCategory.MISC, ModItems.STARRY_INGOT, 1)
                        .pattern("GGG")
                        .pattern("GIG")
                        .pattern("GGG")
                        .input('G', ModItems.STARRY_GEL)
                        .input('I', Items.IRON_INGOT)
                        .criterion("has_starry_gel", conditionsFromItem(ModItems.STARRY_GEL))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModItems.STARRY_TOTEM)
                        .pattern("III")
                        .pattern("ITI")
                        .pattern("III")
                        .input('I', ModItems.STARRY_INGOT)
                        .input('T', Items.TOTEM_OF_UNDYING)
                        .criterion("has_starry_gel", conditionsFromItem(ModItems.STARRY_GEL))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModItems.STARRY_SPYGLASS)
                        .pattern(" I ")
                        .pattern("ISI")
                        .pattern(" I ")
                        .input('I', ModItems.STARRY_INGOT)
                        .input('S', Items.SPYGLASS)
                        .criterion("has_starry_gel", conditionsFromItem(ModItems.STARRY_GEL))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModItems.STARRY_GOLIATH)
                        .pattern("II ")
                        .pattern("IN ")
                        .pattern("S  ")
                        .input('I', ModItems.STARRY_INGOT)
                        .input('S', Items.STICK)
                        .input('N', Items.NETHERITE_AXE)
                        .criterion("has_starry_gel", conditionsFromItem(ModItems.STARRY_GEL))
                        .offerTo(exporter);
            }

            public void createBlockFamily(Block baseBlock, Block stairBlock, Block slabBlock, Block wallBlock) {
                createStairsRecipe(stairBlock, Ingredient.ofItems(baseBlock))
                        .criterion("has_" + baseBlock.getTranslationKey(), conditionsFromItem(baseBlock))
                        .offerTo(exporter);

                createSlabRecipe(RecipeCategory.BUILDING_BLOCKS, slabBlock, Ingredient.ofItems(baseBlock))
                        .criterion("has_" + baseBlock.getTranslationKey(), conditionsFromItem(baseBlock))
                        .offerTo(exporter);

                offerWallRecipe(RecipeCategory.BUILDING_BLOCKS, wallBlock, baseBlock);

                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, stairBlock, baseBlock);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, slabBlock, baseBlock, 2);
                offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, wallBlock, baseBlock);
            }
        };
    }

    @Override
    public String getName() {
        return Starbound.MOD_ID + "RecipeProvider";
    }
}
