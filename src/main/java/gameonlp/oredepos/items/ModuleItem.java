package gameonlp.oredepos.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
}
