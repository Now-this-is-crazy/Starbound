package powercyphe.starbound.mixin.accessor;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor<T extends Entity, S extends EntityRenderState> {

    @Accessor("state")
    S starbound$getRenderState();
}
