package gameonlp.oredepos.net;

import gameonlp.oredepos.OreDepos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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
        INSTANCE.registerMessage(id++, PacketProgressSync.class, PacketProgressSync::encode, PacketProgressSync::decode, PacketProgressSync::handle);
        INSTANCE.registerMessage(id++, PacketProductivitySync.class, PacketProductivitySync::encode, PacketProductivitySync::decode, PacketProductivitySync::handle);
        INSTANCE.registerMessage(id++, PacketTooltipSync.class, PacketTooltipSync::encode, PacketTooltipSync::decode, PacketTooltipSync::handle);
    }
}
