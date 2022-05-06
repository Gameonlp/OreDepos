package gameonlp.oredepos.net;

import gameonlp.oredepos.blocks.miner.MinerTile;
import gameonlp.oredepos.tile.ModuleAcceptorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class PacketTooltipSync {

    private BlockPos pos;
    private List<Component> tooltip;

    public PacketTooltipSync(BlockPos pos, List<Component> tooltip){
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
        BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(msg.pos);
        if (tile instanceof MinerTile){
            ((MinerTile) tile).setReason(msg.tooltip);
        }
    }

    public static void encode(PacketTooltipSync msg, FriendlyByteBuf buffer){
        buffer.writeBlockPos(msg.pos);
        for (Component reason : msg.tooltip) {
            buffer.writeBoolean(true);
            buffer.writeComponent(reason);
        }
        buffer.writeBoolean(false);
    }

    public static PacketTooltipSync decode(FriendlyByteBuf buf){
        BlockPos pos = buf.readBlockPos();
        List<Component> tooltip = new LinkedList<>();
        while (buf.readBoolean()){
            tooltip.add(buf.readComponent());
        }
        return new PacketTooltipSync(pos, tooltip);
    }
}
