package powercyphe.starbound.common.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.registry.SBItems;
import powercyphe.starbound.common.registry.SBParticles;

public record StarryTotemUsePayload(int entityId) implements CustomPacketPayload {
    public static final Type<StarryTotemUsePayload> ID = new Type<>(Starbound.id("starry_totem_use"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StarryTotemUsePayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, StarryTotemUsePayload::entityId, StarryTotemUsePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StarryTotemUsePayload> {

        @Override
        public void receive(StarryTotemUsePayload payload, ClientPlayNetworking.Context context) {
            Minecraft client = context.client();
            Player player = context.player();
            Level world = player.level();

            int entityId = payload.entityId();
            Entity entity = world.getEntity(entityId);
            if (entity instanceof LivingEntity livingEntity) {
                client.particleEngine.createTrackingEmitter(entity, SBParticles.STARRY_SMOKE, 30);
                world.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);

                if (livingEntity == player) {
                    client.gameRenderer.displayItemActivation(SBItems.STARRY_TOTEM.getDefaultInstance());
                }
            }
        }
    }
}
