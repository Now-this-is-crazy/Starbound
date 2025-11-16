package powercyphe.starbound.mixin.starry_spyglass;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.common.registry.SBItems;

@Mixin(Player.class)
public class PlayerMixin {

    @WrapOperation(method = "isScoping", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean starbound$starry_spyglass(ItemStack instance, Item item, Operation<Boolean> operation) {
        return operation.call(instance, item) || operation.call(instance, SBItems.STARRY_SPYGLASS);
    }
}
