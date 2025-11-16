package powercyphe.starbound.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.common.item.consume.SavesFromVoidConsumeEffect;
import powercyphe.starbound.common.item.consume.StarryInvisibilityConsumeEffect;
import powercyphe.starbound.common.network.StarryTotemUsePayload;

import java.util.List;
import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private Optional<InteractionHand> forcedTotem = Optional.empty();

    @Unique
    private ItemStack usedTotem = ItemStack.EMPTY;

    @ModifyExpressionValue(method = "checkTotemDeathProtection", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean starbound$starryTotem(boolean original, DamageSource source) {
        LivingEntity entity = (LivingEntity) (Object) this;

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = entity.getItemInHand(hand);
            DeathProtection deathProtectionComponent = stack.get(DataComponents.DEATH_PROTECTION);

            if (deathProtectionComponent != null) {
                if (source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                    for (ConsumeEffect consumeEffect : deathProtectionComponent.deathEffects()) {
                        if (consumeEffect instanceof SavesFromVoidConsumeEffect) {
                            this.forcedTotem = Optional.of(hand);
                            return false;
                        }
                    }
                }
            }
        }
        return original;
    }

    @WrapOperation(method = "checkTotemDeathProtection", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack starbound$starryTotem(LivingEntity instance, InteractionHand hand, Operation<ItemStack> operation) {
        if (this.forcedTotem.isPresent()) {
            return operation.call(instance, this.forcedTotem.get());
        }
        return operation.call(instance, hand);
    }

    @WrapOperation(method = "checkTotemDeathProtection", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack starbound$starryTotem(ItemStack instance, Operation<ItemStack> operation) {
        this.usedTotem = operation.call(instance);
        return this.usedTotem;
    }

    @WrapWithCondition(method = "checkTotemDeathProtection", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
    private boolean starbound$starryTotem(Level world, Entity entity, byte status) {
        if (this.forcedTotem.isPresent()) {
            this.forcedTotem = Optional.empty();
        }

        if (!this.usedTotem.isEmpty()) {
            ItemStack totemStack = this.usedTotem;
            this.usedTotem = ItemStack.EMPTY;

            if (world instanceof ServerLevel serverWorld) {
                if (StarryInvisibilityConsumeEffect.hasEffect(totemStack)) {
                    List<ServerPlayer> players = serverWorld.getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(entity.position().add(entity.getBbHeight()), 60, 60, 60), EntitySelector.ENTITY_STILL_ALIVE);
                    for (ServerPlayer player : players) {
                        ServerPlayNetworking.send(player, new StarryTotemUsePayload(entity.getId()));
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
