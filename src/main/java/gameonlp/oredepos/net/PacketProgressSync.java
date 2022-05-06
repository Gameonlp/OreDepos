package gameonlp.oredepos.net;

import gameonlp.oredepos.tile.ModuleAcceptorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketProgressSync {

    private BlockPos pos;
    private float progress;

    public PacketProgressSync(BlockPos pos, float progress){
        this.pos = pos;
        this.progress = progress;
    }

    public static void handle(PacketProgressSync msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> sync(msg))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void sync(PacketProgressSync msg){
        BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(msg.pos);
        if (tile instanceof ModuleAcceptorTile){
            ((ModuleAcceptorTile) tile).setProgress(msg.progress);
        }
    }

    public static void encode(PacketProgressSync msg, FriendlyByteBuf buffer){
        buffer.writeBlockPos(msg.pos);
        buffer.writeFloat(msg.progress);
    }

    public static PacketProgressSync decode(FriendlyByteBuf buf){
        return new PacketProgressSync(buf.readBlockPos(), buf.readFloat());
    }
}
