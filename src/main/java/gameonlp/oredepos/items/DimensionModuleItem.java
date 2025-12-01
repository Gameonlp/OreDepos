package gameonlp.oredepos.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
    public void getBoosts(ModuleBoosts moduleBoosts, boolean beaconModule) {
        if (!beaconModule) {
            super.getBoosts(moduleBoosts);
            moduleBoosts.inversion |= this.inversion;
            moduleBoosts.width += this.width;
            moduleBoosts.length += this.length;
            moduleBoosts.depth += this.depth;
        }
    }

    @Override
    public void getBoosts(ModuleBoosts moduleBoosts) {
        getBoosts(moduleBoosts, false);
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable Level p_77624_2_, List<Component> tooltip, TooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, tooltip, p_77624_4_);
        if(width != 0) {
            tooltip.add(Component.translatable("tooltip.oredepos.width").append(" +" + width).withStyle(width > 0 ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
        if(length != 0) {
            tooltip.add(Component.translatable("tooltip.oredepos.length").append(" +" + length).withStyle(length > 0 ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
        if(depth != 0) {
            tooltip.add(Component.translatable("tooltip.oredepos.depth").append(" +" + depth).withStyle(depth > 0 ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
        if (inversion){
            tooltip.add(Component.translatable("tooltip.oredepos.inversion").withStyle(ChatFormatting.GREEN));
        }
    }
}
