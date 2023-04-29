package gameonlp.oredepos.crafting.chemicalplant;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.crafting.FluidIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;

@IRecipeHandler.For(ChemicalPlantRecipe.class)
@ZenRegister
@ZenCodeType.Name("mods.oredepos.ChemicalPlant")
public class ChemicalPlantRecipeManager implements IRecipeManager<ChemicalPlantRecipe>, IRecipeHandler<ChemicalPlantRecipe> {
    @Override
    public RecipeType<ChemicalPlantRecipe> getRecipeType(){
        return RegistryManager.CHEMICAL_PLANT_RECIPE_TYPE.get();
    }

    @Override
    public String dumpToCommandString(IRecipeManager manager, ChemicalPlantRecipe recipe) {
        return manager.getCommandString() + recipe.toString() + recipe.getResultItem() + "[" + recipe.getIngredients() + "]";
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack[] inputs, IFluidStack[] fluidInputs,  IFluidStack outFluid, int energy, int ticks) {
        addRecipe(name, inputs, fluidInputs, MCItemStack.EMPTY.get(), outFluid, energy, ticks);
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack[] inputs, IFluidStack[] fluidInputs, IItemStack outItem, IFluidStack outFluid, int energy, int ticks){
        name = fixRecipeName(name);
        ResourceLocation resourceLocation = new ResourceLocation(CraftTweakerConstants.MOD_ID, name);
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this,
                new ChemicalPlantRecipe(resourceLocation,
                        NonNullList.of(Ingredient.EMPTY, Arrays.stream(inputs)
                                .map(i -> i.asVanillaIngredient())
                                .toArray(Ingredient[]::new)),
                        NonNullList.of(FluidIngredient.EMPTY, Arrays.stream(fluidInputs).map(
                                i -> new FluidIngredient(ForgeRegistries.FLUIDS.tags().createTagKey(i.getRegistryName()), i.getAmount(), i.getTag() != null ? i.getTag().getInternal() : null)
                                ).toArray(FluidIngredient[]::new)
                        ),
                        outItem.getInternal(),
                        outFluid.getInternal(),
                        energy,
                        ticks
                )
        ));
    }
}