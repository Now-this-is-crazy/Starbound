package powercyphe.starbound.client.render.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.RotationAxis;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryObjectComponent;
import powercyphe.starbound.client.util.EntityRenderStateAddon;
import powercyphe.starbound.common.registry.ModItems;
import powercyphe.starbound.common.util.StarboundUtil;
import powercyphe.starbound.mixin.accessor.ItemModelManagerAccessor;

public class StarryObjectFeatureRenderer<S extends LivingEntityRenderState, M extends EntityModel<S> & ModelWithHead> extends FeatureRenderer<S, M>  {
    private final ItemModelManager itemModelManager;

    public StarryObjectFeatureRenderer(FeatureRendererContext<S, M> context, ItemModelManager itemModelManager) {
        super(context);
        this.itemModelManager = itemModelManager;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, S state, float limbAngle, float limbDistance) {
        EntityRenderStateAddon stateAddon = (EntityRenderStateAddon) state;
        MinecraftClient client = MinecraftClient.getInstance();
        float tickProgress = client.getRenderTickCounter().getTickProgress(false);

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180 - state.bodyYaw));

        DefaultedList<Pair<StarryObjectComponent.StarryObject, Integer>> starryObjects = stateAddon.starbound$getStarryObjects();
        float baseRotation = stateAddon.starbound$getStarryObjectBaseRotation() + ((StarryObjectComponent.bRotIncrease +
                ((float) starryObjects.size() / StarryObjectComponent.STARRY_OBJECTS_MAX * 6F)) * tickProgress);
        float floatRotation = stateAddon.starbound$getStarryObjectFloatRotation() + ((StarryObjectComponent.fRotIncrease +
                ((float) starryObjects.size() / StarryObjectComponent.STARRY_OBJECTS_MAX * 9F)) * tickProgress);

        for (int i = 0; i < starryObjects.size(); i++) {
            StarryObjectComponent.StarryObject object = starryObjects.get(i).getLeft();
            int variant = starryObjects.get(i).getRight();

            ItemModel model = ((ItemModelManagerAccessor) this.itemModelManager).starbound$getModelGetter()
                    .apply(Starbound.id("starry_" + object.getId() + "_" + variant));

            ItemRenderState renderState = new ItemRenderState();
            model.update(renderState, ModItems.STARRY_GOLIATH.getDefaultStack(), this.itemModelManager, ItemDisplayContext.FIXED, null, null, 0);

            matrices.push();
            objectPos(baseRotation, floatRotation, matrices, state, i, starryObjects.size());

            renderState.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
            matrices.pop();
        }
        matrices.pop();
    }

    public void objectPos(float baseRotation, float floatRotation, MatrixStack matrices, S state, int objectId, int objectAmount) {
        baseRotation = StarboundUtil.getObjectBaseRot(baseRotation, objectId, objectAmount);
        floatRotation = StarboundUtil.getObjectFloatRot(floatRotation, objectId, objectAmount);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(baseRotation + 117.5F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180F));
        matrices.translate(state.width * 1.5F, Math.sin(Math.toRadians(floatRotation)) * 0.2F - 0.25, -0.5F);

    }
}
