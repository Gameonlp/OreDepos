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
        BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(msg.pos);
        if (tile instanceof ModuleAcceptorTile){
            ((ModuleAcceptorTile) tile).setProductivity(msg.productivity);
        }
    }

    public static void encode(PacketProductivitySync msg, FriendlyByteBuf buffer){
        buffer.writeBlockPos(msg.pos);
        buffer.writeFloat(msg.productivity);
    }

    public static PacketProductivitySync decode(FriendlyByteBuf buf){
        return new PacketProductivitySync(buf.readBlockPos(), buf.readFloat());
    }
}
