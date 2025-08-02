package powercyphe.starbound.mixin.starry_goliath.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryObjectComponent;

import java.util.function.Function;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    private static final Identifier STARRY_SHIELD_FULL = Starbound.id("hud/heart/starry_shield_full");
    @Unique
    private static final Identifier STARRY_SHIELD_FULL_BLINKING = Starbound.id("hud/heart/starry_shield_full_blinking");
    @Unique
    private static final Identifier STARRY_SHIELD_HALF = Starbound.id("hud/heart/starry_shield_half");
    @Unique
    private static final Identifier STARRY_SHIELD_HALF_BLINKING = Starbound.id("hud/heart/starry_shield_half_blinking");

    @Shadow @Nullable protected abstract PlayerEntity getCameraPlayer();

    @WrapOperation(method = "drawHeart", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V"))
    private void starbound$starryShield(DrawContext instance, Function<Identifier, RenderLayer> renderLayers, Identifier sprite, int x, int y, int width, int height, Operation<Void> original, DrawContext context, InGameHud.HeartType type, int x2, int y2, boolean hardcore, boolean blinking, boolean half) {
        PlayerEntity player = this.getCameraPlayer();
        if (type != InGameHud.HeartType.CONTAINER && player != null) {
            StarryObjectComponent component = StarryObjectComponent.get(player);

            if (component.getStarryObjectsAmount(StarryObjectComponent.StarryObject.SHIELD) > 0) {
                original.call(instance, renderLayers, starbound$getHeartTexture(half, blinking), x, y, width, height);
                return;
            }
        }
        original.call(instance, renderLayers, sprite, x, y, width, height);
    }

    @Unique
    private Identifier starbound$getHeartTexture(boolean half, boolean blinking) {
        if (half) {
            return blinking ? STARRY_SHIELD_HALF_BLINKING : STARRY_SHIELD_HALF;
        } else {
            return blinking ? STARRY_SHIELD_FULL_BLINKING : STARRY_SHIELD_FULL;
        }
    }
}
