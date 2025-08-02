package powercyphe.starbound.common.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import powercyphe.starbound.common.Starbound;

public class ModEnchantments {

    public static RegistryKey<Enchantment> FLAIL = of("flail");


    public static void init() {}

    private static RegistryKey<Enchantment> of(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Starbound.id(id));
    }
}
