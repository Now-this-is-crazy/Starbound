package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import powercyphe.starbound.common.registry.SBItems;

public class StarboundDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(SBItemTagProvider::new);
		pack.addProvider(SBBlockTagProvider::new);
		pack.addProvider(SBDamageTypeTagProvider::new);
		pack.addProvider(SBEnchantmentTagProvider::new);
		pack.addProvider(SBRecipeProvider::new);
		pack.addProvider(SBLootTableProvider::new);
		pack.addProvider(SBRegistryProvider::new);
		pack.addProvider(SBAdvancementProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.TRIM_MATERIAL, SBItems.Materials::bootstrap);
	}
}
