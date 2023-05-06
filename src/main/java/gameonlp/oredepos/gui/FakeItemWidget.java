package gameonlp.oredepos.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.crafting.CountIngredient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FakeItemWidget implements Widget, NarratableEntry, GuiEventListener {
    private static final ResourceLocation GUI = new ResourceLocation(OreDepos.MODID,
            "textures/gui/crafter_gui.png");
    private int frame;
    private final int CYCLE = 60;
    private AbstractContainerScreen<?> screen;
    private ItemRenderer itemRenderer;
    private CountIngredient countIngredient;
    private final Font font;
    private int x;
    private int y;
    private boolean enabled;

    public FakeItemWidget(AbstractContainerScreen<?> screen, ItemRenderer itemRenderer, Font font, int x, int y, boolean enabled) {
        this.screen = screen;
        this.itemRenderer = itemRenderer;
        this.font = font;
        this.x = x;
        this.y = y;
        this.enabled = enabled;
        frame = 0;
    }

    @Override
    public void render(PoseStack p_94669_, int p_94670_, int p_94671_, float p_94672_) {
        frame++;
        RenderSystem.setShaderTexture(0, GUI);
        if (enabled) {
            if (countIngredient != null && countIngredient.getItems().length > 0) {
                this.itemRenderer.renderAndDecorateFakeItem(countIngredient.getItems()[(frame / CYCLE) % countIngredient.getItems().length], x, y);
                this.itemRenderer.renderGuiItemDecorations(font, countIngredient.getItems()[(frame / CYCLE) % countIngredient.getItems().length], x, y, String.valueOf(countIngredient.getCount()));
            }
        } else {
            screen.blit(p_94669_, x - 1, y - 1, 194, 45, 18, 18);
        }
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {
    }

    public List<Component> getTooltip() {
        List<Component> components = new LinkedList<>();
        try {
            for (int index = 0; countIngredient != null && index < countIngredient.getItems().length; index++) {
                ItemStack item = countIngredient.getItems()[(index + (frame / CYCLE)) % countIngredient.getItems().length];
                Component component;
                try {
                    component = item.getTooltipLines(null, TooltipFlag.Default.NORMAL).get(0);
                } catch (IndexOutOfBoundsException e) {
                    component = new TextComponent("Missing Tooltip for " + item);
                }
                components.add(component);
            }
        } catch (IndexOutOfBoundsException ignored) {}
        return components;
    }

    public void setCountIngredient(CountIngredient countIngredient) {
        this.countIngredient = countIngredient;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isMouseOver(double x, double y) {
        return x > this.x && x <= this.x + 18 && y > this.y && y <= this.y + 18;
    }
}
