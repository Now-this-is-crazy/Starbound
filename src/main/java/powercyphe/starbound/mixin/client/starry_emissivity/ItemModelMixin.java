package powercyphe.starbound.mixin.client.starry_emissivity;

import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.model.BasicItemModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.starbound.client.util.ItemRenderStateAddon;
import powercyphe.starbound.common.registry.ModItems;
import powercyphe.starbound.common.registry.ModTags;

@Mixin(BasicItemModel.class)
public class ItemModelMixin {

    @Inject(method = "update", at = @At("TAIL"))
    private void starbound$starry_emissivity(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ItemDisplayContext displayContext, ClientWorld world, LivingEntity user, int seed, CallbackInfo ci) {
        ItemRenderStateAddon stateAddon = (ItemRenderStateAddon) state;
        stateAddon.starbound$setStarryEmissivity(
                stack.getOrDefault(ModItems.Components.STARRY_EMISSIVITY, false) ||
                        stack.isIn(ModTags.Items.ALWAYS_HAS_STARRY_EMISSIVITY)
        );
    }
}
