package gameonlp.oredepos.crafting.grinder;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;

@IRecipeHandler.For(GrinderRecipe.class)
@ZenRegister
@ZenCodeType.Name("mods.oredepos.Grinder")
public class GrinderRecipeManager implements IRecipeManager<GrinderRecipe>, IRecipeHandler<GrinderRecipe> {
    @Override
    public RecipeType<GrinderRecipe> getRecipeType(){
        return RegistryManager.GRINDER_RECIPE_TYPE.get();
    }

    @Override
    public String dumpToCommandString(IRecipeManager manager, GrinderRecipe recipe) {
        return manager.getCommandString() + recipe.toString() + recipe.getResultItem() + "[" + recipe.getIngredients() + "]";
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack[] inputs, IItemStack outItem, int energy, int ticks){
        name = fixRecipeName(name);
        ResourceLocation resourceLocation = new ResourceLocation(CraftTweakerConstants.MOD_ID, name);
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this,
                new GrinderRecipe(resourceLocation,
                        NonNullList.of(Ingredient.EMPTY, Arrays.stream(inputs)
                                .map(i -> i.asVanillaIngredient())
                                .toArray(Ingredient[]::new)),
                        outItem.getInternal(),
                        energy,
                        ticks
                )
        ));
    }
}