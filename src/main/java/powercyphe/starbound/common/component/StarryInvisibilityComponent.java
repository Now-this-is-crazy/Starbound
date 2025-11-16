package powercyphe.starbound.common.component;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import powercyphe.starbound.common.registry.SBComponents;
import powercyphe.starbound.common.registry.SBParticles;
import powercyphe.starbound.common.util.StarboundUtil;

import java.util.Optional;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class StarryInvisibilityComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static String INVISIBILITY_STRENGTH_KEY = "invisibilityStrength";
    public float invisibilityStrength, lastInvisibilityStrength = 0;

    public LivingEntity obj;

    public StarryInvisibilityComponent(LivingEntity entity) {
        this.obj = entity;
    }

    public static StarryInvisibilityComponent get(LivingEntity entity) {
        return SBComponents.STARRY_INVISIBILITY.get(entity);
    }

    public void sync() {
        SBComponents.STARRY_INVISIBILITY.sync(this.obj);
    }

    @Override
    public void tick() {
        this.lastInvisibilityStrength = invisibilityStrength;
        if (StarboundUtil.isInStarryGel(this.obj) && !this.obj.isSpectator()) {
            this.add(0.025F);
        } else if (this.invisibilityStrength > 0F) {
            this.add(-this.getDecrement());
        }
    }

    @Override
    public void clientTick() {
        this.tick();
        if (this.invisibilityStrength > 0F) {
            Minecraft client = Minecraft.getInstance();
            LocalPlayer clientPlayer = client.player;

            if (this.obj != clientPlayer || client.options.getCameraType() != CameraType.FIRST_PERSON) {
                for (int i = 0; i < RandomSource.create().nextIntBetweenInclusive(1, (int) (this.invisibilityStrength * 5 + 1)); i++) {
                    this.obj.level().addParticle(SBParticles.STARRY_TRAIL, this.obj.getRandomX(this.obj.getBbWidth() / 1.25F), this.obj.getRandomY(), this.obj.getRandomZ(this.obj.getBbWidth() / 1.25F), 0, 0, 0);
                }
            }
        }

    }

    public float getDecrement() {
        float decrement = 0.001F;
        float m = 1;
        if (this.obj.isUnderWater()) {
            m = 10F;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.isArmor() && !this.obj.getItemBySlot(slot).isEmpty()) {
                    m -= 0.5F;
                }
            }
        } else if (this.obj.isInWaterOrRain()) {
            m = this.obj.getItemBySlot(EquipmentSlot.HEAD).isEmpty() ? 5F : 2.5F;
        }

        return decrement * m;
    }

    public void add(float num) {
        if (this.invisibilityStrength >= 1 && num > 0) {
            return;
        }
        this.invisibilityStrength = Mth.clamp(this.invisibilityStrength + num, 0, 1);
        this.sync();
    }

    public boolean isStealthy() {
        return this.invisibilityStrength > 0.5F;
    }

    @Override
    public void readFromNbt(CompoundTag nbt, HolderLookup.Provider wrapperLookup) {
        Optional<Float> optional = nbt.getFloat(INVISIBILITY_STRENGTH_KEY);
        if (optional.isPresent()) {
            this.invisibilityStrength = optional.get();
        } else {
            this.invisibilityStrength = 0;
        }
    }

    @Override
    public void writeToNbt(CompoundTag nbt, HolderLookup.Provider wrapperLookup) {
        nbt.putFloat(INVISIBILITY_STRENGTH_KEY, this.invisibilityStrength);
    }
}
