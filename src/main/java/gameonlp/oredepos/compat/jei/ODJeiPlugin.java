package gameonlp.oredepos.compat.jei;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.chemicalplant.ChemicalPlantScreen;
import gameonlp.oredepos.blocks.grinder.GrinderScreen;
import gameonlp.oredepos.compat.jei.machine.ChemicalPlantRecipeCategory;
import gameonlp.oredepos.compat.jei.machine.GrinderRecipeCategory;
import gameonlp.oredepos.crafting.ChemicalPlantRecipe;
import gameonlp.oredepos.crafting.GrinderRecipe;
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

@JeiPlugin
public class ODJeiPlugin implements IModPlugin {

    private final RecipeType<ChemicalPlantRecipe> CHEMICAL_PLANT_TYPE = new RecipeType<>(ChemicalPlantRecipe.TYPE, ChemicalPlantRecipe.class);
    private final RecipeType<GrinderRecipe> GRINDER_TYPE = new RecipeType<>(GrinderRecipe.TYPE, GrinderRecipe.class);

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
        registration.addRecipes(CHEMICAL_PLANT_TYPE, recipeManager.getAllRecipesFor(RegistryManager.CHEMICAL_PLANT_RECIPE_TYPE.get()));
        registration.addRecipes(GRINDER_TYPE, recipeManager.getAllRecipesFor(RegistryManager.GRINDER_RECIPE_TYPE.get()));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ChemicalPlantRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new GrinderRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ChemicalPlantScreen.class, 71, 33, 20, 14, CHEMICAL_PLANT_TYPE);
        registration.addRecipeClickArea(GrinderScreen.class, 51, 30, 20, 14, GRINDER_TYPE);
    }

        @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(RegistryManager.CHEMICAL_PLANT.get().asItem()), CHEMICAL_PLANT_TYPE);
        registration.addRecipeCatalyst(new ItemStack(RegistryManager.GRINDER.get().asItem()), GRINDER_TYPE);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(OreDepos.MODID, "jei");
    }
}