package gameonlp.oredepos.compat.jei.machine;

import com.mojang.blaze3d.vertex.PoseStack;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.crafting.ChemicalPlantRecipe;
import gameonlp.oredepos.crafting.FluidIngredient;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;


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
    public RecipeType<ChemicalPlantRecipe> getRecipeType() {
        return new RecipeType<>(ChemicalPlantRecipe.TYPE, ChemicalPlantRecipe.class);
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
    public void setRecipe(IRecipeLayoutBuilder builder, ChemicalPlantRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> inputs = recipe.getIngredients();
        for (int i = 0; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 73, 23 + i * 18).addItemStack(inputs.get(i).getItems()[0]);
        }
        NonNullList<FluidIngredient> fluidInputs = recipe.getFluidIngredients();
        for (int i = 0; i < fluidInputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 33 + i * 18, 18)
                    .setFluidRenderer(1000, false, 18, 45)
                    .setOverlay(overlay, 0, 0).addIngredient(VanillaTypes.FLUID, new FluidStack(ForgeRegistries.FLUIDS.tags().getTag(fluidInputs.get(i).getFluidTag()).iterator().next(), 100));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 119, 52).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 4)
                .setFluidRenderer(1000, false, 18, 45)
                .setOverlay(overlay, 0, 0)
                .addIngredient(VanillaTypes.FLUID, recipe.getResultFluid());
    }
}