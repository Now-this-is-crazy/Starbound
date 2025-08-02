package powercyphe.starbound.mixin.starry_gel.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.client.util.PlayerEntityRendererHeldItemRendererCondition;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.client.util.StarryInvisibilityCache;
import powercyphe.starbound.mixin.accessor.EntityRendererAccessor;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> implements PlayerEntityRendererHeldItemRendererCondition {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Unique
    private boolean isHeldItemRenderer = false;

    @Inject(method = "renderArm", at = @At("HEAD"))
    private void starbound$starryInvisibilityBefore(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Identifier skinTexture, ModelPart arm, boolean sleeveVisible, CallbackInfo ci) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;

        if (this.starbound$isHeldItemRenderer() && clientPlayer != null) {
            float starryInvisibilityStrength = StarryInvisibilityComponent.get(clientPlayer).invisibilityStrength;
            if (starryInvisibilityStrength > 0) {
                StarryInvisibilityCache.shnoxcyphe$setStarryInvisibilityStrength(starryInvisibilityStrength);
            }
        }
    }

    @Inject(method = "renderArm", at = @At("TAIL"))
    private void starbound$starryInvisibilityAfter(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Identifier skinTexture, ModelPart arm, boolean sleeveVisible, CallbackInfo ci) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;

        if (this.starbound$isHeldItemRenderer() && clientPlayer != null) {
            StarryInvisibilityCache.shnoxcyphe$setStarryInvisibilityStrength(-1);
        }
    }

    @Override
    public void starbound$setHeldItemRendererCondition(boolean bl) {
        this.isHeldItemRenderer = bl;
    }

    @Override
    public boolean starbound$isHeldItemRenderer() {
        return this.isHeldItemRenderer;
    }
}
