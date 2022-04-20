package gameonlp.oredepos.net;

import gameonlp.oredepos.OreDepos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketManager {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(OreDepos.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void setup() {
        int id = 0;
        INSTANCE.registerMessage(id++, PacketEnergySync.class, PacketEnergySync::encode, PacketEnergySync::decode, PacketEnergySync::handle);
        INSTANCE.registerMessage(id++, PacketFluidSync.class, PacketFluidSync::encode, PacketFluidSync::decode, PacketFluidSync::handle);
    }
}
