package gameonlp.oredepos.config;

import gameonlp.oredepos.util.Configurable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;

public class OreDeposConfig {
    private static boolean done;

    public static boolean isDone() {
        return done;
    }

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
        public static ForgeConfigSpec.IntValue beaconDrain;
        public static ForgeConfigSpec.IntValue generartorTransferCap;
        public static ForgeConfigSpec.IntValue generartorEnergyRate;

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
            builder.push("Crafter");
            beaconDrain = builder.defineInRange("Drain per tick to support", 120, 0, Integer.MAX_VALUE);
            builder.comment("Decides the FE cost per tick to supply module bonuses");
            builder.pop();
            builder.push("Generator");
            generartorTransferCap = builder.defineInRange("Energy transfer per tick", 120, 0, Integer.MAX_VALUE);
            builder.comment("Decides how much FE generators can transfer per tick");
            generartorEnergyRate = builder.defineInRange("Energy generation per tick", 40, 0, Integer.MAX_VALUE);
            builder.comment("Decides how much FE generators can generate per tick as a base");
            builder.pop();
            builder.pop();
            builder.push("Ore Settings");
            coal = new OreConfig(builder, "coal", 1.0f);
            iron = new OreConfig(builder, "iron", 1.0f);
            redstone = new OreConfig(builder, "redstone", 1.0f);
            gold = new OreConfig(builder, "gold", 1.0f);
            lapis = new OreConfig(builder, "lapis", 1.0f);
            diamond = new OreConfig(builder, "diamond", 1.0f);
            emerald = new OreConfig(builder, "emerald", 1.0f);
            nether_quartz = new OreConfig(builder, "nether_quartz", 1.0f);
            nether_gold = new OreConfig(builder, "nether_gold", 1.0f);
            ancient_debris = new OreConfig(builder, "ancient_debris", 1.0f);
            tin = new OreConfig(builder, "tin", 1.0f);
            copper = new OreConfig(builder, "copper", 1.0f);
            lead = new OreConfig(builder, "lead", 1.0f);
            silver = new OreConfig(builder, "silver", 1.0f);
            aluminum = new OreConfig(builder, "aluminum", 1.0f);
            uranium = new OreConfig(builder, "uranium", 1.0f);
            nickel = new OreConfig(builder, "nickel", 1.0f);
            zinc = new OreConfig(builder, "zinc", 1.0f);
            certus_quartz = new OreConfig(builder, "certus_quartz", 1.0f);
            sulfur = new OreConfig(builder, "sulfur", 1.0f);
            osmium = new OreConfig(builder, "osmium", 1.0f);
            ardite = new OreConfig(builder, "ardite", 1.0f);
            cobalt = new OreConfig(builder, "cobalt", 1.0f);
            platinum = new OreConfig(builder, "platinum", 1.0f);
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
        done = true;
        for (Configurable configurable : configurables) {
            configurable.done();
        }
    }
}
