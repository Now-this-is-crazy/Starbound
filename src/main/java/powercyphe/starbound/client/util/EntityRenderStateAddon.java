package powercyphe.starbound.client.util;

import powercyphe.starbound.common.component.StarryObjectComponent;

import java.util.UUID;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;

public interface EntityRenderStateAddon {

    ItemStack starbound$getActiveStack();
    void starbound$setActiveStack(ItemStack stack);

    float starbound$getStarryInvisibilityStrength();
    void starbound$setStarryInvisibilityStrength(float invisibilityStrength);

    NonNullList<Tuple<StarryObjectComponent.StarryObject, Integer>> starbound$getStarryObjects();
    void starbound$setStarryObjects(NonNullList<Tuple<StarryObjectComponent.StarryObject, Integer>> starryObjects);

    float starbound$getStarryObjectBaseRotation();
    void starbound$setStarryObjectBaseRotation(float baseRotation);

    float starbound$getStarryObjectFloatRotation();
    void starbound$setStarryObjectFloatRotation(float floatRotation);
}
