package powercyphe.starbound.mixin.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.common.component.StarryObjectComponent;
import powercyphe.starbound.client.util.EntityRenderStateAddon;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void starbound$renderStateAddon(T entity, S state, float tickProgress, CallbackInfo ci) {
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;

        if (entity instanceof LivingEntity livingEntity) {
            // Active Item Stack
            stateAddon.starbound$setActiveStack(livingEntity.getUseItem());

            // Starry Invisibility Strength
            StarryInvisibilityComponent starryInvisibilityComponent = StarryInvisibilityComponent.get(livingEntity);
            stateAddon.starbound$setStarryInvisibilityStrength(starryInvisibilityComponent.invisibilityStrength);

            // Starry Objects, Base/Float Rotation
            StarryObjectComponent starryObjectComponent = StarryObjectComponent.get(livingEntity);
            stateAddon.starbound$setStarryObjects(starryObjectComponent.getStarryObjects());
            stateAddon.starbound$setStarryObjectBaseRotation(starryObjectComponent.baseRotation);
            stateAddon.starbound$setStarryObjectFloatRotation(starryObjectComponent.floatRotation);
        }

    }
}
