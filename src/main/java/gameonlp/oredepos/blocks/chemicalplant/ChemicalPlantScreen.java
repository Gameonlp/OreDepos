package gameonlp.oredepos.blocks.chemicalplant;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.gui.FluidHelper;
import gameonlp.oredepos.gui.RenderHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.Collections;

public class ChemicalPlantScreen extends AbstractContainerScreen<ChemicalPlantContainer> {

    private static final ResourceLocation GUI = new ResourceLocation(OreDepos.MODID,
            "textures/gui/chemical_plant_gui.png");

    public ChemicalPlantScreen(ChemicalPlantContainer screenContainer, Inventory inv, Component titleIn) {
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
        ChemicalPlantTile tile = (ChemicalPlantTile)menu.getTileEntity();
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
        renderTank(tile.primaryInputTank, matrixStack, i, 11, j, 18, 10);
        renderTank(tile.secondaryInputTank, matrixStack, i, 29, j, 18, 28);
        renderTank(tile.fluidTank, matrixStack, i, 96, j, 4, 95);
        RenderHelper.renderBar(matrixStack, i + 126, j + 5, 6, 43, tile.getProgress() / tile.getMaxProgress(), 0xFF29D825);
        RenderHelper.renderBar(matrixStack, i + 134, j + 5, 6, 43, tile.getProductivity(), 0xFFBB18BB);
    }

    private void renderTank(FluidTank tank, PoseStack matrixStack, int i, int x, int j, int x1, int x2) {
        if (!tank.getFluid().isEmpty()) {
            FluidHelper.render(matrixStack, i + x, j + x1, 45, tank);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.setShaderTexture(0, GUI);
            this.blit(matrixStack, i + x2, j + x1, 176, 0, 18, 45);
        }
    }

    @Override
    protected void renderTooltip(PoseStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        int i = this.leftPos;
        int j = this.topPos;
        ChemicalPlantTile tile = (ChemicalPlantTile) menu.getTileEntity();
        if (x >= i + 10 && x <= i + 27 && y >= j + 18 && y <= j + 63) {
            renderComponentTooltip(matrixStack,
                    Collections.singletonList(tile.primaryInputTank.isEmpty() ? Component.literal("Nothing") :
                            Component.literal(tile.primaryInputTank.getFluidAmount() + "/" + tile.primaryInputTank.getCapacity() + " ")
                                    .append(tile.primaryInputTank.getFluid().getDisplayName())), x, y);
        }
        if (x >= i + 28 && x <= i + 45 && y >= j + 18 && y <= j + 63) {
            renderComponentTooltip(matrixStack,
                    Collections.singletonList(tile.secondaryInputTank.isEmpty() ? Component.literal("Nothing") :
                            Component.literal(tile.secondaryInputTank.getFluidAmount() + "/" + tile.secondaryInputTank.getCapacity() + " ")
                                    .append(tile.secondaryInputTank.getFluid().getDisplayName())), x, y);
        }
        if (x >= i + 95 && x <= i + 112 && y >= j + 4 && y <= j + 49) {
            renderComponentTooltip(matrixStack,
                    Collections.singletonList(tile.fluidTank.isEmpty() ? Component.literal("Nothing") :
                            Component.literal(tile.fluidTank.getFluidAmount() + "/" + tile.fluidTank.getCapacity() + " ")
                                    .append(tile.fluidTank.getFluid().getDisplayName())), x, y);
        }
        if (x >= i + 151 && x <= i + 169 && y >= j + 4 && y <= j + 49) {
            renderComponentTooltip(matrixStack, Collections.singletonList(Component.literal(tile.getEnergyCell().getEnergyStored() + "/" + tile.getEnergyCell().getMaxEnergyStored())), x, y);
        }
    }
}