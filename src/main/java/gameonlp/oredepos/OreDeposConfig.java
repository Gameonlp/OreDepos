package gameonlp.oredepos;

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

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("deposits");
            builder.comment("How many blocks from spawn is short distance");
            shortDistance = builder.defineInRange("shortDistance", 200, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at least in a short distance deposit");
            leastShortDistance = builder.defineInRange("leastShortDistance", 100, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at most in a short distance deposit");
            mostShortDistance = builder.defineInRange("mostShortDistance", 200, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks from spawn is medium distance");
            mediumDistance = builder.defineInRange("mediumDistance", 1000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at least in a medium distance deposit");
            leastMediumDistance = builder.defineInRange("leastMediumDistance", 1000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at most in a medium distance deposit");
            mostMediumDistance = builder.defineInRange("mostMediumDistance", 2000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks from spawn is long distance");
            longDistance = builder.defineInRange("longDistance", 10000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at least in a long distance deposit");
            leastLongDistance = builder.defineInRange("leastLongDistance", 10000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at most in a long distance deposit");
            mostLongDistance = builder.defineInRange("mostLongDistance", 20000, 0, Integer.MAX_VALUE);
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
