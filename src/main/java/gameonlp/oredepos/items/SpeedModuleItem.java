package gameonlp.oredepos.items;

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
}
