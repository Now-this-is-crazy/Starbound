package powercyphe.starbound.common.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.world.feature.StarryCloudFeature;

public class ModFeatures {

    public static RegistryKey<PlacedFeature> STARRY_CLOUD_FEATURE_ID = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Starbound.id("starry_cloud"));
    public static StarryCloudFeature STARRY_CLOUD_FEATURE = register(STARRY_CLOUD_FEATURE_ID.getValue(), new StarryCloudFeature(DefaultFeatureConfig.CODEC));

    public static void init() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd().and((biomeSelectionContext -> biomeSelectionContext.getBiomeKey() == BiomeKeys.SMALL_END_ISLANDS)),
                GenerationStep.Feature.LAKES,
                STARRY_CLOUD_FEATURE_ID
                );
    }

    public static <T extends Feature<?>> T register(Identifier id, T feature) {
        return Registry.register(Registries.FEATURE, id, feature);
    }
}
