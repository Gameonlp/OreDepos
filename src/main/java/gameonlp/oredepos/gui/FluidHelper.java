package gameonlp.oredepos.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.IFluidTank;

public class FluidHelper {
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
    public static void render(PoseStack matrixStack, int x, int y, int size, IFluidTank tank) {
        int filled;
        Fluid fluid = tank.getFluid().getFluid();
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
        TextureAtlasSprite fluidSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(renderProperties.getStillTexture());

        int fluidColor = renderProperties.getTintColor(tank.getFluid());
        float a = (fluidColor >> 24 & 0xFF) / 255.0f;
        float r = (fluidColor >> 16 & 0xFF) / 255.0f;
        float g = (fluidColor >> 8 & 0xFF) / 255.0f;
        float b = (fluidColor & 0xFF) / 255.0f;
        //noinspection deprecation
        RenderSystem.setShaderColor(r, g, b, a);
        RenderSystem.setShaderTexture(0, fluidSprite.atlas().location());
        float current = tank.getFluid().getAmount();
        int capacity = tank.getCapacity();
        filled = size - (int)(size * (1 - (current / capacity)));
        int repetitions = 0;
        while (repetitions * 16 < filled) {
            GuiComponent.blit(matrixStack, x, y + size - Math.min((repetitions + 1) * 16,  filled), 0, 16, 16 - Math.max(((repetitions + 1) * 16 - filled), 0), fluidSprite);
            repetitions++;
        }
        RenderSystem.clearColor(1f, 1f, 1f, 1f);
    }
}