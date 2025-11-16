package powercyphe.starbound.client.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.common.registry.SBBlocks;
import powercyphe.starbound.common.util.StarboundUtil;

public class StarryGelOverlayEvent implements HudLayerRegistrationCallback, ClientTickEvents.StartTick {
    public static final ResourceLocation STARRY_OVERLAY_TEXTURE = Starbound.id("textures/misc/starry_overlay.png");
    public static final ResourceLocation STARRY_VIGNETTE_TEXTURE = Starbound.id("textures/misc/starry_vignette.png");

    public static float INTERPOLATION_TIME = 0.05F;
    public float frameInterpolation = 0;

    public int frame = 1;
    public int nextFrame = this.frame + 1;

    public float alpha = 0;

    @Override
    public void register(LayeredDrawerWrapper layeredDrawerWrapper) {
        layeredDrawerWrapper.attachLayerAfter(IdentifiedLayer.MISC_OVERLAYS, Starbound.id("starry_gel_overlay"), ((context, tickCounter) -> {
            Minecraft client = Minecraft.getInstance();
            Entity entity = client.getCameraEntity();

            int width = context.guiWidth();
            int height = context.guiHeight();

            // Effect Overlay
            if (entity instanceof LivingEntity livingEntity) {
                StarryInvisibilityComponent component = StarryInvisibilityComponent.get(livingEntity);
                float strength = component.invisibilityStrength;
                float interpolation = Mth.clamp(this.frameInterpolation + (INTERPOLATION_TIME * tickCounter.getGameTimeDeltaPartialTick(false)), 0, 1);

                // Vignette
                context.pose().pushPose();
                float f = Mth.lerp(strength, 1.5F, 1.25F);
                context.pose().translate((float)width / 1.5F, (float)height / 1.5F, 0.0F);
                context.pose().scale(f, f, f);
                context.pose().translate((float)(-width) / 1.5F, (float)(-height) / 1.5F, 0.0F);
                float g = 44F / 255F * strength;
                float h = 55F / 255F * strength;
                float k = 79F / 255F * strength;
                context.blit((identifier) ->
                                RenderType.guiNauseaOverlay(),
                        STARRY_VIGNETTE_TEXTURE, 0, 0, 0.0F, 0.0F, width, height, width, height, ARGB.colorFromFloat(1.0F, g, h, k));
                context.pose().popPose();

                // Stars
                context.blit(RenderType::guiTexturedOverlay, STARRY_OVERLAY_TEXTURE, 0, 0, 0F, this.frame * 256F, width, height, 256, 256, 256, 1024,
                        ARGB.color((int) ((strength * (1 - interpolation)) * 255F), 0xDDDDDD));
                context.blit(RenderType::guiTexturedOverlay, STARRY_OVERLAY_TEXTURE, 0, 0, 0F, this.nextFrame * 256F, width, height, 256, 256, 256, 1024,
                        ARGB.color((int) ((strength * interpolation) * 255F), 0xDDDDDD));
            }

            // Block Overlay
            this.alpha = Mth.clamp(StarboundUtil.isHeadInStarryGel(entity) ? this.alpha + 0.005F : this.alpha - 0.005F, 0F, 0.2F);

            if (this.alpha > 0) {
                int size = Math.max(height, width);
                TextureAtlasSprite sprite = client.getBlockRenderer().getBlockModelShaper().getParticleIcon(SBBlocks.STARRY_GEL_BLOCK.defaultBlockState());
                context.blitSprite(RenderType::blockScreenEffect, sprite, 0, 0, size, size, ARGB.color((int) (this.alpha * 255), 255, 255, 255));
            }


        }));
    }

    @Override
    public void onStartTick(Minecraft client) {
        this.frameInterpolation += INTERPOLATION_TIME;
        if (this.frameInterpolation >= 1) {
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
