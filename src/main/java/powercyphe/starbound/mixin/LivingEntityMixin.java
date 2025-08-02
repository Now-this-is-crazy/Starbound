package powercyphe.starbound.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
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
    private Optional<Hand> forcedTotem = Optional.empty();

    @Unique
    private ItemStack usedTotem = ItemStack.EMPTY;

    @ModifyExpressionValue(method = "tryUseDeathProtector", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean starbound$starryTotem(boolean original, DamageSource source) {
        LivingEntity entity = (LivingEntity) (Object) this;

        for (Hand hand : Hand.values()) {
            ItemStack stack = entity.getStackInHand(hand);
            DeathProtectionComponent deathProtectionComponent = stack.get(DataComponentTypes.DEATH_PROTECTION);

            if (deathProtectionComponent != null) {
                if (source.isOf(DamageTypes.OUT_OF_WORLD)) {
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

    @WrapOperation(method = "tryUseDeathProtector", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack starbound$starryTotem(LivingEntity instance, Hand hand, Operation<ItemStack> operation) {
        if (this.forcedTotem.isPresent()) {
            return operation.call(instance, this.forcedTotem.get());
        }
        return operation.call(instance, hand);
    }

    @WrapOperation(method = "tryUseDeathProtector", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"))
    private ItemStack starbound$starryTotem(ItemStack instance, Operation<ItemStack> operation) {
        this.usedTotem = operation.call(instance);
        return this.usedTotem;
    }

    @WrapWithCondition(method = "tryUseDeathProtector", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V"))
    private boolean starbound$starryTotem(World world, Entity entity, byte status) {
        if (this.forcedTotem.isPresent()) {
            this.forcedTotem = Optional.empty();
        }

        if (!this.usedTotem.isEmpty()) {
            ItemStack totemStack = this.usedTotem;
            this.usedTotem = ItemStack.EMPTY;

            if (world instanceof ServerWorld serverWorld) {
                if (StarryInvisibilityConsumeEffect.hasEffect(totemStack)) {
                    List<ServerPlayerEntity> players = serverWorld.getEntitiesByClass(ServerPlayerEntity.class, Box.of(entity.getPos().add(entity.getHeight()), 60, 60, 60), EntityPredicates.VALID_ENTITY);
                    for (ServerPlayerEntity player : players) {
                        ServerPlayNetworking.send(player, new StarryTotemUsePayload(entity.getId()));
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
