package powercyphe.starbound.mixin.starry_gel.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.client.util.PlayerEntityRendererHeldItemRendererCondition;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderHandsWithItems",
    at = @At("HEAD"))
    private void starbound$enableHeldItemRendererCondition(float tickProgress, PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, LocalPlayer player, int light, CallbackInfo ci) {
        PlayerRenderer playerEntityRenderer = (PlayerRenderer) this.entityRenderDispatcher.getRenderer(this.minecraft.player);
        ((PlayerEntityRendererHeldItemRendererCondition) playerEntityRenderer).starbound$setHeldItemRendererCondition(true);
    }

    @Inject(method = "renderHandsWithItems",
            at = @At("TAIL"))
    private void starbound$disableHeldItemRendererCondition(float tickProgress, PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, LocalPlayer player, int light, CallbackInfo ci) {
        PlayerRenderer playerEntityRenderer = (PlayerRenderer) this.entityRenderDispatcher.getRenderer(this.minecraft.player);
        ((PlayerEntityRendererHeldItemRendererCondition) playerEntityRenderer).starbound$setHeldItemRendererCondition(false);
    }
}
