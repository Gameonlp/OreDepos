package gameonlp.oredepos.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DimensionModuleItem extends ModuleItem{
    private final int width;
    private final int length;
    private final int depth;
    private final boolean inversion;

    public DimensionModuleItem(Properties p_i48487_1_, float energyConsumptionIncrease, int width, int length, int depth, boolean inversion) {
        super(p_i48487_1_, energyConsumptionIncrease);
        this.width = width;
        this.length = length;
        this.depth = depth;
        this.inversion = inversion;
    }

    @Override
    public boolean getInversion(boolean inversion) {
        return inversion || this.inversion;
    }

    @Override
    public int getLength(int length) {
        return length + this.length;
    }

    @Override
    public int getWidth(int width) {
        return width + this.width;
    }

    @Override
    public int getDepth(int depth) {
        return depth + this.depth;
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, tooltip, p_77624_4_);
        if(width != 0) {
            tooltip.add(new TranslationTextComponent("tooltip.oredepos.width").append(" +" + width).withStyle(width > 0 ? TextFormatting.GREEN : TextFormatting.RED));
        }
        if(length != 0) {
            tooltip.add(new TranslationTextComponent("tooltip.oredepos.length").append(" +" + length).withStyle(length > 0 ? TextFormatting.GREEN : TextFormatting.RED));
        }
        if(depth != 0) {
            tooltip.add(new TranslationTextComponent("tooltip.oredepos.depth").append(" +" + depth).withStyle(depth > 0 ? TextFormatting.GREEN : TextFormatting.RED));
        }
        if (inversion){
            tooltip.add(new TranslationTextComponent("tooltip.oredepos.inversion").withStyle(TextFormatting.GREEN));
        }
    }
}
