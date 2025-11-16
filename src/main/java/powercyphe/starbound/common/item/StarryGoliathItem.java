package powercyphe.starbound.common.item;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryObjectComponent;
import powercyphe.starbound.common.network.StarryChargeSoundPayload;
import powercyphe.starbound.common.registry.SBEnchantments;

import java.util.Optional;

public class StarryGoliathItem extends Item {
    public static ResourceLocation BLOCK_INTERACTION_RANGE_MODIFIER_ID = Starbound.id("block_interaction_range");
    public static ResourceLocation ENTITY_INTERACTION_RANGE_MODIFIER_ID = Starbound.id("entity_interaction_range");

    public StarryGoliathItem(ToolMaterial material, float attackDamage, float attackSpeed, float reach, Properties settings) {
        super(settings.axe(material, attackDamage, attackSpeed).attributes(createStarryGoliathAttributes(material, attackDamage, attackSpeed, reach)));
    }

    public static ItemAttributeModifiers createStarryGoliathAttributes(ToolMaterial material, float attackDamage, float attackSpeed, float reach) {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage + material.attackDamageBonus(),
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED,
                new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.BLOCK_INTERACTION_RANGE,
                new AttributeModifier(BLOCK_INTERACTION_RANGE_MODIFIER_ID, reach,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ENTITY_INTERACTION_RANGE,
                new AttributeModifier(ENTITY_INTERACTION_RANGE_MODIFIER_ID, reach,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        StarryObjectComponent component = StarryObjectComponent.get(user);
        StarryObjectComponent.StarryObject object = getStarryObject(stack, user.registryAccess());

        if (!user.getCooldowns().isOnCooldown(stack) && hand == InteractionHand.MAIN_HAND && component.canAddStarryObject(object)) {
            if (world instanceof ServerLevel serverWorld) {
                for (ServerPlayer player : serverWorld.players()) {
                    ServerPlayNetworking.send(player, new StarryChargeSoundPayload(user.getId(), stack));
                }
            }
            user.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        StarryObjectComponent component = StarryObjectComponent.get(user);
        StarryObjectComponent.StarryObject object = getStarryObject(stack, user.registryAccess());

        if ((user.getTicksUsingItem()+1) % object.getReplenishTime() == 0 && !world.isClientSide()) {
            component.addStarryObject(object);
            if (!component.canAddStarryObject(object)) {
                user.releaseUsingItem();
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 700;
    }

    public static StarryObjectComponent.StarryObject getStarryObject(ItemStack stack, RegistryAccess wrapperLookup) {
        Optional<Holder.Reference<Enchantment>> enchantment = wrapperLookup.get(SBEnchantments.FLAIL);
        if (enchantment.isPresent() && EnchantmentHelper.getItemEnchantmentLevel(enchantment.get(), stack) > 0) {
            return StarryObjectComponent.StarryObject.SHARD;
        }
        return StarryObjectComponent.StarryObject.SHIELD;
    }

}
