package gameonlp.oredepos.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ProductivityModuleItem extends SpeedModuleItem{
    private float productivityIncrease;

    public ProductivityModuleItem(Properties p_i48487_1_, float energyConsumptionIncrease, float speedIncrease, float productivityIncrease) {
        super(p_i48487_1_, energyConsumptionIncrease, speedIncrease);
        this.productivityIncrease = productivityIncrease;
    }

    @Override
    public float getProductivity(float productivity) {
        return productivityIncrease + productivity;
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, tooltip, p_77624_4_);
        tooltip.add(new TranslationTextComponent("tooltip.oredepos.productivity").append(" +" + productivityIncrease).withStyle(productivityIncrease > 0 ? TextFormatting.GREEN : TextFormatting.RED));
    }
}
