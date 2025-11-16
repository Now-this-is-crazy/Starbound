package powercyphe.starbound.mixin.starry_gel.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import powercyphe.starbound.client.StarboundClient;
import powercyphe.starbound.client.util.StarryInvisibilityCache;

@Mixin(ModelPart.class)
public class ModelPartMixin {

    @ModifyVariable(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V",
            at = @At("HEAD"), index = 5, argsOnly = true, remap = false)
    private int starbound$opacity(int color) {
        float starryInvisibilityStrength = StarryInvisibilityCache.shnoxcyphe$getStarryInvisibilityStrength();

        if (starryInvisibilityStrength != -1) {
            int red = ARGB.red(color);
            int green = ARGB.green(color);
            int blue = ARGB.blue(color);
            int alpha = ARGB.alpha(color);

            float opacity = 1 - (starryInvisibilityStrength * (StarboundClient.shouldApplyStarrySpyglassEffects() ? 0.25F : 0.8F));
            return ARGB.color((int) (alpha * opacity), (int) (red * opacity), (int) (green * opacity), blue);
        }
        return color;
    }
}
