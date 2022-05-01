package gameonlp.oredepos.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Set;


public interface IFluidInventory extends IInventory {
    FluidStack getFluid(int tank);

    FluidStack removeFluid(int tank, int amount);

    void setFluid(int tank, FluidStack fluidStack);
}
