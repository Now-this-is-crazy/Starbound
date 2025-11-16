package powercyphe.starbound.mixin.starry_gel.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import powercyphe.starbound.client.StarboundClient;
import powercyphe.starbound.client.util.EntityRenderStateAddon;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.mixin.accessor.EntityRendererAccessor;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements RenderLayerParent<S, M> {

    protected LivingEntityRendererMixin(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"))
    private void starbound$starryInvisibility(EntityModel<?> instance, PoseStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, int color, Operation<Void> original) {
        LivingEntityRenderer<?, ?, ?> renderer = (LivingEntityRenderer<?, ?, ?>) (Object) this;
        EntityRenderState state = ((EntityRendererAccessor<?, ?>) renderer).starbound$getRenderState();
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;
        float invisibilityStrength = stateAddon.starbound$getStarryInvisibilityStrength();

        int red = ARGB.red(color);
        int green = ARGB.green(color);
        int blue = ARGB.blue(color);
        int alpha = ARGB.alpha(color);

        if (invisibilityStrength > 0) {
            float f = 1 - (invisibilityStrength * (StarboundClient.shouldApplyStarrySpyglassEffects() ? 0.25F : 0.8F));
            original.call(instance, matrixStack, vertexConsumer, light, overlay, ARGB.color((int) (alpha * f), (int) (red * f), (int) (green * f), blue));
            return;
        }
        original.call(instance, matrixStack, vertexConsumer, light, overlay, color);
    }

    @ModifyVariable(method = "getRenderType", at = @At("HEAD"), index = 3, argsOnly = true)
    private boolean starbound$starryInvisibility(boolean value, S state, boolean showBody, boolean translucent, boolean showOutline) {
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;
        if (stateAddon.starbound$getStarryInvisibilityStrength() > 0) {
            return true;
        }
        return translucent;
    }

    @ModifyReturnValue(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At("RETURN"))
    private boolean starbound$starryGel(boolean original, T livingEntity, double d) {
        StarryInvisibilityComponent component = StarryInvisibilityComponent.get(livingEntity);
        if (component.isStealthy()) {
            original = false;
        }
        return original;
    }
}
