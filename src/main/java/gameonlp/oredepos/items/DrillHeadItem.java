package gameonlp.oredepos.items;

import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

public class DrillHeadItem extends Item {
    private final int miningLevel;
    private final ToolType toolType;

    public DrillHeadItem(Properties p_i48487_1_, int miningLevel, ToolType toolType) {
        super(p_i48487_1_.stacksTo(1));
        this.miningLevel = miningLevel;
        this.toolType = toolType;
    }

    public int getMiningLevel(){
        return this.miningLevel;
    }

    public ToolType getToolType(){
        return this.toolType;
    }
}
