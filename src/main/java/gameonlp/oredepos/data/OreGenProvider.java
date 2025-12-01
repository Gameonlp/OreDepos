package gameonlp.oredepos.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gameonlp.oredepos.OreDepos;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class OreGenProvider implements DataProvider {
    protected final DataGenerator.PathProvider biomeModifierPathProvider;
    protected final DataGenerator.PathProvider configuredPathProvider;
    protected final DataGenerator.PathProvider placedPathProvider;

    public OreGenProvider(DataGenerator dataGenerator) {
        biomeModifierPathProvider = dataGenerator.createPathProvider(DataGenerator.Target.DATA_PACK, "forge/biome_modifier");
        configuredPathProvider = dataGenerator.createPathProvider(DataGenerator.Target.DATA_PACK, "worldgen/configured_feature");
        placedPathProvider = dataGenerator.createPathProvider(DataGenerator.Target.DATA_PACK, "worldgen/placed_feature");
    }

    @Override
    public void run(CachedOutput cachedOutput) throws IOException {
        //base, enabled, replace, factor, vein, min, max, count
        genOre(cachedOutput, "nether_quartz", false, 18, 0, -64, 256, 20, true);
        genOre(cachedOutput, "nether_gold", false, 9, 0, -64, 256, 5, true);
        genOre(cachedOutput, "ancient_debris", false, 3, 1, -20, 128, 1, true);
        genOre(cachedOutput, "ardite", true, 6, 0, 90, 128, 3, true);
        genOre(cachedOutput, "cobalt", true, 6, 0, 90, 128, 3, true);
        genOre(cachedOutput, "sulfur", true, 6, 0, -64, 128, 10, true);

        genOre(cachedOutput, "coal", false, 17, 0, -64, 128, 20);
        genOre(cachedOutput, "iron", false, 9, 0, -64, 68, 20);
        genOre(cachedOutput, "redstone", false, 8, 0, -64, 16, 8);
        genOre(cachedOutput, "gold", false, 9, 0, -64, 30, 5);
        genOre(cachedOutput, "lapis", false, 7, 0, -64, 34, 2);
        genOre(cachedOutput, "diamond", false, 8, 0, -64, 16, 1);
        genOre(cachedOutput, "emerald", false, 1, 0, -64, 33, 1);
        genOre(cachedOutput, "tin", true, 9, 0, -64, 68, 20);
        genOre(cachedOutput, "copper", true, 9, 0, -64, 68, 20);
        genOre(cachedOutput, "lead", true, 6, 0, -64, 35, 8);
        genOre(cachedOutput, "silver", true, 6, 0, -64, 35, 8);
        genOre(cachedOutput, "aluminum", true, 6, 0, -64, 64, 12);
        genOre(cachedOutput, "uranium", true, 2, 0, -64, 64, 4);
        genOre(cachedOutput, "nickel", true, 5, 0, -64, 20, 5);
        genOre(cachedOutput, "zinc", true, 14, 0, -20, 70, 4);
        genOre(cachedOutput, "certus_quartz", true, 4, 0, -35, 74, 4);
        genOre(cachedOutput, "osmium", true, 8, 0, -64, 36, 8);
        genOre(cachedOutput, "platinum", true, 1, 0, -64, 64, 1);
    }

    public void genOre(CachedOutput cachedOutput, String name, boolean base, int veinSize, float discardOnAir, int minHeight, int maxHeight, int count) {
        genOre(cachedOutput, name, base, veinSize, discardOnAir, minHeight, maxHeight, count, false);
    }

    public void genOre(CachedOutput cachedOutput, String name, boolean noBase, int veinSize, float discardOnAir, int minHeight, int maxHeight, int count, boolean nether) {
        if (!nether) {
            saveOreGen(cachedOutput, name + "_ore_deposit", name + "_ore_deposit_placed", "#minecraft:is_overworld", veinSize, discardOnAir, count, minHeight, maxHeight, nether);
            saveOreGen(cachedOutput, name + "_ore_deposit", name + "_ore_deposit_replace_placed", "#minecraft:is_overworld", veinSize, discardOnAir, count, minHeight, maxHeight, nether);
            if (noBase) {
                saveOreGen(cachedOutput, name + "_ore", name + "_ore_placed", "#minecraft:is_overworld", veinSize, discardOnAir, count, minHeight, maxHeight, nether);
            }
        }
        if (nether) {
            saveOreGen(cachedOutput, name + "_ore_deposit", name + "_ore_deposit_placed", "#minecraft:is_nether", veinSize, discardOnAir, count, minHeight, maxHeight, nether);
            saveOreGen(cachedOutput, name + "_ore_deposit", name + "_ore_deposit_replace_placed", "#minecraft:is_nether", veinSize, discardOnAir, count, minHeight, maxHeight, nether);
            if (noBase) {
                saveOreGen(cachedOutput, name + "_ore", name + "_ore_placed", "#minecraft:is_nether", veinSize, discardOnAir, count, minHeight, maxHeight, nether);
            }
        }
    }

    private void saveOreGen(CachedOutput p_236360_, String name, String id, String biomes, int veinSize, float discardOnAir, int count, int minHeight, int maxHeight, boolean nether) {
        JsonObject configured = new JsonObject();

        configured.addProperty("type", "minecraft:ore");

        JsonObject config = new JsonObject();
        config.addProperty("size", veinSize);
        config.addProperty("discard_chance_on_air_exposure", discardOnAir);
        JsonArray targets = createTarget(name, nether);
        config.add("targets", targets);
        configured.add("config", config);

        ResourceLocation saveLocation = new ResourceLocation(OreDepos.MODID, id);

        try {
            DataProvider.saveStable(p_236360_, configured, this.configuredPathProvider.json(saveLocation));
        } catch (IOException ioexception) {
            OreDepos.LOGGER.error("Couldn't save recipe {}", configured, ioexception);
        }

        JsonObject placed = new JsonObject();

        placed.addProperty("feature", OreDepos.MODID + ":" + id);

        JsonArray placement = createPlacement(count, minHeight, maxHeight, nether);
        placed.add("placement", placement);

        try {
            DataProvider.saveStable(p_236360_, placed, this.placedPathProvider.json(saveLocation));
        } catch (IOException ioexception) {
            OreDepos.LOGGER.error("Couldn't save recipe {}", placed, ioexception);
        }


        JsonObject modifier = new JsonObject();
        modifier.addProperty("type", "forge:add_features");
        modifier.addProperty("biomes", biomes);
        modifier.addProperty("features", "oredepos:" + id);
        modifier.addProperty("step", "underground_ores");
        saveLocation = new ResourceLocation(OreDepos.MODID, "add_" + id);

        try {
            DataProvider.saveStable(p_236360_, modifier, this.biomeModifierPathProvider.json(saveLocation));
        } catch (IOException ioexception) {
            OreDepos.LOGGER.error("Couldn't save recipe {}", modifier, ioexception);
        }

    }

    private JsonArray createPlacement(int count, int minHeight, int maxHeight, boolean nether) {
        JsonArray placement = new JsonArray();

        JsonObject countPlacement = new JsonObject();
        countPlacement.addProperty("type", "minecraft:count");
        countPlacement.addProperty("count", count);
        placement.add(countPlacement);

        JsonObject heightPlacement = new JsonObject();
        heightPlacement.addProperty("type", "minecraft:height_range");
        JsonObject trapezoidPlacement = new JsonObject();
        trapezoidPlacement.addProperty("type", "minecraft:trapezoid");
        JsonObject minHeightAbsolute = new JsonObject();
        minHeightAbsolute.addProperty("absolute", minHeight);
        trapezoidPlacement.add("min_inclusive", minHeightAbsolute);
        JsonObject maxHeightAbsolute = new JsonObject();
        maxHeightAbsolute.addProperty("absolute", maxHeight);
        trapezoidPlacement.add("max_inclusive", maxHeightAbsolute);
        heightPlacement.add("height",  trapezoidPlacement);
        placement.add(heightPlacement);

        JsonObject inSquarePlacement = new JsonObject();
        inSquarePlacement.addProperty("type", "minecraft:in_square");
        placement.add(inSquarePlacement);

        return placement;
    }

    private static @NotNull JsonArray createTarget(String name, boolean nether) {
        JsonArray targets = new JsonArray();

        JsonObject target = new JsonObject();
        JsonObject ruleTestBase =  new JsonObject();
        ruleTestBase.addProperty("predicate_type", "minecraft:tag_match");
        if (nether) {
            ruleTestBase.addProperty("tag", "minecraft:base_stone_nether");
        } else {
            ruleTestBase.addProperty("tag", "minecraft:stone_ore_replaceables");
        }
        target.add("target", ruleTestBase);
        JsonObject state = new JsonObject();
        state.addProperty("Name", OreDepos.MODID + ":" + name);
        target.add("state", state);
        targets.add(target);

        if (!nether) {
            JsonObject targetDeep = new JsonObject();
            JsonObject ruleTestDeep = new JsonObject();
            ruleTestDeep.addProperty("predicate_type", "minecraft:tag_match");
            ruleTestDeep.addProperty("tag", "minecraft:deepslate_ore_replaceables");
            targetDeep.add("target", ruleTestDeep);
            JsonObject stateDeep = new JsonObject();
            stateDeep.addProperty("Name", OreDepos.MODID + ":deepslate_" + name);
            targetDeep.add("state", stateDeep);
            targets.add(targetDeep);
        }


        return targets;
    }

    @Override
    public String getName() {
        return "Biome Modifiers for " + OreDepos.MODID;
    }
}
