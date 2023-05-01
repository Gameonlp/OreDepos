package gameonlp.oredepos.items;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class SpeedModuleItem extends ModuleItem{
    private final float speedIncrease;

    public SpeedModuleItem(Properties p_i48487_1_, float energyConsumptionIncrease, float speedIncrease) {
        super(p_i48487_1_, energyConsumptionIncrease);
        this.speedIncrease = speedIncrease;
    }

    @Override
    public float getProgress(float progress) {
        return speedIncrease + progress;
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable Level p_77624_2_, List<Component> tooltip, TooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, tooltip, p_77624_4_);
        tooltip.add(new TranslatableComponent("tooltip.oredepos.speed").append(" x" + (1.0f + speedIncrease)).withStyle(speedIncrease > 0 ? ChatFormatting.GREEN : ChatFormatting.RED));
    }
}
