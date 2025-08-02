package powercyphe.starbound.common.registry;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.recipe.StarryEmissivityRecipe;

public class ModRecipeSerializers {

    public static RecipeSerializer<StarryEmissivityRecipe> STARRY_EMISSIVITY = register("starry_emissivity",
            new SpecialCraftingRecipe.SpecialRecipeSerializer<>(StarryEmissivityRecipe::new));

    public static void init() {}

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Starbound.id(id), serializer);
    }
}
