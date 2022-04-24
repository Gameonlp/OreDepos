package gameonlp.oredepos;

import gameonlp.oredepos.blocks.miner.MinerScreen;
import gameonlp.oredepos.compat.TOPCompat;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.data.DataGen;
import gameonlp.oredepos.net.PacketManager;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("oredepos")
public class OreDepos {
    public static final String MODID = "oredepos";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static volatile boolean configLoaded;

    public OreDepos() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::enqueueIMC);

        modEventBus.addListener(DataGen::generate);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OreDeposConfig.COMMON_SPEC);

        modEventBus.addListener((ModConfig.Loading e) -> OreDeposConfig.onConfigLoad());
        modEventBus.addListener((ModConfig.Reloading e) -> OreDeposConfig.onConfigLoad());

        PacketManager.setup();

        RegistryManager registryManager = new RegistryManager();
        registryManager.register(modEventBus);
    }


    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ScreenManager.register(RegistryManager.MINER_CONTAINER.get(),
                    MinerScreen::new);
        });
    }
    @SubscribeEvent
    public void enqueueIMC(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPCompat::new);
        }
    }
}
