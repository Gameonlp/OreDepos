package gameonlp.oredepos.crafting;

import net.minecraft.world.Container;
import net.minecraftforge.fluids.FluidStack;

import java.util.Set;


public interface IFluidInventory extends Container {
    FluidStack getFluid(int tank);

    FluidStack removeFluid(int tank, int amount);

    void setFluid(int tank, FluidStack fluidStack);
}
