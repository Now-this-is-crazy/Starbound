package powercyphe.starbound.client.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import powercyphe.starbound.common.registry.ModItems;
import powercyphe.starbound.common.registry.ModSounds;

public class StarryChargeSoundInstance extends MovingSoundInstance {
    private final LivingEntity entity;
    private final ItemStack stack;

    private boolean finished;
    private int finishedTicks;

    private final int replenishTime;

    public StarryChargeSoundInstance(LivingEntity entity, ItemStack stack, float volume, float pitch, int replenishTime) {
        super(ModSounds.STARRY_OBJECT_CHARGE, SoundCategory.PLAYERS, Random.create(0));

        this.volume = volume;
        this.pitch = pitch;
        this.repeat = true;

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
            this.setDone();
        } else {
            this.x = ((float)this.entity.getX());
            this.y = ((float)this.entity.getY());
            this.z = ((float)this.entity.getZ());

            if (!this.entity.isUsingItem() || !ItemStack.areItemsEqual(this.entity.getActiveItem(), this.stack)) {
                this.finished = true;
            }
            if (!this.finished) {
                float lowPitchTicks = 5F;
                if (entity.getItemUseTime() % replenishTime <= lowPitchTicks) {
                    this.pitch = 1.3F - ((this.entity.getItemUseTime() % replenishTime) / lowPitchTicks * 0.6F);
                } else {
                    this.pitch = 0.7F + ((this.entity.getItemUseTime() % replenishTime - lowPitchTicks) / (replenishTime - lowPitchTicks) * 0.6F);
                }
            } else if (this.finishedTicks > 0) {
                this.finishedTicks--;
                this.pitch *= 0.98F;
                this.volume *= 0.77F;
            } else {
                this.setDone();
            }
        }
    }
}
