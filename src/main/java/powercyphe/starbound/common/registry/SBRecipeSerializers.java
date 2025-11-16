package powercyphe.starbound.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.recipe.StarryEmissivityRecipe;

public class SBRecipeSerializers {

    public static RecipeSerializer<StarryEmissivityRecipe> STARRY_EMISSIVITY = register("starry_emissivity",
            new CustomRecipe.Serializer<>(StarryEmissivityRecipe::new));

    public static void init() {}

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Starbound.id(id), serializer);
    }
}
