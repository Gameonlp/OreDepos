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
    public static final Tags.IOptionalNamedTag<Block> ORE_LEAD = BlockTags.createOptional(new ResourceLocation("forge", "ores/lead"));
    public static final Tags.IOptionalNamedTag<Block> ORE_SILVER = BlockTags.createOptional(new ResourceLocation("forge", "ores/silver"));
    public static final Tags.IOptionalNamedTag<Block> ORE_ALUMINUM = BlockTags.createOptional(new ResourceLocation("forge", "ores/aluminum"));
    public static final Tags.IOptionalNamedTag<Block> ORE_URANIUM = BlockTags.createOptional(new ResourceLocation("forge", "ores/uranium"));
    public static final Tags.IOptionalNamedTag<Block> ORE_NICKEL = BlockTags.createOptional(new ResourceLocation("forge", "ores/nickel"));
    public static final Tags.IOptionalNamedTag<Block> ORE_ZINC = BlockTags.createOptional(new ResourceLocation("forge", "ores/zinc"));
    public static final Tags.IOptionalNamedTag<Block> ORE_CERTUS_QUARTZ = BlockTags.createOptional(new ResourceLocation("forge", "ores/certus_quartz"));
    public static final Tags.IOptionalNamedTag<Block> ORE_SULFUR = BlockTags.createOptional(new ResourceLocation("forge", "ores/sulfur"));
    public static final Tags.IOptionalNamedTag<Block> ORE_OSMIUM = BlockTags.createOptional(new ResourceLocation("forge", "ores/osmium"));
    public static final Tags.IOptionalNamedTag<Block> ORE_ARDITE = BlockTags.createOptional(new ResourceLocation("forge", "ores/ardite"));
    public static final Tags.IOptionalNamedTag<Block> ORE_COBALT = BlockTags.createOptional(new ResourceLocation("forge", "ores/cobalt"));
    public static final Tags.IOptionalNamedTag<Block> ORE_PLATINUM = BlockTags.createOptional(new ResourceLocation("forge", "ores/platinum"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_TIN = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/tin"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_COPPER = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/copper"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_LEAD = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/lead"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_SILVER = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/silver"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_ALUMINUM = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/aluminum"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_URANIUM = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/uranium"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_NICKEL = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/nickel"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_ZINC = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/zinc"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_CERTUS_QUARTZ = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/certus_quartz"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_SULFUR = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/sulfur"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_OSMIUM = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/osmium"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_ARDITE = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/ardite"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_COBALT = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/cobalt"));
    public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_PLATINUM = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/platinum"));

    public static final Tags.IOptionalNamedTag<Item> ORE_TIN_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/tin"));
    public static final Tags.IOptionalNamedTag<Item> ORE_COPPER_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/copper"));
    public static final Tags.IOptionalNamedTag<Item> ORE_LEAD_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/lead"));
    public static final Tags.IOptionalNamedTag<Item> ORE_SILVER_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/silver"));
    public static final Tags.IOptionalNamedTag<Item> ORE_ALUMINUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/aluminum"));
    public static final Tags.IOptionalNamedTag<Item> ORE_URANIUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/uranium"));
    public static final Tags.IOptionalNamedTag<Item> ORE_NICKEL_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/nickel"));
    public static final Tags.IOptionalNamedTag<Item> ORE_ZINC_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/zinc"));
    public static final Tags.IOptionalNamedTag<Item> ORE_CERTUS_QUARTZ_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/certus_quartz"));
    public static final Tags.IOptionalNamedTag<Item> ORE_SULFUR_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/sulfur"));
    public static final Tags.IOptionalNamedTag<Item> ORE_OSMIUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/osmium"));
    public static final Tags.IOptionalNamedTag<Item> ORE_ARDITE_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/ardite"));
    public static final Tags.IOptionalNamedTag<Item> ORE_COBALT_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/cobalt"));
    public static final Tags.IOptionalNamedTag<Item> ORE_PLATINUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/platinum"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_TIN_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/tin"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_COPPER_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/copper"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_LEAD_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/lead"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_SILVER_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/silver"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_ALUMINUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/aluminum"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_URANIUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/uranium"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_NICKEL_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/nickel"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_ZINC_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/zinc"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_CERTUS_QUARTZ_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/certus_quartz"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_SULFUR_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/sulfur"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_OSMIUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/osmium"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_ARDITE_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/ardite"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_COBALT_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/cobalt"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_PLATINUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/platinum"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_COPPER = ItemTags.createOptional(new ResourceLocation("forge", "ingots/copper"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_TIN = ItemTags.createOptional(new ResourceLocation("forge", "ingots/tin"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_LEAD = ItemTags.createOptional(new ResourceLocation("forge", "ingots/lead"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_SILVER = ItemTags.createOptional(new ResourceLocation("forge", "ingots/silver"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_ALUMINUM = ItemTags.createOptional(new ResourceLocation("forge", "ingots/aluminum"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_URANIUM = ItemTags.createOptional(new ResourceLocation("forge", "ingots/uranium"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_NICKEL = ItemTags.createOptional(new ResourceLocation("forge", "ingots/nickel"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_ZINC = ItemTags.createOptional(new ResourceLocation("forge", "ingots/zinc"));
    public static final Tags.IOptionalNamedTag<Item> GEMS_CERTUS_QUARTZ = ItemTags.createOptional(new ResourceLocation("forge", "gems/certus_quartz"));
    public static final Tags.IOptionalNamedTag<Item> DUSTS_SULFUR = ItemTags.createOptional(new ResourceLocation("forge", "dusts/sulfur"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_OSMIUM = ItemTags.createOptional(new ResourceLocation("forge", "ingots/osmium"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_ARDITE = ItemTags.createOptional(new ResourceLocation("forge", "ingots/ardite"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_COBALT = ItemTags.createOptional(new ResourceLocation("forge", "ingots/cobalt"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_PLATINUM = ItemTags.createOptional(new ResourceLocation("forge", "ingots/platinum"));

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
            tag(ORE_TIN).add(RegistryManager.TIN_ORE);
            tag(ORE_COPPER).add(RegistryManager.COPPER_ORE);
            tag(ORE_LEAD).add(RegistryManager.LEAD_ORE);
            tag(ORE_SILVER).add(RegistryManager.SILVER_ORE);
            tag(ORE_ALUMINUM).add(RegistryManager.ALUMINUM_ORE);
            tag(ORE_URANIUM).add(RegistryManager.URANIUM_ORE);
            tag(ORE_NICKEL).add(RegistryManager.NICKEL_ORE);
            tag(ORE_ZINC).add(RegistryManager.ZINC_ORE);
            tag(ORE_CERTUS_QUARTZ).add(RegistryManager.CERTUS_QUARTZ_ORE);
            tag(ORE_SULFUR).add(RegistryManager.SULFUR_ORE);
            tag(ORE_OSMIUM).add(RegistryManager.OSMIUM_ORE);
            tag(ORE_ARDITE).add(RegistryManager.ARDITE_ORE);
            tag(ORE_COBALT).add(RegistryManager.COBALT_ORE);
            tag(ORE_PLATINUM).add(RegistryManager.PLATINUM_ORE);
            tag(Tags.Blocks.ORES).addTags(
                    ORE_PLATINUM,
                    ORE_COBALT,
                    ORE_ARDITE,
                    ORE_OSMIUM,
                    ORE_SULFUR,
                    ORE_CERTUS_QUARTZ,
                    ORE_ZINC,
                    ORE_NICKEL,
                    ORE_TIN,
                    ORE_COPPER,
                    ORE_LEAD,
                    ORE_SILVER,
                    ORE_ALUMINUM,
                    ORE_URANIUM
            );

            tag(STORAGE_BLOCKS_TIN).add(RegistryManager.TIN_BLOCK);
            tag(STORAGE_BLOCKS_COPPER).add(RegistryManager.COPPER_BLOCK);
            tag(STORAGE_BLOCKS_LEAD).add(RegistryManager.LEAD_BLOCK);
            tag(STORAGE_BLOCKS_SILVER).add(RegistryManager.SILVER_BLOCK);
            tag(STORAGE_BLOCKS_ALUMINUM).add(RegistryManager.ALUMINUM_BLOCK);
            tag(STORAGE_BLOCKS_URANIUM).add(RegistryManager.URANIUM_BLOCK);
            tag(STORAGE_BLOCKS_NICKEL).add(RegistryManager.NICKEL_BLOCK);
            tag(STORAGE_BLOCKS_ZINC).add(RegistryManager.ZINC_BLOCK);
            tag(STORAGE_BLOCKS_CERTUS_QUARTZ).add(RegistryManager.CERTUS_QUARTZ_BLOCK);
            tag(STORAGE_BLOCKS_SULFUR).add(RegistryManager.SULFUR_BLOCK);
            tag(STORAGE_BLOCKS_OSMIUM).add(RegistryManager.OSMIUM_BLOCK);
            tag(STORAGE_BLOCKS_ARDITE).add(RegistryManager.ARDITE_BLOCK);
            tag(STORAGE_BLOCKS_COBALT).add(RegistryManager.COBALT_BLOCK);
            tag(STORAGE_BLOCKS_PLATINUM).add(RegistryManager.PLATINUM_BLOCK);
            tag(Tags.Blocks.STORAGE_BLOCKS).addTags(
                    STORAGE_BLOCKS_PLATINUM,
                    STORAGE_BLOCKS_COBALT,
                    STORAGE_BLOCKS_ARDITE,
                    STORAGE_BLOCKS_OSMIUM,
                    STORAGE_BLOCKS_SULFUR,
                    STORAGE_BLOCKS_CERTUS_QUARTZ,
                    STORAGE_BLOCKS_ZINC,
                    STORAGE_BLOCKS_NICKEL,
                    STORAGE_BLOCKS_TIN,
                    STORAGE_BLOCKS_COPPER,
                    STORAGE_BLOCKS_LEAD,
                    STORAGE_BLOCKS_SILVER,
                    STORAGE_BLOCKS_ALUMINUM,
                    STORAGE_BLOCKS_URANIUM
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
            copy(ORE_LEAD, ORE_LEAD_ITEM);
            copy(ORE_SILVER, ORE_SILVER_ITEM);
            copy(ORE_ALUMINUM, ORE_ALUMINUM_ITEM);
            copy(ORE_URANIUM, ORE_URANIUM_ITEM);
            copy(ORE_NICKEL, ORE_NICKEL_ITEM);
            copy(ORE_ZINC, ORE_ZINC_ITEM);
            copy(ORE_CERTUS_QUARTZ, ORE_CERTUS_QUARTZ_ITEM);
            copy(ORE_SULFUR, ORE_SULFUR_ITEM);
            copy(ORE_OSMIUM, ORE_OSMIUM_ITEM);
            copy(ORE_ARDITE, ORE_ARDITE_ITEM);
            copy(ORE_COBALT, ORE_COBALT_ITEM);
            copy(ORE_PLATINUM, ORE_PLATINUM_ITEM);
            tag(Tags.Items.ORES).addTags(
                    ORE_PLATINUM_ITEM,
                    ORE_COBALT_ITEM,
                    ORE_ARDITE_ITEM,
                    ORE_OSMIUM_ITEM,
                    ORE_SULFUR_ITEM,
                    ORE_CERTUS_QUARTZ_ITEM,
                    ORE_ZINC_ITEM,
                    ORE_NICKEL_ITEM,
                    ORE_COPPER_ITEM,
                    ORE_TIN_ITEM,
                    ORE_LEAD_ITEM,
                    ORE_SILVER_ITEM,
                    ORE_ALUMINUM_ITEM,
                    ORE_URANIUM_ITEM
            );

            copy(STORAGE_BLOCKS_COPPER, STORAGE_BLOCKS_COPPER_ITEM);
            copy(STORAGE_BLOCKS_TIN, STORAGE_BLOCKS_TIN_ITEM);
            copy(STORAGE_BLOCKS_LEAD, STORAGE_BLOCKS_LEAD_ITEM);
            copy(STORAGE_BLOCKS_SILVER, STORAGE_BLOCKS_SILVER_ITEM);
            copy(STORAGE_BLOCKS_ALUMINUM, STORAGE_BLOCKS_ALUMINUM_ITEM);
            copy(STORAGE_BLOCKS_URANIUM, STORAGE_BLOCKS_URANIUM_ITEM);
            copy(STORAGE_BLOCKS_NICKEL, STORAGE_BLOCKS_NICKEL_ITEM);
            copy(STORAGE_BLOCKS_ZINC, STORAGE_BLOCKS_ZINC_ITEM);
            copy(STORAGE_BLOCKS_CERTUS_QUARTZ, STORAGE_BLOCKS_CERTUS_QUARTZ_ITEM);
            copy(STORAGE_BLOCKS_SULFUR, STORAGE_BLOCKS_SULFUR_ITEM);
            copy(STORAGE_BLOCKS_OSMIUM, STORAGE_BLOCKS_OSMIUM_ITEM);
            copy(STORAGE_BLOCKS_ARDITE, STORAGE_BLOCKS_ARDITE_ITEM);
            copy(STORAGE_BLOCKS_COBALT, STORAGE_BLOCKS_COBALT_ITEM);
            copy(STORAGE_BLOCKS_PLATINUM, STORAGE_BLOCKS_PLATINUM_ITEM);
            tag(Tags.Items.STORAGE_BLOCKS).addTags(
                    STORAGE_BLOCKS_PLATINUM_ITEM,
                    STORAGE_BLOCKS_COBALT_ITEM,
                    STORAGE_BLOCKS_ARDITE_ITEM,
                    STORAGE_BLOCKS_OSMIUM_ITEM,
                    STORAGE_BLOCKS_SULFUR_ITEM,
                    STORAGE_BLOCKS_CERTUS_QUARTZ_ITEM,
                    STORAGE_BLOCKS_ZINC_ITEM,
                    STORAGE_BLOCKS_NICKEL_ITEM,
                    STORAGE_BLOCKS_COPPER_ITEM,
                    STORAGE_BLOCKS_TIN_ITEM,
                    STORAGE_BLOCKS_LEAD_ITEM,
                    STORAGE_BLOCKS_SILVER_ITEM,
                    STORAGE_BLOCKS_ALUMINUM_ITEM,
                    STORAGE_BLOCKS_URANIUM_ITEM
            );

            tag(INGOTS_COPPER).add(RegistryManager.COPPER_INGOT);
            tag(INGOTS_TIN).add(RegistryManager.TIN_INGOT);
            tag(INGOTS_LEAD).add(RegistryManager.LEAD_INGOT);
            tag(INGOTS_SILVER).add(RegistryManager.SILVER_INGOT);
            tag(INGOTS_ALUMINUM).add(RegistryManager.ALUMINUM_INGOT);
            tag(INGOTS_URANIUM).add(RegistryManager.URANIUM_INGOT);
            tag(INGOTS_NICKEL).add(RegistryManager.NICKEL_INGOT);
            tag(INGOTS_ZINC).add(RegistryManager.ZINC_INGOT);
            tag(INGOTS_OSMIUM).add(RegistryManager.OSMIUM_INGOT);
            tag(INGOTS_ARDITE).add(RegistryManager.ARDITE_INGOT);
            tag(INGOTS_COBALT).add(RegistryManager.COBALT_INGOT);
            tag(INGOTS_PLATINUM).add(RegistryManager.PLATINUM_INGOT);
            tag(Tags.Items.INGOTS).addTags(
                    INGOTS_PLATINUM,
                    INGOTS_COBALT,
                    INGOTS_ARDITE,
                    INGOTS_OSMIUM,
                    INGOTS_ZINC,
                    INGOTS_NICKEL,
                    INGOTS_COPPER,
                    INGOTS_TIN,
                    INGOTS_LEAD,
                    INGOTS_SILVER,
                    INGOTS_ALUMINUM,
                    INGOTS_URANIUM
            );

            tag(GEMS_CERTUS_QUARTZ).add(RegistryManager.CERTUS_QUARTZ);
            tag(Tags.Items.GEMS).addTags(
                    GEMS_CERTUS_QUARTZ
            );

            tag(DUSTS_SULFUR).add(RegistryManager.SULFUR);
            tag(Tags.Items.DUSTS).addTags(
                    DUSTS_SULFUR
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
