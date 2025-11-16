package powercyphe.starbound.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.common.registry.SBItems;

@Mixin(TrimMaterialProperty.class)
public class TrimMaterialPropertyMixin {

    @ModifyReturnValue(method = "get(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/LivingEntity;ILnet/minecraft/world/item/ItemDisplayContext;)Lnet/minecraft/resources/ResourceKey;",
    at = @At("RETURN"))
    private ResourceKey<TrimMaterial> starbound$useIronTrim(ResourceKey<TrimMaterial> original) {
        if (original == SBItems.Materials.STARRY_TRIM_MATERIAL) {
            return TrimMaterials.LAPIS;
        }
        return original;
    }
}
