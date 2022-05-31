package gameonlp.oredepos.net;

import gameonlp.oredepos.tile.EnergyHandlerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketEnergySync {

    private BlockPos pos;
    private int energy;

    public PacketEnergySync(BlockPos pos, int energy){
        this.pos = pos;
        this.energy = energy;
    }

    public static void handle(PacketEnergySync msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> sync(msg))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void sync(PacketEnergySync msg){
        if (Minecraft.getInstance().level == null || !Minecraft.getInstance().level.hasChunkAt(msg.pos))
            return;
        TileEntity tile = Minecraft.getInstance().level.getBlockEntity(msg.pos);
        if (tile instanceof EnergyHandlerTile){
            ((EnergyHandlerTile) tile).setEnergy(msg.energy);
        }
    }

    public static void encode(PacketEnergySync msg, PacketBuffer buffer){
        buffer.writeBlockPos(msg.pos);
        buffer.writeInt(msg.energy);
    }

    public static PacketEnergySync decode(PacketBuffer buf){
        return new PacketEnergySync(buf.readBlockPos(), buf.readInt());
    }
}
