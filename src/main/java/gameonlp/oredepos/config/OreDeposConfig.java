package gameonlp.oredepos.config;

import gameonlp.oredepos.OreDepos;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class OreDeposConfig {
    public static class Server {
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
        public static OreConfig coal;
        public static OreConfig iron;
        public static OreConfig redstone;
        public static OreConfig gold;
        public static OreConfig lapis;
        public static OreConfig diamond;
        public static OreConfig emerald;
        public static OreConfig tin;
        public static OreConfig copper;
        public static OreConfig lead;
        public static OreConfig silver;
        public static OreConfig aluminum;
        public static OreConfig uranium;
        public static OreConfig nickel;
        public static OreConfig zinc;

        public Server(ForgeConfigSpec.Builder builder) {
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
            coal = new OreConfig(builder, "coal", false, true, false, 1.0f, 17, -64, 128, 20);
            iron = new OreConfig(builder, "iron", false, true, false, 1.0f, 9, -64, 68, 20);
            redstone = new OreConfig(builder, "redstone", false, true, false, 1.0f, 8, -64, 16, 8);
            gold = new OreConfig(builder, "gold", false, true, false, 1.0f, 9, -64, 30, 5);
            lapis = new OreConfig(builder, "lapis", false, true, false, 1.0f, 7, -64, 34, 2);
            diamond = new OreConfig(builder, "diamond", false, true, false, 1.0f, 8, -64, 16, 1);
            emerald = new OreConfig(builder, "emerald", false, true, false, 1.0f, 1, -64, 33, 1);
            tin = new OreConfig(builder, "tin", true, true, false, 1.0f, 9, -64, 68, 20);
            copper = new OreConfig(builder, "copper", true, true, false, 1.0f, 9, -64, 68, 20);
            lead = new OreConfig(builder, "lead", true, true, false, 1.0f, 6, -64, 35, 8);
            silver = new OreConfig(builder, "silver", true, true, false, 1.0f, 6, -64, 35, 8);
            aluminum = new OreConfig(builder, "aluminum", true, true, false, 1.0f, 6, -64, 64, 12);
            uranium = new OreConfig(builder, "uranium", true, true, false, 1.0f, 2, -64, 64, 4);
            nickel = new OreConfig(builder, "nickel", true, true, false, 1.0f, 5, -64, 20, 5);
            zinc = new OreConfig(builder, "zinc", true, true, false, 1.0f, 14, -64, 70, 4);
            builder.pop();
            builder.pop();
        }
    }
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static void onConfigLoad() {
        OreDepos.configLoaded = true;
    }
}
