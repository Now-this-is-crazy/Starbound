package powercyphe.starbound.mixin.client.starry_emissivity;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.item.ItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.client.util.ItemRenderStateAddon;

@Mixin(ItemRenderState.class)
public class ItemRenderStateMixin implements ItemRenderStateAddon {

    @Unique
    private boolean hasStarryEmissivity = false;

    @ModifyVariable(method = "render", at = @At("HEAD"), index = 3, argsOnly = true)
    private int starbound$starryEmissivity(int light) {
        if (this.starbound$hasStarryEmissivity()) {
            return LightmapTextureManager.MAX_LIGHT_COORDINATE;
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
