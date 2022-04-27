package gameonlp.oredepos.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ModuleItem extends Item {
    private float energyConsumptionIncrease;

    public ModuleItem(Properties p_i48487_1_, float energyConsumptionIncrease) {
        super(p_i48487_1_.stacksTo(1));
        this.energyConsumptionIncrease = energyConsumptionIncrease;
    }

    public float getEnergyConsumption(float energyDrain) {
        return (1.0f + energyConsumptionIncrease) * energyDrain;
    }

    public float getProgress(float progress){
        return progress;
    }

    public float getProductivity(float productivity){
        return productivity;
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        tooltip.add(new TranslationTextComponent("tooltip.oredepos.energy").append(" x" + (1.0f + energyConsumptionIncrease)).withStyle(energyConsumptionIncrease < 0 ? TextFormatting.GREEN : TextFormatting.RED));
        super.appendHoverText(p_77624_1_, p_77624_2_, tooltip, p_77624_4_);
    }
}
