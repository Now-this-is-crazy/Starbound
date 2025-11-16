package powercyphe.starbound.common.item.consume;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import powercyphe.starbound.common.Starbound;

public record SavesFromVoidConsumeEffect() implements ConsumeEffect {
    public static final SavesFromVoidConsumeEffect INSTANCE = new SavesFromVoidConsumeEffect();
    public static final MapCodec<SavesFromVoidConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, SavesFromVoidConsumeEffect> PACKET_CODEC = StreamCodec.unit(INSTANCE);

    public static final Type<SavesFromVoidConsumeEffect> TYPE = Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, Starbound.id("saves_from_void"), new Type<>(CODEC, PACKET_CODEC));


    @Override
    public Type<? extends ConsumeEffect> getType() {
        return TYPE;
    }

    @Override
    public boolean apply(Level world, ItemStack stack, LivingEntity user) {
        if (user.getY() < (double)(world.getMinY() - 64)) {

            user.teleportTo(user.getX(), world.getMinY() - 32, user.getZ());
            user.setDeltaMovement(user.getDeltaMovement().x(), 0, user.getDeltaMovement().z());

            user.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 400, 4));
            user.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 600, 0));
            user.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 45, 0));
        }
        return true;
    }
}
