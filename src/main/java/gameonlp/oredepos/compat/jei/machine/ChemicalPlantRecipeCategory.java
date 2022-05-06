package gameonlp.oredepos.compat.jei.machine;

import com.mojang.blaze3d.vertex.PoseStack;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.crafting.ChemicalPlantRecipe;
import gameonlp.oredepos.crafting.FluidIngredient;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedList;
import java.util.List;


public class ChemicalPlantRecipeCategory implements IRecipeCategory<ChemicalPlantRecipe> {
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OreDepos.MODID, "textures/gui/chemical_plant_gui_jei.png");

    private final IDrawable bg;
    private final IDrawable icon;
    private final IDrawableStatic overlay;
    public ChemicalPlantRecipeCategory(IGuiHelper guiHelper) {
        this.bg = guiHelper.createDrawable(TEXTURE, 0, 0, 176, 76);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(RegistryManager.CHEMICAL_PLANT.get().asItem()));
        this.overlay = guiHelper.createDrawable(TEXTURE, 176, 0, 18, 45);
    }

    @Override
    public ResourceLocation getUid() {
        return ChemicalPlantRecipe.TYPE;
    }

    @Override
    public Class<? extends ChemicalPlantRecipe> getRecipeClass() {
        return ChemicalPlantRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TextComponent(RegistryManager.CHEMICAL_PLANT.get().getName().getString());
    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(ChemicalPlantRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        List<List<FluidStack>> fluidInputs = new LinkedList<>();
        for (FluidIngredient fluidIngredient : recipe.getFluidIngredients()) {
            List<FluidStack> possibilities = new LinkedList<>();
            for (Fluid value : ForgeRegistries.FLUIDS.tags().getTag(fluidIngredient.getFluidTag())) {
                possibilities.add(new FluidStack(value, fluidIngredient.getAmount(), fluidIngredient.getNbt()));
            }
            fluidInputs.add(possibilities);
        }
        ingredients.setInputLists(VanillaTypes.FLUID, fluidInputs);
        if (recipe.getResultItem() != ItemStack.EMPTY)
            ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
        if (recipe.getResultFluid() != FluidStack.EMPTY)
            ingredients.setOutput(VanillaTypes.FLUID, recipe.getResultFluid());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ChemicalPlantRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 72, 22);
        recipeLayout.getItemStacks().init(1, true, 72, 40);
        recipeLayout.getFluidStacks().init(0, true, 33, 18, 18, 45, 1000, false, overlay);
        recipeLayout.getFluidStacks().init(1, true, 51, 18, 18, 45, 1000, false, overlay);
        recipeLayout.getItemStacks().init(2, false, 128, 51);
        recipeLayout.getFluidStacks().init(2, false, 118, 4, 18, 45, 1000, false, overlay);
        recipeLayout.getItemStacks().set(ingredients);
        recipeLayout.getFluidStacks().set(ingredients);
    }

    @Override
    public void draw(ChemicalPlantRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, matrixStack, mouseX, mouseY);
    }
}