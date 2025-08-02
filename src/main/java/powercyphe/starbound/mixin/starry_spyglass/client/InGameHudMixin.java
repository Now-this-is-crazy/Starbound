package powercyphe.starbound.mixin.starry_spyglass.client;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import powercyphe.starbound.client.StarboundClient;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @ModifyArg(method = "renderSpyglassOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIFFIIII)V"), index = 1)
    private Identifier starbound$starry_spyglass(Identifier sprite) {
        if (StarboundClient.shouldApplyStarrySpyglassEffects()) {
            return StarboundClient.STARRY_SPYGLASS_SCOPE;
        }

        return sprite;
    }
}
