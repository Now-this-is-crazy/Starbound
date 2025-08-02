package powercyphe.starbound.mixin.starry_spyglass.client;

import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import powercyphe.starbound.client.StarboundClient;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {


    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setUniform(Ljava/lang/String;[F)V", ordinal = 4), index = 1)
    private float[] starbound$starry_spyglass(float[] values) {
        if (StarboundClient.shouldApplyStarrySpyglassEffects()) {
            return new float[]{Math.max(values[0], 0.7F)};
        }
        return values;
    }
}
