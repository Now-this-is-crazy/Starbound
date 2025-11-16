package powercyphe.starbound.mixin.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.mixin.accessor.EntityRendererAccessor;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow public abstract EntityRenderDispatcher getEntityRenderDispatcher();

    @Shadow @Nullable public LocalPlayer player;

    @Shadow public abstract DeltaTracker getDeltaTracker();

    @Inject(method = "runTick", at = @At("TAIL"))
    private void starbound$updateHand(boolean tick, CallbackInfo ci) {
        LocalPlayer clientPlayer = this.player;
        if (clientPlayer != null && StarryInvisibilityComponent.get(clientPlayer).lastInvisibilityStrength > 0) {
            EntityRenderer<?, ?> renderer = this.getEntityRenderDispatcher().getRenderer(clientPlayer);
            if (renderer instanceof PlayerRenderer playerEntityRenderer) {
                PlayerRenderState state = (PlayerRenderState) starbound$getRenderStateInstance(playerEntityRenderer);
                playerEntityRenderer.extractRenderState(clientPlayer, state, this.getDeltaTracker().getGameTimeDeltaPartialTick(false));
            }
        }

    }

    @Unique
    public EntityRenderState starbound$getRenderStateInstance(EntityRenderer<?, ?> renderer) {
        return ((EntityRendererAccessor<?, ?>) renderer).starbound$getRenderState();
    }
}
