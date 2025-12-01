package gameonlp.oredepos.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class OreConfig {
    public final ForgeConfigSpec.DoubleValue factor;
    private final String name;

    public OreConfig(ForgeConfigSpec.Builder builder, String name, float factor) {
        this.name = name;
        builder.push(name);
        builder.comment("Factor for " + name + " deposit size");
        this.factor = builder.defineInRange(name + "Factor", factor, 0.0, Float.MAX_VALUE);
        builder.pop();

    }

    public String getName() {
        return name;
    }
}
