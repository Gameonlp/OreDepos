package gameonlp.oredepos.items;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class OreDeposTab extends ItemGroup {
    public static final OreDeposTab ORE_DEPOS_TAB = new OreDeposTab();

    public OreDeposTab() {
        super("oreDeposits");
    }

    @Override
    public ItemStack makeIcon() {
        return RegistryManager.DIAMOND_PICKAXE_DRILL_HEAD.getDefaultInstance();
    }
}
