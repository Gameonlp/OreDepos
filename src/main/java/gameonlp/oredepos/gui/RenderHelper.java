package gameonlp.oredepos.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

public class RenderHelper {

    public static void renderBar(PoseStack matrixStack, int x, int y, int width, int height, float filled, int color) {
        int filled_pixels = Math.min(height, (int) (filled * height));
        GuiComponent.fill(matrixStack, x, y + height - filled_pixels,x + width, y + height, color);
    }
}
