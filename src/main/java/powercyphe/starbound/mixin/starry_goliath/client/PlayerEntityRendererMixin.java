package powercyphe.starbound.mixin.starry_goliath.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.starbound.client.render.feature.StarryObjectFeatureRenderer;
import powercyphe.starbound.common.registry.ModItems;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin  extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void starbound$init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.addFeature(new StarryObjectFeatureRenderer<>(this, ctx.getItemModelManager()));
    }

    @Inject(method = "getArmPose(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", at = @At("HEAD"), cancellable = true)
    private static void starbound$holdPose(PlayerEntity player, ItemStack stack, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        if (player.getActiveItem().isOf(ModItems.STARRY_GOLIATH) && player.getItemUseTimeLeft() > 0) {
            cir.setReturnValue(BipedEntityModel.ArmPose.BOW_AND_ARROW);
        }
    }
}
