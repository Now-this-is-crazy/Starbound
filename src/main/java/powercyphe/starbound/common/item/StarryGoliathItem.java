package powercyphe.starbound.common.item;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.component.StarryObjectComponent;
import powercyphe.starbound.common.network.StarryChargeSoundPayload;
import powercyphe.starbound.common.registry.ModEnchantments;

import java.util.Optional;

public class StarryGoliathItem extends Item {
    public static Identifier BLOCK_INTERACTION_RANGE_MODIFIER_ID = Starbound.id("block_interaction_range");
    public static Identifier ENTITY_INTERACTION_RANGE_MODIFIER_ID = Starbound.id("entity_interaction_range");

    public StarryGoliathItem(ToolMaterial material, float attackDamage, float attackSpeed, float reach, Settings settings) {
        super(settings.axe(material, attackDamage, attackSpeed).attributeModifiers(createStarryGoliathAttributes(material, attackDamage, attackSpeed, reach)));
    }

    public static AttributeModifiersComponent createStarryGoliathAttributes(ToolMaterial material, float attackDamage, float attackSpeed, float reach) {
        return AttributeModifiersComponent.builder().add(EntityAttributes.ATTACK_DAMAGE,
                new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage + material.attackDamageBonus(),
                        EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ATTACK_SPEED,
                new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed,
                        EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.BLOCK_INTERACTION_RANGE,
                new EntityAttributeModifier(BLOCK_INTERACTION_RANGE_MODIFIER_ID, reach,
                        EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.ENTITY_INTERACTION_RANGE,
                new EntityAttributeModifier(ENTITY_INTERACTION_RANGE_MODIFIER_ID, reach,
                        EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        StarryObjectComponent component = StarryObjectComponent.get(user);
        StarryObjectComponent.StarryObject object = getStarryObject(stack, user.getRegistryManager());

        if (!user.getItemCooldownManager().isCoolingDown(stack) && hand == Hand.MAIN_HAND && component.canAddStarryObject(object)) {
            if (world instanceof ServerWorld serverWorld) {
                for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                    ServerPlayNetworking.send(player, new StarryChargeSoundPayload(user.getId(), stack));
                }
            }
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        StarryObjectComponent component = StarryObjectComponent.get(user);
        StarryObjectComponent.StarryObject object = getStarryObject(stack, user.getRegistryManager());

        if ((user.getItemUseTime()+1) % object.getReplenishTime() == 0 && !world.isClient()) {
            component.addStarryObject(object);
            if (!component.canAddStarryObject(object)) {
                user.stopUsingItem();
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 700;
    }

    public static StarryObjectComponent.StarryObject getStarryObject(ItemStack stack, DynamicRegistryManager wrapperLookup) {
        Optional<RegistryEntry.Reference<Enchantment>> enchantment = wrapperLookup.getOptionalEntry(ModEnchantments.FLAIL);
        if (enchantment.isPresent() && EnchantmentHelper.getLevel(enchantment.get(), stack) > 0) {
            return StarryObjectComponent.StarryObject.SHARD;
        }
        return StarryObjectComponent.StarryObject.SHIELD;
    }

}
