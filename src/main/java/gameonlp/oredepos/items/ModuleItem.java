package gameonlp.oredepos.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ModuleItem extends Item {
    public static class ModuleBoosts {
        public float progress = 1;
        public float energy = 1;
        public float productivity = 0;
        public int width = 0;
        public int depth = 0;
        public int length = 0;
        public boolean inversion = false;
        public boolean ejecting = false;
        public boolean stacking = false;

    }
    private final float energyConsumptionIncrease;
    private final List<String> accepted;

    public ModuleItem(Properties p_i48487_1_, float energyConsumptionIncrease) {
        super(p_i48487_1_.stacksTo(1));
        this.energyConsumptionIncrease = energyConsumptionIncrease;
        accepted = new LinkedList<>();
    }

    public void getBoosts(ModuleBoosts moduleBoosts, boolean beaconModule) {
        if (beaconModule) {
            moduleBoosts.energy *= 1 + (energyConsumptionIncrease / 2);
        }
        else {
            moduleBoosts.energy *= 1 + energyConsumptionIncrease;
        }
    }

    public void getBoosts(ModuleBoosts moduleBoosts) {
        getBoosts(moduleBoosts, false);
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
        tooltip.add(Component.translatable("tooltip.oredepos.energy").append(" x" + (1 + energyConsumptionIncrease)).withStyle(energyConsumptionIncrease < 0 ? ChatFormatting.GREEN : ChatFormatting.RED));
        super.appendHoverText(p_77624_1_, p_77624_2_, tooltip, p_77624_4_);
    }
}
