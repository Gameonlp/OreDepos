package gameonlp.oredepos.worldgen;

import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WorldGen {

    @SubscribeEvent
    public static void generate(final BiomeLoadingEvent event){
        OreGen.oreGeneration(event);
    }
}
