package powercyphe.starbound.client.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import powercyphe.starbound.common.component.StarryObjectComponent;

import java.util.UUID;

public interface EntityRenderStateAddon {

    UUID starbound$getUUID();
    void starbound$setUUID(UUID uuid);

    ItemStack starbound$getActiveStack();
    void starbound$setActiveStack(ItemStack stack);

    float starbound$getStarryInvisibilityStrength();
    void starbound$setStarryInvisibilityStrength(float invisibilityStrength);

    DefaultedList<Pair<StarryObjectComponent.StarryObject, Integer>> starbound$getStarryObjects();
    void starbound$setStarryObjects(DefaultedList<Pair<StarryObjectComponent.StarryObject, Integer>> starryObjects);

    float starbound$getStarryObjectBaseRotation();
    void starbound$setStarryObjectBaseRotation(float baseRotation);

    float starbound$getStarryObjectFloatRotation();
    void starbound$setStarryObjectFloatRotation(float floatRotation);
}
