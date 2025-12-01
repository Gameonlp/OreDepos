package gameonlp.oredepos.crafting.cutter;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.crafting.cutter.CutterRecipe;
import gameonlp.oredepos.crafting.grinder.GrinderRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.checkerframework.checker.units.qual.C;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.Optional;

@IRecipeHandler.For(CutterRecipe.class)
@ZenRegister
@ZenCodeType.Name("mods.oredepos.Cutter")
public class CutterRecipeManager implements IRecipeManager<CutterRecipe>, IRecipeHandler<CutterRecipe> {
    @Override
    public RecipeType<CutterRecipe> getRecipeType(){
        return RegistryManager.CUTTER_RECIPE_TYPE.get();
    }

    @Override
    public String dumpToCommandString(IRecipeManager manager, CutterRecipe recipe) {
        return manager.getCommandString() + recipe.toString() + recipe.getResultItem() + "[" + recipe.getIngredients() + "]";
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack[] inputs, IItemStack outItem, int energy, int ticks){
        name = fixRecipeName(name);
        ResourceLocation resourceLocation = new ResourceLocation(CraftTweakerConstants.MOD_ID, name);
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this,
                new CutterRecipe(resourceLocation,
                        NonNullList.of(Ingredient.EMPTY, Arrays.stream(inputs)
                                .map(i -> i.asVanillaIngredient())
                                .toArray(Ingredient[]::new)),
                        outItem.getInternal(),
                        energy,
                        ticks
                )
        ));
    }

    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super CutterRecipe> manager, CutterRecipe firstRecipe, U secondRecipe) {
        return false;
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super CutterRecipe> manager, CutterRecipe recipe) {
        return Optional.empty();
    }

    @Override
    public Optional<CutterRecipe> recompose(IRecipeManager<? super CutterRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        return Optional.empty();
    }
}