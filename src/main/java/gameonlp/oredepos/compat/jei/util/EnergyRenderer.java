package gameonlp.oredepos.compat.jei.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gameonlp.oredepos.OreDepos;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;

import java.util.Collections;
import java.util.List;

public class EnergyRenderer implements IIngredientRenderer<TotalEnergy> {
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(OreDepos.MODID, "textures/gui/chemical_plant_gui_jei.png");
    private final IDrawableStatic energyBG;
    private final IDrawableStatic energy;
    private final IDrawableStatic energyOverlay;
    private final IGuiHelper guiHelper;
    private final int filled;
    private final int width;
    private final int height;

    public EnergyRenderer() {
        energyBG = null;
        energy = null;
        energyOverlay = null;
        guiHelper = null;
        filled = 0;
        width = 16;
        height = 16;
    }

    public EnergyRenderer(IGuiHelper guiHelper, int filled) {
        this.guiHelper = guiHelper;
        this.filled = filled;
        this.energyBG = guiHelper.createDrawable(TEXTURE, 212, 0, 18, 45);
        this.width = 18;
        this.height = 45;
        this.energy = guiHelper.createDrawable(TEXTURE, 194, 45 - filled, 18, filled);
        this.energyOverlay = guiHelper.createDrawable(TEXTURE, 230, 0, 18, 45);
    }

    @Override
    public void render(PoseStack stack, TotalEnergy ingredient) {
        if (guiHelper == null) {
            return;
        }
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        energyBG.draw(stack);
        energy.draw(stack, 0, 45 - filled);
        energyOverlay.draw(stack);
    }

    @Override
    public List<Component> getTooltip(TotalEnergy ingredient, TooltipFlag tooltipFlag) {
        return Collections.singletonList(new TextComponent(ingredient.energy() + ingredient.ticks() + "FE over " + String.format("%.2f", ingredient.ticks() / 20f) + "s (" + ingredient.energy() + "FE/t)"));
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
