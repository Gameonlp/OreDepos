package gameonlp.oredepos.blocks.beacon;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.blocks.beacon.BeaconContainer;
import gameonlp.oredepos.blocks.beacon.BeaconTile;
import gameonlp.oredepos.gui.RenderHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Collections;

public class BeaconScreen extends AbstractContainerScreen<BeaconContainer> {

    private static final ResourceLocation GUI = new ResourceLocation(OreDepos.MODID,
            "textures/gui/beacon_gui.png");

    public BeaconScreen(BeaconContainer screenContainer, Inventory inv, Component titleIn) {
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
        BeaconTile tile = (BeaconTile)menu.getTileEntity();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, 175, 164);
        float currentEnergy = tile.getEnergyCell().getEnergyStored();
        int maxEnergy = tile.getEnergyCell().getMaxEnergyStored();
        this.blit(matrixStack, i + 115, j + 21, 212, 0, 18, 45);
        int filled = 45 - (int)(45 * (1 - (currentEnergy / maxEnergy)));
        this.blit(matrixStack, i + 115, j + 21 + 45 - filled, 194, 45 - filled, 18, filled);
        this.blit(matrixStack, i + 115, j + 21, 230, 0, 18, 45);
    }

    @Override
    protected void renderTooltip(PoseStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        int i = this.leftPos;
        int j = this.topPos;
        BeaconTile tile = (BeaconTile) menu.getTileEntity();
        if (x >= i + 115 && x <= i + 133 && y >= j + 21 && y <= j + 66) {
            renderComponentTooltip(matrixStack, Collections.singletonList(new TextComponent(tile.getEnergyCell().getEnergyStored() + "/" + tile.getEnergyCell().getMaxEnergyStored())), x, y);
        }
    }
}