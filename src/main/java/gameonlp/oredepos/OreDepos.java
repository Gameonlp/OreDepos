package gameonlp.oredepos;

import gameonlp.oredepos.blocks.chemicalplant.ChemicalPlantScreen;
import gameonlp.oredepos.blocks.grinder.GrinderScreen;
import gameonlp.oredepos.blocks.miner.MinerScreen;
import gameonlp.oredepos.blocks.smelter.SmelterScreen;
import gameonlp.oredepos.compat.TOPCompat;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.data.DataGen;
import gameonlp.oredepos.net.PacketManager;
import gameonlp.oredepos.worldgen.OreGen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("oredepos")
public class OreDepos {
    public static final String MODID = "oredepos";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public OreDepos() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::setup);

        modEventBus.addListener(DataGen::generate);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OreDeposConfig.COMMON_SPEC);

        PacketManager.setup();

        RegistryManager registryManager = new RegistryManager();
        registryManager.register(modEventBus);
    }


    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(RegistryManager.MINER_CONTAINER.get(),
                    MinerScreen::new);
            MenuScreens.register(RegistryManager.CHEMICAL_PLANT_CONTAINER.get(),
                    ChemicalPlantScreen::new);
            MenuScreens.register(RegistryManager.GRINDER_CONTAINER.get(),
                    GrinderScreen::new);
            MenuScreens.register(RegistryManager.SMELTER_CONTAINER.get(),
                    SmelterScreen::new);
        });
    }
    @SubscribeEvent
    public void enqueueIMC(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPCompat::new);
        }
    }

    @SubscribeEvent
    public void setup(ModConfigEvent event) {
        OreGen.Ore x = OreGen.Ore.EMERALD_DEPOSIT;
        OreGen.NetherOre y = OreGen.NetherOre.ANCIENT_DEBRIS_DEPOSIT;
        OreDeposConfig.onConfigLoad();
    }
}
