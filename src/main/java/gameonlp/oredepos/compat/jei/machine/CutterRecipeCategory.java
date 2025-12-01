package gameonlp.oredepos.compat.jei.machine;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.compat.jei.ODJeiPlugin;
import gameonlp.oredepos.compat.jei.util.EnergyRenderer;
import gameonlp.oredepos.compat.jei.util.TotalEnergy;
import gameonlp.oredepos.crafting.cutter.CutterRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;


public class CutterRecipeCategory implements IRecipeCategory<CutterRecipe> {
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OreDepos.MODID, "textures/gui/cutter_gui_jei.png");

    private final IDrawable bg;
    private final IDrawable icon;
    private IGuiHelper guiHelper;

    public CutterRecipeCategory(IGuiHelper guiHelper) {
        this.bg = guiHelper.createDrawable(TEXTURE, 0, 0, 126, 55);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.CUTTER.get().asItem()));
        this.guiHelper = guiHelper;
    }

    @Override
    public RecipeType<CutterRecipe> getRecipeType() {
        return new RecipeType<>(CutterRecipe.TYPE, CutterRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.literal(RegistryManager.CUTTER.get().getName().getString());
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
    public void setRecipe(IRecipeLayoutBuilder builder, CutterRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> inputs = recipe.getIngredients();
        builder.addSlot(RecipeIngredientRole.INPUT, 17, 19).addItemStack(inputs.get(0).getItems()[0]);
        int filled = Math.max(0, 45 - (int)(45 * (1 - (recipe.getEnergy() / 400f))));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 94, 4)
                .setCustomRenderer(ODJeiPlugin.ENERGY, new EnergyRenderer(guiHelper, filled))
                .addIngredient(ODJeiPlugin.ENERGY, new TotalEnergy(recipe.getEnergy(), recipe.getTicks()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 19).addItemStack(recipe.getResultItem());
    }
}