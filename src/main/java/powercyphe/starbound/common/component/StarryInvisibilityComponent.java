package powercyphe.starbound.common.component;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import powercyphe.starbound.common.registry.ModComponents;
import powercyphe.starbound.common.registry.ModParticles;
import powercyphe.starbound.common.util.StarboundUtil;

import java.util.Optional;

public class StarryInvisibilityComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static String INVISIBILITY_STRENGTH_KEY = "invisibilityStrength";
    public float invisibilityStrength, lastInvisibilityStrength = 0;

    public LivingEntity obj;

    public StarryInvisibilityComponent(LivingEntity entity) {
        this.obj = entity;
    }

    public static StarryInvisibilityComponent get(LivingEntity entity) {
        return ModComponents.STARRY_INVISIBILITY.get(entity);
    }

    public void sync() {
        ModComponents.STARRY_INVISIBILITY.sync(this.obj);
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
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity clientPlayer = client.player;

            if (this.obj != clientPlayer || client.options.getPerspective() != Perspective.FIRST_PERSON) {
                for (int i = 0; i < Random.create().nextBetween(1, (int) (this.invisibilityStrength * 5 + 1)); i++) {
                    this.obj.getWorld().addParticleClient(ModParticles.STARRY_TRAIL, this.obj.getParticleX(this.obj.getWidth() / 1.25F), this.obj.getRandomBodyY(), this.obj.getParticleZ(this.obj.getWidth() / 1.25F), 0, 0, 0);
                }
            }
        }

    }

    public float getDecrement() {
        float decrement = 0.001F;
        float m = 1;
        if (this.obj.isSubmergedInWater()) {
            m = 10F;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.isArmorSlot() && !this.obj.getEquippedStack(slot).isEmpty()) {
                    m -= 0.5F;
                }
            }
        } else if (this.obj.isTouchingWaterOrRain()) {
            m = this.obj.getEquippedStack(EquipmentSlot.HEAD).isEmpty() ? 5F : 2.5F;
        }

        return decrement * m;
    }

    public void add(float num) {
        if (this.invisibilityStrength >= 1 && num > 0) {
            return;
        }
        this.invisibilityStrength = MathHelper.clamp(this.invisibilityStrength + num, 0, 1);
        this.sync();
    }

    public boolean isStealthy() {
        return this.invisibilityStrength > 0.5F;
    }

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        Optional<Float> optional = nbt.getFloat(INVISIBILITY_STRENGTH_KEY);
        if (optional.isPresent()) {
            this.invisibilityStrength = optional.get();
        } else {
            this.invisibilityStrength = 0;
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbt.putFloat(INVISIBILITY_STRENGTH_KEY, this.invisibilityStrength);
    }
}
