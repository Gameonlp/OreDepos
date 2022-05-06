package gameonlp.oredepos.crafting;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.core.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public interface IBaseRecipe extends Recipe<IFluidInventory> {
    FluidStack getResultFluid();

    NonNullList<FluidIngredient> getFluidIngredients();
}
