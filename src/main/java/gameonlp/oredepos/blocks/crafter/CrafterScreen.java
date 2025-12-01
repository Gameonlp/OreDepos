package gameonlp.oredepos.blocks.crafter;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.crafting.CountIngredient;
import gameonlp.oredepos.gui.FakeItemWidget;
import gameonlp.oredepos.gui.RenderHelper;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CrafterScreen extends AbstractContainerScreen<CrafterContainer> {

    private static final ResourceLocation GUI = new ResourceLocation(OreDepos.MODID,
            "textures/gui/crafter_gui.png");
    private int gridSize;
    private List<FakeItemWidget> fakeItemWidgets;

    public CrafterScreen(CrafterContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        CrafterTile tile = (CrafterTile)menu.getTileEntity();
        gridSize = tile.getGridSize();
        fakeItemWidgets = new LinkedList<>();
    }

    @Override
    protected void init() {
        super.init();
        layoutFakeSlots(this.leftPos, this.topPos);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        CrafterTile tile = (CrafterTile)menu.getTileEntity();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, 175, 189);
        float currentEnergy = tile.getEnergyCell().getEnergyStored();
        int maxEnergy = tile.getEnergyCell().getMaxEnergyStored();
        this.blit(matrixStack, i + 151, j + 4, 212, 0, 18, 45);
        int filled = 45 - (int)(45 * (1 - (currentEnergy / maxEnergy)));
        this.blit(matrixStack, i + 151, j + 4 + 45 - filled, 194, 45 - filled, 18, filled);
        this.blit(matrixStack, i + 151, j + 4, 230, 0, 18, 45);
        RenderHelper.renderBar(matrixStack, i + 117, j + 5, 6, 43, tile.getProgress() / tile.getMaxProgress(), 0xFF29D825);
        RenderHelper.renderBar(matrixStack, i + 125, j + 5, 6, 43, tile.getProductivity(), 0xFFBB18BB);
        if (tile.locked) {
            this.blit(matrixStack, i + 133, j + 4, 176, 45, 18, 18);
        }
        if (tile.currentRecipe != null) {
            List<CountIngredient> countIngredients = new LinkedList<>(tile.currentRecipe.getCountIngredients());
            for (int k = countIngredients.size(); k < 9; k++) {
                countIngredients.add(CountIngredient.of(Ingredient.EMPTY, 0));
            }
            for (Widget renderable : renderables) {
                if (renderable instanceof FakeItemWidget fakeItemWidget) {
                    if (fakeItemWidget.isEnabled() && countIngredients.size() > 0) {
                        fakeItemWidget.setCountIngredient(countIngredients.remove(0));
                    }
                }
            }
            if (tile.handler.getPlayerAccessible().getStackInSlot(0).isEmpty()) {
                this.itemRenderer.renderAndDecorateFakeItem(tile.currentRecipe.getResultItem(), i + 95, j + 34);
                this.itemRenderer.renderGuiItemDecorations(font, tile.currentRecipe.getResultItem(), i + 95, j + 34, String.valueOf(tile.currentRecipe.getResultItem().getCount()));
            }
        }
    }

    private void layoutFakeSlots(int i, int j) {
        List<List<Boolean>> enabledSlots = switch (gridSize) {
            case 1 -> List.of(List.of(false, false, false), List.of(false, true, false), List.of(false, false, false));
            case 3 -> List.of(List.of(false, false, false), List.of(true, true, true), List.of(false, false, false));
            case 5 -> List.of(List.of(false, true, false), List.of(true, true, true), List.of(false, true, false));
            case 7 -> List.of(List.of(true, true, true), List.of(false, true, false), List.of(true, true, true));
            case 9 -> List.of(List.of(true, true, true), List.of(true, true, true), List.of(true, true, true));
            default -> throw new IllegalStateException("Unexpected value: " + gridSize);
        };
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                fakeItemWidgets.add(addRenderableWidget(new FakeItemWidget(this, this.itemRenderer, this.font, i + 14 + l * 18, j + 16 + k * 18, enabledSlots.get(k).get(l))));
            }
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int p_97750_) {
        CrafterTile tile = (CrafterTile)menu.getTileEntity();
        int i = this.leftPos;
        int j = this.topPos;
        if (x > i + 135 && x <= i + 151 && y > j + 4 && y <= j + 20) {
            tile.toggleLocked();
            return true;
        }
        if (x > i + 96 && x <= i + 110 && y > j + 23 && y <= j + 30) {
            tile.changeRecipe(1);
            return true;
        }
        if (x > i + 96 && x <= i + 110 && y > j + 54 && y <= j + 61) {
            tile.changeRecipe(-1);
            return true;
        }
        return super.mouseClicked(x, y, p_97750_);
    }

    @Override
    protected void renderTooltip(PoseStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        int i = this.leftPos;
        int j = this.topPos;
        CrafterTile tile = (CrafterTile) menu.getTileEntity();
        if (tile.currentRecipe != null) {
            for (FakeItemWidget fakeItemWidget : fakeItemWidgets) {
                if (fakeItemWidget.isMouseOver(x, y)) {
                    renderComponentTooltip(matrixStack, fakeItemWidget.getTooltip(), x, y);
                }
            }
            if (x >= i + 95 && x <= i + 113 && y >= j + 34 && y <= j + 52 && tile.handler.getPlayerAccessible().getStackInSlot(0).isEmpty()) {
                renderComponentTooltip(matrixStack, tile.currentRecipe.getResultItem().getTooltipLines(null, TooltipFlag.Default.NORMAL), x, y);
            }
        }
        if (x >= i + 151 && x <= i + 169 && y >= j + 4 && y <= j + 49) {
            renderComponentTooltip(matrixStack, Collections.singletonList(Component.literal(tile.getEnergyCell().getEnergyStored() + "/" + tile.getEnergyCell().getMaxEnergyStored())), x, y);
        }
    }

    @Override
    protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {
        this.font.draw(p_97808_, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        this.font.draw(p_97808_, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)92, 4210752);
    }
}