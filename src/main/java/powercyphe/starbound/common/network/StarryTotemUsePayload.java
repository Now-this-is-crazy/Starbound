package powercyphe.starbound.common.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.registry.ModItems;
import powercyphe.starbound.common.registry.ModParticles;

public record StarryTotemUsePayload(int entityId) implements CustomPayload {
    public static final Id<StarryTotemUsePayload> ID = new Id<>(Starbound.id("starry_totem_use"));
    public static final PacketCodec<RegistryByteBuf, StarryTotemUsePayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, StarryTotemUsePayload::entityId, StarryTotemUsePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StarryTotemUsePayload> {

        @Override
        public void receive(StarryTotemUsePayload payload, ClientPlayNetworking.Context context) {
            MinecraftClient client = context.client();
            PlayerEntity player = context.player();
            World world = player.getWorld();

            int entityId = payload.entityId();
            Entity entity = world.getEntityById(entityId);
            if (entity instanceof LivingEntity livingEntity) {
                client.particleManager.addEmitter(entity, ModParticles.STARRY_SMOKE, 30);
                world.playSoundClient(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);

                if (livingEntity == player) {
                    client.gameRenderer.showFloatingItem(ModItems.STARRY_TOTEM.getDefaultStack());
                }
            }
        }
    }
}
