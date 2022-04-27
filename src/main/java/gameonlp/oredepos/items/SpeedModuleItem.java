package gameonlp.oredepos.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SpeedModuleItem extends ModuleItem{
    private float speedIncrease;

    public SpeedModuleItem(Properties p_i48487_1_, float energyConsumptionIncrease, float speedIncrease) {
        super(p_i48487_1_, energyConsumptionIncrease);
        this.speedIncrease = speedIncrease;
    }

    @Override
    public float getProgress(float progress) {
        return (1.0f + speedIncrease) * progress;
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, tooltip, p_77624_4_);
        tooltip.add(new TranslationTextComponent("tooltip.oredepos.speed").append(" x" + (1.0f + speedIncrease)).withStyle(speedIncrease > 0 ? TextFormatting.GREEN : TextFormatting.RED));
    }
}
