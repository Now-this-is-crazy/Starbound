package powercyphe.starbound.mixin.starry_goliath.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.common.registry.ModItems;
import powercyphe.starbound.client.util.EntityRenderStateAddon;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends BipedEntityRenderState> extends EntityModel<T> implements ModelWithArms, ModelWithHead {

    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart head;

    protected BipedEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "positionLeftArm", at = @At("HEAD"), cancellable = true)
    private void starbound$starryGoliathLeft(T state, BipedEntityModel.ArmPose armPose, CallbackInfo ci) {
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;

        if (stateAddon.starbound$getActiveStack().isOf(ModItems.STARRY_GOLIATH) && state.itemUseTime > 0) {
            this.rightArm.yaw = -0.1F + 0.2F + this.head.yaw * 0.025F;
            this.leftArm.yaw = 0.1F - 0.2F + this.head.yaw * 0.025F;
            this.rightArm.pitch = -1.5707964F + this.head.pitch;
            this.leftArm.pitch = -1.5707964F + this.head.pitch;
            ci.cancel();
        }
    }

    @Inject(method = "positionRightArm", at = @At("HEAD"), cancellable = true)
    private void starbound$starryGoliathRight(T state, BipedEntityModel.ArmPose armPose, CallbackInfo ci) {
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;

        if (stateAddon.starbound$getActiveStack().isOf(ModItems.STARRY_GOLIATH) && state.itemUseTime > 0) {
            this.rightArm.yaw = -0.1F + 0.2F + this.head.yaw * 0.025F;
            this.leftArm.yaw = 0.1F  - 0.2F + this.head.yaw * 0.025F;
            this.rightArm.pitch = -1.5707964F + this.head.pitch;
            this.leftArm.pitch = -1.5707964F + this.head.pitch;
            ci.cancel();
        }
    }
}
