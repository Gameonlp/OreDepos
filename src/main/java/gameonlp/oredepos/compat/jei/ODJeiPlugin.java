package gameonlp.oredepos.compat.jei;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.blocks.chemicalplant.ChemicalPlantScreen;
import gameonlp.oredepos.blocks.crafter.CrafterManager;
import gameonlp.oredepos.blocks.crafter.CrafterScreen;
import gameonlp.oredepos.blocks.grinder.GrinderScreen;
import gameonlp.oredepos.blocks.smelter.SmelterScreen;
import gameonlp.oredepos.blocks.smelter.SmelterTile;
import gameonlp.oredepos.compat.jei.machine.ChemicalPlantRecipeCategory;
import gameonlp.oredepos.compat.jei.machine.CrafterRecipeCategory;
import gameonlp.oredepos.compat.jei.machine.GrinderRecipeCategory;
import gameonlp.oredepos.compat.jei.machine.SmelterRecipeCategory;
import gameonlp.oredepos.compat.jei.util.EnergyHelper;
import gameonlp.oredepos.compat.jei.util.EnergyRenderer;
import gameonlp.oredepos.compat.jei.util.TotalEnergy;
import gameonlp.oredepos.crafting.chemicalplant.ChemicalPlantRecipe;
import gameonlp.oredepos.crafting.crafter.CrafterRecipe;
import gameonlp.oredepos.crafting.grinder.GrinderRecipe;
import gameonlp.oredepos.crafting.smelter.SmelterRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

@JeiPlugin
public class ODJeiPlugin implements IModPlugin {

    public static final IIngredientType<TotalEnergy> ENERGY = () -> TotalEnergy.class;
    private final RecipeType<ChemicalPlantRecipe> CHEMICAL_PLANT_TYPE = new RecipeType<>(ChemicalPlantRecipe.TYPE, ChemicalPlantRecipe.class);
    private final RecipeType<GrinderRecipe> GRINDER_TYPE = new RecipeType<>(GrinderRecipe.TYPE, GrinderRecipe.class);
    private final RecipeType<SmelterRecipe> SMELTER_TYPE = new RecipeType<>(SmelterRecipe.TYPE, SmelterRecipe.class);
    private final RecipeType<CrafterRecipe> CRAFTER_TYPE = new RecipeType<>(CrafterRecipe.TYPE, CrafterRecipe.class);

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
        List<SmelterRecipe> smelterRecipes = new LinkedList<>(recipeManager.getAllRecipesFor(RegistryManager.SMELTER_RECIPE_TYPE.get()));
        List<AbstractCookingRecipe> furnaceRecipes = new LinkedList<>(recipeManager.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.SMELTING));
        furnaceRecipes.addAll(recipeManager.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.BLASTING));
        furnaceRecipes.addAll(recipeManager.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.SMOKING));
        for (AbstractCookingRecipe smeltingRecipe : furnaceRecipes) {
            smelterRecipes.add(new SmelterRecipe(
                    new ResourceLocation("oredepos", "wrapped_" + smeltingRecipe.getId().getPath() + "_for_smelter"),
                    smeltingRecipe.getIngredients(),
                    smeltingRecipe.getResultItem(),
                    SmelterTile.getVanillaDrain(),
                    smeltingRecipe.getCookingTime() * SmelterTile.getVanillaSpeedFactor()));
        }
        registration.addRecipes(SMELTER_TYPE, smelterRecipes);
        CrafterManager crafterManager = new CrafterManager();
        crafterManager.refresh(world);
        registration.addRecipes(CRAFTER_TYPE, crafterManager.possibilities());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ChemicalPlantRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new GrinderRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SmelterRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CrafterRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ChemicalPlantScreen.class, 71, 33, 20, 14, CHEMICAL_PLANT_TYPE);
        registration.addRecipeClickArea(GrinderScreen.class, 51, 35, 20, 14, GRINDER_TYPE);
        registration.addRecipeClickArea(SmelterScreen.class, 51, 35, 20, 14, SMELTER_TYPE);
        registration.addRecipeClickArea(CrafterScreen.class, 71, 35, 20, 14, CRAFTER_TYPE);
    }

        @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(RegistryManager.CHEMICAL_PLANT.get().asItem()), CHEMICAL_PLANT_TYPE);
        registration.addRecipeCatalyst(new ItemStack(RegistryManager.GRINDER.get().asItem()), GRINDER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(RegistryManager.SMELTER.get().asItem()), SMELTER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(RegistryManager.CRAFTER.get().asItem()), CRAFTER_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
       registration.register(ENERGY, new LinkedList<>(), new EnergyHelper(), new EnergyRenderer());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(OreDepos.MODID, "jei");
    }
}