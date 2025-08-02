package powercyphe.starbound.mixin.starry_goliath;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.common.network.EmitterParticlePayload;
import powercyphe.starbound.common.registry.ModItems;
import powercyphe.starbound.common.registry.ModParticles;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @WrapWithCondition(method = "addCritParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;sendToNearbyPlayers(Lnet/minecraft/entity/Entity;Lnet/minecraft/network/packet/Packet;)V", ordinal = 0))
    private boolean starbound$starryGoliathHit(ServerChunkManager instance, Entity entity, Packet<?> packet, Entity target) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (player.getMainHandStack().isOf(ModItems.STARRY_GOLIATH)) {
            for (ServerPlayerEntity otherPlayer : ((ServerWorld) player.getWorld()).getPlayers()) {
                if (player.getPos().distanceTo(otherPlayer.getPos()) <= 50) {
                    ServerPlayNetworking.send(otherPlayer, new EmitterParticlePayload(target.getId(), ModParticles.STARRY_CRIT));
                }
            }
            return false;
        }
        return true;
    }
}
