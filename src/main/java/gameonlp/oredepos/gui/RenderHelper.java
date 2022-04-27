package gameonlp.oredepos.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;

public class RenderHelper {

    public static void renderBar(MatrixStack matrixStack, int x, int y, int width, int height, float filled, int color) {
        int filled_pixels = Math.min(height, (int) (filled * height));
        AbstractGui.fill(matrixStack, x, y + height - filled_pixels,x + width, y + height, color);
    }
}
