package gameonlp.oredepos.crafting;

import net.minecraft.world.SimpleContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.LinkedList;
import java.util.List;

public class FluidInventory extends SimpleContainer implements IFluidInventory {
    private final List<FluidStack> stacks;
    private final int fluidTanks;

    public FluidInventory(int itemStacks, int fluidTanks){
        super(itemStacks);
        this.fluidTanks = fluidTanks;
        stacks = new LinkedList<>();
    }

    @Override
    public FluidStack getFluid(int tank) {
        if (tank < fluidTanks && tank >= 0)
            return stacks.get(tank);
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack removeFluid(int tank, int amount) {
        if (tank < fluidTanks && tank >= 0)
            return stacks.get(tank);
        return FluidStack.EMPTY;
    }

    @Override
    public void setFluid(int tank, FluidStack fluidStack) {
        stacks.add(tank, fluidStack);
    }
}
