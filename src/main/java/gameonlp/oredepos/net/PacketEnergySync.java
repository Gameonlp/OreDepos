package gameonlp.oredepos.net;

import gameonlp.oredepos.tile.EnergyHandlerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketEnergySync {

    private final BlockPos pos;
    private final int energy;

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
        BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(msg.pos);
        if (tile instanceof EnergyHandlerTile){
            ((EnergyHandlerTile) tile).setEnergy(msg.energy);
        }
    }

    public static void encode(PacketEnergySync msg, FriendlyByteBuf buffer){
        buffer.writeBlockPos(msg.pos);
        buffer.writeInt(msg.energy);
    }

    public static PacketEnergySync decode(FriendlyByteBuf buf){
        return new PacketEnergySync(buf.readBlockPos(), buf.readInt());
    }
}
