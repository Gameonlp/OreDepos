package gameonlp.oredepos;

import com.electronwill.nightconfig.core.EnumGetMethod;
import gameonlp.oredepos.blocks.miner.MinerBlock;
import gameonlp.oredepos.blocks.miner.MinerContainer;
import gameonlp.oredepos.blocks.oredeposit.OreDepositBlock;
import gameonlp.oredepos.items.DrillHeadItem;
import gameonlp.oredepos.items.OreDeposTab;
import gameonlp.oredepos.blocks.miner.MinerTile;
import gameonlp.oredepos.blocks.oredeposit.OreDepositTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class RegistryManager {
    // Template class
    private static class DepositTemplate {
        private final String name;
        private final Block block;

        private DepositTemplate(String location, String name){
            this.name = name;
            this.block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location, name));
        }

        private DepositTemplate(String name, Block block){
            this.name = name;
            this.block = block;
        }
    }

    //Registries
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, OreDepos.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OreDepos.MODID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OreDepos.MODID);
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, OreDepos.MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, OreDepos.MODID);
    //Resource Locations
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");

    //Items
    public static final RegistryObject<Item> SULFURID_ACID_BUCKET = ITEMS.register("sulfuric_acid_bucket",
            () -> new BucketItem(() -> RegistryManager.SULFURIC_ACID_FLUID.get(),
                    new Item.Properties().stacksTo(1).tab(OreDeposTab.ORE_DEPOS_TAB)));

    //Blocks
    public static RegistryObject<Block> MINER;
    public static final RegistryObject<FlowingFluidBlock> SULFURIC_ACID_BLOCK = RegistryManager.BLOCKS.register("sulfuric_acid",
            () -> new FlowingFluidBlock(() -> RegistryManager.SULFURIC_ACID_FLUID.get(), AbstractBlock.Properties.of(Material.WATER)
                    .noCollission().strength(100f).noDrops()));
    public static RegistryObject<Block> COPPER_ORE;
    public static RegistryObject<Block> COPPER_BLOCK;
    public static RegistryObject<Block> TIN_ORE;
    public static RegistryObject<Block> TIN_BLOCK;

    //Tile Entities
    public static RegistryObject<TileEntityType<OreDepositTile>> ORE_DEPOSIT_TILE;
    public static RegistryObject<TileEntityType<MinerTile>> MINER_TILE;

    //Containers
    public static RegistryObject<ContainerType<MinerContainer>> MINER_CONTAINER = CONTAINERS.register("miner_container", () -> IForgeContainerType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new MinerContainer(windowId, world, pos, inv, inv.player);
    })));

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



    private void registerAllDeposits(){
        Block tinOreBlock = new Block(AbstractBlock.Properties.of(Material.STONE)
                .strength(2, 5)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .requiresCorrectToolForDrops());
        TIN_ORE = registerBlock("tin_ore", () -> tinOreBlock);
        Block copperOreBlock = new Block(AbstractBlock.Properties.of(Material.STONE)
                .strength(2, 5)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .requiresCorrectToolForDrops());
        COPPER_ORE = registerBlock("copper_ore", () -> copperOreBlock);
        List<DepositTemplate> depositTemplates = new LinkedList<>();
        depositTemplates.add(new DepositTemplate("minecraft", "coal_ore"));
        depositTemplates.add(new DepositTemplate("minecraft", "iron_ore"));
        depositTemplates.add(new DepositTemplate("minecraft", "gold_ore"));
        depositTemplates.add(new DepositTemplate("minecraft", "diamond_ore"));
        depositTemplates.add(new DepositTemplate("minecraft", "emerald_ore"));
        depositTemplates.add(new DepositTemplate("copper_ore", copperOreBlock));
        depositTemplates.add(new DepositTemplate("tin_ore", tinOreBlock));

        List<Block> deposits = new LinkedList<>();
        for (DepositTemplate depositTemplate : depositTemplates) {
            deposits.add(registerOreDeposit(depositTemplate));
        }
        ORE_DEPOSIT_TILE = TILE_ENTITIES.register("ore_deposit_tile", () -> TileEntityType.Builder.of(OreDepositTile::new, deposits.toArray(new Block[0])).build(null));
    }

    private Block registerOreDeposit(DepositTemplate contained){
        Block block;
        System.out.println(contained.block);
        if (contained.block.defaultBlockState().requiresCorrectToolForDrops()) {
            block = new OreDepositBlock(AbstractBlock.Properties.copy(contained.block)
                    .harvestLevel(contained.block.getHarvestLevel(contained.block.defaultBlockState()))
                    .harvestTool(contained.block.getHarvestTool(contained.block.defaultBlockState()))
                    .lootFrom(contained.block::getBlock)
                    .requiresCorrectToolForDrops());
        } else {
            block = new OreDepositBlock(AbstractBlock.Properties.copy(contained.block)
                    .harvestLevel(contained.block.getHarvestLevel(contained.block.defaultBlockState()))
                    .harvestTool(contained.block.getHarvestTool(contained.block.defaultBlockState()))
                    .lootFrom(contained.block::getBlock));
        }
        registerBlock( contained.name + "_deposit",
                () -> block);
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
        COPPER_BLOCK = registerBlock("copper_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL)
                .strength(2.5f, 5)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .requiresCorrectToolForDrops()));
        TIN_BLOCK = registerBlock("tin_block", () -> new Block(AbstractBlock.Properties.of(Material.METAL)
                .strength(2.5f, 5)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
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
        ITEMS.register("copper_ingot", () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
        ITEMS.register("tin_ingot", () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
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

        FLUIDS.register(eventBus);
    }
}
