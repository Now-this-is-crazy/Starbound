package powercyphe.starbound.common.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import powercyphe.starbound.common.block.StarryGelBlock;
import powercyphe.starbound.common.registry.ModBlocks;

public class StarboundUtil {

    public static boolean hasNearbyStarryGel(BlockView blockView, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            if (blockView.getBlockState(blockPos.offset(direction)).getBlock() instanceof StarryGelBlock) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldApplyStarryGelMovement(Entity entity) {
        return isInStarryGel(entity) && !(entity instanceof PlayerEntity player && player.getAbilities().flying);
    }

    public static boolean isInStarryGel(Entity entity) {
        return boxInStarryGel(entity, entity.getBoundingBox());
    }

    public static boolean isHeadInStarryGel(Entity entity) {
        if (entity != null) {
            Box eyeBox = Box.of(entity.getEyePos(), entity.getWidth() / 1.5, Math.abs(entity.getEyeHeight(entity.getPose()) - entity.getHeight()), entity.getWidth() / 1.5);
            return boxInStarryGel(entity, eyeBox);
        }
        return false;
    }

    public static boolean boxInStarryGel(Entity entity, Box box) {
        World world = entity.getWorld();

        BlockPos blockPos = BlockPos.ofFloored(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7);
        BlockPos blockPos2 = BlockPos.ofFloored(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7);

        if (world.isRegionLoaded(blockPos, blockPos2)) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for(int i = blockPos.getX(); i <= blockPos2.getX(); ++i) {
                for(int j = blockPos.getY(); j <= blockPos2.getY(); ++j) {
                    for(int k = blockPos.getZ(); k <= blockPos2.getZ(); ++k) {
                        mutable.set(i, j, k);
                        BlockState state = world.getBlockState(mutable);

                        VoxelShape collisionShape = state.getOutlineShape(world, blockPos, ShapeContext.of(entity));
                        if (!collisionShape.isEmpty() && box.intersects(collisionShape.getBoundingBox().offset(blockPos))
                                && state.getBlock() instanceof StarryGelBlock) {
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }

    public static float getObjectBaseRot(float baseRotation, int objectId, int objectAmount) {
        return baseRotation + (360F / objectAmount) * objectId;
    }

    public static float getObjectFloatRot(float floatRotation, int objectId, int objectAmount) {
        return floatRotation + (360F / objectAmount) * objectId;
    }

    public static Vec3d objectPos(float baseRotation, float floatRotation, Entity entity, int objectId, int objectAmount) {
        baseRotation = getObjectBaseRot(baseRotation, objectId, objectAmount);
        floatRotation = getObjectFloatRot(floatRotation, objectId, objectAmount);
        return entity.getPos().add(entity.getRotationVector(0, baseRotation).multiply(0.5 + entity.getWidth() * 1.25F))
                .add(0, Math.sin(Math.toRadians(floatRotation)) * 0.2F + 0.125 + (entity.getHeight() / 2), 0);
    }
}
