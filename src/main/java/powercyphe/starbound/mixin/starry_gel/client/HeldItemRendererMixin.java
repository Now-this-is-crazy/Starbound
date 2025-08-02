package powercyphe.starbound.mixin.starry_gel.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.client.util.PlayerEntityRendererHeldItemRendererCondition;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V",
    at = @At("HEAD"))
    private void starbound$enableHeldItemRendererCondition(float tickProgress, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci) {
        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer) this.entityRenderDispatcher.getRenderer(this.client.player);
        ((PlayerEntityRendererHeldItemRendererCondition) playerEntityRenderer).starbound$setHeldItemRendererCondition(true);
    }

    @Inject(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V",
            at = @At("TAIL"))
    private void starbound$disableHeldItemRendererCondition(float tickProgress, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci) {
        PlayerEntityRenderer playerEntityRenderer = (PlayerEntityRenderer) this.entityRenderDispatcher.getRenderer(this.client.player);
        ((PlayerEntityRendererHeldItemRendererCondition) playerEntityRenderer).starbound$setHeldItemRendererCondition(false);
    }
}
