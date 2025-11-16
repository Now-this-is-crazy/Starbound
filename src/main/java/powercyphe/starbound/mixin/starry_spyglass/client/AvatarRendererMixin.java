package powercyphe.starbound.mixin.starry_spyglass.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.common.registry.SBItems;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {

    @WrapOperation(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean starbound$starry_spyglass(ItemStack instance, Item item, Operation<Boolean> operation) {
        return operation.call(instance, item) || operation.call(instance, SBItems.STARRY_SPYGLASS);
    }
}
