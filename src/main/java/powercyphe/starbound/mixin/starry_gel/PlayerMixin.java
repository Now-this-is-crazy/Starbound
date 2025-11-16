package powercyphe.starbound.mixin.starry_gel;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.starbound.common.util.StarboundUtil;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void starbound$starryGel(double fallDistance, float damagePerDistance, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (StarboundUtil.isInStarryGel(player)) {
            cir.setReturnValue(false);
        }
    }
}
