package powercyphe.starbound.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import powercyphe.starbound.common.registry.ModItems;
import powercyphe.starbound.common.registry.ModRecipeSerializers;
import powercyphe.starbound.common.registry.ModTags;

public class StarryEmissivityRecipe extends SpecialCraftingRecipe {
    public StarryEmissivityRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        boolean hasStarryGel = false;
        boolean hasSingleStack = false;

        boolean invalid = false;
        for (ItemStack stack : input.getStacks()) {
            if (stack.isOf(ModItems.STARRY_GEL) && !hasStarryGel) {
                hasStarryGel = true;
            } else if (!hasSingleStack && !stack.isIn(ModTags.Items.CANNOT_HAVE_STARRY_EMISSIVITY) && !stack.getOrDefault(ModItems.Components.STARRY_EMISSIVITY, false)) {
                hasSingleStack = true;
            } else if (!stack.isEmpty()) {
                invalid = true;
                break;
            }
        }

        return !invalid && hasSingleStack && hasStarryGel;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack singleStack = ItemStack.EMPTY;

        for (ItemStack stack : input.getStacks()) {
            if (!stack.isIn(ModTags.Items.CANNOT_HAVE_STARRY_EMISSIVITY)) {
                singleStack = stack;
                break;
            }
        }
        ItemStack result = singleStack.copyWithCount(1);
        result.set(ModItems.Components.STARRY_EMISSIVITY, true);
        return result;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipeSerializers.STARRY_EMISSIVITY;
    }
}
