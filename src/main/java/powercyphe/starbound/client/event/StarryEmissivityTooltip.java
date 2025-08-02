package powercyphe.starbound.client.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import powercyphe.starbound.common.registry.ModItems;

import java.util.List;

public class StarryEmissivityTooltip implements ItemTooltipCallback {
    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> list) {
        if (stack.getOrDefault(ModItems.Components.STARRY_EMISSIVITY, false)) {
            list.add(Text.translatable("tooltip.starbound.starry_emissivity").formatted(Formatting.GRAY));
        }
    }
}
