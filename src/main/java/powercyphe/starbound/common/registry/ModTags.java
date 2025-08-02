package powercyphe.starbound.common.registry;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import powercyphe.starbound.common.Starbound;

public class ModTags {

    public static class Items {
        public static TagKey<Item> ENCHANTABLE_STARRY_GOLIATH = tagKey(RegistryKeys.ITEM, "enchantable/starry_goliath");

        public static TagKey<Item> ALWAYS_HAS_STARRY_EMISSIVITY = tagKey(RegistryKeys.ITEM, "always_has_starry_emissivity");
        public static TagKey<Item> CANNOT_HAVE_STARRY_EMISSIVITY = tagKey(RegistryKeys.ITEM, "cannot_have_starry_emissivity");
    }

    public static class Blocks {
        public static TagKey<Block> STARSTONE_CONVERTABLE = tagKey(RegistryKeys.BLOCK, "starstone_convertable");
    }

    public static class Enchantments {
        public static TagKey<Enchantment> EXCLUSIVE_SET_STARRY_GOLIATH = tagKey(RegistryKeys.ENCHANTMENT, "exclusive_set/starry_goliath");
    }

    public static class DamageTypes {
        public static TagKey<DamageType> BYPASSES_STARRY_SHIELD = tagKey(RegistryKeys.DAMAGE_TYPE, "bypasses_starry_shield");
    }

    public static <T> TagKey<T> tagKey(RegistryKey<? extends Registry<T>> registry, String key) {
        return TagKey.of(registry, Starbound.id(key));
    }
}
