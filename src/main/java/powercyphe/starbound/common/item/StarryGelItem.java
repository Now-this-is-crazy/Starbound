package powercyphe.starbound.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import powercyphe.starbound.common.registry.ModBlocks;
import powercyphe.starbound.common.registry.ModParticles;
import powercyphe.starbound.common.registry.ModSounds;
import powercyphe.starbound.common.registry.ModTags;

import java.util.function.Consumer;

public class StarryGelItem extends BlockItem {
    public StarryGelItem(Settings settings) {
        super(ModBlocks.STARRY_GEL_LAYER, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.translatable("tooltip.starbound.starry_gel_1").formatted(Formatting.GRAY));
        textConsumer.accept(Text.translatable("tooltip.starbound.starry_gel_2").formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        ItemStack stack = context.getStack();
        PlayerEntity player = context.getPlayer();

        BlockPos blockPos = context.getBlockPos();
        BlockState state = world.getBlockState(blockPos);

        if (player != null && !player.isSneaking()) {
            if (state.isIn(ModTags.Blocks.STARSTONE_CONVERTABLE)) {

                if (world instanceof ServerWorld serverWorld) {
                    for (int i = 0; i < Random.create().nextBetween(3, 7); i++) {
                        BlockPos nextPos = blockPos;
                        for (Direction.Axis axis : Direction.Axis.VALUES) {
                            nextPos = nextPos.offset(axis, Random.create().nextBetween(-1, 1));
                        }

                        tryConvert(serverWorld, nextPos);
                    }
                    tryConvert(serverWorld, blockPos);
                    serverWorld.playSound(null, blockPos, ModSounds.STARRY_GEL_USE, SoundCategory.BLOCKS, 1F, 1F);

                    stack.decrement(1);
                    return ActionResult.SUCCESS_SERVER;
                }

                return ActionResult.SUCCESS;
            }
        }

        return super.useOnBlock(context);
    }

    public static void tryConvert(ServerWorld world, BlockPos blockPos) {
        BlockState state = world.getBlockState(blockPos);
        if (state.isIn(ModTags.Blocks.STARSTONE_CONVERTABLE)) {
            world.setBlockState(blockPos, ModBlocks.STARSTONE.getDefaultState());

            for (Direction direction : Direction.values()) {
                BlockPos nextPos = blockPos.offset(direction);
                BlockState nextState = world.getBlockState(nextPos);

                if (nextState.isReplaceable() || nextState.isOpaque()) {
                    Vec3d sidePos = blockPos.toCenterPos().offset(direction, 0.6);
                    world.spawnParticles(ModParticles.STARRY_TRAIL, sidePos.getX(), sidePos.getY(), sidePos.getZ(), 3, 0.25, 0.25, 0.25, 0.25);
                }
            }
        }
    }
}
