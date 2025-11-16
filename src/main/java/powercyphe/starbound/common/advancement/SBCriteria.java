package powercyphe.starbound.common.advancement;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import powercyphe.starbound.common.Starbound;

public class SBCriteria {

    public static final DamageBlockedByStarryGoliathCriterion DAMAGE_BLOCKED_WITH_STARRY_GOLIATH = register("damage_blocked_with_starry_goliath", new DamageBlockedByStarryGoliathCriterion());

    public static void init() {}

    public static <T extends CriterionTrigger<?>> T register(String id, T criterion) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Starbound.id(id), criterion);
    }
}
