package powercyphe.starbound.common.registry;

import net.minecraft.sounds.SoundEvent;
import powercyphe.starbound.common.Starbound;

public class SBSounds {

    public static SoundEvent STARRY_OBJECT_CHARGE = register("starry_object.charge");

    public static SoundEvent STARRY_SHIELD_CREATE = register("starry_shield.create");
    public static SoundEvent STARRY_SHIELD_BREAK = register("starry_shield.break");

    public static SoundEvent STARRY_SHARD_CREATE = register("starry_shard.create");
    public static SoundEvent STARRY_SHARD_BREAK = register("starry_shard.break");

    public static SoundEvent STARRY_GEL_STEP = register("block.starry_gel.step");

    public static SoundEvent STARRY_GEL_USE = register("item.starry_gel.use");

    public static void init() {}

    public static SoundEvent register(String id) {
        return SoundEvent.createVariableRangeEvent(Starbound.id(id));
    }
}
