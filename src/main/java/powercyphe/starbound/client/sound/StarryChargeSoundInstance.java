package powercyphe.starbound.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import powercyphe.starbound.common.registry.SBSounds;

public class StarryChargeSoundInstance extends AbstractTickableSoundInstance {
    private final LivingEntity entity;
    private final ItemStack stack;

    private boolean finished;
    private int finishedTicks;

    private final int replenishTime;

    public StarryChargeSoundInstance(LivingEntity entity, ItemStack stack, float volume, float pitch, int replenishTime) {
        super(SBSounds.STARRY_OBJECT_CHARGE, SoundSource.PLAYERS, RandomSource.create(0));

        this.volume = volume;
        this.pitch = pitch;
        this.looping = true;

        this.entity = entity;
        this.stack = stack;

        this.finishedTicks = 5;
        this.finished = false;

        this.replenishTime = replenishTime;

        this.x = ((float)this.entity.getX());
        this.y = ((float)this.entity.getY());
        this.z = ((float)this.entity.getZ());
    }

    @Override
    public void tick() {
        if (this.entity == null || this.entity.isRemoved()) {
            this.stop();
        } else {
            this.x = ((float)this.entity.getX());
            this.y = ((float)this.entity.getY());
            this.z = ((float)this.entity.getZ());

            if (!this.entity.isUsingItem() || !ItemStack.isSameItem(this.entity.getUseItem(), this.stack)) {
                this.finished = true;
            }
            if (!this.finished) {
                float lowPitchTicks = 5F;
                if (entity.getTicksUsingItem() % replenishTime <= lowPitchTicks) {
                    this.pitch = 1.3F - ((this.entity.getTicksUsingItem() % replenishTime) / lowPitchTicks * 0.6F);
                } else {
                    this.pitch = 0.7F + ((this.entity.getTicksUsingItem() % replenishTime - lowPitchTicks) / (replenishTime - lowPitchTicks) * 0.6F);
                }
            } else if (this.finishedTicks > 0) {
                this.finishedTicks--;
                this.pitch *= 0.98F;
                this.volume *= 0.77F;
            } else {
                this.stop();
            }
        }
    }
}
