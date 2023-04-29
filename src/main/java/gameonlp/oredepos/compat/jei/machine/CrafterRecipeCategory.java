package gameonlp.oredepos.compat.jei.machine;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.compat.jei.ODJeiPlugin;
import gameonlp.oredepos.compat.jei.util.EnergyRenderer;
import gameonlp.oredepos.compat.jei.util.TotalEnergy;
import gameonlp.oredepos.crafting.CountIngredient;
import gameonlp.oredepos.crafting.crafter.CrafterRecipe;
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
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.LinkedList;
import java.util.List;


public class CrafterRecipeCategory implements IRecipeCategory<CrafterRecipe> {
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OreDepos.MODID, "textures/gui/crafter_gui_jei.png");

    private final IDrawable bg;
    private final IDrawable icon;
    private IGuiHelper guiHelper;

    public CrafterRecipeCategory(IGuiHelper guiHelper) {
        this.bg = guiHelper.createDrawable(TEXTURE, 0, 0, 176, 76);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(RegistryManager.CRAFTER.get().asItem()));
        this.guiHelper = guiHelper;
    }

    @Override
    public ResourceLocation getUid() {
        return CrafterRecipe.TYPE;
    }

    @Override
    public RecipeType<CrafterRecipe> getRecipeType() {
        return new RecipeType<>(CrafterRecipe.TYPE, CrafterRecipe.class);
    }

    @Override
    public Class<? extends CrafterRecipe> getRecipeClass() {
        return CrafterRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TextComponent(RegistryManager.CRAFTER.get().getName().getString());
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
    public void setRecipe(IRecipeLayoutBuilder builder, CrafterRecipe recipe, IFocusGroup focuses) {
        NonNullList<CountIngredient> inputs = recipe.getCountIngredients();
        int x = 0, y = 0;
        for (CountIngredient input : inputs) {
            List<ItemStack> itemStacks = new LinkedList<>();
            for (ItemStack item : input.getItems()) {
                ItemStack backup = item.copy();
                backup.setCount(input.getCount());
                itemStacks.add(backup);
            }
            builder.addSlot(RecipeIngredientRole.INPUT, 34 + 18 * x, 12 + 18 * y)
                    .addIngredients(VanillaTypes.ITEM, itemStacks);
            x++;
            if (x == 3) {
                y++;
                x = 0;
            }
        }
        int filled = Math.max(0, 45 - (int)(45 * (1 - (recipe.getEnergy() / 400f))));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 137, 16)
                .setCustomRenderer(ODJeiPlugin.ENERGY, new EnergyRenderer(guiHelper, filled))
                .addIngredient(ODJeiPlugin.ENERGY, new TotalEnergy(recipe.getEnergy(), recipe.getTicks()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 115, 30).addItemStack(recipe.getResultItem());
    }
}