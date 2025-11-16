package powercyphe.starbound.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.advancement.DamageBlockedByStarryGoliathCriterion;
import powercyphe.starbound.common.registry.SBBlocks;
import powercyphe.starbound.common.registry.SBItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SBAdvancementProvider extends FabricAdvancementProvider {
    public SBAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider wrapperLookup, Consumer<AdvancementHolder> consumer) {

        // Root Achievement
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(SBItems.STARRY_GEL,
                        Component.translatable("advancements.starbound.obtain_starry_gel.title"),
                        Component.translatable("advancements.starbound.obtain_starry_gel.description"),
                        Starbound.id("block/starstone_lit"),
                        AdvancementType.TASK,
                        false,
                        false,
                        false)
                .addCriterion("obtain_starry_gel", InventoryChangeTrigger.TriggerInstance.hasItems(SBItems.STARRY_GEL))
                .build(Starbound.id("obtain_starry_gel"));
        consumer.accept(root);


        // Achievements
        consumer.accept(Advancement.Builder.advancement()
                .display(SBBlocks.STARRY_GEL_BLOCK,
                        Component.translatable("advancements.starbound.move_in_starry_gel.title"),
                        Component.translatable("advancements.starbound.move_in_starry_gel.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false)
                .addCriterion("move_in_starry_gel", EnterBlockTrigger.TriggerInstance.entersBlock(SBBlocks.STARRY_GEL_BLOCK))
                .addCriterion("move_in_starry_gel_layer", EnterBlockTrigger.TriggerInstance.entersBlock(SBBlocks.STARRY_GEL_LAYER))
                .requirements(AdvancementRequirements.Strategy.OR)
                .parent(root)
                .build(Starbound.id("move_in_starry_gel")));

        consumer.accept(Advancement.Builder.advancement()
                .display(SBItems.STARRY_SPYGLASS,
                        Component.translatable("advancements.starbound.obtain_starry_spyglass.title"),
                        Component.translatable("advancements.starbound.obtain_starry_spyglass.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false)
                .addCriterion("obtain_starry_spyglass", InventoryChangeTrigger.TriggerInstance.hasItems(SBItems.STARRY_SPYGLASS))
                .parent(root)
                .build(Starbound.id("obtain_starry_spyglass")));

        consumer.accept(Advancement.Builder.advancement()
                .display(SBItems.STARRY_GOLIATH,
                        Component.translatable("advancements.starbound.block_critical_damage_with_starry_goliath.title"),
                        Component.translatable("advancements.starbound.block_critical_damage_with_starry_goliath.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false)
                .addCriterion("block_critical_damage_with_starry_goliath", DamageBlockedByStarryGoliathCriterion.Conditions.create(EntityPredicate.Builder.entity(), 20F))
                .parent(root)
                .build(Starbound.id("block_critical_damage_with_starry_goliath")));




    }
}
