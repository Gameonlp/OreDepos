package gameonlp.oredepos.data;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class TagProvider {
    public static final Tags.IOptionalNamedTag<Block> ORE_TIN = BlockTags.createOptional(new ResourceLocation("forge", "ores/tin"));
    public static final Tags.IOptionalNamedTag<Block> ORE_COPPER = BlockTags.createOptional(new ResourceLocation("forge", "ores/copper"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_TIN = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/tin"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_COPPER = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/copper"));

    public static final Tags.IOptionalNamedTag<Item> ORE_TIN_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/tin"));
    public static final Tags.IOptionalNamedTag<Item> ORE_COPPER_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/copper"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_TIN_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/tin"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_COPPER_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/copper"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_COPPER = ItemTags.createOptional(new ResourceLocation("forge", "ingots/copper"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_TIN = ItemTags.createOptional(new ResourceLocation("forge", "ingots/tin"));

    public static final Tags.IOptionalNamedTag<Fluid> SULFURIC_ACID = FluidTags.createOptional(new ResourceLocation("forge", "sulfuric_acid"));

    public static class OreDepositBlockTags extends BlockTagsProvider {

        public OreDepositBlockTags(DataGenerator p_i48256_1_, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_i48256_1_, OreDepos.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return "Ore Deposits: Block Tags";
        }

        @Override
        protected void addTags() {
            tag(ORE_TIN).add(RegistryManager.TIN_ORE.get());
            tag(ORE_COPPER).add(RegistryManager.COPPER_ORE.get());
            tag(Tags.Blocks.ORES).addTags(
                    ORE_TIN,
                    ORE_COPPER
            );

            tag(STORAGE_BLOCKS_TIN).add(RegistryManager.TIN_BLOCK.get());
            tag(STORAGE_BLOCKS_COPPER).add(RegistryManager.COPPER_BLOCK.get());
            tag(Tags.Blocks.STORAGE_BLOCKS).addTags(
                    STORAGE_BLOCKS_TIN,
                    STORAGE_BLOCKS_COPPER
            );
        }
    }
    public static class OreDepositItemTags extends ItemTagsProvider {

        public OreDepositItemTags(DataGenerator p_i232552_1_, BlockTagsProvider p_i232552_2_, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_i232552_1_, p_i232552_2_, OreDepos.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return "Ore Deposits: Item Tags";
        }

        @Override
        protected void addTags() {
            copy(ORE_COPPER, ORE_COPPER_ITEM);
            copy(ORE_TIN, ORE_TIN_ITEM);
            tag(Tags.Items.ORES).addTags(
                    ORE_COPPER_ITEM,
                    ORE_TIN_ITEM
            );

            copy(STORAGE_BLOCKS_COPPER, STORAGE_BLOCKS_COPPER_ITEM);
            copy(STORAGE_BLOCKS_TIN, STORAGE_BLOCKS_TIN_ITEM);
            tag(Tags.Items.STORAGE_BLOCKS).addTags(
                    STORAGE_BLOCKS_COPPER_ITEM,
                    STORAGE_BLOCKS_TIN_ITEM
            );

            tag(INGOTS_COPPER).add(RegistryManager.COPPER_INGOT);
            tag(INGOTS_TIN).add(RegistryManager.TIN_INGOT);
            tag(Tags.Items.INGOTS).addTags(
                    INGOTS_COPPER,
                    INGOTS_TIN
            );
        }
    }
    public static class OreDepositFluidTags extends FluidTagsProvider {

        public OreDepositFluidTags(DataGenerator p_i49156_1_, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_i49156_1_, OreDepos.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return "Ore Deposits: Fluid Tags";
        }

        @Override
        protected void addTags() {
            tag(SULFURIC_ACID).add(RegistryManager.SULFURIC_ACID_FLUID.get());
        }
    }
}
