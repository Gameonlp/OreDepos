package gameonlp.oredepos.net;

import gameonlp.oredepos.tile.SettableRecipeTile;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRecipeSync {

    private final BlockPos pos;
    private final ResourceLocation recipe;

    public PacketRecipeSync(BlockPos pos, ResourceLocation recipe){
        this.pos = pos;
        this.recipe = recipe;
    }

    public static void handle(PacketRecipeSync msg, Supplier<NetworkEvent.Context> ctx) {
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
            if (tile instanceof SettableRecipeTile base) {
                base.setRecipe(msg.recipe);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void sync(PacketRecipeSync msg){
        if (Minecraft.getInstance().level == null || !Minecraft.getInstance().level.hasChunkAt(msg.pos))
            return;
        BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(msg.pos);
        if (tile instanceof SettableRecipeTile base){
            base.setRecipe(msg.recipe);
        }
    }

    public static void encode(PacketRecipeSync msg, FriendlyByteBuf buffer){
        buffer.writeBlockPos(msg.pos);
        buffer.writeResourceLocation(msg.recipe);
    }

    public static PacketRecipeSync decode(FriendlyByteBuf buf){
        return new PacketRecipeSync(buf.readBlockPos(), buf.readResourceLocation());
    }
}
