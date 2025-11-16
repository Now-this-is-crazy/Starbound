package powercyphe.starbound.mixin.starry_spyglass.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import powercyphe.starbound.client.StarboundClient;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @Definition(id = "putFloat", method = "Lcom/mojang/blaze3d/buffers/Std140Builder;putFloat(F)Lcom/mojang/blaze3d/buffers/Std140Builder;")
    @Definition(id = "intoBuffer", method = "Lcom/mojang/blaze3d/buffers/Std140Builder;intoBuffer(Ljava/nio/ByteBuffer;)Lcom/mojang/blaze3d/buffers/Std140Builder;")
    @Expression("intoBuffer(?).putFloat(?).putFloat(?).putFloat(?).putFloat(?)")
    @ModifyArg(method = "updateLightTexture", at = @At("MIXINEXTRAS:EXPRESSION"), remap = false)
    private float starbound$starry_spyglass(float original) {
        if (StarboundClient.shouldApplyStarrySpyglassEffects()) {
            return Math.max(original, 0.7F);
        }
        return original;
    }
}
