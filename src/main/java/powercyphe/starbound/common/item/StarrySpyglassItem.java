package powercyphe.starbound.common.item;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpyglassItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public class StarrySpyglassItem extends SpyglassItem {
    public StarrySpyglassItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.translatable("tooltip.starbound.starry_spyglass_1").formatted(Formatting.GRAY));
        textConsumer.accept(Text.translatable("tooltip.starbound.starry_spyglass_2").formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
