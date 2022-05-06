package gameonlp.oredepos.data;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.worldgen.OreGen;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class DataGen {

    @SubscribeEvent
    public static void generate(final GatherDataEvent event){
        DataGenerator gen = event.getGenerator();
        BlockTagsProvider blockTags = new TagProvider.OreDepositBlockTags(gen, event.getExistingFileHelper());
        gen.addProvider(blockTags);
        gen.addProvider(new TagProvider.OreDepositItemTags(gen, blockTags, event.getExistingFileHelper()));
        gen.addProvider(new TagProvider.OreDepositFluidTags(gen, event.getExistingFileHelper()));
    }
}
