package gameonlp.oredepos.blocks.crafter;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.blocks.crafter.CrafterContainer;
import gameonlp.oredepos.blocks.crafter.CrafterTile;
import gameonlp.oredepos.crafting.CountIngredient;
import gameonlp.oredepos.crafting.crafter.CrafterRecipe;
import gameonlp.oredepos.gui.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;
import java.util.stream.Stream;

public class CrafterScreen extends AbstractContainerScreen<CrafterContainer> {

    private static final ResourceLocation GUI = new ResourceLocation(OreDepos.MODID,
            "textures/gui/crafter_gui.png");
    private final int CYCLE = 60;
    private int frame;

    public CrafterScreen(CrafterContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        frame = 0;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        frame++;
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
            int x_ = 0, y_ = 0;
            for (CountIngredient countIngredient : tile.currentRecipe.getCountIngredients()) {
                this.itemRenderer.renderAndDecorateFakeItem(countIngredient.getItems()[(frame / CYCLE) % countIngredient.getItems().length], i + 14 + x_ * 18, j + 16 + y_ * 18);
                this.itemRenderer.renderGuiItemDecorations(font, countIngredient.getItems()[(frame / CYCLE) % countIngredient.getItems().length], i + 14 + x_ * 18, j + 16 + y_ * 18, String.valueOf(countIngredient.getCount()));
                x_++;
                if (x_ == 3) {
                    y_++;
                    x_ = 0;
                }
            }
            this.itemRenderer.renderAndDecorateFakeItem(tile.currentRecipe.getResultItem(), i + 95, j + 34);
            if (tile.handler.getPlayerAccessible().getStackInSlot(0).isEmpty()) {
                this.itemRenderer.renderGuiItemDecorations(font, tile.currentRecipe.getResultItem(), i + 95, j + 34, String.valueOf(tile.currentRecipe.getResultItem().getCount()));
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
        if (x >= i + 13 && x <= i + 67 && y >= j + 15 && y <= j + 69) {
            List<Component> components = new ArrayList<>();
            if (tile.currentRecipe != null) {
                try {
                    CountIngredient countIngredient = tile.currentRecipe.getCountIngredients().get((x - (i + 13)) / 18 + 3 * ((y - (j + 15)) / 18));
                    for (int index = 0; index < countIngredient.getItems().length; index++) {
                        ItemStack item = countIngredient.getItems()[(index + (frame / CYCLE)) % countIngredient.getItems().length];
                        try {
                            components.add(item.getTooltipLines(null, TooltipFlag.Default.NORMAL).get(0));
                        } catch (IndexOutOfBoundsException e) {
                            components.add(new TextComponent("Missing Tooltip for " + item));
                        }
                    }
                } catch (IndexOutOfBoundsException ignored) {}
            }
            renderComponentTooltip(matrixStack, components, x, y);
        }
        if (x >= i + 151 && x <= i + 169 && y >= j + 4 && y <= j + 49) {
            renderComponentTooltip(matrixStack, Collections.singletonList(new TextComponent(tile.getEnergyCell().getEnergyStored() + "/" + tile.getEnergyCell().getMaxEnergyStored())), x, y);
        }
    }

    @Override
    protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {
        this.font.draw(p_97808_, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        this.font.draw(p_97808_, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)92, 4210752);
    }
}