package powercyphe.starbound.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import powercyphe.starbound.common.block.StarryGelBlock;

public class StarboundUtil {

    public static boolean hasNearbyStarryGel(BlockGetter blockView, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            if (blockView.getBlockState(blockPos.relative(direction)).getBlock() instanceof StarryGelBlock) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldApplyStarryGelMovement(Entity entity) {
        return isInStarryGel(entity) && !(entity instanceof Player player && player.getAbilities().flying);
    }

    public static boolean isInStarryGel(Entity entity) {
        return boxInStarryGel(entity, entity.getBoundingBox());
    }

    public static boolean isHeadInStarryGel(Entity entity) {
        if (entity != null) {
            AABB eyeBox = AABB.ofSize(entity.getEyePosition(), entity.getBbWidth() / 1.5, Math.abs(entity.getEyeHeight(entity.getPose()) - entity.getBbHeight()), entity.getBbWidth() / 1.5);
            return boxInStarryGel(entity, eyeBox);
        }
        return false;
    }

    public static boolean boxInStarryGel(Entity entity, AABB box) {
        Level world = entity.level();

        BlockPos blockPos = BlockPos.containing(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7);
        BlockPos blockPos2 = BlockPos.containing(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7);

        if (world.hasChunksAt(blockPos, blockPos2)) {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

            for(int i = blockPos.getX(); i <= blockPos2.getX(); ++i) {
                for(int j = blockPos.getY(); j <= blockPos2.getY(); ++j) {
                    for(int k = blockPos.getZ(); k <= blockPos2.getZ(); ++k) {
                        mutable.set(i, j, k);
                        BlockState state = world.getBlockState(mutable);

                        VoxelShape collisionShape = state.getShape(world, blockPos, CollisionContext.of(entity));
                        if (!collisionShape.isEmpty() && box.intersects(collisionShape.bounds().move(blockPos))
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

    public static Vec3 objectPos(float baseRotation, float floatRotation, Entity entity, int objectId, int objectAmount) {
        baseRotation = getObjectBaseRot(baseRotation, objectId, objectAmount);
        floatRotation = getObjectFloatRot(floatRotation, objectId, objectAmount);
        return entity.position().add(entity.calculateViewVector(0, baseRotation).scale(0.5 + entity.getBbWidth() * 1.25F))
                .add(0, Math.sin(Math.toRadians(floatRotation)) * 0.2F + 0.125 + (entity.getBbHeight() / 2), 0);
    }
}
