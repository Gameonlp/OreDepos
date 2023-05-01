package gameonlp.oredepos.data;

import com.google.gson.JsonObject;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.worldgen.old.OreGen;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class BiomeModifiers implements DataProvider {
    protected final DataGenerator.PathProvider modifierPathProvider;

    public BiomeModifiers(DataGenerator dataGenerator) {
        modifierPathProvider = dataGenerator.createPathProvider(DataGenerator.Target.DATA_PACK, "forge/biome_modifier");
    }

    @Override
    public void run(CachedOutput cachedOutput) throws IOException {
        for (OreGen.Ore value : OreGen.Ore.values()) {
            saveOreGen(cachedOutput, value.depositPlaced.getId(), "#minecraft:is_overworld");
            saveOreGen(cachedOutput, value.replacePlaced.getId(), "#minecraft:is_overworld");
            if (value.replacablePlaced != null) {
                saveOreGen(cachedOutput, value.replacablePlaced.getId(), "#minecraft:is_overworld");
            }
        }
        for (OreGen.NetherOre value : OreGen.NetherOre.values()) {
            saveOreGen(cachedOutput, value.depositPlaced.getId(), "#minecraft:is_nether");
            saveOreGen(cachedOutput, value.replacePlaced.getId(), "#minecraft:is_nether");
            if (value.replacablePlaced != null) {
                saveOreGen(cachedOutput, value.replacablePlaced.getId(), "#minecraft:is_nether");
            }
        }
    }

    private void saveOreGen(CachedOutput p_236360_, ResourceLocation id, String biomes) {
        JsonObject serialized = new JsonObject();
        serialized.addProperty("type", "forge:add_features");
        serialized.addProperty("biomes", biomes);
        serialized.addProperty("features", id.toString());
        serialized.addProperty("step", "underground_ores");
        ResourceLocation saveLocation = new ResourceLocation(id.getNamespace(), "add_" + id.getPath().substring(0, id.getPath().length() - 7));

        try {
            DataProvider.saveStable(p_236360_, serialized, this.modifierPathProvider.json(saveLocation));
        } catch (IOException ioexception) {
            OreDepos.LOGGER.error("Couldn't save recipe {}", serialized, ioexception);
        }

    }

    @Override
    public String getName() {
        return "Biome Modifiers for " + OreDepos.MODID;
    }
}
