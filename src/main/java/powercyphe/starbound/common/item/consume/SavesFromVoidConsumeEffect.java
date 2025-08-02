package powercyphe.starbound.common.item.consume;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.*;
import net.minecraft.world.World;
import powercyphe.starbound.common.Starbound;

public record SavesFromVoidConsumeEffect() implements ConsumeEffect {
    public static final SavesFromVoidConsumeEffect INSTANCE = new SavesFromVoidConsumeEffect();
    public static final MapCodec<SavesFromVoidConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
    public static final PacketCodec<RegistryByteBuf, SavesFromVoidConsumeEffect> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    public static final Type<SavesFromVoidConsumeEffect> TYPE = Registry.register(Registries.CONSUME_EFFECT_TYPE, Starbound.id("saves_from_void"), new Type<>(CODEC, PACKET_CODEC));


    @Override
    public Type<? extends ConsumeEffect> getType() {
        return TYPE;
    }

    @Override
    public boolean onConsume(World world, ItemStack stack, LivingEntity user) {
        if (user.getY() < (double)(world.getBottomY() - 64)) {

            user.requestTeleport(user.getX(), world.getBottomY() - 32, user.getZ());
            user.setVelocity(user.getVelocity().getX(), 0, user.getVelocity().getZ());

            user.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 400, 4));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 600, 0));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 45, 0));
        }
        return true;
    }
}
