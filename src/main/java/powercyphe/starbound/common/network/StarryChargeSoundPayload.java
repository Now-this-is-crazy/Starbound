package powercyphe.starbound.common.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import powercyphe.starbound.client.sound.StarryChargeSoundInstance;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.item.StarryGoliathItem;

public record StarryChargeSoundPayload(int entityId, ItemStack stack) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StarryChargeSoundPayload> ID = new CustomPacketPayload.Type<>(Starbound.id("starry_charge_sound"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StarryChargeSoundPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, StarryChargeSoundPayload::entityId, ItemStack.STREAM_CODEC, StarryChargeSoundPayload::stack, StarryChargeSoundPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<StarryChargeSoundPayload> {

        @Override
        public void receive(StarryChargeSoundPayload payload, ClientPlayNetworking.Context context) {
            Minecraft client = Minecraft.getInstance();

            if (client != null && client.level != null) {
                ClientLevel world = client.level;
                Entity entity = world.getEntity(payload.entityId);

                if (entity instanceof LivingEntity livingEntity) {
                    client.getSoundManager().play(new StarryChargeSoundInstance(livingEntity, payload.stack, 1F, 1F, StarryGoliathItem.getStarryObject(payload.stack, world.registryAccess()).getReplenishTime()));
                }
            }
        }
    }
}
