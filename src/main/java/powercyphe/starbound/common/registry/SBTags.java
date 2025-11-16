package powercyphe.starbound.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import powercyphe.starbound.common.Starbound;

public class SBTags {

    public static class Items {
        public static TagKey<Item> ENCHANTABLE_STARRY_GOLIATH = tagKey(Registries.ITEM, "enchantable/starry_goliath");

        public static TagKey<Item> ALWAYS_HAS_STARRY_EMISSIVITY = tagKey(Registries.ITEM, "always_has_starry_emissivity");
        public static TagKey<Item> CANNOT_HAVE_STARRY_EMISSIVITY = tagKey(Registries.ITEM, "cannot_have_starry_emissivity");
    }

    public static class Blocks {
        public static TagKey<Block> STARSTONE_CONVERTABLE = tagKey(Registries.BLOCK, "starstone_convertable");
    }

    public static class Enchantments {
        public static TagKey<Enchantment> EXCLUSIVE_SET_STARRY_GOLIATH = tagKey(Registries.ENCHANTMENT, "exclusive_set/starry_goliath");
    }

    public static class DamageTypes {
        public static TagKey<DamageType> BYPASSES_STARRY_SHIELD = tagKey(Registries.DAMAGE_TYPE, "bypasses_starry_shield");
    }

    public static <T> TagKey<T> tagKey(ResourceKey<? extends Registry<T>> registry, String key) {
        return TagKey.create(registry, Starbound.id(key));
    }
}
