package powercyphe.starbound.common.item;

import powercyphe.starbound.common.registry.SBBlocks;
import powercyphe.starbound.common.registry.SBParticles;
import powercyphe.starbound.common.registry.SBSounds;
import powercyphe.starbound.common.registry.SBTags;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StarryGelItem extends BlockItem {
    public StarryGelItem(Properties settings) {
        super(SBBlocks.STARRY_GEL_LAYER, settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.translatable("tooltip.starbound.starry_gel_1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("tooltip.starbound.starry_gel_2").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        BlockPos blockPos = context.getClickedPos();
        BlockState state = world.getBlockState(blockPos);

        if (player != null && !player.isShiftKeyDown()) {
            if (state.is(SBTags.Blocks.STARSTONE_CONVERTABLE)) {

                if (world instanceof ServerLevel serverWorld) {
                    for (int i = 0; i < RandomSource.create().nextIntBetweenInclusive(3, 7); i++) {
                        BlockPos nextPos = blockPos;
                        for (Direction.Axis axis : Direction.Axis.VALUES) {
                            nextPos = nextPos.relative(axis, RandomSource.create().nextIntBetweenInclusive(-1, 1));
                        }

                        tryConvert(serverWorld, nextPos);
                    }
                    tryConvert(serverWorld, blockPos);
                    serverWorld.playSound(null, blockPos, SBSounds.STARRY_GEL_USE, SoundSource.BLOCKS, 1F, 1F);

                    stack.shrink(1);
                    return InteractionResult.SUCCESS_SERVER;
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(context);
    }

    public static void tryConvert(ServerLevel world, BlockPos blockPos) {
        BlockState state = world.getBlockState(blockPos);
        if (state.is(SBTags.Blocks.STARSTONE_CONVERTABLE)) {
            world.setBlockAndUpdate(blockPos, SBBlocks.STARSTONE.defaultBlockState());

            for (Direction direction : Direction.values()) {
                BlockPos nextPos = blockPos.relative(direction);
                BlockState nextState = world.getBlockState(nextPos);

                if (nextState.canBeReplaced() || nextState.canOcclude()) {
                    Vec3 sidePos = blockPos.getCenter().relative(direction, 0.6);
                    world.sendParticles(SBParticles.STARRY_TRAIL, sidePos.x(), sidePos.y(), sidePos.z(), 3, 0.25, 0.25, 0.25, 0.25);
                }
            }
        }
    }
}
