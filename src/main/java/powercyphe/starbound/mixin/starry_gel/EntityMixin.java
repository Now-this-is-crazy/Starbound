package powercyphe.starbound.mixin.starry_gel;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.common.registry.SBParticles;
import powercyphe.starbound.common.util.StarboundUtil;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyReturnValue(method = "getGravity", at = @At("RETURN"))
    private double starbound$starryGel(double original) {
        Entity entity = (Entity) (Object) this;
        return StarboundUtil.isInStarryGel(entity) ? 0 : original;
    }

    @WrapWithCondition(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;checkFallDamage(DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V"))
    private boolean starbound$starryGel(Entity entity, double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        return !StarboundUtil.isInStarryGel(entity);
    }

    @ModifyExpressionValue(method = "moveRelative", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getInputVector(Lnet/minecraft/world/phys/Vec3;FF)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 starbound$starryGel(Vec3 original, float speed, Vec3 movementInput) {
        Entity entity = (Entity) (Object) this;
        if (StarboundUtil.shouldApplyStarryGelMovement(entity)) {
            if (entity instanceof LivingEntity livingEntity) {
                speed = livingEntity.getSpeed() * (livingEntity.isSprinting() ? 0.35f : 0.175f);
            }
            return starbound$movementInputToVelocity(movementInput, speed, entity.getXRot(), entity.getYRot());
        }
        return original;
    }

    @ModifyArg(method = "moveRelative", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"), index = 0)
    private Vec3 starbound$starryGel(Vec3 original) {
        Entity entity = (Entity) (Object) this;
        if (StarboundUtil.shouldApplyStarryGelMovement(entity)) {
            return original.multiply(0.77, 0.88, 0.77);
        }

        return original;
    }

    @ModifyReturnValue(method = "canSpawnSprintParticle", at = @At("RETURN"))
    private boolean starbound$starryGel(boolean original) {
        Entity entity = (Entity) (Object) this;
        return original && !StarboundUtil.shouldApplyStarryGelMovement(entity);
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void starbound$starryGel(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        Vec3 vel = entity.getDeltaMovement();

        if (StarboundUtil.isInStarryGel(entity) && vel.length() > 0.02) {
            // Display Movement Particles
            entity.level().addParticle(SBParticles.STARRY_TRAIL, entity.getRandomX(0.7),
                    entity.getRandomY(), entity.getRandomZ(0.7), vel.x() * -4.0, vel.y() * -4.0, vel.z() * -4.0);
        }
    }

    @Unique
    private static Vec3 starbound$movementInputToVelocity(Vec3 movementInput, float speed, float pitch, float yaw) {
        double d = movementInput.lengthSqr();
        if (d < 1.0E-7) {
            return Vec3.ZERO;
        }
        else {
            Vec3 vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).scale(speed);
            float f = Mth.sin(yaw * (float) (Math.PI / 180.0));
            float g = Mth.cos(yaw * (float) (Math.PI / 180.0));
            float h = -Mth.sin(pitch * (float) (Math.PI / 180.0));

            return new Vec3(
                    vec3d.x * g - vec3d.z * f,
                    (vec3d.z > 0 ? h : -h) * speed * (movementInput.z != 0 ? 1.1 : 0),
                    vec3d.z * g + vec3d.x * f
            );
        }
    }
}
