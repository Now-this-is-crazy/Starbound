package powercyphe.starbound.common.item.consume;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.EntityGetter;
import org.jetbrains.annotations.NotNull;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;

import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record StarryInvisibilityConsumeEffect() implements ConsumeEffect {
    public static DeathProtection STARRY_TOTEM = new DeathProtection(List.of(
            new ClearAllStatusEffectsConsumeEffect(), new StarryInvisibilityConsumeEffect(), new SavesFromVoidConsumeEffect(),
            new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.REGENERATION, 300, 1), new MobEffectInstance(MobEffects.ABSORPTION, 100, 1))
    )));

    public static final StarryInvisibilityConsumeEffect INSTANCE = new StarryInvisibilityConsumeEffect();
    public static final MapCodec<StarryInvisibilityConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, StarryInvisibilityConsumeEffect> PACKET_CODEC = StreamCodec.unit(INSTANCE);

    public static final Type<StarryInvisibilityConsumeEffect> TYPE = Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, Starbound.id("starry_invisibility_effect"), new Type<>(CODEC, PACKET_CODEC));

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return TYPE;
    }

    @Override
    public boolean apply(Level world, ItemStack stack, LivingEntity user) {
        if (world instanceof ServerLevel serverWorld) {
            Vec3 basePos = user.position().add(user.getBbHeight() / 2);
            double effectDistance = (user.getBbWidth() + 7 + user.getBbHeight() + 5) / 2;

            List<Entity> entities = serverWorld.getEntities((Entity) null, AABB.ofSize(basePos, effectDistance, effectDistance, effectDistance),
                    EntitySelector.LIVING_ENTITY_STILL_ALIVE.and(EntitySelector.NO_CREATIVE_OR_SPECTATOR));

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    StarryInvisibilityComponent component = StarryInvisibilityComponent.get(livingEntity);
                    double distance = livingEntity.position().add(livingEntity.getBbHeight() / 2).distanceTo(basePos);

                    component.add((float) (1 - Math.clamp(distance / effectDistance, 0, 1)));
                }
            }
        }
        return true;
    }

    public static boolean hasEffect(ItemStack stack) {
        DeathProtection component = stack.get(DataComponents.DEATH_PROTECTION);
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
