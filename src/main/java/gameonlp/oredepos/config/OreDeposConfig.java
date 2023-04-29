package gameonlp.oredepos.config;

import gameonlp.oredepos.util.Configurable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;

public class OreDeposConfig {
    public static class Common {
        public static ForgeConfigSpec.IntValue shortDistance;
        public static ForgeConfigSpec.IntValue mediumDistance;
        public static ForgeConfigSpec.IntValue longDistance;
        public static ForgeConfigSpec.LongValue leastShortDistance;
        public static ForgeConfigSpec.LongValue leastMediumDistance;
        public static ForgeConfigSpec.LongValue leastLongDistance;
        public static ForgeConfigSpec.LongValue mostShortDistance;
        public static ForgeConfigSpec.LongValue mostMediumDistance;
        public static ForgeConfigSpec.LongValue mostLongDistance;
        public static ForgeConfigSpec.BooleanValue longDistanceIncreasesFurther;
        public static OreConfig coal;
        public static OreConfig iron;
        public static OreConfig redstone;
        public static OreConfig gold;
        public static OreConfig lapis;
        public static OreConfig diamond;
        public static OreConfig emerald;
        public static OreConfig nether_quartz;
        public static OreConfig nether_gold;
        public static OreConfig ancient_debris;
        public static OreConfig tin;
        public static OreConfig copper;
        public static OreConfig lead;
        public static OreConfig silver;
        public static OreConfig aluminum;
        public static OreConfig uranium;
        public static OreConfig nickel;
        public static OreConfig zinc;
        public static OreConfig certus_quartz;
        public static OreConfig sulfur;
        public static OreConfig osmium;
        public static OreConfig ardite;
        public static OreConfig cobalt;
        public static OreConfig platinum;
        public static ForgeConfigSpec.IntValue crafterDrain;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> blackListedItems;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("Deposits");
            builder.push("Short Distance");
            builder.comment("How many blocks from spawn until short distance stops");
            shortDistance = builder.defineInRange("shortDistance", 1000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at least in a short distance deposit");
            leastShortDistance = builder.defineInRange("leastShortDistance", 100, 0, Long.MAX_VALUE);
            builder.comment("How many blocks are at most in a short distance deposit");
            mostShortDistance = builder.defineInRange("mostShortDistance", 200, 0, Long.MAX_VALUE);
            builder.pop();
            builder.push("Medium Distance");
            builder.comment("How many blocks from spawn until medium distance stops");
            mediumDistance = builder.defineInRange("mediumDistance", 10000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at least in a medium distance deposit");
            leastMediumDistance = builder.defineInRange("leastMediumDistance", 1000, 0, Long.MAX_VALUE);
            builder.comment("How many blocks are at most in a medium distance deposit");
            mostMediumDistance = builder.defineInRange("mostMediumDistance", 2000, 0, Long.MAX_VALUE);
            builder.pop();
            builder.push("Long Distance");
            builder.comment("How many blocks from spawn until long distance is at a maximum");
            longDistance = builder.defineInRange("longDistance", 100000, 0, Integer.MAX_VALUE);
            builder.comment("How many blocks are at least in a long distance deposit");
            leastLongDistance = builder.defineInRange("leastLongDistance", 10000, 0, Long.MAX_VALUE);
            builder.comment("How many blocks are at most in a long distance deposit");
            mostLongDistance = builder.defineInRange("mostLongDistance", 20000, 0, Long.MAX_VALUE);
            builder.comment("If this setting is set long distance deposits increase in maximum size by most - least every 'longDistance' blocks");
            longDistanceIncreasesFurther = builder.define("longDistanceIncreasesFurther", true);
            builder.pop();
            builder.push("Machines");
            builder.push("Crafter");
            crafterDrain = builder.defineInRange("Drain per tick to craft items", 40, 0, Integer.MAX_VALUE);
            builder.comment("Decides the FE cost per tick to craft an item");
            blackListedItems = builder.defineList("Items to blacklist from crafting", new LinkedList<>(List.of()), item -> ForgeRegistries.ITEMS.containsKey(new ResourceLocation((String) item)));
            builder.comment("Disables crafting of recipes resulting in items in this list");
            builder.pop();
            builder.pop();
            builder.push("Ore Settings");
            coal = new OreConfig(builder, "coal", false, true, false, 1.0f, 17, -64, 128, 20);
            iron = new OreConfig(builder, "iron", false, true, false, 1.0f, 9, -64, 68, 20);
            redstone = new OreConfig(builder, "redstone", false, true, false, 1.0f, 8, -64, 16, 8);
            gold = new OreConfig(builder, "gold", false, true, false, 1.0f, 9, -64, 30, 5);
            lapis = new OreConfig(builder, "lapis", false, true, false, 1.0f, 7, -64, 34, 2);
            diamond = new OreConfig(builder, "diamond", false, true, false, 1.0f, 8, -64, 16, 1);
            emerald = new OreConfig(builder, "emerald", false, true, false, 1.0f, 1, -64, 33, 1);
            nether_quartz = new OreConfig(builder, "nether_quartz", false, true, false, 1.0f, 18, -64, 256, 20);
            nether_gold = new OreConfig(builder, "nether_gold", false, true, false, 1.0f, 9, -64, 256, 5);
            ancient_debris = new OreConfig(builder, "ancient_debris", false, true, false, 1.0f, 3, -20, 128, 1);
            tin = new OreConfig(builder, "tin", true, true, false, 1.0f, 9, -64, 68, 20);
            copper = new OreConfig(builder, "copper", true, true, false, 1.0f, 9, -64, 68, 20);
            lead = new OreConfig(builder, "lead", true, true, false, 1.0f, 6, -64, 35, 8);
            silver = new OreConfig(builder, "silver", true, true, false, 1.0f, 6, -64, 35, 8);
            aluminum = new OreConfig(builder, "aluminum", true, true, false, 1.0f, 6, -64, 64, 12);
            uranium = new OreConfig(builder, "uranium", true, true, false, 1.0f, 2, -64, 64, 4);
            nickel = new OreConfig(builder, "nickel", true, true, false, 1.0f, 5, -64, 20, 5);
            zinc = new OreConfig(builder, "zinc", true, true, false, 1.0f, 14, -20, 70, 4);
            certus_quartz = new OreConfig(builder, "certus_quartz", true, false, false, 1.0f, 4, -35, 74, 4);
            sulfur = new OreConfig(builder, "sulfur", true, true, false, 1.0f, 6, -64, 128, 10);
            osmium = new OreConfig(builder, "osmium", true, false, false, 1.0f, 8, -64, 36, 8);
            ardite = new OreConfig(builder, "ardite", true, false, false, 1.0f, 6, 90, 128, 3);
            cobalt = new OreConfig(builder, "cobalt", true, false, false, 1.0f, 6, 90, 128, 3);
            platinum = new OreConfig(builder, "platinum", true, false, false, 1.0f, 1, -64, 64, 1);
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
        configurables = new LinkedList<>();
    }

    private static final List<Configurable> configurables;

    public static void register(Configurable c){
        configurables.add(c);
    }

    public static void onConfigLoad() {
        for (Configurable configurable : configurables) {
            configurable.done();
        }
    }
}
