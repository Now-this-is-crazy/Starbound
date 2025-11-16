package powercyphe.starbound.mixin.starry_emissivity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.starbound.common.registry.SBItems;
import powercyphe.starbound.common.registry.SBParticles;
import powercyphe.starbound.common.registry.SBSounds;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void starbound$cleanEmissivity(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (stack.getOrDefault(SBItems.Components.STARRY_EMISSIVITY, false)) {
            if (world.isClientSide()) {
                for (int i = 0; i < RandomSource.create().nextInt(7) + 4; i++) {
                    world.addParticle(SBParticles.STARRY_TRAIL, pos.getX() + RandomSource.create().nextDouble(), pos.above().getY() + 0.25, pos.getZ() + RandomSource.create().nextDouble(), 0, 0, 0);
                }
            } else {
                LayeredCauldronBlock.lowerFillLevel(state, world, pos);
            }

            world.playSound(null, pos, SBSounds.STARRY_GEL_USE, SoundSource.BLOCKS, 1F, 1F);
            stack.remove(SBItems.Components.STARRY_EMISSIVITY);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
