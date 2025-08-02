package powercyphe.starbound.common.component;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import powercyphe.starbound.common.registry.*;
import powercyphe.starbound.common.util.StarboundUtil;

import java.util.List;
import java.util.Optional;

public class StarryObjectComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final String STARRY_OBJECTS_KEY = "starryObjects";
    public static final String STARRY_OBJECT_KEY = "starryObject";
    public static final String STARRY_OBJECT_VARIANT_KEY = "starryObjectVariant";

    public static final int STARRY_OBJECTS_MAX = 7;
    private final DefaultedList<Pair<StarryObject, Integer>> starryObjects;

    private static final String BREAK_COOLDOWN_KEY = "breakCooldown";
    private int breakCooldown = 0;

    public static final String BASE_ROTATION_KEY = "baseRotation";
    public static float bRotIncrease = 6F;
    public float baseRotation = 0;

    public static final String FLOAT_ROTATION_KEY = "floatRotation";
    public static float fRotIncrease = 9F;
    public float floatRotation = 0;

    public LivingEntity obj;

    public StarryObjectComponent(LivingEntity entity) {
        this.obj = entity;
        this.starryObjects = DefaultedList.of();
    }

    public static StarryObjectComponent get(LivingEntity entity) {
        return ModComponents.STARRY_OBJECTS.get(entity);
    }

    public void sync() {
        ModComponents.STARRY_OBJECTS.sync(this.obj);
    }

    // Nbt

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.starryObjects.clear();

        NbtList nbtList = (NbtList) nbt.get(STARRY_OBJECTS_KEY);
        if (nbtList != null) {
            for (NbtElement nbtElement : nbtList) {

                if (nbtElement instanceof NbtCompound nbtCompound) {
                    Optional<String> objectKey = nbtCompound.getString(STARRY_OBJECT_KEY);
                    Optional<Integer> objectVariant = nbtCompound.getInt(STARRY_OBJECT_VARIANT_KEY);

                    if (objectKey.isPresent() && objectVariant.isPresent()) {
                        StarryObject object = StarryObject.fromId(objectKey.get());
                        if (object != null) {
                            int variant = MathHelper.clamp(objectVariant.get(), 1, object.getVariants());
                            this.starryObjects.add(new Pair<>(object, variant));
                        }
                    }
                }
            }
        }

        this.breakCooldown = nbt.getInt(BREAK_COOLDOWN_KEY, 0);

        this.baseRotation = nbt.getFloat(BASE_ROTATION_KEY, 0);
        this.floatRotation = nbt.getFloat(FLOAT_ROTATION_KEY, 0);
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        NbtList nbtList = new NbtList();
        for (Pair<StarryObject, Integer> pair : this.getStarryObjects()) {
            StarryObject object = pair.getLeft();
            int variant = pair.getRight();

            NbtCompound objectNbt = new NbtCompound();
            objectNbt.putString(STARRY_OBJECT_KEY, object.getId());
            objectNbt.putInt(STARRY_OBJECT_VARIANT_KEY, variant);

            nbtList.add(objectNbt);
        }
        nbt.put(STARRY_OBJECTS_KEY, nbtList);
        nbt.putInt(BREAK_COOLDOWN_KEY, this.breakCooldown);

        nbt.putFloat(BASE_ROTATION_KEY, this.baseRotation);
        nbt.putFloat(FLOAT_ROTATION_KEY, this.floatRotation);
    }

    // Ticking

    @Override
    public void tick() {
        if (this.breakCooldown > 0) {
            this.breakCooldown--;
        }

        this.baseRotation += bRotIncrease + ((float) this.getStarryObjectsAmount() / STARRY_OBJECTS_MAX * 6F);
        if (this.baseRotation > 360) {
            this.baseRotation -= 360F;
        }

        this.floatRotation += fRotIncrease + ((float) this.getStarryObjectsAmount() / STARRY_OBJECTS_MAX * 9F);
        if (this.floatRotation > 360) {
            this.floatRotation -= 360F;
        }

        this.tickShards();
    }

    public void tickShards() {
        if (this.canDealShardDamage() && this.obj.getWorld() instanceof ServerWorld serverWorld) {
            DamageSource source = ModDamageTypes.create(this.obj.getWorld(), ModDamageTypes.STARRY_SHARD, this.obj, this.obj);
            for (int index = 0; index < this.getStarryObjectsAmount(); index++) {
                StarryObject object = this.getStarryObjects().get(index).getLeft();
                if (object == StarryObjectComponent.StarryObject.SHARD) {
                    Vec3d objectPos = StarboundUtil.objectPos(this.baseRotation, this.floatRotation, this.obj, index, this.getStarryObjectsAmount());
                    Box box = Box.of(objectPos, 0.35, 0.35, 0.35);

                    List<Entity> entities = this.obj.getWorld().getOtherEntities(this.obj, box, EntityPredicates.VALID_LIVING_ENTITY.and(EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR));
                    if (!entities.isEmpty()) {
                        Entity entity = entities.getFirst();

                        if (!((LivingEntity) entity).isInvulnerableTo(serverWorld, source)) {
                            entity.damage(serverWorld, source, 2);
                            this.removeStarryObject(index);
                            break;
                        }
                    }
                }
            }
        }
    }

    // Object Manipulation

    public DefaultedList<Pair<StarryObject, Integer>> getStarryObjects(StarryObject object) {
        DefaultedList<Pair<StarryObject, Integer>> list = DefaultedList.of();
        list.addAll(this.getStarryObjects());

        list.removeIf(o -> !o.getLeft().equals(object));
        return list;
    }

    public DefaultedList<Pair<StarryObject, Integer>> getStarryObjects() {
        return this.starryObjects;
    }

    public int getStarryObjectsAmount(StarryObject object) {
        return this.getStarryObjects(object).size();
    }

    public int getStarryObjectsAmount() {
        return getStarryObjects().size();
    }

    public void addStarryObject(StarryObject object) {
        if (canAddStarryObject(object)) {
            this.starryObjects.add(new Pair<>(object, Random.create().nextBetween(1, object.getVariants())));

            if (this.obj.getWorld() instanceof ServerWorld serverWorld) {
                Vec3d vec3d = StarboundUtil.objectPos(baseRotation, floatRotation, this.obj, this.getStarryObjectsAmount()-1, this.getStarryObjectsAmount());
                serverWorld.spawnParticles(ModParticles.STARRY_CRIT, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 7, 0, 0, 0, 1);
            }
            this.playSound(object.getCreateSound());
            this.sync();
        }
    }

    public void removeStarryObject(StarryObject object) {
        int index = 0;
        for (Pair<StarryObject, Integer> pair : this.getStarryObjects()) {
            if (pair.getLeft() == object) {
                break;
            }
            index++;
        }
        this.removeStarryObject(index);
    }

    public void removeStarryObject(int index) {
        if (getStarryObjectsAmount() > 0 && this.getStarryObjectsAmount() > index) {
            StarryObject object = this.getStarryObjects().get(index).getLeft();
            this.starryObjects.remove(index);
            this.breakCooldown = object.getBreakCooldown();

            if (this.obj.getWorld() instanceof ServerWorld serverWorld) {
                Vec3d vec3d = StarboundUtil.objectPos(baseRotation, floatRotation, this.obj, index, this.getStarryObjectsAmount()+1);
                serverWorld.spawnParticles(ModParticles.STARRY_CRIT, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 7, 0, 0, 0, 1);
            }
            this.playSound(object.getBreakSound());
            this.sync();
        }
    }

    public boolean hasMaxStarryObjects() {
        return getStarryObjectsAmount() >= STARRY_OBJECTS_MAX;
    }

    public boolean canAddStarryObject(StarryObject object) {
        return !hasMaxStarryObjects() && getStarryObjectsAmount(object) < object.getMax();
    }

    public boolean canBreakStarryObject(StarryObject object) {
        return this.breakCooldown <= 0;
    }

    // Shard Functionality
    public boolean canDealShardDamage() {
        return getStarryObjectsAmount(StarryObject.SHARD) > 0 && canBreakStarryObject(StarryObject.SHARD);
    }


    // Shield Functionality
    public boolean hasShieldInvulnerability(DamageSource source) {
        return getStarryObjectsAmount(StarryObject.SHIELD) > 0 && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) && source.getAttacker() != null
                && !source.isIn(ModTags.DamageTypes.BYPASSES_STARRY_SHIELD);
    }

    public void playSound(SoundEvent soundEvent) {
        Vec3d pos = this.obj.getPos();
        this.obj.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.PLAYERS, 1F, 0.85F + (Random.create().nextFloat() * 0.3F));
    }

    public enum StarryObject {
        SHIELD("shield", 25, 10, 5, 1, ModSounds.STARRY_SHIELD_CREATE, ModSounds.STARRY_SHIELD_BREAK),
        SHARD("shard", 20, 10, 7, 3, ModSounds.STARRY_SHARD_CREATE, ModSounds.STARRY_SHARD_BREAK)
        ;

        private final String id;

        private final int replenishTime;
        private final int breakCooldown;
        private final int max;

        private final int variants;
        private final SoundEvent createSound;
        private final SoundEvent breakSound;

        StarryObject(String id, int replenishTime, int breakCooldown, int max, int variants, SoundEvent createSound, SoundEvent breakSound) {
            this.id = id;

            this.replenishTime = replenishTime;
            this.breakCooldown = breakCooldown;
            this.max = max;

            this.variants = variants;
            this.createSound = createSound;
            this.breakSound = breakSound;
        }

        public static StarryObject fromId(String id) {
            for (StarryObject object : StarryObject.values()) {
                if (object.getId().equalsIgnoreCase(id)) {
                    return object;
                }
            }
            return null;
        }

        public String getId() {
            return this.id;
        }

        public int getReplenishTime() {
            return this.replenishTime;
        }

        public int getBreakCooldown() {
            return this.breakCooldown;
        }

        public int getMax() {
            return this.max;
        }

        public int getVariants() {
            return this.variants;
        }

        public SoundEvent getCreateSound() {
            return this.createSound;
        }

        public SoundEvent getBreakSound() {
            return this.breakSound;
        }

    }
}
