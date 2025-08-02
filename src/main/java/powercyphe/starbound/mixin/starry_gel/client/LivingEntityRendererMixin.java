package powercyphe.starbound.mixin.starry_gel.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import powercyphe.starbound.client.StarboundClient;
import powercyphe.starbound.client.util.EntityRenderStateAddon;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.mixin.accessor.EntityRendererAccessor;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements FeatureRendererContext<S, M> {

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"))
    private void starbound$starryInvisibility(EntityModel<?> instance, MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, int color, Operation<Void> original) {
        LivingEntityRenderer<?, ?, ?> renderer = (LivingEntityRenderer<?, ?, ?>) (Object) this;
        EntityRenderState state = ((EntityRendererAccessor<?, ?>) renderer).starbound$getRenderState();
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;
        float invisibilityStrength = stateAddon.starbound$getStarryInvisibilityStrength();

        int red = ColorHelper.getRed(color);
        int green = ColorHelper.getGreen(color);
        int blue = ColorHelper.getBlue(color);
        int alpha = ColorHelper.getAlpha(color);

        if (invisibilityStrength > 0) {
            float f = 1 - (invisibilityStrength * (StarboundClient.shouldApplyStarrySpyglassEffects() ? 0.25F : 0.8F));
            original.call(instance, matrixStack, vertexConsumer, light, overlay, ColorHelper.getArgb((int) (alpha * f), (int) (red * f), (int) (green * f), blue));
            return;
        }
        original.call(instance, matrixStack, vertexConsumer, light, overlay, color);
    }

    @ModifyVariable(method = "getRenderLayer", at = @At("HEAD"), index = 3, argsOnly = true)
    private boolean starbound$starryInvisibility(boolean value, S state, boolean showBody, boolean translucent, boolean showOutline) {
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;
        if (stateAddon.starbound$getStarryInvisibilityStrength() > 0) {
            return true;
        }
        return translucent;
    }

    @ModifyReturnValue(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z", at = @At("RETURN"))
    private boolean starbound$starryGel(boolean original, T livingEntity, double d) {
        StarryInvisibilityComponent component = StarryInvisibilityComponent.get(livingEntity);
        if (component.isStealthy()) {
            original = false;
        }
        return original;
    }
}
