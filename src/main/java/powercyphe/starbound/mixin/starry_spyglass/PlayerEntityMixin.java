package powercyphe.starbound.mixin.starry_spyglass;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.common.registry.ModItems;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @WrapOperation(method = "isUsingSpyglass", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean starbound$starry_spyglass(ItemStack instance, Item item, Operation<Boolean> operation) {
        return operation.call(instance, item) || operation.call(instance, ModItems.STARRY_SPYGLASS);
    }
}
