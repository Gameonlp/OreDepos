package gameonlp.oredepos.items;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class ModuleItem extends Item {
    private final float energyConsumptionIncrease;
    private final List<String> accepted;

    public ModuleItem(Properties p_i48487_1_, float energyConsumptionIncrease) {
        super(p_i48487_1_.stacksTo(1));
        this.energyConsumptionIncrease = energyConsumptionIncrease;
        accepted = new LinkedList<>();
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

    public int getWidth(int width){
        return width;
    }

    public int getLength(int length){
        return length;
    }

    public int getDepth(int depth){
        return depth;
    }

    public boolean getInversion(boolean inversion){
        return inversion;
    }

    public boolean isAccepted(String name) {
        return accepted.contains(name);
    }

    public ModuleItem accept(String name){
        accepted.add(name);
        return this;
    }

    public ModuleItem acceptAll(Collection<String> names){
        accepted.addAll(names);
        return this;
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable Level p_77624_2_, List<Component> tooltip, TooltipFlag p_77624_4_) {
        tooltip.add(new TranslatableComponent("tooltip.oredepos.energy").append(" x" + (1.0f + energyConsumptionIncrease)).withStyle(energyConsumptionIncrease < 0 ? ChatFormatting.GREEN : ChatFormatting.RED));
        super.appendHoverText(p_77624_1_, p_77624_2_, tooltip, p_77624_4_);
    }
}
