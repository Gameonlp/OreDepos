package gameonlp.oredepos.items;

import net.minecraft.world.item.Item;

import net.minecraft.world.item.Item.Properties;

public class DrillHeadItem extends Item {
    private final Item corresponding;
    public DrillHeadItem(Properties p_i48487_1_, Item corresponding) {
        super(p_i48487_1_.stacksTo(1));
        this.corresponding = corresponding;
    }

    public Item getCorresponding() {
        return corresponding;
    }
}
