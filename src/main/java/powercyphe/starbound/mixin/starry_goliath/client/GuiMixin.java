package powercyphe.starbound.mixin.starry_goliath.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryObjectComponent;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Unique
    private static final ResourceLocation STARRY_SHIELD_FULL = Starbound.id("hud/heart/starry_shield_full");
    @Unique
    private static final ResourceLocation STARRY_SHIELD_FULL_BLINKING = Starbound.id("hud/heart/starry_shield_full_blinking");
    @Unique
    private static final ResourceLocation STARRY_SHIELD_HALF = Starbound.id("hud/heart/starry_shield_half");
    @Unique
    private static final ResourceLocation STARRY_SHIELD_HALF_BLINKING = Starbound.id("hud/heart/starry_shield_half_blinking");

    @Shadow @Nullable protected abstract Player getCameraPlayer();

    @WrapOperation(method = "renderHeart", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIII)V"))
    private void starbound$starryShield(GuiGraphics instance, RenderPipeline renderPipeline, ResourceLocation resourceLocation, int x, int y, int width, int height, Operation<Void> original, GuiGraphics guiGraphics, Gui.HeartType heartType, int i, int j, boolean hardcore, boolean blinking, boolean half) {
        Player player = this.getCameraPlayer();
        if (heartType != Gui.HeartType.CONTAINER && player != null) {
            StarryObjectComponent component = StarryObjectComponent.get(player);

            if (component.getStarryObjectsAmount(StarryObjectComponent.StarryObject.SHIELD) > 0) {
                original.call(instance, renderPipeline, starbound$getHeartTexture(half, blinking), x, y, width, height);
                return;
            }
        }
        original.call(instance, renderPipeline, resourceLocation, x, y, width, height);
    }

    @Unique
    private ResourceLocation starbound$getHeartTexture(boolean half, boolean blinking) {
        if (half) {
            return blinking ? STARRY_SHIELD_HALF_BLINKING : STARRY_SHIELD_HALF;
        } else {
            return blinking ? STARRY_SHIELD_FULL_BLINKING : STARRY_SHIELD_FULL;
        }
    }
}
