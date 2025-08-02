package powercyphe.starbound.common.registry;

import net.minecraft.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryInvisibilityComponent;
import powercyphe.starbound.common.component.StarryObjectComponent;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<StarryObjectComponent> STARRY_OBJECTS = ComponentRegistry.getOrCreate(Starbound.id("starry_objects"), StarryObjectComponent.class);
    public static final ComponentKey<StarryInvisibilityComponent> STARRY_INVISIBILITY = ComponentRegistry.getOrCreate(Starbound.id("starry_invisibility"), StarryInvisibilityComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerFor(LivingEntity.class, STARRY_OBJECTS, StarryObjectComponent::new);
        entityComponentFactoryRegistry.registerFor(LivingEntity.class, STARRY_INVISIBILITY, StarryInvisibilityComponent::new);
    }
}
