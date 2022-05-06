package gameonlp.oredepos.items;

import gameonlp.oredepos.RegistryManager;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class OreDeposTab extends CreativeModeTab {
    public static final OreDeposTab ORE_DEPOS_TAB = new OreDeposTab();

    public OreDeposTab() {
        super("oreDeposits");
    }

    @Override
    public ItemStack makeIcon() {
        return RegistryManager.DIAMOND_PICKAXE_DRILL_HEAD.getDefaultInstance();
    }
}
