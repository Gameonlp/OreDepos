package gameonlp.oredepos.net;

import gameonlp.oredepos.tile.ModuleAcceptorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketProductivitySync {

    private BlockPos pos;
    private float productivity;

    public PacketProductivitySync(BlockPos pos, float productivity){
        this.pos = pos;
        this.productivity = productivity;
    }

    public static void handle(PacketProductivitySync msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> sync(msg))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void sync(PacketProductivitySync msg){
        TileEntity tile = Minecraft.getInstance().level.getBlockEntity(msg.pos);
        if (tile instanceof ModuleAcceptorTile){
            ((ModuleAcceptorTile) tile).setProductivity(msg.productivity);
        }
    }

    public static void encode(PacketProductivitySync msg, PacketBuffer buffer){
        buffer.writeBlockPos(msg.pos);
        buffer.writeFloat(msg.productivity);
    }

    public static PacketProductivitySync decode(PacketBuffer buf){
        return new PacketProductivitySync(buf.readBlockPos(), buf.readFloat());
    }
}
