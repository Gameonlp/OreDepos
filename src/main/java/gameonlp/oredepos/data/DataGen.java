package gameonlp.oredepos.data;

import gameonlp.oredepos.OreDepos;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DataGen {

    @SubscribeEvent
    public static void generate(final GatherDataEvent event){
        DataGenerator gen = event.getGenerator();
        BlockTagsProvider blockTags = new TagProvider.OreDepositBlockTags(gen, event.getExistingFileHelper());
        gen.addProvider(true, blockTags);
        gen.addProvider(true, new TagProvider.OreDepositItemTags(gen, blockTags, event.getExistingFileHelper()));
        gen.addProvider(true, new TagProvider.OreDepositFluidTags(gen, event.getExistingFileHelper()));
        gen.addProvider(true, new ModelProviders.OreDepositsItemModelProvider(gen, OreDepos.MODID, event.getExistingFileHelper()));
        gen.addProvider(true, new ModelProviders.OreDepositsBlockStateProvider(gen, OreDepos.MODID, event.getExistingFileHelper()));
        gen.addProvider(true, new OreDepositsRecipeProvider(gen));
    }
}
