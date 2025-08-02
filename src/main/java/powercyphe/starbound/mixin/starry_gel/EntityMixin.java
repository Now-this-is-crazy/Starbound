package powercyphe.starbound.mixin.starry_gel;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.common.registry.ModParticles;
import powercyphe.starbound.common.util.StarboundUtil;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyReturnValue(method = "getFinalGravity", at = @At("RETURN"))
    private double starbound$starryGel(double original) {
        Entity entity = (Entity) (Object) this;
        return StarboundUtil.isInStarryGel(entity) ? 0 : original;
    }

    @WrapWithCondition(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V"))
    private boolean starbound$starryGel(Entity entity, double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        return !StarboundUtil.isInStarryGel(entity);
    }

    @ModifyExpressionValue(method = "updateVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;movementInputToVelocity(Lnet/minecraft/util/math/Vec3d;FF)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d starbound$starryGel(Vec3d original, float speed, Vec3d movementInput) {
        Entity entity = (Entity) (Object) this;
        if (StarboundUtil.shouldApplyStarryGelMovement(entity)) {
            if (entity instanceof LivingEntity livingEntity) {
                speed = livingEntity.getMovementSpeed() * (livingEntity.isSprinting() ? 0.35f : 0.175f);
            }
            return starbound$movementInputToVelocity(movementInput, speed, entity.getPitch(), entity.getYaw());
        }
        return original;
    }

    @ModifyArg(method = "updateVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"), index = 0)
    private Vec3d starbound$starryGel(Vec3d original) {
        Entity entity = (Entity) (Object) this;
        if (StarboundUtil.shouldApplyStarryGelMovement(entity)) {
            return original.multiply(0.77, 0.88, 0.77);
        }

        return original;
    }

    @ModifyReturnValue(method = "shouldSpawnSprintingParticles", at = @At("RETURN"))
    private boolean starbound$starryGel(boolean original) {
        Entity entity = (Entity) (Object) this;
        return original && !StarboundUtil.shouldApplyStarryGelMovement(entity);
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void starbound$starryGel(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        Vec3d vel = entity.getVelocity();

        if (StarboundUtil.isInStarryGel(entity) && vel.length() > 0.02) {
            // Display Movement Particles
            entity.getWorld().addParticleClient(ModParticles.STARRY_TRAIL, entity.getParticleX(0.7),
                    entity.getRandomBodyY(), entity.getParticleZ(0.7), vel.getX() * -4.0, vel.getY() * -4.0, vel.getZ() * -4.0);
        }
    }

    @Unique
    private static Vec3d starbound$movementInputToVelocity(Vec3d movementInput, float speed, float pitch, float yaw) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7) {
            return Vec3d.ZERO;
        }
        else {
            Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
            float f = MathHelper.sin(yaw * (float) (Math.PI / 180.0));
            float g = MathHelper.cos(yaw * (float) (Math.PI / 180.0));
            float h = -MathHelper.sin(pitch * (float) (Math.PI / 180.0));

            return new Vec3d(
                    vec3d.x * g - vec3d.z * f,
                    (vec3d.z > 0 ? h : -h) * speed * (movementInput.z != 0 ? 1.1 : 0),
                    vec3d.z * g + vec3d.x * f
            );
        }
    }
}
