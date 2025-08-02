package powercyphe.starbound.mixin.starry_gel;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.starbound.common.util.StarboundUtil;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void starbound$starryGel(double fallDistance, float damagePerDistance, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (StarboundUtil.isInStarryGel(player)) {
            cir.setReturnValue(false);
        }
    }
}
