package powercyphe.starbound.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.common.registry.ModBlocks;
import powercyphe.starbound.common.util.StarboundUtil;

import java.util.function.Function;

public class StarryGelOverlayEvent implements HudLayerRegistrationCallback, ClientTickEvents.StartTick {
    public static final Identifier STARRY_OVERLAY_TEXTURE = Starbound.id("textures/misc/starry_overlay.png");
    public static final Identifier STARRY_VIGNETTE_TEXTURE = Starbound.id("textures/misc/starry_vignette.png");

    public static float INTERPOLATION_TIME = 0.1F;
    public float frameInterpolation = 0;

    public int frame = 1;
    public int nextFrame = this.frame + 1;

    public float alpha = 0;

    @Override
    public void register(LayeredDrawerWrapper layeredDrawerWrapper) {
        layeredDrawerWrapper.attachLayerAfter(IdentifiedLayer.MISC_OVERLAYS, Starbound.id("starry_gel_overlay"), ((context, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            Entity entity = client.getCameraEntity();

            int width = context.getScaledWindowWidth();
            int height = context.getScaledWindowHeight();

            // Effect Overlay
            if (entity instanceof LivingEntity livingEntity) {
                StarryInvisibilityComponent component = StarryInvisibilityComponent.get(livingEntity);
                float strength = component.invisibilityStrength;
                float interpolation = Math.clamp(this.frameInterpolation + (INTERPOLATION_TIME * tickCounter.getTickProgress(false)), 0, 1);

                context.drawTexture(RenderLayer::getGuiTexturedOverlay, STARRY_OVERLAY_TEXTURE, 0, 0, 0F, this.frame * 256F, width, height, 256, 256, 256, 1024,
                        ColorHelper.withAlpha((int) ((strength * (1 - interpolation)) * 255F), -1));
                context.drawTexture(RenderLayer::getGuiTexturedOverlay, STARRY_OVERLAY_TEXTURE, 0, 0, 0F, this.nextFrame * 256F, width, height, 256, 256, 256, 1024,
                        ColorHelper.withAlpha((int) ((strength * interpolation) * 255F), -1));

                context.getMatrices().push();
                float f = MathHelper.lerp(strength, 1.5F, 1.25F);
                context.getMatrices().translate((float)width / 1.5F, (float)height / 1.5F, 0.0F);
                context.getMatrices().scale(f, f, f);
                context.getMatrices().translate((float)(-width) / 1.5F, (float)(-height) / 1.5F, 0.0F);
                float g = 44F / 255F * strength;
                float h = 55F / 255F * strength;
                float k = 79F / 255F * strength;
                context.drawTexture((identifier) ->
                    RenderLayer.getGuiNauseaOverlay(),
                        STARRY_VIGNETTE_TEXTURE, 0, 0, 0.0F, 0.0F, width, height, width, height, ColorHelper.fromFloats(1.0F, g, h, k));
                context.getMatrices().pop();
            }

            // Block Overlay
            this.alpha = MathHelper.clamp(StarboundUtil.isHeadInStarryGel(entity) ? this.alpha + 0.005F : this.alpha - 0.005F, 0F, 0.2F);

            if (this.alpha > 0) {
                int size = Math.max(height, width);
                Sprite sprite = client.getBlockRenderManager().getModels().getModelParticleSprite(ModBlocks.STARRY_GEL_BLOCK.getDefaultState());
                context.drawSpriteStretched(RenderLayer::getBlockScreenEffect, sprite, 0, 0, size, size, ColorHelper.getArgb((int) (this.alpha * 255), 255, 255, 255));
            }


        }));
    }

    @Override
    public void onStartTick(MinecraftClient client) {
        this.frameInterpolation += INTERPOLATION_TIME;
        if (this.frameInterpolation == 1) {
            this.frameInterpolation = 0;

            this.frame++;
            if (this.frame > 4) {
                this.frame = 1;
            }
            this.nextFrame++;
            if (this.nextFrame > 4) {
                this.nextFrame = 1;
            }
        }
    }
}
