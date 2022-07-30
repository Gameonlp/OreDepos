package gameonlp.oredepos.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class OreConfig {
    public final ForgeConfigSpec.BooleanValue enabled;
    public final ForgeConfigSpec.BooleanValue replace;
    public final ForgeConfigSpec.DoubleValue factor;
    public final ForgeConfigSpec.IntValue veinSize;
    public final ForgeConfigSpec.IntValue minHeight;
    public final ForgeConfigSpec.IntValue maxHeight;
    public final ForgeConfigSpec.DoubleValue count;
    private String name;
    public final boolean noBaseBlock;

    public OreConfig(ForgeConfigSpec.Builder builder, String name, boolean noBaseBlock, boolean enabled, boolean replace, float factor, int veinSize, int minHeight, int maxHeight, float count) {
        this.name = name;
        this.noBaseBlock = noBaseBlock;
        builder.push(name);
        builder.comment("Enable the generation of " + name + " deposits");
        this.enabled = builder.define(name + "Enable", enabled);
        builder.comment("Replace the base ore for the generation of " + name + " deposits");
        if (noBaseBlock)
            builder.comment("Replace the base ore for the generation of " + name + " deposits \n" +
                    "This will generate " + name + " ore (according to the deposit's parameters) and then replace it\n" +
                    "To make this more efficient the ore configuration is also used to get the min and max height for the ore to replace!");
        this.replace = builder.define(name + "Replace", replace);
        builder.comment("Factor for " + name + " deposit size");
        this.factor = builder.defineInRange(name + "Factor", factor, 0.0, Float.MAX_VALUE);
        builder.comment("Vein size of " + name + " deposits");
        this.veinSize = builder.defineInRange(name + "VeinSize", veinSize, 0, Integer.MAX_VALUE);
        builder.comment("Minimum height of " + name + " deposits (currently NOT working)");
        this.minHeight = builder.defineInRange(name + "MinHeight", minHeight, -64, Integer.MAX_VALUE);
        builder.comment("Maximum height of " + name + " deposits (currently NOT working)");
        this.maxHeight = builder.defineInRange(name + "MaxHeight", maxHeight, -64, Integer.MAX_VALUE);
        builder.comment("Maximum count of " + name + " deposits per chunk");
        this.count = builder.defineInRange(name + "Count", count, 0, 256);
        builder.pop();

    }
}
