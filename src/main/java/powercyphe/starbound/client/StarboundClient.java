package powercyphe.starbound.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import powercyphe.starbound.client.event.StarryGelOverlayEvent;
import powercyphe.starbound.client.particle.FancyCritParticle;
import powercyphe.starbound.client.particle.StarrySmokeParticle;
import powercyphe.starbound.client.particle.StarryTrailParticle;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.client.event.StarryEmissivityTooltip;
import powercyphe.starbound.common.network.EmitterParticlePayload;
import powercyphe.starbound.common.network.StarryChargeSoundPayload;
import powercyphe.starbound.common.network.StarryTotemUsePayload;
import powercyphe.starbound.common.registry.ModBlocks;
import powercyphe.starbound.common.registry.ModItems;
import powercyphe.starbound.common.registry.ModParticles;

public class StarboundClient implements ClientModInitializer {

    public static final Identifier STARRY_SPYGLASS_SCOPE = Starbound.id("textures/misc/starry_spyglass_scope.png");
    private static final StarryGelOverlayEvent STARRY_GEL_OVERLAY_EVENT = new StarryGelOverlayEvent();

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), ModBlocks.STARRY_HONEY_BLOCK, ModBlocks.STARRY_GEL_BLOCK, ModBlocks.STARRY_GEL_LAYER);

        ParticleFactoryRegistry.getInstance().register(ModParticles.STARRY_CRIT, FancyCritParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.STARRY_TRAIL, StarryTrailParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.STARRY_SMOKE, StarrySmokeParticle.Factory::new);

        ItemTooltipCallback.EVENT.register(new StarryEmissivityTooltip());
        HudLayerRegistrationCallback.EVENT.register(STARRY_GEL_OVERLAY_EVENT);
        ClientTickEvents.START_CLIENT_TICK.register(STARRY_GEL_OVERLAY_EVENT);

        initNetworking();
    }

    public static void initNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(EmitterParticlePayload.ID, new EmitterParticlePayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(StarryChargeSoundPayload.ID, new StarryChargeSoundPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(StarryTotemUsePayload.ID, new StarryTotemUsePayload.Receiver());
    }

    public static boolean shouldApplyStarrySpyglassEffects() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return false;
        }
        return client.options.getPerspective().isFirstPerson() && player.isUsingSpyglass() && player.getActiveItem().isOf(ModItems.STARRY_SPYGLASS);
    }
}
