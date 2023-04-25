package gameonlp.oredepos.compat.jei.util;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.compat.jei.ODJeiPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import org.jetbrains.annotations.Nullable;

public class EnergyHelper implements IIngredientHelper<TotalEnergy> {
    @Override
    public IIngredientType<TotalEnergy> getIngredientType() {
        return ODJeiPlugin.ENERGY;
    }

    @Override
    public String getDisplayName(TotalEnergy ingredient) {
        return ingredient.energy() + ingredient.ticks() + "FE over " + String.format("%.2f", ingredient.ticks() / 20f) + "s";
    }

    @Override
    public String getUniqueId(TotalEnergy ingredient, UidContext context) {
        return "";
    }

    @Override
    public String getModId(TotalEnergy ingredient) {
        return OreDepos.MODID;
    }

    @Override
    public String getResourceId(TotalEnergy ingredient) {
        return "energy";
    }

    @Override
    public TotalEnergy copyIngredient(TotalEnergy ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable TotalEnergy ingredient) {
        return null;
    }
}

