package powercyphe.starbound.mixin.accessor;

import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;

@Mixin(ItemModelManager.class)
public interface ItemModelManagerAccessor {

    @Accessor("modelGetter")
    Function<Identifier, ItemModel> starbound$getModelGetter();

}
