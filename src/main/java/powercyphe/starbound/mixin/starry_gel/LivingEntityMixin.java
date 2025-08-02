package powercyphe.starbound.mixin.starry_gel;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.common.item.consume.StarryInvisibilityConsumeEffect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyReturnValue(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("RETURN"))
    private boolean starbound$starryGel(boolean original, LivingEntity livingEntity) {
        StarryInvisibilityComponent component = StarryInvisibilityComponent.get(livingEntity);
        if (component.isStealthy()) {
            return false;
        }

        return original;
    }
}
