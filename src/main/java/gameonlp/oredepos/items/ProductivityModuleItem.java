package gameonlp.oredepos.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ProductivityModuleItem extends SpeedModuleItem{
    private final float productivityIncrease;

    public ProductivityModuleItem(Properties p_i48487_1_, float energyConsumptionIncrease, float speedIncrease, float productivityIncrease) {
        super(p_i48487_1_, energyConsumptionIncrease, speedIncrease);
        this.productivityIncrease = productivityIncrease;
    }

    @Override
    public void getBoosts(ModuleBoosts moduleBoosts, boolean beaconModule) {
        super.getBoosts(moduleBoosts, beaconModule);
        if (beaconModule) {
            moduleBoosts.productivity *= 1 + (productivityIncrease / 2);
        }
        else {
            moduleBoosts.productivity *= 1 + productivityIncrease;
        }
    }

    @Override
    public void getBoosts(ModuleBoosts moduleBoosts) {
        getBoosts(moduleBoosts, false);
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable Level p_77624_2_, List<Component> tooltip, TooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, tooltip, p_77624_4_);
        tooltip.add(Component.translatable("tooltip.oredepos.productivity").append(" +" + productivityIncrease).withStyle(productivityIncrease > 0 ? ChatFormatting.GREEN : ChatFormatting.RED));
    }
}
