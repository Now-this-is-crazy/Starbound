package powercyphe.starbound.common.registry;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.level.block.Block;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.item.StarryGelItem;
import powercyphe.starbound.common.item.StarryGoliathItem;
import powercyphe.starbound.common.item.StarrySpyglassItem;
import powercyphe.starbound.common.item.consume.StarryInvisibilityConsumeEffect;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class SBItems {

    public static final ResourceKey<CreativeModeTab> STARBOUND_GROUP_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Starbound.id(Starbound.MOD_ID + "_group"));
    public static CreativeModeTab STARBOUND_GROUP = FabricItemGroup.builder()
            .title(Component.translatable("itemGroup." + Starbound.MOD_ID))
            .icon(() -> SBItems.STARRY_GOLIATH.getDefaultInstance())
            .build();

    public static class Materials {

        public static final ToolMaterial STARRY_TOOL_MATERIAL = new ToolMaterial(BlockTags.MINEABLE_WITH_AXE, 1721, 8.0F, 3.0F, 10, ItemTags.DIAMOND_TOOL_MATERIALS);
        public static final ResourceKey<TrimMaterial> STARRY_TRIM_MATERIAL = trimOf("starry");

        public static void bootstrap(BootstrapContext<TrimMaterial> registerable) {
            register(registerable, STARRY_TRIM_MATERIAL, Style.EMPTY.withColor(0x2c3c4f));
        }

        public static void register(BootstrapContext<TrimMaterial> registerable, ResourceKey<TrimMaterial> key, Style style) {
            String id = key.location().getPath();
            TrimMaterial material = new TrimMaterial(MaterialAssetGroup.create(id), Component.translatable("trim_material.starbound." + id).withStyle(style));
            registerable.register(key, material);
        }

        public static ResourceKey<TrimMaterial> trimOf(String key) {
            return ResourceKey.create(Registries.TRIM_MATERIAL, Starbound.id(key));
        }
    }

    public static class Components {

        public static final DataComponentType<Boolean> STARRY_EMISSIVITY = register("starry_emissivity", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

        public static void init() {}

        public static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
            return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Starbound.id(id), builderOperator.apply(DataComponentType.builder()).build());
        }
    }

    public static Item STARRY_GEL = register("starry_gel", StarryGelItem::new, new Item.Properties());
    public static Item STARRY_INGOT = register("starry_ingot", Item::new, new Item.Properties().trimMaterial(Materials.STARRY_TRIM_MATERIAL));

    public static Item STARRY_SPYGLASS = register("starry_spyglass", StarrySpyglassItem::new, new Item.Properties().stacksTo(1));
    public static Item STARRY_TOTEM = register("starry_totem", Item::new, new Item.Properties().stacksTo(1).component(DataComponents.DEATH_PROTECTION, StarryInvisibilityConsumeEffect.STARRY_TOTEM));
    public static Item STARRY_GOLIATH = register("starry_goliath", settings -> new StarryGoliathItem(Materials.STARRY_TOOL_MATERIAL, 4F, -2.7F, 0.5F, settings), new Item.Properties());

    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, STARBOUND_GROUP_KEY, STARBOUND_GROUP);
    }

    public static Item registerBlockItem(Block block, BiFunction<Block, Item.Properties, BlockItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, BuiltInRegistries.BLOCK.getKey(block));
        Item returnItem = Registry.register(BuiltInRegistries.ITEM, itemKey, itemFactory.apply(block, settings.setId(itemKey)));
        addToGroup(returnItem);
        return returnItem;
    }

    public static Item register(String id, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Starbound.id(id));
        return register(itemKey, itemFactory, settings);
    }

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item returnItem = Registry.register(BuiltInRegistries.ITEM, itemKey, itemFactory.apply(settings.setId(itemKey)));
        addToGroup(returnItem);
        return returnItem;
    }

    public static void addToGroup(Item item) {
        ItemGroupEvents.modifyEntriesEvent(STARBOUND_GROUP_KEY).register(itemGroup -> itemGroup.accept(item.getDefaultInstance()));
    }
}
