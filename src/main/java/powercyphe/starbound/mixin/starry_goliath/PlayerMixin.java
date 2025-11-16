package powercyphe.starbound.mixin.starry_goliath;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.common.advancement.SBCriteria;
import powercyphe.starbound.common.component.StarryObjectComponent;
import powercyphe.starbound.common.registry.SBItems;

@Mixin(Player.class)
public class PlayerMixin {

    @ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), index = 3, argsOnly = true)
    private float starbound$starry_shield(float amount, ServerLevel world, DamageSource source) {
        Player player = (Player) (Object) this;
        StarryObjectComponent component = StarryObjectComponent.get(player);
        StarryObjectComponent.StarryObject object = StarryObjectComponent.StarryObject.SHIELD;

        // Shield Damage Reduction
        if (component.hasShieldInvulnerability(source)) {
            if (component.canBreakStarryObject(object)) {
                if (source.getDirectEntity() instanceof LivingEntity livingEntity && source.isDirect()) {
                    livingEntity.knockback(0.9,  player.getX() - livingEntity.getX(), player.getZ() - livingEntity.getZ());
                }
                int shields = component.getStarryObjectsAmount(object);
                int removed = (int) Math.min(shields, (amount / 14F) + 1F);

                player.displayClientMessage(Component.literal(shields + " " + removed), true);
                for (int i = 0; i < removed; i++) {
                    component.removeStarryObject(object);
                }

                float blockedDamage = amount * (1F - shields * 0.15F);
                if (player instanceof ServerPlayer serverPlayer) {
                    SBCriteria.DAMAGE_BLOCKED_WITH_STARRY_GOLIATH.trigger(serverPlayer, Math.max(0, amount - blockedDamage));
                }

                return blockedDamage;
            }
        }

        return amount;
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    private void starbound$interrupt(ServerLevel world, DamageSource source, float amount, CallbackInfo ci) {
        Player player = (Player) (Object) this;

        // Use Interruption
        if (!player.isInvulnerableTo(world, source) && source.getEntity() != null && amount > 1F) {
            StarryObjectComponent component = StarryObjectComponent.get(player);
            StarryObjectComponent.StarryObject shard = StarryObjectComponent.StarryObject.SHARD;
            if (component.getStarryObjectsAmount(shard) > 0 && component.canBreakStarryObject(shard)) {
                component.removeStarryObject(shard);
            }

            ItemStack activeStack = player.getUseItem();
            if (player.isUsingItem() && activeStack.is(SBItems.STARRY_GOLIATH)) {
                player.releaseUsingItem();
                player.getCooldowns().addCooldown(activeStack, source.isDirect() ? 200 : 60);
            }
        }
    }
}
