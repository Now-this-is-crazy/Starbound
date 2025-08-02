package powercyphe.starbound.mixin.client.starry_emissivity;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import powercyphe.starbound.common.registry.ModItems;
import powercyphe.starbound.common.registry.ModTags;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<S extends BipedEntityRenderState, M extends BipedEntityModel<S>, A extends BipedEntityModel<S>> extends FeatureRenderer<S, M> {
    public ArmorFeatureRendererMixin(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @ModifyArgs(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;ILnet/minecraft/client/render/entity/model/BipedEntityModel;)V"))
    private void starbound$starry_emissivity(Args args) {
        if (args.get(2) instanceof ItemStack stack) {
            if (stack.getOrDefault(ModItems.Components.STARRY_EMISSIVITY, false) || stack.isIn(ModTags.Items.ALWAYS_HAS_STARRY_EMISSIVITY)) {
                args.set(4, LightmapTextureManager.MAX_LIGHT_COORDINATE);
            }
        }
    }
}
