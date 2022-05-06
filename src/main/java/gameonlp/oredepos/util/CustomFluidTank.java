package gameonlp.oredepos.util;

import gameonlp.oredepos.net.PacketFluidSync;
import gameonlp.oredepos.net.PacketManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.PacketDistributor;

public class CustomFluidTank extends FluidTank {
    private BlockEntity tile;
    private int id;

    public CustomFluidTank(BlockEntity tile, int capacity, int id) {
        super(capacity);
        this.tile = tile;
        this.id = id;
    }

    @Override
    protected void onContentsChanged() {
        if (tile.getLevel() != null && !tile.getLevel().isClientSide()){
            tile.setChanged();
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketFluidSync(tile.getBlockPos(), this.fluid, id));
        }
        super.onContentsChanged();
    }

    @Override
    public void setFluid(FluidStack stack) {
        if (tile.getLevel() != null && !tile.getLevel().isClientSide()){
            tile.setChanged();
            PacketManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketFluidSync(tile.getBlockPos(), this.fluid, id));
        }
        super.setFluid(stack);
    }
}
