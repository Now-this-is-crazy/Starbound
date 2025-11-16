package powercyphe.starbound.mixin.starry_goliath;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.starbound.common.network.EmitterParticlePayload;
import powercyphe.starbound.common.registry.SBItems;
import powercyphe.starbound.common.registry.SBParticles;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @WrapWithCondition(method = "crit", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerChunkCache;broadcastAndSend(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/protocol/Packet;)V", ordinal = 0))
    private boolean starbound$starryGoliathHit(ServerChunkCache instance, Entity entity, Packet<?> packet, Entity target) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (player.getMainHandItem().is(SBItems.STARRY_GOLIATH)) {
            for (ServerPlayer otherPlayer : ((ServerLevel) player.level()).players()) {
                if (player.position().distanceTo(otherPlayer.position()) <= 50) {
                    ServerPlayNetworking.send(otherPlayer, new EmitterParticlePayload(target.getId(), SBParticles.STARRY_CRIT));
                }
            }
            return false;
        }
        return true;
    }
}
