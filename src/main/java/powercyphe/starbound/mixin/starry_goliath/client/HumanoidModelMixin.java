package powercyphe.starbound.mixin.starry_goliath.client;

import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.common.registry.SBItems;
import powercyphe.starbound.client.util.EntityRenderStateAddon;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends HumanoidRenderState> extends EntityModel<T> implements ArmedModel, HeadedModel {

    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart head;

    protected HumanoidModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "poseLeftArm", at = @At("HEAD"), cancellable = true)
    private void starbound$starryGoliathLeft(T state, HumanoidModel.ArmPose armPose, CallbackInfo ci) {
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;

        if (stateAddon.starbound$getActiveStack().is(SBItems.STARRY_GOLIATH) && state.ticksUsingItem > 0) {
            this.rightArm.yRot = -0.1F + 0.2F + this.head.yRot * 0.025F;
            this.leftArm.yRot = 0.1F - 0.2F + this.head.yRot * 0.025F;
            this.rightArm.xRot = -1.5707964F + this.head.xRot;
            this.leftArm.xRot = -1.5707964F + this.head.xRot;
            ci.cancel();
        }
    }

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    private void starbound$starryGoliathRight(T state, HumanoidModel.ArmPose armPose, CallbackInfo ci) {
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;

        if (stateAddon.starbound$getActiveStack().is(SBItems.STARRY_GOLIATH) && state.ticksUsingItem > 0) {
            this.rightArm.yRot = -0.1F + 0.2F + this.head.yRot * 0.025F;
            this.leftArm.yRot = 0.1F  - 0.2F + this.head.yRot * 0.025F;
            this.rightArm.xRot = -1.5707964F + this.head.xRot;
            this.leftArm.xRot = -1.5707964F + this.head.xRot;
            ci.cancel();
        }
    }
}
