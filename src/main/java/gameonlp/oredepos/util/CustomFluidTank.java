package gameonlp.oredepos.util;

import gameonlp.oredepos.net.PacketFluidSync;
import gameonlp.oredepos.net.PacketManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.PacketDistributor;

public class CustomFluidTank extends FluidTank {
    private TileEntity tile;

    public CustomFluidTank(TileEntity tile, int capacity) {
        super(capacity);
        this.tile = tile;
    }

    @Override
    protected void onContentsChanged() {
        if (tile.getLevel() != null && !tile.getLevel().isClientSide()){
            tile.setChanged();
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketFluidSync(tile.getBlockPos(), this.fluid));
        }
        super.onContentsChanged();
    }

    @Override
    public void setFluid(FluidStack stack) {
        if (tile.getLevel() != null && !tile.getLevel().isClientSide()){
            tile.setChanged();
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketFluidSync(tile.getBlockPos(), this.fluid));
        }
        super.setFluid(stack);
    }
}
