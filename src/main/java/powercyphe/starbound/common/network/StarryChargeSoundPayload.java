package powercyphe.starbound.common.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import powercyphe.starbound.client.sound.StarryChargeSoundInstance;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.item.StarryGoliathItem;

public record StarryChargeSoundPayload(int entityId, ItemStack stack) implements CustomPayload {
    public static final CustomPayload.Id<StarryChargeSoundPayload> ID = new CustomPayload.Id<>(Starbound.id("starry_charge_sound"));
    public static final PacketCodec<RegistryByteBuf, StarryChargeSoundPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, StarryChargeSoundPayload::entityId, ItemStack.PACKET_CODEC, StarryChargeSoundPayload::stack, StarryChargeSoundPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StarryChargeSoundPayload> {

        @Override
        public void receive(StarryChargeSoundPayload payload, ClientPlayNetworking.Context context) {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client != null && client.world != null) {
                ClientWorld world = client.world;
                Entity entity = world.getEntityById(payload.entityId);

                if (entity instanceof LivingEntity livingEntity) {
                    client.getSoundManager().play(new StarryChargeSoundInstance(livingEntity, payload.stack, 1F, 1F, StarryGoliathItem.getStarryObject(payload.stack, world.getRegistryManager()).getReplenishTime()));
                }
            }
        }
    }
}
