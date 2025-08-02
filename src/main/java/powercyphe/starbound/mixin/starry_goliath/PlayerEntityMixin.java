package powercyphe.starbound.mixin.starry_goliath;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.common.component.StarryObjectComponent;
import powercyphe.starbound.common.registry.ModItems;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), index = 3, argsOnly = true)
    private float starbound$starry_shield(float amount, ServerWorld world, DamageSource source) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        StarryObjectComponent component = StarryObjectComponent.get(player);
        StarryObjectComponent.StarryObject object = StarryObjectComponent.StarryObject.SHIELD;

        // Shield Damage Reduction
        if (component.hasShieldInvulnerability(source)) {
            if (component.canBreakStarryObject(object)) {
                if (source.getSource() instanceof LivingEntity livingEntity && source.isDirect()) {
                    livingEntity.takeKnockback(0.9,  player.getX() - livingEntity.getX(), player.getZ() - livingEntity.getZ());
                }
                int shields = component.getStarryObjectsAmount(object);
                component.removeStarryObject(object);

                return amount * (1F - shields * 0.15F);
            }
        }

        return amount;
    }

    @Inject(method = "applyDamage", at = @At("HEAD"))
    private void starbound$interrupt(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Use Interruption
        if (!player.isInvulnerableTo(world, source) && source.getAttacker() != null && amount > 1F) {
            StarryObjectComponent component = StarryObjectComponent.get(player);
            StarryObjectComponent.StarryObject shard = StarryObjectComponent.StarryObject.SHARD;
            if (component.getStarryObjectsAmount(shard) > 0 && component.canBreakStarryObject(shard)) {
                component.removeStarryObject(shard);
            }

            ItemStack activeStack = player.getActiveItem();
            if (player.isUsingItem() && activeStack.isOf(ModItems.STARRY_GOLIATH)) {
                player.stopUsingItem();
                player.getItemCooldownManager().set(activeStack, source.isDirect() ? 200 : 60);
            }
        }
    }
}
