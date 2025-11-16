package powercyphe.starbound.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

public class DamageBlockedByStarryGoliathCriterion extends SimpleCriterionTrigger<DamageBlockedByStarryGoliathCriterion.Conditions> {
    public DamageBlockedByStarryGoliathCriterion() {}

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayer player, float blockedDamage) {
        this.trigger(player, (conditions) -> {
            return blockedDamage >= conditions.blockedDamage().orElse(0F);
        });
    }

    public record Conditions(Optional<ContextAwarePredicate> player, Optional<Float> blockedDamage) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<DamageBlockedByStarryGoliathCriterion.Conditions> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(DamageBlockedByStarryGoliathCriterion.Conditions::player), ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("blockedDamage").forGetter(DamageBlockedByStarryGoliathCriterion.Conditions::blockedDamage)).apply(instance, DamageBlockedByStarryGoliathCriterion.Conditions::new);
        });

        public static Criterion<DamageBlockedByStarryGoliathCriterion.Conditions> create(EntityPredicate.Builder player, float blockedDamage) {
            return SBCriteria.DAMAGE_BLOCKED_WITH_STARRY_GOLIATH.createCriterion(new DamageBlockedByStarryGoliathCriterion.Conditions(Optional.of(EntityPredicate.wrap(player)), Optional.of(blockedDamage)));
        }

    }
}
