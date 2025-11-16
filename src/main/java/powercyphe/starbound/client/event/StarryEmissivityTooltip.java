package powercyphe.starbound.client.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import powercyphe.starbound.common.registry.SBItems;

import java.util.List;

public class StarryEmissivityTooltip implements ItemTooltipCallback {
    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipType, List<Component> list) {
        if (stack.getOrDefault(SBItems.Components.STARRY_EMISSIVITY, false)) {
            list.add(Component.translatable("tooltip.starbound.starry_emissivity").withStyle(ChatFormatting.GRAY));
        }
    }
}
