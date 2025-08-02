package powercyphe.starbound.mixin.client;

import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import powercyphe.starbound.common.component.StarryObjectComponent;
import powercyphe.starbound.client.util.EntityRenderStateAddon;

import java.util.UUID;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateAddon {

    @Unique
    private UUID uuid = null;

    @Unique
    private ItemStack activeStack = ItemStack.EMPTY;

    @Unique
    private float starryInvisibilityStrength = 0F;

    @Unique
    private DefaultedList<Pair<StarryObjectComponent.StarryObject, Integer>> starryObjects = DefaultedList.of();

    @Unique
    private float starryObjectBaseRotation = 0F;

    @Unique
    private float starryObjectFloatRotation = 0F;

    @Override
    public UUID starbound$getUUID() {
        return this.uuid;
    }

    @Override
    public void starbound$setUUID(UUID uuid) {
        this.uuid = uuid;
    }

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
    public DefaultedList<Pair<StarryObjectComponent.StarryObject, Integer>> starbound$getStarryObjects() {
        return this.starryObjects;
    }

    @Override
    public void starbound$setStarryObjects(DefaultedList<Pair<StarryObjectComponent.StarryObject, Integer>> starryObjects) {
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
