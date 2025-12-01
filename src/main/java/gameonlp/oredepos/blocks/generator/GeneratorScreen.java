package gameonlp.oredepos.blocks.generator;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.blocks.generator.GeneratorContainer;
import gameonlp.oredepos.blocks.generator.GeneratorTile;
import gameonlp.oredepos.gui.RenderHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Collections;

public class GeneratorScreen extends AbstractContainerScreen<GeneratorContainer> {

    private static final ResourceLocation GUI = new ResourceLocation(OreDepos.MODID,
            "textures/gui/generator_gui.png");

    public GeneratorScreen(GeneratorContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        GeneratorTile tile = (GeneratorTile)menu.getTileEntity();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, 175, 164);
        float currentEnergy = tile.getEnergyCell().getEnergyStored();
        int maxEnergy = tile.getEnergyCell().getMaxEnergyStored();
        this.blit(matrixStack, i + 151, j + 4, 212, 0, 18, 45);
        int filled = 45 - (int)(45 * (1 - (currentEnergy / maxEnergy)));
        this.blit(matrixStack, i + 151, j + 4 + 45 - filled, 194, 45 - filled, 18, filled);
        this.blit(matrixStack, i + 151, j + 4, 230, 0, 18, 45);
        RenderHelper.renderBar(matrixStack, i + 135, j + 5, 6, 43, tile.getProgress() / tile.getMaxProgress(), 0xFF29D825);
        RenderHelper.renderBar(matrixStack, i + 143, j + 5, 6, 43, tile.getProductivity(), 0xFFBB18BB);
    }

    @Override
    protected void renderTooltip(PoseStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        int i = this.leftPos;
        int j = this.topPos;
        GeneratorTile tile = (GeneratorTile) menu.getTileEntity();
        if (x >= i + 151 && x <= i + 169 && y >= j + 4 && y <= j + 49) {
            renderComponentTooltip(matrixStack, Collections.singletonList(Component.literal(tile.getEnergyCell().getEnergyStored() + "/" + tile.getEnergyCell().getMaxEnergyStored())), x, y);
        }
    }
}