package gameonlp.oredepos.crafting.crafter;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.crafting.CountIngredient;
import gameonlp.oredepos.crafting.crafter.CrafterRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.Optional;

@IRecipeHandler.For(CrafterRecipe.class)
@ZenRegister
@ZenCodeType.Name("mods.oredepos.Crafter")
public class CrafterRecipeManager implements IRecipeManager<CrafterRecipe>, IRecipeHandler<CrafterRecipe> {
    @Override
    public RecipeType<CrafterRecipe> getRecipeType(){
        return RegistryManager.CRAFTER_RECIPE_TYPE.get();
    }

    @Override
    public String dumpToCommandString(IRecipeManager manager, CrafterRecipe recipe) {
        return manager.getCommandString() + recipe.toString() + recipe.getResultItem() + "[" + recipe.getIngredients() + "]";
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack[] inputs, IItemStack outItem, int energy, int ticks){
        name = fixRecipeName(name);
        ResourceLocation resourceLocation = new ResourceLocation(CraftTweakerConstants.MOD_ID, name);
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this,
                new CrafterRecipe(resourceLocation,
                        NonNullList.of(CountIngredient.of(Ingredient.EMPTY, 0), Arrays.stream(inputs)
                                .map(i -> CountIngredient.of(i.asVanillaIngredient(), i.getAmount()))
                                .toArray(CountIngredient[]::new)),
                        outItem.getInternal(),
                        energy,
                        ticks
                )
        ));
    }

    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super CrafterRecipe> manager, CrafterRecipe firstRecipe, U secondRecipe) {
        return false;
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super CrafterRecipe> manager, CrafterRecipe recipe) {
        return Optional.empty();
    }

    @Override
    public Optional<CrafterRecipe> recompose(IRecipeManager<? super CrafterRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        return Optional.empty();
    }
}