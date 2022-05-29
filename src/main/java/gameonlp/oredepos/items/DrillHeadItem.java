package gameonlp.oredepos.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if(enchantment.equals(Enchantments.BLOCK_FORTUNE) || enchantment.equals(Enchantments.SILK_TOUCH)){
            return true;
        }
        return false;
    }
    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 40;
    }
}
