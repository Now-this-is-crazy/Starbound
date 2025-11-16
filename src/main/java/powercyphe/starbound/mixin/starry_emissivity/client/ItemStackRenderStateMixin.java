package powercyphe.starbound.mixin.starry_emissivity.client;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.client.util.ItemRenderStateAddon;

@Mixin(ItemStackRenderState.class)
public class ItemStackRenderStateMixin implements ItemRenderStateAddon {

    @Unique
    private boolean hasStarryEmissivity = false;

    @ModifyVariable(method = "submit", at = @At("HEAD"), index = 3, argsOnly = true)
    private int starbound$starryEmissivity(int light) {
        if (this.starbound$hasStarryEmissivity()) {
            return LightTexture.FULL_BRIGHT;
        }

        return light;
    }

    @Inject(method = "clear", at = @At("HEAD"))
    private void starbound$starryEmissivity(CallbackInfo ci) {
        this.starbound$setStarryEmissivity(false);
    }

    @Override
    public void starbound$setStarryEmissivity(boolean bl) {
        this.hasStarryEmissivity = bl;
    }

    @Override
    public boolean starbound$hasStarryEmissivity() {
        return this.hasStarryEmissivity;
    }
}
