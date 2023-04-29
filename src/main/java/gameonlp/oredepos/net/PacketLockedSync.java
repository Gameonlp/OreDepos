package gameonlp.oredepos.net;

import gameonlp.oredepos.tile.LockableTile;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLockedSync {

    private final BlockPos pos;
    private final boolean locked;

    public PacketLockedSync(BlockPos pos, boolean locked){
        this.pos = pos;
        this.locked = locked;
    }

    public static void handle(PacketLockedSync msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> sync(msg))
        );
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            Level world = player.getCommandSenderWorld();
            if (!world.hasChunkAt(msg.pos)) {
                return;
            }
            BlockEntity tile = world.getBlockEntity(msg.pos);
            if (tile instanceof LockableTile base) {
                base.setLocked(msg.locked);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void sync(PacketLockedSync msg){
        if (Minecraft.getInstance().level == null || !Minecraft.getInstance().level.hasChunkAt(msg.pos))
            return;
        BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(msg.pos);
        if (tile instanceof LockableTile lockableTile){
            lockableTile.setLocked(msg.locked);
        }
    }

    public static void encode(PacketLockedSync msg, FriendlyByteBuf buffer){
        buffer.writeBlockPos(msg.pos);
        buffer.writeBoolean(msg.locked);
    }

    public static PacketLockedSync decode(FriendlyByteBuf buf){
        return new PacketLockedSync(buf.readBlockPos(), buf.readBoolean());
    }
}
