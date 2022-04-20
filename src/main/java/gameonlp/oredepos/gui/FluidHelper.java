package gameonlp.oredepos.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidHelper {
    public static TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        FluidAttributes attributes = fluid.getAttributes();
        ResourceLocation fluidStill = attributes.getStillTexture(fluidStack);
        return Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(fluidStill);
    }

    /**
     * Renders the fluid in the tank as 16 * 16 blocks.
     *
     * Modifies the opengl texture!
     * @param matrixStack The stack that is drawn to
     * @param x the x position of the top left corner
     * @param y the y position of the top left corner
     * @param size the height to draw to
     * @param tank the tank that holds the data
     */
    public static void render(MatrixStack matrixStack, int x, int y, int size, IFluidTank tank) {
        int filled;
        int fluidColor = tank.getFluid().getFluid().getAttributes().getColor();
        float a = (fluidColor >> 24 & 0xFF) / 255.0f;
        float r = (fluidColor >> 16 & 0xFF) / 255.0f;
        float g = (fluidColor >> 8 & 0xFF) / 255.0f;
        float b = (fluidColor & 0xFF) / 255.0f;
        //noinspection deprecation
        RenderSystem.color4f(r, g, b, a);
        TextureAtlasSprite fluidSprite = FluidHelper.getStillFluidSprite(tank.getFluid());
        Minecraft.getInstance().getTextureManager().bind(fluidSprite.atlas().location());
        float current = tank.getFluid().getAmount();
        int capacity = tank.getCapacity();
        filled = size - (int)(size * (1 - (current / capacity)));
        int repetitions = 0;
        while (repetitions * 16 < filled) {
            AbstractGui.blit(matrixStack, x, y + size - Math.min((repetitions + 1) * 16,  filled), 0, 16, 16 - Math.max(((repetitions + 1) * 16 - filled), 0), fluidSprite);
            repetitions++;
        }
        RenderSystem.color4f(1f, 1f, 1f, 1f);
    }
}