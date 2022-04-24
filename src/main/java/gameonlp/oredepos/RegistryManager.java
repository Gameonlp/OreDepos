package gameonlp.oredepos;

import gameonlp.oredepos.blocks.miner.MinerBlock;
import gameonlp.oredepos.blocks.miner.MinerContainer;
import gameonlp.oredepos.blocks.oredeposit.OreDepositBlock;
import gameonlp.oredepos.blocks.oredeposit.RedstoneOreDepositBlock;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.items.*;
import gameonlp.oredepos.blocks.miner.MinerTile;
import gameonlp.oredepos.blocks.oredeposit.OreDepositTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.feature.ReplaceBlockFeature;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class RegistryManager {
    // Template class
    private static class DepositTemplate {
        private final String name;
        private final Block block;
        private double factor;

        private DepositTemplate(String location, String name, double factor){
            this(name, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location, name)), factor);
        }

        private DepositTemplate(String name, Block block, double factor){
            this.name = name;
            this.block = block;
            this.factor = factor;
        }
    }

    //Registries
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, OreDepos.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OreDepos.MODID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OreDepos.MODID);
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, OreDepos.MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, OreDepos.MODID);
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, OreDepos.MODID);

    //Resource Locations
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");

    //Items
    public static final RegistryObject<Item> SULFURID_ACID_BUCKET = ITEMS.register("sulfuric_acid_bucket",
            () -> new BucketItem(() -> RegistryManager.SULFURIC_ACID_FLUID.get(),
                    new Item.Properties().stacksTo(1).tab(OreDeposTab.ORE_DEPOS_TAB)));
    @ObjectHolder("oredepos:diamond_pickaxe_drill_head")
    public static final Item DIAMOND_PICKAXE_DRILL_HEAD = null;
    @ObjectHolder("oredepos:copper_ingot")
    public static final Item COPPER_INGOT = null;
    @ObjectHolder("oredepos:tin_ingot")
    public static final Item TIN_INGOT = null;
    @ObjectHolder("oredepos:lead_ingot")
    public static final Item LEAD_INGOT = null;
    @ObjectHolder("oredepos:silver_ingot")
    public static final Item SILVER_INGOT = null;
    @ObjectHolder("oredepos:aluminum_ingot")
    public static final Item ALUMINUM_INGOT = null;

    //Blocks
    public static RegistryObject<Block> MINER;
    public static final RegistryObject<FlowingFluidBlock> SULFURIC_ACID_BLOCK = RegistryManager.BLOCKS.register("sulfuric_acid",
            () -> new FlowingFluidBlock(() -> RegistryManager.SULFURIC_ACID_FLUID.get(), AbstractBlock.Properties.of(Material.WATER)
                    .noCollission().strength(100f).noDrops()));
    @ObjectHolder("oredepos:copper_ore")
    public static final Block COPPER_ORE = null;
    @ObjectHolder("oredepos:copper_block")
    public static final Block COPPER_BLOCK = null;
    @ObjectHolder("oredepos:tin_ore")
    public static final Block TIN_ORE = null;
    @ObjectHolder("oredepos:tin_block")
    public static final Block TIN_BLOCK = null;
    @ObjectHolder("oredepos:lead_ore")
    public static final Block LEAD_ORE = null;
    @ObjectHolder("oredepos:lead_block")
    public static final Block LEAD_BLOCK = null;
    @ObjectHolder("oredepos:silver_ore")
    public static final Block SILVER_ORE = null;
    @ObjectHolder("oredepos:silver_block")
    public static final Block SILVER_BLOCK = null;
    @ObjectHolder("oredepos:aluminum_ore")
    public static final Block ALUMINUM_ORE = null;
    @ObjectHolder("oredepos:aluminum_block")
    public static final Block ALUMINUM_BLOCK = null;
    @ObjectHolder("minecraft:coal_ore")
    public static final Block COAL_ORE = null;
    @ObjectHolder("minecraft:iron_ore")
    public static final Block IRON_ORE = null;
    @ObjectHolder("minecraft:gold_ore")
    public static final Block GOLD_ORE = null;
    @ObjectHolder("minecraft:diamond_ore")
    public static final Block DIAMOND_ORE = null;
    @ObjectHolder("minecraft:emerald_ore")
    public static final Block EMERALD_ORE = null;
    @ObjectHolder("minecraft:redstone_ore")
    public static final Block REDSTONE_ORE = null;
    @ObjectHolder("minecraft:lapis_ore")
    public static final Block LAPIS_ORE = null;
    @ObjectHolder("oredepos:coal_ore_deposit")
    public static final Block COAL_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:iron_ore_deposit")
    public static final Block IRON_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:gold_ore_deposit")
    public static final Block GOLD_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:diamond_ore_deposit")
    public static final Block DIAMOND_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:emerald_ore_deposit")
    public static final Block EMERALD_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:redstone_ore_deposit")
    public static final Block REDSTONE_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:lapis_ore_deposit")
    public static final Block LAPIS_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:tin_ore_deposit")
    public static final Block TIN_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:copper_ore_deposit")
    public static final Block COPPER_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:lead_ore_deposit")
    public static final Block LEAD_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:silver_ore_deposit")
    public static final Block SILVER_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:aluminum_ore_deposit")
    public static final Block ALUMINUM_ORE_DEPOSIT = null;

    //Tile Entities
    public static RegistryObject<TileEntityType<OreDepositTile>> ORE_DEPOSIT_TILE;
    public static RegistryObject<TileEntityType<MinerTile>> MINER_TILE;

    //Containers
    public static RegistryObject<ContainerType<MinerContainer>> MINER_CONTAINER = CONTAINERS.register("miner_container", () -> IForgeContainerType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new MinerContainer(windowId, world, pos, inv, inv.player);
    })));

    //Features
    @ObjectHolder("oredepos:replace")
    public static final ReplaceBlockFeature REPLACE_FEATURE = null;

    //Fluids
    public static final RegistryObject<FlowingFluid> SULFURIC_ACID_FLUID
            = FLUIDS.register("sulfuric_acid_fluid", () -> new ForgeFlowingFluid.Source(RegistryManager.SULFURIC_ACID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> SULFURIC_ACID_FLOWING
            = FLUIDS.register("sulfuric_acid_flowing", () -> new ForgeFlowingFluid.Flowing(RegistryManager.SULFURIC_ACID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties SULFURIC_ACID_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> RegistryManager.SULFURIC_ACID_FLUID.get(), () -> RegistryManager.SULFURIC_ACID_FLOWING.get(), FluidAttributes.builder(RegistryManager.WATER_STILL_RL, RegistryManager.WATER_FLOWING_RL)
            .density(15).luminosity(2).viscosity(5).overlay(RegistryManager.WATER_OVERLAY_RL)
            .color(0xbffed0d0)).slopeFindDistance(2).levelDecreasePerBlock(2)
            .block(() -> RegistryManager.SULFURIC_ACID_BLOCK.get()).bucket(() -> RegistryManager.SULFURID_ACID_BUCKET.get());

    private Block prepareDeposit(String name, Material material, float hardness, float resistance, ToolType tool, int harvestLevel){
        return prepareDeposit(name, material, hardness,resistance,tool,harvestLevel,hardness, resistance, true,true);
    }

    private Block prepareDeposit(String name, Material material, float hardness, float resistance, ToolType tool, int harvestLevel, float blockHardness, float blockResistance, boolean hasIngot, boolean hasBlock){
        Block oreBlock = new Block(AbstractBlock.Properties.of(material)
                .strength(hardness, resistance)
                .harvestTool(tool)
                .harvestLevel(harvestLevel)
                .requiresCorrectToolForDrops());
        registerBlock(name + "_ore", () -> oreBlock);
        if (hasBlock) {
            registerBlock(name + "_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL)
                    .strength(blockHardness, blockResistance)
                    .harvestTool(tool)
                    .harvestLevel(harvestLevel)
                    .requiresCorrectToolForDrops()));
        }
        if (hasIngot) {
            ITEMS.register(name + "_ingot", () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
        }
        return oreBlock;
    }


    private void registerAllDeposits(){
        Block tinOreBlock = prepareDeposit("tin", Material.STONE, 2, 5, ToolType.PICKAXE, 1);
        Block copperOreBlock = prepareDeposit("copper", Material.STONE, 2, 5, ToolType.PICKAXE, 1);
        Block leadOreBlock = prepareDeposit("lead", Material.STONE, 3, 7, ToolType.PICKAXE, 2);
        Block silverOreBlock = prepareDeposit("silver", Material.STONE, 3, 4, ToolType.PICKAXE, 2);
        Block aluminumOreBlock = prepareDeposit("aluminum", Material.STONE, 2, 4, ToolType.PICKAXE, 1);

        List<DepositTemplate> depositTemplates = new LinkedList<>();
        depositTemplates.add(new DepositTemplate("minecraft", "coal_ore", OreDeposConfig.Common.coal.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "iron_ore", OreDeposConfig.Common.iron.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "gold_ore", OreDeposConfig.Common.gold.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "diamond_ore", OreDeposConfig.Common.diamond.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "emerald_ore", OreDeposConfig.Common.emerald.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "lapis_ore", OreDeposConfig.Common.lapis.factor.get()));
        depositTemplates.add(new DepositTemplate("copper_ore", copperOreBlock, OreDeposConfig.Common.copper.factor.get()));
        depositTemplates.add(new DepositTemplate("tin_ore", tinOreBlock, OreDeposConfig.Common.tin.factor.get()));
        depositTemplates.add(new DepositTemplate("lead_ore", leadOreBlock, OreDeposConfig.Common.lead.factor.get()));
        depositTemplates.add(new DepositTemplate("silver_ore", silverOreBlock, OreDeposConfig.Common.silver.factor.get()));
        depositTemplates.add(new DepositTemplate("aluminum_ore", aluminumOreBlock, OreDeposConfig.Common.aluminum.factor.get()));


        DepositTemplate redstoneTemplate = new DepositTemplate("minecraft", "redstone_ore", OreDeposConfig.Common.redstone.factor.get());
        Block redstoneOreDepositBlock = new RedstoneOreDepositBlock(AbstractBlock.Properties.copy(redstoneTemplate.block)
                    .harvestLevel(redstoneTemplate.block.getHarvestLevel(redstoneTemplate.block.defaultBlockState()))
                    .harvestTool(redstoneTemplate.block.getHarvestTool(redstoneTemplate.block.defaultBlockState()))
                    .lootFrom(redstoneTemplate.block::getBlock)
                    .requiresCorrectToolForDrops(), redstoneTemplate.factor);
        registerBlock( redstoneTemplate.name + "_deposit",
                () -> redstoneOreDepositBlock);

        List<Block> deposits = new LinkedList<>();
        for (DepositTemplate depositTemplate : depositTemplates) {
            deposits.add(registerOreDeposit(depositTemplate));
        }
        deposits.add(redstoneOreDepositBlock);
        ORE_DEPOSIT_TILE = TILE_ENTITIES.register("ore_deposit_tile", () -> TileEntityType.Builder.of(OreDepositTile::new, deposits.toArray(new Block[0])).build(null));
    }

    private Block registerOreDeposit(DepositTemplate contained){
        Block block;
        if (contained.block.defaultBlockState().requiresCorrectToolForDrops()) {
            block = new OreDepositBlock(AbstractBlock.Properties.copy(contained.block)
                    .harvestLevel(contained.block.getHarvestLevel(contained.block.defaultBlockState()))
                    .harvestTool(contained.block.getHarvestTool(contained.block.defaultBlockState()))
                    .lootFrom(contained.block::getBlock)
                    .requiresCorrectToolForDrops(), contained.factor);
        } else {
            block = new OreDepositBlock(AbstractBlock.Properties.copy(contained.block)
                    .harvestLevel(contained.block.getHarvestLevel(contained.block.defaultBlockState()))
                    .harvestTool(contained.block.getHarvestTool(contained.block.defaultBlockState()))
                    .lootFrom(contained.block::getBlock), contained.factor);
        }
        registerBlock( contained.name + "_deposit", () -> block);
        return block;
    }

    private <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private <T extends Block> void registerBlockItem(String name, RegistryObject<T> toReturn){
        ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
    }

    private void registerBlocks(){
        MINER = registerBlock("miner", () -> new MinerBlock(AbstractBlock.Properties.of(Material.METAL)
                .strength(3, 10)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops()));
    }

    private void registerItems(){
        ITEMS.register("iron_pickaxe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 2, ToolType.PICKAXE));
        ITEMS.register("diamond_pickaxe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 3, ToolType.PICKAXE));
        ITEMS.register("netherite_pickaxe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 4, ToolType.PICKAXE));
        ITEMS.register("iron_shovel_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 2, ToolType.SHOVEL));
        ITEMS.register("diamond_shovel_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 3, ToolType.SHOVEL));
        ITEMS.register("netherite_shovel_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 4, ToolType.SHOVEL));
        ITEMS.register("iron_axe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 2, ToolType.AXE));
        ITEMS.register("diamond_axe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 3, ToolType.AXE));
        ITEMS.register("netherite_axe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 4, ToolType.AXE));
        ITEMS.register("wire", () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
        ITEMS.register("circuit", () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
        ITEMS.register("speed_module_1", () -> new SpeedModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.5f, 0.2f));
        ITEMS.register("speed_module_2", () -> new SpeedModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.6f, 0.3f));
        ITEMS.register("speed_module_3", () -> new SpeedModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.7f, 0.5f));
        ITEMS.register("efficiency_module_1", () -> new ModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), -0.3f));
        ITEMS.register("efficiency_module_2", () -> new ModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), -0.4f));
        ITEMS.register("efficiency_module_3", () -> new ModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), -0.5f));
        ITEMS.register("productivity_module_1", () -> new ProductivityModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.4f, -0.05f, 0.04f));
        ITEMS.register("productivity_module_2", () -> new ProductivityModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.6f, -0.1f, 0.06f));
        ITEMS.register("productivity_module_3", () -> new ProductivityModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.8f, -0.15f, 0.1f));
    }

    private void registerFeatures() {
        FEATURES.register("replace", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    }

    private void registerTileEntities(){
        MINER_TILE = TILE_ENTITIES.register("miner_tile", () -> TileEntityType.Builder.of(MinerTile::new, MINER.get()).build(null));
    }

    public void register(IEventBus eventBus){
        registerItems();
        registerBlocks();
        registerAllDeposits();
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);

        registerTileEntities();
        TILE_ENTITIES.register(eventBus);
        CONTAINERS.register(eventBus);
        registerFeatures();
        FEATURES.register(eventBus);

        FLUIDS.register(eventBus);
    }
}
