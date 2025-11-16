package powercyphe.starbound.common.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import powercyphe.starbound.common.registry.SBItems;
import powercyphe.starbound.common.registry.SBRecipeSerializers;
import powercyphe.starbound.common.registry.SBTags;

public class StarryEmissivityRecipe extends CustomRecipe {
    public StarryEmissivityRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {
        boolean hasStarryGel = false;
        boolean hasSingleStack = false;

        boolean invalid = false;
        for (ItemStack stack : input.items()) {
            if (stack.is(SBItems.STARRY_GEL) && !hasStarryGel) {
                hasStarryGel = true;
            } else if (!hasSingleStack && !stack.is(SBTags.Items.CANNOT_HAVE_STARRY_EMISSIVITY) && !stack.getOrDefault(SBItems.Components.STARRY_EMISSIVITY, false)) {
                hasSingleStack = true;
            } else if (!stack.isEmpty()) {
                invalid = true;
                break;
            }
        }

        return !invalid && hasSingleStack && hasStarryGel;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack singleStack = ItemStack.EMPTY;

        for (ItemStack stack : input.items()) {
            if (!stack.is(SBTags.Items.CANNOT_HAVE_STARRY_EMISSIVITY)) {
                singleStack = stack;
                break;
            }
        }
        ItemStack result = singleStack.copyWithCount(1);
        result.set(SBItems.Components.STARRY_EMISSIVITY, true);
        return result;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SBRecipeSerializers.STARRY_EMISSIVITY;
    }
}
