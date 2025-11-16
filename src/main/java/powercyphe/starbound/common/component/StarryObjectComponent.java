package powercyphe.starbound.common.component;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import powercyphe.starbound.common.registry.*;
import powercyphe.starbound.common.util.StarboundUtil;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class StarryObjectComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final String STARRY_OBJECTS_KEY = "starryObjects";
    public static final String STARRY_OBJECT_KEY = "starryObject";
    public static final String STARRY_OBJECT_VARIANT_KEY = "starryObjectVariant";

    public static final int STARRY_OBJECTS_MAX = 7;
    private final NonNullList<Tuple<StarryObject, Integer>> starryObjects;

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
        this.starryObjects = NonNullList.create();
    }

    public static StarryObjectComponent get(LivingEntity entity) {
        return SBComponents.STARRY_OBJECTS.get(entity);
    }

    public void sync() {
        SBComponents.STARRY_OBJECTS.sync(this.obj);
    }

    // Nbt

    @Override
    public void readFromNbt(CompoundTag nbt, HolderLookup.Provider wrapperLookup) {
        this.starryObjects.clear();

        ListTag nbtList = (ListTag) nbt.get(STARRY_OBJECTS_KEY);
        if (nbtList != null) {
            for (Tag nbtElement : nbtList) {

                if (nbtElement instanceof CompoundTag nbtCompound) {
                    Optional<String> objectKey = nbtCompound.getString(STARRY_OBJECT_KEY);
                    Optional<Integer> objectVariant = nbtCompound.getInt(STARRY_OBJECT_VARIANT_KEY);

                    if (objectKey.isPresent() && objectVariant.isPresent()) {
                        StarryObject object = StarryObject.fromId(objectKey.get());
                        if (object != null) {
                            int variant = Mth.clamp(objectVariant.get(), 1, object.getVariants());
                            this.starryObjects.add(new Tuple<>(object, variant));
                        }
                    }
                }
            }
        }

        this.breakCooldown = nbt.getIntOr(BREAK_COOLDOWN_KEY, 0);

        this.baseRotation = nbt.getFloatOr(BASE_ROTATION_KEY, 0);
        this.floatRotation = nbt.getFloatOr(FLOAT_ROTATION_KEY, 0);
    }

    @Override
    public void writeToNbt(CompoundTag nbt, HolderLookup.Provider wrapperLookup) {
        ListTag nbtList = new ListTag();
        for (Tuple<StarryObject, Integer> pair : this.getStarryObjects()) {
            StarryObject object = pair.getA();
            int variant = pair.getB();

            CompoundTag objectNbt = new CompoundTag();
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
        if (this.canDealShardDamage() && this.obj.level() instanceof ServerLevel serverWorld) {
            DamageSource source = SBDamageTypes.create(this.obj.level(), SBDamageTypes.STARRY_SHARD, this.obj, this.obj);
            for (int index = 0; index < this.getStarryObjectsAmount(); index++) {
                StarryObject object = this.getStarryObjects().get(index).getA();
                if (object == StarryObjectComponent.StarryObject.SHARD) {
                    Vec3 objectPos = StarboundUtil.objectPos(this.baseRotation, this.floatRotation, this.obj, index, this.getStarryObjectsAmount());
                    AABB box = AABB.ofSize(objectPos, 0.35, 0.35, 0.35);

                    List<Entity> entities = this.obj.level().getEntities(this.obj, box, EntitySelector.LIVING_ENTITY_STILL_ALIVE.and(EntitySelector.NO_CREATIVE_OR_SPECTATOR));
                    if (!entities.isEmpty()) {
                        Entity entity = entities.getFirst();

                        if (!((LivingEntity) entity).isInvulnerableTo(serverWorld, source)) {
                            entity.hurtServer(serverWorld, source, 2);
                            this.removeStarryObject(index);
                            break;
                        }
                    }
                }
            }
        }
    }

    // Object Manipulation

    public NonNullList<Tuple<StarryObject, Integer>> getStarryObjects(StarryObject object) {
        NonNullList<Tuple<StarryObject, Integer>> list = NonNullList.create();
        list.addAll(this.getStarryObjects());

        list.removeIf(o -> !o.getA().equals(object));
        return list;
    }

    public NonNullList<Tuple<StarryObject, Integer>> getStarryObjects() {
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
            this.starryObjects.add(new Tuple<>(object, RandomSource.create().nextIntBetweenInclusive(1, object.getVariants())));

            if (this.obj.level() instanceof ServerLevel serverWorld) {
                Vec3 vec3d = StarboundUtil.objectPos(baseRotation, floatRotation, this.obj, this.getStarryObjectsAmount()-1, this.getStarryObjectsAmount());
                serverWorld.sendParticles(SBParticles.STARRY_CRIT, vec3d.x(), vec3d.y(), vec3d.z(), 7, 0, 0, 0, 1);
            }
            this.playSound(object.getCreateSound());
            this.sync();
        }
    }

    public void removeStarryObject(StarryObject object) {
        int index = 0;
        for (Tuple<StarryObject, Integer> pair : this.getStarryObjects()) {
            if (pair.getA() == object) {
                break;
            }
            index++;
        }
        this.removeStarryObject(index);
    }

    public void removeStarryObject(int index) {
        if (getStarryObjectsAmount() > 0 && this.getStarryObjectsAmount() > index) {
            StarryObject object = this.getStarryObjects().get(index).getA();
            this.starryObjects.remove(index);
            this.breakCooldown = object.getBreakCooldown();

            if (this.obj.level() instanceof ServerLevel serverWorld) {
                Vec3 vec3d = StarboundUtil.objectPos(baseRotation, floatRotation, this.obj, index, this.getStarryObjectsAmount()+1);
                serverWorld.sendParticles(SBParticles.STARRY_CRIT, vec3d.x(), vec3d.y(), vec3d.z(), 7, 0, 0, 0, 1);
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
        return getStarryObjectsAmount(StarryObject.SHIELD) > 0 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && source.getEntity() != null
                && !source.is(SBTags.DamageTypes.BYPASSES_STARRY_SHIELD);
    }

    public void playSound(SoundEvent soundEvent) {
        Vec3 pos = this.obj.position();
        this.obj.level().playSound(null, pos.x(), pos.y(), pos.z(), soundEvent, SoundSource.PLAYERS, 1F, 0.85F + (RandomSource.create().nextFloat() * 0.3F));
    }

    public enum StarryObject {
        SHIELD("shield", 25, 10, 5, 1, SBSounds.STARRY_SHIELD_CREATE, SBSounds.STARRY_SHIELD_BREAK),
        SHARD("shard", 20, 10, 7, 3, SBSounds.STARRY_SHARD_CREATE, SBSounds.STARRY_SHARD_BREAK)
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
