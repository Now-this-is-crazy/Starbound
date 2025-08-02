package powercyphe.starbound.common.registry;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.*;
import net.minecraft.item.equipment.trim.ArmorTrimAssets;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Codecs;
import powercyphe.starbound.common.Starbound;
import powercyphe.starbound.common.item.StarryGelItem;
import powercyphe.starbound.common.item.StarryGoliathItem;
import powercyphe.starbound.common.item.StarrySpyglassItem;
import powercyphe.starbound.common.item.consume.StarryInvisibilityConsumeEffect;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ModItems {

    public static final RegistryKey<ItemGroup> STARBOUND_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Starbound.id(Starbound.MOD_ID + "_group"));
    public static ItemGroup STARBOUND_GROUP = FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup." + Starbound.MOD_ID))
            .icon(() -> ModItems.STARRY_GOLIATH.getDefaultStack())
            .build();

    public static class Materials {

        public static final ToolMaterial STARRY_TOOL_MATERIAL = new ToolMaterial(BlockTags.AXE_MINEABLE, 1721, 8.0F, 3.0F, 10, ItemTags.DIAMOND_TOOL_MATERIALS);
        public static final RegistryKey<ArmorTrimMaterial> STARRY_TRIM_MATERIAL = trimOf("starry");

        public static void bootstrap(Registerable<ArmorTrimMaterial> registerable) {
            register(registerable, STARRY_TRIM_MATERIAL, Style.EMPTY.withColor(0x2c3c4f));
        }

        public static void register(Registerable<ArmorTrimMaterial> registerable, RegistryKey<ArmorTrimMaterial> key, Style style) {
            String id = key.getValue().getPath();
            ArmorTrimMaterial material = new ArmorTrimMaterial(ArmorTrimAssets.of(id), Text.translatable("trim_material.starbound." + id).fillStyle(style));
            registerable.register(key, material);
        }

        public static RegistryKey<ArmorTrimMaterial> trimOf(String key) {
            return RegistryKey.of(RegistryKeys.TRIM_MATERIAL, Starbound.id(key));
        }
    }

    public static class Components {

        public static final ComponentType<Boolean> STARRY_EMISSIVITY = register("starry_emissivity", builder -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN));

        public static void init() {}

        public static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
            return Registry.register(Registries.DATA_COMPONENT_TYPE, Starbound.id(id), builderOperator.apply(ComponentType.builder()).build());
        }
    }

    public static Item STARRY_GEL = register("starry_gel", StarryGelItem::new, new Item.Settings());
    public static Item STARRY_INGOT = register("starry_ingot", Item::new, new Item.Settings().trimMaterial(Materials.STARRY_TRIM_MATERIAL));

    public static Item STARRY_SPYGLASS = register("starry_spyglass", StarrySpyglassItem::new, new Item.Settings().maxCount(1));
    public static Item STARRY_TOTEM = register("starry_totem", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.DEATH_PROTECTION, StarryInvisibilityConsumeEffect.STARRY_TOTEM));
    public static Item STARRY_GOLIATH = register("starry_goliath", settings -> new StarryGoliathItem(Materials.STARRY_TOOL_MATERIAL, 4F, -2.7F, 0.5F, settings), new Item.Settings());

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, STARBOUND_GROUP_KEY, STARBOUND_GROUP);
    }

    public static Item registerBlockItem(Block block, BiFunction<Block, Item.Settings, BlockItem> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Registries.BLOCK.getId(block));
        Item returnItem = Registry.register(Registries.ITEM, itemKey, itemFactory.apply(block, settings.registryKey(itemKey)));
        addToGroup(returnItem);
        return returnItem;
    }

    public static Item register(String id, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Starbound.id(id));
        return register(itemKey, itemFactory, settings);
    }

    public static Item register(RegistryKey<Item> itemKey, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        Item returnItem = Registry.register(Registries.ITEM, itemKey, itemFactory.apply(settings.registryKey(itemKey)));
        addToGroup(returnItem);
        return returnItem;
    }

    public static void addToGroup(Item item) {
        ItemGroupEvents.modifyEntriesEvent(STARBOUND_GROUP_KEY).register(itemGroup -> itemGroup.add(item.getDefaultStack()));
    }
}
