package gameonlp.oredepos.crafting;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public interface IBaseRecipe extends IRecipe<IFluidInventory> {
    FluidStack getResultFluid();

    NonNullList<FluidIngredient> getFluidIngredients();
}
