package powercyphe.starbound.mixin.starry_spyglass.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.client.StarboundClient;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @Definition(id = "renderPass", local = @Local(type = RenderPass.class))
    @Definition(id = "setUniform", method = "Lcom/mojang/blaze3d/systems/RenderPass;setUniform(Ljava/lang/String;[F)V")
    @Expression("renderPass.setUniform('NightVisionFactor', new float[]{@(?)})")
    @ModifyExpressionValue(method = "updateLightTexture", at = @At("MIXINEXTRAS:EXPRESSION"), remap = false)
    private float starbound$starry_spyglass(float original) {
        if (StarboundClient.shouldApplyStarrySpyglassEffects()) {
            return Math.max(original, 0.7F);
        }
        return original;
    }
}
