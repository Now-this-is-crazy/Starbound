package powercyphe.starbound.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import powercyphe.starbound.common.component.StarryObjectComponent;
import powercyphe.starbound.client.util.EntityRenderStateAddon;

import java.util.UUID;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateAddon {

    @Unique
    private ItemStack activeStack = ItemStack.EMPTY;

    @Unique
    private float starryInvisibilityStrength = 0F;

    @Unique
    private NonNullList<Tuple<StarryObjectComponent.StarryObject, Integer>> starryObjects = NonNullList.create();

    @Unique
    private float starryObjectBaseRotation = 0F;

    @Unique
    private float starryObjectFloatRotation = 0F;

    @Override
    public ItemStack starbound$getActiveStack() {
        return this.activeStack;
    }

    @Override
    public void starbound$setActiveStack(ItemStack stack) {
        this.activeStack = stack;
    }

    @Override
    public float starbound$getStarryInvisibilityStrength() {
        return this.starryInvisibilityStrength;
    }

    @Override
    public void starbound$setStarryInvisibilityStrength(float invisibilityStrength) {
        this.starryInvisibilityStrength = invisibilityStrength;
    }

    @Override
    public NonNullList<Tuple<StarryObjectComponent.StarryObject, Integer>> starbound$getStarryObjects() {
        return this.starryObjects;
    }

    @Override
    public void starbound$setStarryObjects(NonNullList<Tuple<StarryObjectComponent.StarryObject, Integer>> starryObjects) {
        this.starryObjects = starryObjects;
    }

    @Override
    public float starbound$getStarryObjectBaseRotation() {
        return this.starryObjectBaseRotation;
    }

    @Override
    public void starbound$setStarryObjectBaseRotation(float baseRotation) {
        this.starryObjectBaseRotation = baseRotation;
    }

    @Override
    public float starbound$getStarryObjectFloatRotation() {
        return this.starryObjectFloatRotation;
    }

    @Override
    public void starbound$setStarryObjectFloatRotation(float floatRotation) {
        this.starryObjectFloatRotation = floatRotation;
    }
}
