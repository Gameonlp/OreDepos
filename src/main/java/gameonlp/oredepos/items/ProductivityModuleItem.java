package gameonlp.oredepos.items;

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
}
