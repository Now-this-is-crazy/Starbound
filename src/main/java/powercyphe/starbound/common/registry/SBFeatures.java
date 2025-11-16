package powercyphe.starbound.common.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.world.feature.StarryCloudFeature;

public class SBFeatures {

    public static ResourceKey<PlacedFeature> STARRY_CLOUD_FEATURE_ID = ResourceKey.create(Registries.PLACED_FEATURE, Starbound.id("starry_cloud"));
    public static StarryCloudFeature STARRY_CLOUD_FEATURE = register(STARRY_CLOUD_FEATURE_ID.location(), new StarryCloudFeature(NoneFeatureConfiguration.CODEC));

    public static void init() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd().and((biomeSelectionContext -> biomeSelectionContext.getBiomeKey() == Biomes.SMALL_END_ISLANDS)),
                GenerationStep.Decoration.LAKES,
                STARRY_CLOUD_FEATURE_ID
                );
    }

    public static <T extends Feature<?>> T register(ResourceLocation id, T feature) {
        return Registry.register(BuiltInRegistries.FEATURE, id, feature);
    }
}
