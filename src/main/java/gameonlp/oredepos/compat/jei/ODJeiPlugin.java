package gameonlp.oredepos.compat.jei;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.chemicalplant.ChemicalPlantScreen;
import gameonlp.oredepos.compat.jei.machine.ChemicalPlantRecipeCategory;
import gameonlp.oredepos.crafting.ChemicalPlantRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Array;
import java.util.List;

@JeiPlugin
public class ODJeiPlugin implements IModPlugin {

    private final RecipeType<ChemicalPlantRecipe> TYPE = new RecipeType<>(ChemicalPlantRecipe.TYPE, ChemicalPlantRecipe.class);

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        RecipeManager recipeManager = null;
        ClientLevel world = Minecraft.getInstance().level;
        if (world != null) {
            recipeManager = world.getRecipeManager();
        }
        if (recipeManager == null) {
            return;
        }
        registration.addRecipes(TYPE, recipeManager.getAllRecipesFor(RegistryManager.CHEMICAL_PLANT_RECIPE_TYPE.get()));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new ChemicalPlantRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ChemicalPlantScreen.class, 72, 33, 19, 14, TYPE);
    }

        @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(RegistryManager.CHEMICAL_PLANT.get().asItem()), TYPE);
    }

    @Override
    public ResourceLocation getPluginUid() {

        return new ResourceLocation(OreDepos.MODID, "jei");
    }
}