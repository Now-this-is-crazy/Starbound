package powercyphe.starbound.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.resources.ResourceLocation;

@Mixin(ItemModelResolver.class)
public interface ItemModelResolverAccessor {

    @Accessor("modelGetter")
    Function<ResourceLocation, ItemModel> starbound$getModelGetter();

}
