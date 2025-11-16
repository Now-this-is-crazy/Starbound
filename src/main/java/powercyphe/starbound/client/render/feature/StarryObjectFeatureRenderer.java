package powercyphe.starbound.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemDisplayContext;
import powercyphe.starbound.client.util.EntityRenderStateAddon;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryObjectComponent;
import powercyphe.starbound.common.registry.SBItems;
import powercyphe.starbound.common.util.StarboundUtil;
import powercyphe.starbound.mixin.accessor.ItemModelResolverAccessor;

public class StarryObjectFeatureRenderer<S extends LivingEntityRenderState, M extends EntityModel<S> & HeadedModel> extends RenderLayer<S, M>  {
    private final ItemModelResolver itemModelManager;

    public StarryObjectFeatureRenderer(RenderLayerParent<S, M> context, ItemModelResolver itemModelManager) {
        super(context);
        this.itemModelManager = itemModelManager;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, S state, float limbAngle, float limbDistance) {
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;
        Minecraft client = Minecraft.getInstance();
        float tickProgress = client.getDeltaTracker().getGameTimeDeltaPartialTick(false);

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - state.bodyRot));

        NonNullList<Tuple<StarryObjectComponent.StarryObject, Integer>> starryObjects = stateAddon.starbound$getStarryObjects();
        float baseRotation = stateAddon.starbound$getStarryObjectBaseRotation() + ((StarryObjectComponent.bRotIncrease +
                ((float) starryObjects.size() / StarryObjectComponent.STARRY_OBJECTS_MAX * 6F)) * tickProgress);
        float floatRotation = stateAddon.starbound$getStarryObjectFloatRotation() + ((StarryObjectComponent.fRotIncrease +
                ((float) starryObjects.size() / StarryObjectComponent.STARRY_OBJECTS_MAX * 9F)) * tickProgress);

        for (int i = 0; i < starryObjects.size(); i++) {
            StarryObjectComponent.StarryObject object = starryObjects.get(i).getA();
            int variant = starryObjects.get(i).getB();

            ItemModel model = ((ItemModelResolverAccessor) this.itemModelManager).starbound$getModelGetter()
                    .apply(Starbound.id("starry_" + object.getId() + "_" + variant));

            ItemStackRenderState renderState = new ItemStackRenderState();
            model.update(renderState, SBItems.STARRY_GOLIATH.getDefaultInstance(), this.itemModelManager, ItemDisplayContext.FIXED, null, null, 0);

            poseStack.pushPose();
            objectPos(baseRotation, floatRotation, poseStack, state, i, starryObjects.size());

            renderState.submit(poseStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    public void objectPos(float baseRotation, float floatRotation, PoseStack matrices, S state, int objectId, int objectAmount) {
        baseRotation = StarboundUtil.getObjectBaseRot(baseRotation, objectId, objectAmount);
        floatRotation = StarboundUtil.getObjectFloatRot(floatRotation, objectId, objectAmount);

        matrices.mulPose(Axis.YP.rotationDegrees(baseRotation + 117.5F));
        matrices.mulPose(Axis.ZP.rotationDegrees(180F));
        matrices.translate(state.boundingBoxWidth * 1.5F, Math.sin(Math.toRadians(floatRotation)) * 0.2F - 0.25, -0.5F);

    }
}
