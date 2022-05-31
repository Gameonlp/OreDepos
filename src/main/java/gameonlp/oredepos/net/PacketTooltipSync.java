package gameonlp.oredepos.net;

import gameonlp.oredepos.blocks.miner.MinerTile;
import gameonlp.oredepos.tile.ModuleAcceptorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class PacketTooltipSync {

    private BlockPos pos;
    private List<ITextComponent> tooltip;

    public PacketTooltipSync(BlockPos pos, List<ITextComponent> tooltip){
        this.pos = pos;
        this.tooltip = tooltip;
    }

    public static void handle(PacketTooltipSync msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> sync(msg))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void sync(PacketTooltipSync msg){
        if (Minecraft.getInstance().level == null || !Minecraft.getInstance().level.hasChunkAt(msg.pos))
            return;
        TileEntity tile = Minecraft.getInstance().level.getBlockEntity(msg.pos);
        if (tile instanceof MinerTile){
            ((MinerTile) tile).setReason(msg.tooltip);
        }
    }

    public static void encode(PacketTooltipSync msg, PacketBuffer buffer){
        buffer.writeBlockPos(msg.pos);
        for (ITextComponent reason : msg.tooltip) {
            buffer.writeBoolean(true);
            buffer.writeComponent(reason);
        }
        buffer.writeBoolean(false);
    }

    public static PacketTooltipSync decode(PacketBuffer buf){
        BlockPos pos = buf.readBlockPos();
        List<ITextComponent> tooltip = new LinkedList<>();
        while (buf.readBoolean()){
            tooltip.add(buf.readComponent());
        }
        return new PacketTooltipSync(pos, tooltip);
    }
}
