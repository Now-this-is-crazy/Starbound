package powercyphe.starbound.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import powercyphe.starbound.common.Starbound;

public class SBEnchantments {

    public static ResourceKey<Enchantment> FLAIL = of("flail");


    public static void init() {}

    private static ResourceKey<Enchantment> of(String id) {
        return ResourceKey.create(Registries.ENCHANTMENT, Starbound.id(id));
    }
}
