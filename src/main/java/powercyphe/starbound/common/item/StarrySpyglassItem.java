package powercyphe.starbound.common.item;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class StarrySpyglassItem extends SpyglassItem {
    public StarrySpyglassItem(Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.translatable("tooltip.starbound.starry_spyglass_1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("tooltip.starbound.starry_spyglass_2").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
    }
}
