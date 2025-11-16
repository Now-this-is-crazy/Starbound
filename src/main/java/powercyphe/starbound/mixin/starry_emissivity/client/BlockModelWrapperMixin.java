package powercyphe.starbound.mixin.starry_emissivity.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.client.util.ItemRenderStateAddon;
import powercyphe.starbound.common.registry.SBItems;
import powercyphe.starbound.common.registry.SBTags;

@Mixin(BlockModelWrapper.class)
public class BlockModelWrapperMixin {

    @Inject(method = "update", at = @At("TAIL"))
    private void starbound$starry_emissivity(ItemStackRenderState state, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext itemDisplayContext, ClientLevel clientLevel, ItemOwner itemOwner, int i, CallbackInfo ci) {
        ItemRenderStateAddon stateAddon = (ItemRenderStateAddon) state;
        stateAddon.starbound$setStarryEmissivity(
                stack.getOrDefault(SBItems.Components.STARRY_EMISSIVITY, false) ||
                        stack.is(SBTags.Items.ALWAYS_HAS_STARRY_EMISSIVITY)
        );
    }
}
