package gameonlp.oredepos.compat.jei;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.chemicalplant.ChemicalPlantScreen;
import gameonlp.oredepos.compat.jei.machine.ChemicalPlantRecipeCategory;
import gameonlp.oredepos.crafting.ChemicalPlantRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class ODJeiPlugin implements IModPlugin {

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        RecipeManager recipeManager = null;
        ClientWorld world = Minecraft.getInstance().level;
        if (world != null) {
            recipeManager = world.getRecipeManager();
        }
        if (recipeManager == null) {
            return;
        }
        registration.addRecipes(recipeManager.getAllRecipesFor(RegistryManager.CHEMICAL_PLANT_RECIPE_TYPE), ChemicalPlantRecipe.TYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new ChemicalPlantRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ChemicalPlantScreen.class, 72, 33, 19, 14, ChemicalPlantRecipe.TYPE);
    }

        @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(RegistryManager.CHEMICAL_PLANT.get().asItem()), ChemicalPlantRecipe.TYPE);
    }

    @Override
    public ResourceLocation getPluginUid() {

        return new ResourceLocation(OreDepos.MODID, "jei");
    }
}