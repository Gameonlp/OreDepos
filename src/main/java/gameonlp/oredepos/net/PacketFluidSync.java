package gameonlp.oredepos.net;

import gameonlp.oredepos.tile.EnergyHandlerTile;
import gameonlp.oredepos.tile.FluidHandlerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFluidSync {

    private BlockPos pos;
    private FluidStack fluid;

    public PacketFluidSync(BlockPos pos, FluidStack fluid){
        this.pos = pos;
        this.fluid = fluid;
    }

    public static void handle(PacketFluidSync msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> sync(msg))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void sync(PacketFluidSync msg){
        TileEntity tile = Minecraft.getInstance().level.getBlockEntity(msg.pos);
        if (tile instanceof EnergyHandlerTile){
            ((FluidHandlerTile) tile).setFluid(msg.fluid);
        }
    }

    public static void encode(PacketFluidSync msg, PacketBuffer buffer){
        buffer.writeBlockPos(msg.pos);
        buffer.writeFluidStack(msg.fluid);
    }

    public static PacketFluidSync decode(PacketBuffer buf){
        return new PacketFluidSync(buf.readBlockPos(), buf.readFluidStack());
    }
}
