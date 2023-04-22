package gameonlp.oredepos.compat.jei.machine;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.crafting.GrinderRecipe;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;


public class GrinderRecipeCategory implements IRecipeCategory<GrinderRecipe> {
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OreDepos.MODID, "textures/gui/grinder_gui_jei.png");

    private final IDrawable bg;
    private final IDrawable icon;
    private final IDrawableStatic overlay;

    public GrinderRecipeCategory(IGuiHelper guiHelper) {
        this.bg = guiHelper.createDrawable(TEXTURE, 0, 0, 176, 76);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(RegistryManager.GRINDER.get().asItem()));
        this.overlay = guiHelper.createDrawable(TEXTURE, 176, 0, 18, 45);
    }

    @Override
    public ResourceLocation getUid() {
        return GrinderRecipe.TYPE;
    }

    @Override
    public RecipeType<GrinderRecipe> getRecipeType() {
        return new RecipeType<>(GrinderRecipe.TYPE, GrinderRecipe.class);
    }

    @Override
    public Class<? extends GrinderRecipe> getRecipeClass() {
        return GrinderRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TextComponent(RegistryManager.GRINDER.get().getName().getString());
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
    public void setRecipe(IRecipeLayoutBuilder builder, GrinderRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> inputs = recipe.getIngredients();
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 23).addItemStack(inputs.get(0).getItems()[0]);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 119, 52).addItemStack(recipe.getResultItem());
    }
}