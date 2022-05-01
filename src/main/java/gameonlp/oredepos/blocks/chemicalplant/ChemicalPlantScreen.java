package gameonlp.oredepos.blocks.chemicalplant;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.gui.FluidHelper;
import gameonlp.oredepos.gui.RenderHelper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.Collections;

public class ChemicalPlantScreen extends ContainerScreen<ChemicalPlantContainer> {

    private static final ResourceLocation GUI = new ResourceLocation(OreDepos.MODID,
            "textures/gui/chemical_plant_gui.png");

    public ChemicalPlantScreen(ChemicalPlantContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        ChemicalPlantTile tile = (ChemicalPlantTile)menu.getTileEntity();
        //noinspection deprecation
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(GUI);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, 175, 164);
        float currentEnergy = tile.energyCell.getEnergyStored();
        int maxEnergy = tile.energyCell.getMaxEnergyStored();
        this.blit(matrixStack, i + 151, j + 4, 212, 0, 18, 45);
        int filled = 45 - (int)(45 * (1 - (currentEnergy / maxEnergy)));
        this.blit(matrixStack, i + 151, j + 4 + 45 - filled, 194, 45 - filled, 18, filled);
        this.blit(matrixStack, i + 151, j + 4, 230, 0, 18, 45);
        FluidTank tank = tile.primaryInputTank;
        if (!tank.getFluid().isEmpty()) {
            FluidHelper.render(matrixStack, i + 11, j + 18, 45, tank);
            //noinspection deprecation
            this.minecraft.getTextureManager().bind(GUI);
            this.blit(matrixStack, i + 10, j + 18, 176, 0, 18, 45);
        }
        tank = tile.secondaryInputTank;
        if (!tank.getFluid().isEmpty()) {
            FluidHelper.render(matrixStack, i + 29, j + 18, 45, tank);
            //noinspection deprecation
            this.minecraft.getTextureManager().bind(GUI);
            this.blit(matrixStack, i + 28, j + 18, 176, 0, 18, 45);
        }
        tank = tile.fluidTank;
        if (!tank.getFluid().isEmpty()) {
            FluidHelper.render(matrixStack, i + 96, j + 4, 45, tank);
            //noinspection deprecation
            this.minecraft.getTextureManager().bind(GUI);
            this.blit(matrixStack, i + 95, j + 4, 176, 0, 18, 45);
        }
        RenderHelper.renderBar(matrixStack, i + 126, j + 5, 6, 43, tile.progress / tile.maxProgress, 0xFF29D825);
        RenderHelper.renderBar(matrixStack, i + 134, j + 5, 6, 43, tile.productivity, 0xFFBB18BB);
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        int i = this.leftPos;
        int j = this.topPos;
        ChemicalPlantTile tile = (ChemicalPlantTile) menu.getTileEntity();
        if (x >= i + 10 && x <= i + 27 && y >= j + 18 && y <= j + 63) {
            renderComponentTooltip(matrixStack,
                    Collections.singletonList(tile.primaryInputTank.isEmpty() ? new StringTextComponent("Nothing") :
                            new StringTextComponent(tile.primaryInputTank.getFluidAmount() + "/" + tile.primaryInputTank.getCapacity() + " ")
                                    .append(tile.primaryInputTank.getFluid().getDisplayName())), x, y);
        }
        if (x >= i + 28 && x <= i + 45 && y >= j + 18 && y <= j + 63) {
            renderComponentTooltip(matrixStack,
                    Collections.singletonList(tile.secondaryInputTank.isEmpty() ? new StringTextComponent("Nothing") :
                            new StringTextComponent(tile.secondaryInputTank.getFluidAmount() + "/" + tile.secondaryInputTank.getCapacity() + " ")
                                    .append(tile.secondaryInputTank.getFluid().getDisplayName())), x, y);
        }
        if (x >= i + 95 && x <= i + 112 && y >= j + 4 && y <= j + 49) {
            renderComponentTooltip(matrixStack,
                    Collections.singletonList(tile.fluidTank.isEmpty() ? new StringTextComponent("Nothing") :
                            new StringTextComponent(tile.fluidTank.getFluidAmount() + "/" + tile.fluidTank.getCapacity() + " ")
                                    .append(tile.fluidTank.getFluid().getDisplayName())), x, y);
        }
        if (x >= i + 151 && x <= i + 169 && y >= j + 4 && y <= j + 49) {
            renderComponentTooltip(matrixStack, Collections.singletonList(new StringTextComponent(tile.energyCell.getEnergyStored() + "/" + tile.energyCell.getMaxEnergyStored())), x, y);
        }
    }
}