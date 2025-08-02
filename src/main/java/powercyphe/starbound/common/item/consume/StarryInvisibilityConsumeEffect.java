package powercyphe.starbound.common.item.consume;

import com.mojang.serialization.MapCodec;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ClearAllEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;

import java.util.List;

public record StarryInvisibilityConsumeEffect() implements ConsumeEffect {
    public static DeathProtectionComponent STARRY_TOTEM = new DeathProtectionComponent(List.of(
            new ClearAllEffectsConsumeEffect(), new StarryInvisibilityConsumeEffect(), new SavesFromVoidConsumeEffect(),
            new ApplyEffectsConsumeEffect(List.of(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 1), new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1))
    )));

    public static final StarryInvisibilityConsumeEffect INSTANCE = new StarryInvisibilityConsumeEffect();
    public static final MapCodec<StarryInvisibilityConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
    public static final PacketCodec<RegistryByteBuf, StarryInvisibilityConsumeEffect> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    public static final Type<StarryInvisibilityConsumeEffect> TYPE = Registry.register(Registries.CONSUME_EFFECT_TYPE, Starbound.id("starry_invisibility_effect"), new Type<>(CODEC, PACKET_CODEC));

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return TYPE;
    }

    @Override
    public boolean onConsume(World world, ItemStack stack, LivingEntity user) {
        if (world instanceof ServerWorld serverWorld) {
            Vec3d basePos = user.getPos().add(user.getHeight() / 2);
            double effectDistance = (user.getWidth() + 7 + user.getHeight() + 5) / 2;

            List<Entity> entities = serverWorld.getOtherEntities(null, Box.of(basePos, effectDistance, effectDistance, effectDistance),
                    EntityPredicates.VALID_LIVING_ENTITY.and(EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR));

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    StarryInvisibilityComponent component = StarryInvisibilityComponent.get(livingEntity);
                    double distance = livingEntity.getPos().add(livingEntity.getHeight() / 2).distanceTo(basePos);

                    component.add((float) (1 - Math.clamp(distance / effectDistance, 0, 1)));
                }
            }
        }
        return true;
    }

    public static boolean hasEffect(ItemStack stack) {
        DeathProtectionComponent component = stack.getOrDefault(DataComponentTypes.DEATH_PROTECTION, null);
        if (component != null) {
            for (ConsumeEffect effect : component.deathEffects()) {
                if (effect instanceof StarryInvisibilityConsumeEffect) {
                    return true;
                }
            }
        }

        return false;
    }
}
