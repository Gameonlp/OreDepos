package gameonlp.oredepos;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class OreDeposConfig {
    public static class Common {
        public static ForgeConfigSpec.IntValue shortDistance;
        public static ForgeConfigSpec.IntValue mediumDistance;
        public static ForgeConfigSpec.IntValue longDistance;
        public static ForgeConfigSpec.IntValue leastShortDistance;
        public static ForgeConfigSpec.IntValue leastMediumDistance;
        public static ForgeConfigSpec.IntValue leastLongDistance;
        public static ForgeConfigSpec.IntValue mostShortDistance;
        public static ForgeConfigSpec.IntValue mostMediumDistance;
        public static ForgeConfigSpec.IntValue mostLongDistance;
        public static ForgeConfigSpec.BooleanValue longDistanceIncreasesFurther;
        public static ForgeConfigSpec.DoubleValue coalFactor;
        public static ForgeConfigSpec.BooleanValue coalEnable;
        public static ForgeConfigSpec.DoubleValue ironFactor;
        public static ForgeConfigSpec.BooleanValue ironEnable;
        public static ForgeConfigSpec.DoubleValue goldFactor;
        public static ForgeConfigSpec.BooleanValue goldEnable;
        public static ForgeConfigSpec.DoubleValue diamondFactor;
        public static ForgeConfigSpec.BooleanValue diamondEnable;
        public static ForgeConfigSpec.DoubleValue emeraldFactor;
        public static ForgeConfigSpec.BooleanValue emeraldEnable;
        public static ForgeConfigSpec.DoubleValue redstoneFactor;
        public static ForgeConfigSpec.BooleanValue redstoneEnable;
        public static ForgeConfigSpec.DoubleValue lapisFactor;
        public static ForgeConfigSpec.BooleanValue lapisEnable;
        public static ForgeConfigSpec.DoubleValue tinFactor;
        public static ForgeConfigSpec.BooleanValue tinEnable;
        public static ForgeConfigSpec.DoubleValue copperFactor;
        public static ForgeConfigSpec.BooleanValue copperEnable;
        public static ForgeConfigSpec.DoubleValue leadFactor;
        public static ForgeConfigSpec.BooleanValue leadEnable;
        public static ForgeConfigSpec.DoubleValue silverFactor;
        public static ForgeConfigSpec.BooleanValue silverEnable;
        public static ForgeConfigSpec.DoubleValue aluminumFactor;
        public static ForgeConfigSpec.BooleanValue aluminumEnable;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("Deposits");
            builder.push("Short Distance");
            builder.comment("How many blocks from spawn is short distance stops");
            shortDistance = builder.defineInRange("shortDistance", 1000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at least in a short distance deposit");
            leastShortDistance = builder.defineInRange("leastShortDistance", 100, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at most in a short distance deposit");
            mostShortDistance = builder.defineInRange("mostShortDistance", 200, 0, Integer.MAX_VALUE);
            builder.pop();
            builder.push("Medium Distance");
            builder.comment("How many blocks from spawn is medium distance stops");
            mediumDistance = builder.defineInRange("mediumDistance", 10000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at least in a medium distance deposit");
            leastMediumDistance = builder.defineInRange("leastMediumDistance", 1000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at most in a medium distance deposit");
            mostMediumDistance = builder.defineInRange("mostMediumDistance", 2000, 0, Integer.MAX_VALUE);
            builder.pop();
            builder.push("Long Distance");
            builder.comment("How many blocks from spawn long distance is at a maximum");
            longDistance = builder.defineInRange("longDistance", 100000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at least in a long distance deposit");
            leastLongDistance = builder.defineInRange("leastLongDistance", 10000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at most in a long distance deposit");
            mostLongDistance = builder.defineInRange("mostLongDistance", 20000, 0, Integer.MAX_VALUE);
            builder.comment("If this setting is set long distance deposits increase in maximum size by most - least every 'longDistance' blocks");
            longDistanceIncreasesFurther = builder.define("longDistanceIncreasesFurther", true);
            builder.pop();
            builder.push("Ore Settings");
            builder.comment("Factor for coal deposit size");
            coalFactor = builder.defineInRange("coalFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of coal deposits");
            coalEnable = builder.define("coalEnable", true);
            builder.comment("Factor for iron deposit size");
            ironFactor = builder.defineInRange("ironFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of iron deposits");
            ironEnable = builder.define("ironEnable", true);
            builder.comment("Factor for gold deposit size");
            goldFactor = builder.defineInRange("goldFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of gold deposits");
            goldEnable = builder.define("goldEnable", true);
            builder.comment("Factor for diamond deposit size");
            diamondFactor = builder.defineInRange("diamondFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of diamond deposits");
            diamondEnable = builder.define("diamondEnable", true);
            builder.comment("Factor for emerald deposit size");
            emeraldFactor = builder.defineInRange("emeraldFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of emerald deposits");
            emeraldEnable = builder.define("emeraldEnable", true);
            builder.comment("Factor for redstone deposit size");
            redstoneFactor = builder.defineInRange("redstoneFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of redstone deposits");
            redstoneEnable = builder.define("redstoneEnable", true);
            builder.comment("Factor for lapis deposit size");
            lapisFactor = builder.defineInRange("lapisFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of lapis deposits");
            lapisEnable = builder.define("lapisEnable", true);
            builder.comment("Factor for copper deposit size");
            copperFactor = builder.defineInRange("copperFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of copper deposits");
            copperEnable = builder.define("copperEnable", true);
            builder.comment("Factor for tin deposit size");
            tinFactor = builder.defineInRange("tinFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of tin deposits");
            tinEnable = builder.define("tinEnable", true);
            builder.comment("Factor for lead deposit size");
            leadFactor = builder.defineInRange("leadFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of lead deposits");
            leadEnable = builder.define("leadEnable", true);
            builder.comment("Factor for silver deposit size");
            silverFactor = builder.defineInRange("silverFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of silver deposits");
            silverEnable = builder.define("silverEnable", true);
            builder.comment("Factor for aluminum deposit size");
            aluminumFactor = builder.defineInRange("aluminumFactor", 1.0, 0.0, Float.MAX_VALUE);
            builder.comment("Enable the generation of aluminum deposits");
            aluminumEnable = builder.define("aluminumEnable", true);
            builder.pop();
            builder.pop();
        }
    }
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static void onConfigLoad() {
        OreDepos.configLoaded = true;
    }
}
