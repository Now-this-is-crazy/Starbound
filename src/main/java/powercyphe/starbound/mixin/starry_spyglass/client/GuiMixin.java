package powercyphe.starbound.mixin.starry_spyglass.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import powercyphe.starbound.client.StarboundClient;

@Mixin(Gui.class)
public class GuiMixin {

    @ModifyArg(method = "renderSpyglassOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"), index = 1)
    private ResourceLocation starbound$starry_spyglass(ResourceLocation sprite) {
        if (StarboundClient.shouldApplyStarrySpyglassEffects()) {
            return StarboundClient.STARRY_SPYGLASS_SCOPE;
        }

        return sprite;
    }
}
