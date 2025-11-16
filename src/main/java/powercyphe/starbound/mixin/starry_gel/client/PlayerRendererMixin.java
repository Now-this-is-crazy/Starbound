package powercyphe.starbound.mixin.starry_gel.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.client.util.PlayerEntityRendererHeldItemRendererCondition;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.client.util.StarryInvisibilityCache;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel> implements PlayerEntityRendererHeldItemRendererCondition {
    public PlayerRendererMixin(EntityRendererProvider.Context ctx, PlayerModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Unique
    private boolean isHeldItemRenderer = false;

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void starbound$starryInvisibilityBefore(PoseStack matrices, MultiBufferSource vertexConsumers, int light, ResourceLocation skinTexture, ModelPart arm, boolean sleeveVisible, CallbackInfo ci) {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;

        if (this.starbound$isHeldItemRenderer() && clientPlayer != null) {
            float starryInvisibilityStrength = StarryInvisibilityComponent.get(clientPlayer).invisibilityStrength;
            if (starryInvisibilityStrength > 0) {
                StarryInvisibilityCache.shnoxcyphe$setStarryInvisibilityStrength(starryInvisibilityStrength);
            }
        }
    }

    @Inject(method = "renderHand", at = @At("TAIL"))
    private void starbound$starryInvisibilityAfter(PoseStack matrices, MultiBufferSource vertexConsumers, int light, ResourceLocation skinTexture, ModelPart arm, boolean sleeveVisible, CallbackInfo ci) {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;

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
