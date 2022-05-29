package gameonlp.oredepos.items;

import net.minecraft.world.item.Item;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class DrillHeadItem extends Item {
    private final Item corresponding;
    public DrillHeadItem(Properties p_i48487_1_, Item corresponding) {
        super(p_i48487_1_.stacksTo(1));
        this.corresponding = corresponding;
    }

    public Item getCorresponding() {
        return corresponding;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if(enchantment.equals(Enchantments.BLOCK_FORTUNE) || enchantment.equals(Enchantments.SILK_TOUCH)){
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return true;
    }
}
