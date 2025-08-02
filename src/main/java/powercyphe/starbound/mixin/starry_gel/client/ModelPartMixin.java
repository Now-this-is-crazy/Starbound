package powercyphe.starbound.mixin.starry_gel.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import powercyphe.starbound.client.StarboundClient;
import powercyphe.starbound.client.util.StarryInvisibilityCache;

@Mixin(ModelPart.class)
public class ModelPartMixin {

    @ModifyVariable(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V",
            at = @At("HEAD"), index = 5, argsOnly = true)
    private int starbound$opacity(int color) {
        float starryInvisibilityStrength = StarryInvisibilityCache.shnoxcyphe$getStarryInvisibilityStrength();

        if (starryInvisibilityStrength != -1) {
            int red = ColorHelper.getRed(color);
            int green = ColorHelper.getGreen(color);
            int blue = ColorHelper.getBlue(color);
            int alpha = ColorHelper.getAlpha(color);

            float opacity = 1 - (starryInvisibilityStrength * (StarboundClient.shouldApplyStarrySpyglassEffects() ? 0.25F : 0.8F));
            return ColorHelper.getArgb((int) (alpha * opacity), (int) (red * opacity), (int) (green * opacity), blue);
        }
        return color;
    }
}
