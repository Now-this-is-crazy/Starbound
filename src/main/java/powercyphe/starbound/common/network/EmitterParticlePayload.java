package powercyphe.starbound.common.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import powercyphe.starbound.common.Starbound;

public record EmitterParticlePayload(int entityId, ParticleOptions particleEffect) implements CustomPacketPayload {
    public static final Type<EmitterParticlePayload> ID = new Type<>(Starbound.id("emitter_particle"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EmitterParticlePayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, EmitterParticlePayload::entityId, ParticleTypes.STREAM_CODEC, EmitterParticlePayload::particleEffect, EmitterParticlePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<EmitterParticlePayload> {
        @Override
        public void receive(EmitterParticlePayload candyCritPayload, ClientPlayNetworking.Context context) {
            Minecraft client = Minecraft.getInstance();

            if (client != null && client.level != null) {
                Entity entity = client.level.getEntity(candyCritPayload.entityId);
                if (entity != null) {
                    client.particleEngine.createTrackingEmitter(entity, candyCritPayload.particleEffect);
                }
            }
        }
    }
}
