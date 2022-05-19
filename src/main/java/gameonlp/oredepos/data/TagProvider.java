package gameonlp.oredepos.data;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITagManager;

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
    public static final TagKey<Block> ORE_TIN = BlockTags.create(new ResourceLocation("forge", "ores/tin"));
    public static final TagKey<Block> ORE_LEAD = BlockTags.create(new ResourceLocation("forge", "ores/lead"));
    public static final TagKey<Block> ORE_SILVER = BlockTags.create(new ResourceLocation("forge", "ores/silver"));
    public static final TagKey<Block> ORE_ALUMINUM = BlockTags.create(new ResourceLocation("forge", "ores/aluminum"));
    public static final TagKey<Block> ORE_URANIUM = BlockTags.create(new ResourceLocation("forge", "ores/uranium"));
    public static final TagKey<Block> ORE_NICKEL = BlockTags.create(new ResourceLocation("forge", "ores/nickel"));
    public static final TagKey<Block> ORE_ZINC = BlockTags.create(new ResourceLocation("forge", "ores/zinc"));
    public static final TagKey<Block> ORE_CERTUS_QUARTZ = BlockTags.create(new ResourceLocation("forge", "ores/certus_quartz"));
    public static final TagKey<Block> ORE_SULFUR = BlockTags.create(new ResourceLocation("forge", "ores/sulfur"));
    public static final TagKey<Block> STORAGE_BLOCKS_TIN = BlockTags.create(new ResourceLocation("forge", "storage_blocks/tin"));
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_TIN = BlockTags.create(new ResourceLocation("forge", "storage_blocks/raw_tin"));
    public static final TagKey<Block> STORAGE_BLOCKS_LEAD = BlockTags.create(new ResourceLocation("forge", "storage_blocks/lead"));
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_LEAD = BlockTags.create(new ResourceLocation("forge", "storage_blocks/raw_lead"));
    public static final TagKey<Block> STORAGE_BLOCKS_SILVER = BlockTags.create(new ResourceLocation("forge", "storage_blocks/silver"));
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_SILVER = BlockTags.create(new ResourceLocation("forge", "storage_blocks/raw_silver"));
    public static final TagKey<Block> STORAGE_BLOCKS_ALUMINUM = BlockTags.create(new ResourceLocation("forge", "storage_blocks/aluminum"));
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_ALUMINUM = BlockTags.create(new ResourceLocation("forge", "storage_blocks/raw_aluminum"));
    public static final TagKey<Block> STORAGE_BLOCKS_URANIUM = BlockTags.create(new ResourceLocation("forge", "storage_blocks/uranium"));
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_URANIUM = BlockTags.create(new ResourceLocation("forge", "storage_blocks/raw_uranium"));
    public static final TagKey<Block> STORAGE_BLOCKS_NICKEL = BlockTags.create(new ResourceLocation("forge", "storage_blocks/nickel"));
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_NICKEL = BlockTags.create(new ResourceLocation("forge", "storage_blocks/raw_nickel"));
    public static final TagKey<Block> STORAGE_BLOCKS_ZINC = BlockTags.create(new ResourceLocation("forge", "storage_blocks/zinc"));
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_ZINC = BlockTags.create(new ResourceLocation("forge", "storage_blocks/raw_zinc"));
    public static final TagKey<Block> STORAGE_BLOCKS_CERTUS_QUARTZ = BlockTags.create(new ResourceLocation("forge", "storage_blocks/certus_quartz"));
    public static final TagKey<Block> STORAGE_BLOCKS_SULFUR = BlockTags.create(new ResourceLocation("forge", "storage_blocks/sulfur"));
    public static final TagKey<Block> PICKAXE = BlockTags.create(new ResourceLocation("minecraft:mineable/pickaxe"));
    public static final TagKey<Block> WOOD = BlockTags.create(new ResourceLocation("forge:needs_wood_tool"));
    public static final TagKey<Block> GOLD = BlockTags.create(new ResourceLocation("forge:needs_gold_tool"));
    public static final TagKey<Block> STONE = BlockTags.create(new ResourceLocation("minecraft:needs_stone_tool"));
    public static final TagKey<Block> IRON = BlockTags.create(new ResourceLocation("minecraft:needs_iron_tool"));
    public static final TagKey<Block> DIAMOND = BlockTags.create(new ResourceLocation("minecraft:needs_diamond_tool"));
    public static final TagKey<Block> NETHERITE = BlockTags.create(new ResourceLocation("forge:needs_netherite_tool"));

    public static final TagKey<Item> ORE_TIN_ITEM = ItemTags.create(new ResourceLocation("forge", "ores/tin"));
    public static final TagKey<Item> ORE_LEAD_ITEM = ItemTags.create(new ResourceLocation("forge", "ores/lead"));
    public static final TagKey<Item> ORE_SILVER_ITEM = ItemTags.create(new ResourceLocation("forge", "ores/silver"));
    public static final TagKey<Item> ORE_ALUMINUM_ITEM = ItemTags.create(new ResourceLocation("forge", "ores/aluminum"));
    public static final TagKey<Item> ORE_URANIUM_ITEM = ItemTags.create(new ResourceLocation("forge", "ores/uranium"));
    public static final TagKey<Item> ORE_NICKEL_ITEM = ItemTags.create(new ResourceLocation("forge", "ores/nickel"));
    public static final TagKey<Item> ORE_ZINC_ITEM = ItemTags.create(new ResourceLocation("forge", "ores/zinc"));
    public static final TagKey<Item> ORE_CERTUS_QUARTZ_ITEM = ItemTags.create(new ResourceLocation("forge", "ores/certus_quartz"));
    public static final TagKey<Item> ORE_SULFUR_ITEM = ItemTags.create(new ResourceLocation("forge", "ores/sulfur"));
    public static final TagKey<Item> ORE_OSMIUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/osmium"));
    public static final Tags.IOptionalNamedTag<Item> ORE_ARDITE_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/ardite"));
    public static final Tags.IOptionalNamedTag<Item> ORE_COBALT_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/cobalt"));
    public static final Tags.IOptionalNamedTag<Item> ORE_PLATINUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "ores/platinum"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_TIN_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/tin"));
    public static final TagKey<Item> STORAGE_BLOCKS_RAW_TIN_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/raw_tin"));
    public static final TagKey<Item> STORAGE_BLOCKS_LEAD_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/lead"));
    public static final TagKey<Item> STORAGE_BLOCKS_RAW_LEAD_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/raw_lead"));
    public static final TagKey<Item> STORAGE_BLOCKS_SILVER_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/silver"));
    public static final TagKey<Item> STORAGE_BLOCKS_RAW_SILVER_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/raw_silver"));
    public static final TagKey<Item> STORAGE_BLOCKS_ALUMINUM_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/aluminum"));
    public static final TagKey<Item> STORAGE_BLOCKS_RAW_ALUMINUM_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/raw_aluminum"));
    public static final TagKey<Item> STORAGE_BLOCKS_URANIUM_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/uranium"));
    public static final TagKey<Item> STORAGE_BLOCKS_RAW_URANIUM_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/raw_uranium"));
    public static final TagKey<Item> STORAGE_BLOCKS_NICKEL_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/nickel"));
    public static final TagKey<Item> STORAGE_BLOCKS_RAW_NICKEL_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/raw_nickel"));
    public static final TagKey<Item> STORAGE_BLOCKS_ZINC_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/zinc"));
    public static final TagKey<Item> STORAGE_BLOCKS_RAW_ZINC_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/raw_zinc"));
    public static final TagKey<Item> STORAGE_BLOCKS_CERTUS_QUARTZ_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/certus_quartz"));
    public static final TagKey<Item> STORAGE_BLOCKS_SULFUR_ITEM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/sulfur"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_OSMIUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/osmium"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_ARDITE_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/ardite"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_COBALT_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/cobalt"));
    public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_PLATINUM_ITEM = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/platinum"));
    public static final TagKey<Item> INGOTS_TIN = ItemTags.create(new ResourceLocation("forge", "ingots/tin"));
    public static final TagKey<Item> RAW_TIN = ItemTags.create(new ResourceLocation("forge", "raw_materials/tin"));
    public static final TagKey<Item> INGOTS_LEAD = ItemTags.create(new ResourceLocation("forge", "ingots/lead"));
    public static final TagKey<Item> RAW_LEAD = ItemTags.create(new ResourceLocation("forge", "raw_materials/lead"));
    public static final TagKey<Item> INGOTS_SILVER = ItemTags.create(new ResourceLocation("forge", "ingots/silver"));
    public static final TagKey<Item> RAW_SILVER = ItemTags.create(new ResourceLocation("forge", "raw_materials/silver"));
    public static final TagKey<Item> INGOTS_ALUMINUM = ItemTags.create(new ResourceLocation("forge", "ingots/aluminum"));
    public static final TagKey<Item> RAW_ALUMINUM = ItemTags.create(new ResourceLocation("forge", "raw_materials/aluminum"));
    public static final TagKey<Item> INGOTS_URANIUM = ItemTags.create(new ResourceLocation("forge", "ingots/uranium"));
    public static final TagKey<Item> RAW_URANIUM = ItemTags.create(new ResourceLocation("forge", "raw_materials/uranium"));
    public static final TagKey<Item> INGOTS_NICKEL = ItemTags.create(new ResourceLocation("forge", "ingots/nickel"));
    public static final TagKey<Item> RAW_NICKEL = ItemTags.create(new ResourceLocation("forge", "raw_materials/nickel"));
    public static final TagKey<Item> INGOTS_ZINC = ItemTags.create(new ResourceLocation("forge", "ingots/zinc"));
    public static final TagKey<Item> RAW_ZINC = ItemTags.create(new ResourceLocation("forge", "raw_materials/zinc"));
    public static final TagKey<Item> GEMS_CERTUS_QUARTZ = ItemTags.create(new ResourceLocation("forge", "gems/certus_quartz"));
    public static final TagKey<Item> DUSTS_SULFUR = ItemTags.create(new ResourceLocation("forge", "dusts/sulfur"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_OSMIUM = ItemTags.createOptional(new ResourceLocation("forge", "ingots/osmium"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_ARDITE = ItemTags.createOptional(new ResourceLocation("forge", "ingots/ardite"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_COBALT = ItemTags.createOptional(new ResourceLocation("forge", "ingots/cobalt"));
    public static final Tags.IOptionalNamedTag<Item> INGOTS_PLATINUM = ItemTags.createOptional(new ResourceLocation("forge", "ingots/platinum"));

    public static final TagKey<Fluid> SULFURIC_ACID = FluidTags.create(new ResourceLocation("forge", "sulfuric_acid"));

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
            tag(ORE_CERTUS_QUARTZ).add(RegistryManager.CERTUS_QUARTZ_ORE);
            tag(ORE_SULFUR).add(RegistryManager.SULFUR_ORE);
            tag(ORE_TIN).add(RegistryManager.TIN_ORE, RegistryManager.DEEPSLATE_TIN_ORE);
            tag(ORE_LEAD).add(RegistryManager.LEAD_ORE, RegistryManager.DEEPSLATE_LEAD_ORE);
            tag(ORE_SILVER).add(RegistryManager.SILVER_ORE, RegistryManager.DEEPSLATE_SILVER_ORE);
            tag(ORE_ALUMINUM).add(RegistryManager.ALUMINUM_ORE, RegistryManager.DEEPSLATE_ALUMINUM_ORE);
            tag(ORE_URANIUM).add(RegistryManager.URANIUM_ORE, RegistryManager.DEEPSLATE_URANIUM_ORE);
            tag(ORE_NICKEL).add(RegistryManager.NICKEL_ORE, RegistryManager.DEEPSLATE_NICKEL_ORE);
            tag(ORE_ZINC).add(RegistryManager.ZINC_ORE, RegistryManager.DEEPSLATE_ZINC_ORE);
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
                    ORE_LEAD,
                    ORE_SILVER,
                    ORE_ALUMINUM,
                    ORE_URANIUM
            );

            tag(STORAGE_BLOCKS_TIN).add(RegistryManager.TIN_BLOCK);
            tag(STORAGE_BLOCKS_RAW_TIN).add(RegistryManager.RAW_TIN_BLOCK);
            tag(STORAGE_BLOCKS_LEAD).add(RegistryManager.LEAD_BLOCK);
            tag(STORAGE_BLOCKS_RAW_LEAD).add(RegistryManager.RAW_LEAD_BLOCK);
            tag(STORAGE_BLOCKS_SILVER).add(RegistryManager.SILVER_BLOCK);
            tag(STORAGE_BLOCKS_RAW_SILVER).add(RegistryManager.RAW_SILVER_BLOCK);
            tag(STORAGE_BLOCKS_ALUMINUM).add(RegistryManager.ALUMINUM_BLOCK);
            tag(STORAGE_BLOCKS_RAW_ALUMINUM).add(RegistryManager.RAW_ALUMINUM_BLOCK);
            tag(STORAGE_BLOCKS_URANIUM).add(RegistryManager.URANIUM_BLOCK);
            tag(STORAGE_BLOCKS_RAW_URANIUM).add(RegistryManager.RAW_URANIUM_BLOCK);
            tag(STORAGE_BLOCKS_NICKEL).add(RegistryManager.NICKEL_BLOCK);
            tag(STORAGE_BLOCKS_RAW_NICKEL).add(RegistryManager.RAW_NICKEL_BLOCK);
            tag(STORAGE_BLOCKS_ZINC).add(RegistryManager.ZINC_BLOCK);
            tag(STORAGE_BLOCKS_CERTUS_QUARTZ).add(RegistryManager.CERTUS_QUARTZ_BLOCK);
            tag(STORAGE_BLOCKS_SULFUR).add(RegistryManager.SULFUR_BLOCK);
            tag(STORAGE_BLOCKS_RAW_ZINC).add(RegistryManager.RAW_ZINC_BLOCK);
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
                    STORAGE_BLOCKS_RAW_ZINC,
                    STORAGE_BLOCKS_NICKEL,
                    STORAGE_BLOCKS_RAW_NICKEL,
                    STORAGE_BLOCKS_TIN,
                    STORAGE_BLOCKS_RAW_TIN,
                    STORAGE_BLOCKS_LEAD,
                    STORAGE_BLOCKS_RAW_LEAD,
                    STORAGE_BLOCKS_SILVER,
                    STORAGE_BLOCKS_RAW_SILVER,
                    STORAGE_BLOCKS_ALUMINUM,
                    STORAGE_BLOCKS_RAW_ALUMINUM,
                    STORAGE_BLOCKS_URANIUM,
                    STORAGE_BLOCKS_RAW_URANIUM
            );

            tag(PICKAXE).add(
                    RegistryManager.ALUMINUM_BLOCK,
                    RegistryManager.RAW_ALUMINUM_BLOCK,
                    RegistryManager.ALUMINUM_ORE,
                    RegistryManager.ALUMINUM_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_ALUMINUM_ORE,
                    RegistryManager.DEEPSLATE_ALUMINUM_ORE_DEPOSIT,
                    RegistryManager.TIN_ORE,
                    RegistryManager.TIN_BLOCK,
                    RegistryManager.RAW_TIN_BLOCK,
                    RegistryManager.TIN_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_TIN_ORE,
                    RegistryManager.DEEPSLATE_TIN_ORE_DEPOSIT,
                    RegistryManager.LEAD_ORE,
                    RegistryManager.LEAD_BLOCK,
                    RegistryManager.RAW_LEAD_BLOCK,
                    RegistryManager.LEAD_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_LEAD_ORE,
                    RegistryManager.DEEPSLATE_LEAD_ORE_DEPOSIT,
                    RegistryManager.SILVER_ORE,
                    RegistryManager.SILVER_BLOCK,
                    RegistryManager.RAW_SILVER_BLOCK,
                    RegistryManager.SILVER_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_SILVER_ORE,
                    RegistryManager.DEEPSLATE_SILVER_ORE_DEPOSIT,
                    RegistryManager.URANIUM_ORE,
                    RegistryManager.URANIUM_BLOCK,
                    RegistryManager.RAW_URANIUM_BLOCK,
                    RegistryManager.URANIUM_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_URANIUM_ORE,
                    RegistryManager.DEEPSLATE_URANIUM_ORE_DEPOSIT,
                    RegistryManager.NICKEL_ORE,
                    RegistryManager.NICKEL_BLOCK,
                    RegistryManager.RAW_NICKEL_BLOCK,
                    RegistryManager.NICKEL_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_NICKEL_ORE,
                    RegistryManager.DEEPSLATE_NICKEL_ORE_DEPOSIT,
                    RegistryManager.ZINC_ORE,
                    RegistryManager.ZINC_BLOCK,
                    RegistryManager.RAW_ZINC_BLOCK,
                    RegistryManager.ZINC_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_ZINC_ORE,
                    RegistryManager.DEEPSLATE_ZINC_ORE_DEPOSIT,
                    RegistryManager.CERTUS_QUARTZ_ORE,
                    RegistryManager.CERTUS_QUARTZ_BLOCK,
                    RegistryManager.CERTUS_QUARTZ_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_CERTUS_QUARTZ_ORE,
                    RegistryManager.DEEPSLATE_CERTUS_QUARTZ_ORE_DEPOSIT,
                    RegistryManager.SULFUR_ORE,
                    RegistryManager.SULFUR_BLOCK,
                    RegistryManager.SULFUR_ORE_DEPOSIT,

                    RegistryManager.COAL_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_COAL_ORE_DEPOSIT,
                    RegistryManager.IRON_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_IRON_ORE_DEPOSIT,
                    RegistryManager.DIAMOND_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_DIAMOND_ORE_DEPOSIT,
                    RegistryManager.EMERALD_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_EMERALD_ORE_DEPOSIT,
                    RegistryManager.GOLD_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_GOLD_ORE_DEPOSIT,
                    RegistryManager.LAPIS_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_LAPIS_ORE_DEPOSIT,
                    RegistryManager.REDSTONE_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_REDSTONE_ORE_DEPOSIT,
                    RegistryManager.COPPER_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_COPPER_ORE_DEPOSIT,
                    RegistryManager.NETHER_QUARTZ_ORE_DEPOSIT,
                    RegistryManager.NETHER_GOLD_ORE_DEPOSIT,
                    RegistryManager.ANCIENT_DEBRIS_DEPOSIT,

                    RegistryManager.MINER.get(),
                    RegistryManager.CHEMICAL_PLANT.get()
            );

            tag(WOOD).add(
                    RegistryManager.NETHER_GOLD_ORE_DEPOSIT,
                    RegistryManager.NETHER_QUARTZ_ORE,

                    RegistryManager.COAL_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_COAL_ORE_DEPOSIT,
                    RegistryManager.COPPER_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_COPPER_ORE_DEPOSIT,
                    RegistryManager.ALUMINUM_BLOCK,
                    RegistryManager.RAW_ALUMINUM_BLOCK,
                    RegistryManager.ALUMINUM_ORE,
                    RegistryManager.ALUMINUM_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_ALUMINUM_ORE,
                    RegistryManager.DEEPSLATE_ALUMINUM_ORE_DEPOSIT,
                    RegistryManager.TIN_ORE,
                    RegistryManager.TIN_BLOCK,
                    RegistryManager.RAW_TIN_BLOCK,
                    RegistryManager.TIN_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_TIN_ORE,
                    RegistryManager.DEEPSLATE_TIN_ORE_DEPOSIT,
                    RegistryManager.ZINC_ORE,
                    RegistryManager.ZINC_BLOCK,
                    RegistryManager.RAW_ZINC_BLOCK,
                    RegistryManager.ZINC_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_ZINC_ORE,
                    RegistryManager.DEEPSLATE_ZINC_ORE_DEPOSIT
            );

            tag(STONE).add(
                    RegistryManager.LAPIS_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_LAPIS_ORE_DEPOSIT,
                    RegistryManager.IRON_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_IRON_ORE_DEPOSIT,
                    RegistryManager.LEAD_ORE,
                    RegistryManager.LEAD_BLOCK,
                    RegistryManager.RAW_LEAD_BLOCK,
                    RegistryManager.LEAD_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_LEAD_ORE,
                    RegistryManager.DEEPSLATE_LEAD_ORE_DEPOSIT,
                    RegistryManager.SILVER_ORE,
                    RegistryManager.SILVER_BLOCK,
                    RegistryManager.RAW_SILVER_BLOCK,
                    RegistryManager.SILVER_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_SILVER_ORE,
                    RegistryManager.DEEPSLATE_SILVER_ORE_DEPOSIT,
                    RegistryManager.NICKEL_ORE,
                    RegistryManager.NICKEL_BLOCK,
                    RegistryManager.RAW_NICKEL_BLOCK,
                    RegistryManager.NICKEL_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_NICKEL_ORE,
                    RegistryManager.DEEPSLATE_NICKEL_ORE_DEPOSIT
            );

            tag(IRON).add(
                    RegistryManager.SULFUR_ORE,
                    RegistryManager.SULFUR_BLOCK,
                    RegistryManager.SULFUR_ORE_DEPOSIT,
                    RegistryManager.CERTUS_QUARTZ_ORE,
                    RegistryManager.CERTUS_QUARTZ_BLOCK,
                    RegistryManager.CERTUS_QUARTZ_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_CERTUS_QUARTZ_ORE,
                    RegistryManager.DEEPSLATE_CERTUS_QUARTZ_ORE_DEPOSIT,

                    RegistryManager.REDSTONE_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_REDSTONE_ORE_DEPOSIT,
                    RegistryManager.GOLD_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_GOLD_ORE_DEPOSIT,
                    RegistryManager.EMERALD_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_EMERALD_ORE_DEPOSIT,
                    RegistryManager.DIAMOND_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_DIAMOND_ORE_DEPOSIT
            );

            tag(DIAMOND).add(
                    RegistryManager.ANCIENT_DEBRIS_DEPOSIT
            );

            tag(NETHERITE).add(
                    RegistryManager.URANIUM_ORE,
                    RegistryManager.URANIUM_BLOCK,
                    RegistryManager.RAW_URANIUM_BLOCK,
                    RegistryManager.URANIUM_ORE_DEPOSIT,
                    RegistryManager.DEEPSLATE_URANIUM_ORE,
                    RegistryManager.DEEPSLATE_URANIUM_ORE_DEPOSIT
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
                    ORE_TIN_ITEM,
                    ORE_LEAD_ITEM,
                    ORE_SILVER_ITEM,
                    ORE_ALUMINUM_ITEM,
                    ORE_URANIUM_ITEM
            );

            copy(STORAGE_BLOCKS_TIN, STORAGE_BLOCKS_TIN_ITEM);
            copy(STORAGE_BLOCKS_RAW_TIN, STORAGE_BLOCKS_RAW_TIN_ITEM);
            copy(STORAGE_BLOCKS_LEAD, STORAGE_BLOCKS_LEAD_ITEM);
            copy(STORAGE_BLOCKS_RAW_LEAD, STORAGE_BLOCKS_RAW_LEAD_ITEM);
            copy(STORAGE_BLOCKS_SILVER, STORAGE_BLOCKS_SILVER_ITEM);
            copy(STORAGE_BLOCKS_RAW_SILVER, STORAGE_BLOCKS_RAW_SILVER_ITEM);
            copy(STORAGE_BLOCKS_ALUMINUM, STORAGE_BLOCKS_ALUMINUM_ITEM);
            copy(STORAGE_BLOCKS_RAW_ALUMINUM, STORAGE_BLOCKS_RAW_ALUMINUM_ITEM);
            copy(STORAGE_BLOCKS_URANIUM, STORAGE_BLOCKS_URANIUM_ITEM);
            copy(STORAGE_BLOCKS_RAW_URANIUM, STORAGE_BLOCKS_RAW_URANIUM_ITEM);
            copy(STORAGE_BLOCKS_NICKEL, STORAGE_BLOCKS_NICKEL_ITEM);
            copy(STORAGE_BLOCKS_RAW_NICKEL, STORAGE_BLOCKS_RAW_NICKEL_ITEM);
            copy(STORAGE_BLOCKS_ZINC, STORAGE_BLOCKS_ZINC_ITEM);
            copy(STORAGE_BLOCKS_RAW_ZINC, STORAGE_BLOCKS_RAW_ZINC_ITEM);
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
                    STORAGE_BLOCKS_RAW_ZINC_ITEM,
                    STORAGE_BLOCKS_NICKEL_ITEM,
                    STORAGE_BLOCKS_RAW_NICKEL_ITEM,
                    STORAGE_BLOCKS_TIN_ITEM,
                    STORAGE_BLOCKS_RAW_TIN_ITEM,
                    STORAGE_BLOCKS_LEAD_ITEM,
                    STORAGE_BLOCKS_RAW_LEAD_ITEM,
                    STORAGE_BLOCKS_SILVER_ITEM,
                    STORAGE_BLOCKS_RAW_SILVER_ITEM,
                    STORAGE_BLOCKS_ALUMINUM_ITEM,
                    STORAGE_BLOCKS_RAW_ALUMINUM_ITEM,
                    STORAGE_BLOCKS_URANIUM_ITEM,
                    STORAGE_BLOCKS_RAW_URANIUM_ITEM
            );

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
                    INGOTS_TIN,
                    INGOTS_LEAD,
                    INGOTS_SILVER,
                    INGOTS_ALUMINUM,
                    INGOTS_URANIUM
            );

            tag(RAW_TIN).add(RegistryManager.RAW_TIN);
            tag(RAW_LEAD).add(RegistryManager.RAW_LEAD);
            tag(RAW_SILVER).add(RegistryManager.RAW_SILVER);
            tag(RAW_ALUMINUM).add(RegistryManager.RAW_ALUMINUM);
            tag(RAW_URANIUM).add(RegistryManager.RAW_URANIUM);
            tag(RAW_NICKEL).add(RegistryManager.RAW_NICKEL);
            tag(RAW_ZINC).add(RegistryManager.RAW_ZINC);
            tag(Tags.Items.RAW_MATERIALS).addTags(
                    RAW_ZINC,
                    RAW_NICKEL,
                    RAW_TIN,
                    RAW_LEAD,
                    RAW_SILVER,
                    RAW_ALUMINUM,
                    RAW_URANIUM
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
