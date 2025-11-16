package powercyphe.starbound.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import powercyphe.starbound.common.advancement.SBCriteria;
import powercyphe.starbound.common.network.EmitterParticlePayload;
import powercyphe.starbound.common.network.StarryChargeSoundPayload;
import powercyphe.starbound.common.network.StarryTotemUsePayload;
import powercyphe.starbound.common.registry.*;

public class Starbound implements ModInitializer {
	public static final String MOD_ID = "starbound";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SBItems.init();
		SBItems.Components.init();
		SBBlocks.init();
		SBRecipeSerializers.init();
		SBEnchantments.init();
		SBDamageTypes.init();
		SBSounds.init();
		SBParticles.init();
		SBFeatures.init();
		SBCriteria.init();

		initNetworking();

	}

	public static void initNetworking() {
		PayloadTypeRegistry.playS2C().register(EmitterParticlePayload.ID, EmitterParticlePayload.CODEC);
		PayloadTypeRegistry.playS2C().register(StarryChargeSoundPayload.ID, StarryChargeSoundPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(StarryTotemUsePayload.ID, StarryTotemUsePayload.CODEC);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

}