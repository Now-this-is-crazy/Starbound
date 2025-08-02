package powercyphe.starbound.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.mixin.accessor.EntityRendererAccessor;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow public abstract EntityRenderDispatcher getEntityRenderDispatcher();

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow public abstract RenderTickCounter getRenderTickCounter();

    @Inject(method = "render", at = @At("TAIL"))
    private void starbound$updateHand(boolean tick, CallbackInfo ci) {
        ClientPlayerEntity clientPlayer = this.player;
        if (clientPlayer != null && StarryInvisibilityComponent.get(clientPlayer).lastInvisibilityStrength > 0) {
            EntityRenderer<?, ?> renderer = this.getEntityRenderDispatcher().getRenderer(clientPlayer);
            if (renderer instanceof PlayerEntityRenderer playerEntityRenderer) {
                PlayerEntityRenderState state = (PlayerEntityRenderState) starbound$getRenderStateInstance(playerEntityRenderer);
                playerEntityRenderer.updateRenderState(clientPlayer, state, this.getRenderTickCounter().getTickProgress(false));
            }
        }

    }

    @Unique
    public EntityRenderState starbound$getRenderStateInstance(EntityRenderer<?, ?> renderer) {
        return ((EntityRendererAccessor<?, ?>) renderer).starbound$getRenderState();
    }
}
