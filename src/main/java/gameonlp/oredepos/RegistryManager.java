package gameonlp.oredepos;

import gameonlp.oredepos.blocks.beacon.BeaconBlock;
import gameonlp.oredepos.blocks.beacon.BeaconContainer;
import gameonlp.oredepos.blocks.beacon.BeaconTile;
import gameonlp.oredepos.blocks.chemicalplant.ChemicalPlantBlock;
import gameonlp.oredepos.blocks.chemicalplant.ChemicalPlantContainer;
import gameonlp.oredepos.blocks.chemicalplant.ChemicalPlantTile;
import gameonlp.oredepos.blocks.crafter.CrafterBlock;
import gameonlp.oredepos.blocks.crafter.CrafterContainer;
import gameonlp.oredepos.blocks.crafter.CrafterTile;
import gameonlp.oredepos.blocks.grinder.GrinderBlock;
import gameonlp.oredepos.blocks.grinder.GrinderContainer;
import gameonlp.oredepos.blocks.grinder.GrinderTile;
import gameonlp.oredepos.blocks.miner.MinerBlock;
import gameonlp.oredepos.blocks.miner.MinerContainer;
import gameonlp.oredepos.blocks.oredeposit.OreDepositBlock;
import gameonlp.oredepos.blocks.smelter.SmelterBlock;
import gameonlp.oredepos.blocks.smelter.SmelterContainer;
import gameonlp.oredepos.blocks.smelter.SmelterTile;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.crafting.chemicalplant.ChemicalPlantRecipe;
import gameonlp.oredepos.crafting.crafter.CrafterRecipe;
import gameonlp.oredepos.crafting.grinder.GrinderRecipe;
import gameonlp.oredepos.crafting.smelter.SmelterRecipe;
import gameonlp.oredepos.items.*;
import gameonlp.oredepos.blocks.miner.MinerTile;
import gameonlp.oredepos.blocks.oredeposit.OreDepositTile;
import gameonlp.oredepos.worldgen.hacks.ODCountPlacement;
import gameonlp.oredepos.worldgen.hacks.ODOreFeature;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class RegistryManager {

    // Template class
    private static class DepositTemplate {
        private final String name;
        private final Supplier<Block> block;
        private final String needed;
        private final double factor;
        private final float hardness;
        private final float resistance;

        private DepositTemplate(String location, String name, double factor, float hardness, float resistance){
            this(name, () -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location, name)), factor, hardness, resistance);
        }

        private DepositTemplate(String location, String name, double factor){
            this(name, () -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location, name)), factor, 3, 5);
        }

        private DepositTemplate(String name, Supplier<Block> block, double factor){
            this(name, block, factor, 3, 5);
        }

        private DepositTemplate(String name, Supplier<Block> block, double factor, float hardness, float resistance){
            this.name = name;
            this.block = block;
            this.needed = "oredepos:mining/" + name + "_deposit";
            this.factor = factor;
            this.hardness = hardness;
            this.resistance = resistance;
        }
    }
    //Constants
    private static final List<String> moduleAcceptors = List.of(
            MinerTile.getName(),
            ChemicalPlantTile.getName(),
            GrinderTile.getName(),
            SmelterTile.getName(),
            CrafterTile.getName(),
            BeaconTile.getName()
    );


    //Registries
    private static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, OreDepos.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OreDepos.MODID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OreDepos.MODID);
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, OreDepos.MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, OreDepos.MODID);
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, OreDepos.MODID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OreDepos.MODID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, OreDepos.MODID);
    private static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, OreDepos.MODID);

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
    @ObjectHolder("oredepos:tin_ingot")
    public static final Item TIN_INGOT = null;
    @ObjectHolder("oredepos:raw_tin")
    public static final Item RAW_TIN = null;
    @ObjectHolder("oredepos:lead_ingot")
    public static final Item LEAD_INGOT = null;
    @ObjectHolder("oredepos:raw_lead")
    public static final Item RAW_LEAD = null;
    @ObjectHolder("oredepos:silver_ingot")
    public static final Item SILVER_INGOT = null;
    @ObjectHolder("oredepos:raw_silver")
    public static final Item RAW_SILVER = null;
    @ObjectHolder("oredepos:aluminum_ingot")
    public static final Item ALUMINUM_INGOT = null;
    @ObjectHolder("oredepos:raw_aluminum")
    public static final Item RAW_ALUMINUM = null;
    @ObjectHolder("oredepos:uranium_ingot")
    public static final Item URANIUM_INGOT = null;
    @ObjectHolder("oredepos:raw_uranium")
    public static final Item RAW_URANIUM = null;
    @ObjectHolder("oredepos:nickel_ingot")
    public static final Item NICKEL_INGOT = null;
    @ObjectHolder("oredepos:raw_nickel")
    public static final Item RAW_NICKEL = null;
    @ObjectHolder("oredepos:zinc_ingot")
    public static final Item ZINC_INGOT = null;
    @ObjectHolder("oredepos:raw_zinc")
    public static final Item RAW_ZINC = null;
    @ObjectHolder("oredepos:osmium_ingot")
    public static final Item OSMIUM_INGOT = null;
    @ObjectHolder("oredepos:raw_osmium")
    public static final Item RAW_OSMIUM = null;
    @ObjectHolder("oredepos:ardite_ingot")
    public static final Item ARDITE_INGOT = null;
    @ObjectHolder("oredepos:raw_ardite")
    public static final Item RAW_ARDITE = null;
    @ObjectHolder("oredepos:cobalt_ingot")
    public static final Item COBALT_INGOT = null;
    @ObjectHolder("oredepos:raw_cobalt")
    public static final Item RAW_COBALT = null;
    @ObjectHolder("oredepos:platinum_ingot")
    public static final Item PLATINUM_INGOT = null;
    @ObjectHolder("oredepos:raw_platinum")
    public static final Item RAW_PLATINUM = null;
    @ObjectHolder("oredepos:certus_quartz")
    public static final Item CERTUS_QUARTZ = null;
    @ObjectHolder("oredepos:sulfur")
    public static final Item SULFUR = null;

    @ObjectHolder("oredepos:length_module_1")
    public static final Item LENGTH_MODULE_1 = null;
    @ObjectHolder("oredepos:width_module_1")
    public static final Item WIDTH_MODULE_1 = null;
    @ObjectHolder("oredepos:depth_module_1")
    public static final Item DEPTH_MODULE_1 = null;
    @ObjectHolder("oredepos:inversion_module")
    public static final Item INVERSION_MODULE = null;

    //Blocks
    public static RegistryObject<Block> MINER;
    public static RegistryObject<Block> CHEMICAL_PLANT;
    public static RegistryObject<Block> GRINDER;
    public static RegistryObject<Block> SMELTER;
    public static RegistryObject<Block> CRAFTER;
    public static RegistryObject<Block> BEACON;
    public static final RegistryObject<LiquidBlock> SULFURIC_ACID_BLOCK = RegistryManager.BLOCKS.register("sulfuric_acid",
            () -> new LiquidBlock(() -> RegistryManager.SULFURIC_ACID_FLUID.get(), BlockBehaviour.Properties.of(Material.WATER)
                    .noCollission().strength(100f).noDrops()));
    @ObjectHolder("minecraft:coal_ore")
    public static final Block COAL_ORE = null;
    @ObjectHolder("oredepos:coal_ore_deposit")
    public static final Block COAL_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:deepslate_coal_ore")
    public static final Block DEEPSLATE_COAL_ORE = null;
    @ObjectHolder("oredepos:deepslate_coal_ore_deposit")
    public static final Block DEEPSLATE_COAL_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:iron_ore")
    public static final Block IRON_ORE = null;
    @ObjectHolder("oredepos:iron_ore_deposit")
    public static final Block IRON_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:deepslate_iron_ore")
    public static final Block DEEPSLATE_IRON_ORE = null;
    @ObjectHolder("oredepos:deepslate_iron_ore_deposit")
    public static final Block DEEPSLATE_IRON_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:gold_ore")
    public static final Block GOLD_ORE = null;
    @ObjectHolder("oredepos:gold_ore_deposit")
    public static final Block GOLD_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:deepslate_gold_ore")
    public static final Block DEEPSLATE_GOLD_ORE = null;
    @ObjectHolder("oredepos:deepslate_gold_ore_deposit")
    public static final Block DEEPSLATE_GOLD_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:diamond_ore")
    public static final Block DIAMOND_ORE = null;
    @ObjectHolder("oredepos:diamond_ore_deposit")
    public static final Block DIAMOND_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:deepslate_diamond_ore")
    public static final Block DEEPSLATE_DIAMOND_ORE = null;
    @ObjectHolder("oredepos:deepslate_diamond_ore_deposit")
    public static final Block DEEPSLATE_DIAMOND_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:emerald_ore")
    public static final Block EMERALD_ORE = null;
    @ObjectHolder("oredepos:emerald_ore_deposit")
    public static final Block EMERALD_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:deepslate_emerald_ore")
    public static final Block DEEPSLATE_EMERALD_ORE = null;
    @ObjectHolder("oredepos:deepslate_emerald_ore_deposit")
    public static final Block DEEPSLATE_EMERALD_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:redstone_ore")
    public static final Block REDSTONE_ORE = null;
    @ObjectHolder("oredepos:redstone_ore_deposit")
    public static final Block REDSTONE_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:deepslate_redstone_ore")
    public static final Block DEEPSLATE_REDSTONE_ORE = null;
    @ObjectHolder("oredepos:deepslate_redstone_ore_deposit")
    public static final Block DEEPSLATE_REDSTONE_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:lapis_ore")
    public static final Block LAPIS_ORE = null;
    @ObjectHolder("oredepos:lapis_ore_deposit")
    public static final Block LAPIS_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:deepslate_lapis_ore")
    public static final Block DEEPSLATE_LAPIS_ORE = null;
    @ObjectHolder("oredepos:deepslate_lapis_ore_deposit")
    public static final Block DEEPSLATE_LAPIS_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:copper_ore")
    public static final Block COPPER_ORE = null;
    @ObjectHolder("oredepos:copper_ore_deposit")
    public static final Block COPPER_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:deepslate_copper_ore")
    public static final Block DEEPSLATE_COPPER_ORE = null;
    @ObjectHolder("oredepos:deepslate_copper_ore_deposit")
    public static final Block DEEPSLATE_COPPER_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:nether_quartz_ore")
    public static final Block NETHER_QUARTZ_ORE = null;
    @ObjectHolder("oredepos:nether_quartz_ore_deposit")
    public static final Block NETHER_QUARTZ_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:nether_gold_ore")
    public static final Block NETHER_GOLD_ORE = null;
    @ObjectHolder("oredepos:nether_gold_ore_deposit")
    public static final Block NETHER_GOLD_ORE_DEPOSIT = null;
    @ObjectHolder("minecraft:ancient_debris")
    public static final Block ANCIENT_DEBRIS = null;
    @ObjectHolder("oredepos:ancient_debris_deposit")
    public static final Block ANCIENT_DEBRIS_DEPOSIT = null;
    @ObjectHolder("oredepos:tin_ore")
    public static final Block TIN_ORE = null;
    @ObjectHolder("oredepos:tin_block")
    public static final Block TIN_BLOCK = null;
    @ObjectHolder("oredepos:raw_tin_block")
    public static final Block RAW_TIN_BLOCK = null;
    @ObjectHolder("oredepos:tin_ore_deposit")
    public static final Block TIN_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:deepslate_tin_ore")
    public static final Block DEEPSLATE_TIN_ORE = null;
    @ObjectHolder("oredepos:deepslate_tin_ore_deposit")
    public static final Block DEEPSLATE_TIN_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:lead_ore")
    public static final Block LEAD_ORE = null;
    @ObjectHolder("oredepos:lead_block")
    public static final Block LEAD_BLOCK = null;
    @ObjectHolder("oredepos:raw_lead_block")
    public static final Block RAW_LEAD_BLOCK = null;
    @ObjectHolder("oredepos:lead_ore_deposit")
    public static final Block LEAD_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:deepslate_lead_ore")
    public static final Block DEEPSLATE_LEAD_ORE = null;
    @ObjectHolder("oredepos:deepslate_lead_ore_deposit")
    public static final Block DEEPSLATE_LEAD_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:silver_ore")
    public static final Block SILVER_ORE = null;
    @ObjectHolder("oredepos:silver_block")
    public static final Block SILVER_BLOCK = null;
    @ObjectHolder("oredepos:raw_silver_block")
    public static final Block RAW_SILVER_BLOCK = null;
    @ObjectHolder("oredepos:silver_ore_deposit")
    public static final Block SILVER_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:deepslate_silver_ore")
    public static final Block DEEPSLATE_SILVER_ORE = null;
    @ObjectHolder("oredepos:deepslate_silver_ore_deposit")
    public static final Block DEEPSLATE_SILVER_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:aluminum_ore")
    public static final Block ALUMINUM_ORE = null;
    @ObjectHolder("oredepos:aluminum_block")
    public static final Block ALUMINUM_BLOCK = null;
    @ObjectHolder("oredepos:raw_aluminum_block")
    public static final Block RAW_ALUMINUM_BLOCK = null;
    @ObjectHolder("oredepos:aluminum_ore_deposit")
    public static final Block ALUMINUM_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:deepslate_aluminum_ore")
    public static final Block DEEPSLATE_ALUMINUM_ORE = null;
    @ObjectHolder("oredepos:deepslate_aluminum_ore_deposit")
    public static final Block DEEPSLATE_ALUMINUM_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:uranium_ore")
    public static final Block URANIUM_ORE = null;
    @ObjectHolder("oredepos:uranium_block")
    public static final Block URANIUM_BLOCK = null;
    @ObjectHolder("oredepos:raw_uranium_block")
    public static final Block RAW_URANIUM_BLOCK = null;
    @ObjectHolder("oredepos:uranium_ore_deposit")
    public static final Block URANIUM_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:deepslate_uranium_ore")
    public static final Block DEEPSLATE_URANIUM_ORE = null;
    @ObjectHolder("oredepos:deepslate_uranium_ore_deposit")
    public static final Block DEEPSLATE_URANIUM_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:nickel_ore")
    public static final Block NICKEL_ORE = null;
    @ObjectHolder("oredepos:nickel_block")
    public static final Block NICKEL_BLOCK = null;
    @ObjectHolder("oredepos:raw_nickel_block")
    public static final Block RAW_NICKEL_BLOCK = null;
    @ObjectHolder("oredepos:nickel_ore_deposit")
    public static final Block NICKEL_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:deepslate_nickel_ore")
    public static final Block DEEPSLATE_NICKEL_ORE = null;
    @ObjectHolder("oredepos:deepslate_nickel_ore_deposit")
    public static final Block DEEPSLATE_NICKEL_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:zinc_ore")
    public static final Block ZINC_ORE = null;
    @ObjectHolder("oredepos:zinc_block")
    public static final Block ZINC_BLOCK = null;
    @ObjectHolder("oredepos:raw_zinc_block")
    public static final Block RAW_ZINC_BLOCK = null;
    @ObjectHolder("oredepos:zinc_ore_deposit")
    public static final Block ZINC_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:deepslate_zinc_ore")
    public static final Block DEEPSLATE_ZINC_ORE = null;
    @ObjectHolder("oredepos:deepslate_zinc_ore_deposit")
    public static final Block DEEPSLATE_ZINC_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:certus_quartz_ore")
    public static final Block CERTUS_QUARTZ_ORE = null;
    @ObjectHolder("oredepos:certus_quartz_block")
    public static final Block CERTUS_QUARTZ_BLOCK = null;
    @ObjectHolder("oredepos:certus_quartz_ore_deposit")
    public static final Block CERTUS_QUARTZ_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:deepslate_certus_quartz_ore")
    public static final Block DEEPSLATE_CERTUS_QUARTZ_ORE = null;
    @ObjectHolder("oredepos:deepslate_certus_quartz_ore_deposit")
    public static final Block DEEPSLATE_CERTUS_QUARTZ_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:sulfur_ore")
    public static final Block SULFUR_ORE = null;
    @ObjectHolder("oredepos:sulfur_block")
    public static final Block SULFUR_BLOCK = null;
    @ObjectHolder("oredepos:sulfur_ore_deposit")
    public static final Block SULFUR_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:osmium_ore")
    public static final Block OSMIUM_ORE = null;
    @ObjectHolder("oredepos:osmium_block")
    public static final Block OSMIUM_BLOCK = null;
    @ObjectHolder("oredepos:raw_osmium_block")
    public static final Block RAW_OSMIUM_BLOCK = null;
    @ObjectHolder("oredepos:osmium_ore_deposit")
    public static final Block OSMIUM_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:deepslate_osmium_ore")
    public static final Block DEEPSLATE_OSMIUM_ORE = null;
    @ObjectHolder("oredepos:deepslate_osmium_ore_deposit")
    public static final Block DEEPSLATE_OSMIUM_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:ardite_ore")
    public static final Block ARDITE_ORE = null;
    @ObjectHolder("oredepos:ardite_block")
    public static final Block ARDITE_BLOCK = null;
    @ObjectHolder("oredepos:raw_ardite_block")
    public static final Block RAW_ARDITE_BLOCK = null;
    @ObjectHolder("oredepos:ardite_ore_deposit")
    public static final Block ARDITE_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:cobalt_ore")
    public static final Block COBALT_ORE = null;
    @ObjectHolder("oredepos:cobalt_block")
    public static final Block COBALT_BLOCK = null;
    @ObjectHolder("oredepos:raw_cobalt_block")
    public static final Block RAW_COBALT_BLOCK = null;
    @ObjectHolder("oredepos:cobalt_ore_deposit")
    public static final Block COBALT_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:platinum_ore")
    public static final Block PLATINUM_ORE = null;
    @ObjectHolder("oredepos:platinum_block")
    public static final Block PLATINUM_BLOCK = null;
    @ObjectHolder("oredepos:raw_platinum_block")
    public static final Block RAW_PLATINUM_BLOCK = null;
    @ObjectHolder("oredepos:platinum_ore_deposit")
    public static final Block PLATINUM_ORE_DEPOSIT = null;
    @ObjectHolder("oredepos:deepslate_platinum_ore")
    public static final Block DEEPSLATE_PLATINUM_ORE = null;
    @ObjectHolder("oredepos:deepslate_platinum_ore_deposit")
    public static final Block DEEPSLATE_PLATINUM_ORE_DEPOSIT = null;

    //Tile Entities
    public static RegistryObject<BlockEntityType<OreDepositTile>> ORE_DEPOSIT_TILE;
    public static RegistryObject<BlockEntityType<MinerTile>> MINER_TILE;
    public static RegistryObject<BlockEntityType<ChemicalPlantTile>> CHEMICAL_PLANT_TILE;
    public static RegistryObject<BlockEntityType<GrinderTile>> GRINDER_TILE;
    public static RegistryObject<BlockEntityType<SmelterTile>> SMELTER_TILE;
    public static RegistryObject<BlockEntityType<CrafterTile>> CRAFTER_TILE;
    public static RegistryObject<BlockEntityType<BeaconTile>> BEACON_TILE;

    //Containers
    public static RegistryObject<MenuType<MinerContainer>> MINER_CONTAINER = CONTAINERS.register("miner_container", () -> IForgeMenuType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new MinerContainer(windowId, world, pos, inv, inv.player);
    })));
    public static RegistryObject<MenuType<ChemicalPlantContainer>> CHEMICAL_PLANT_CONTAINER = CONTAINERS.register("chemical_plant_container", () -> IForgeMenuType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new ChemicalPlantContainer(windowId, world, pos, inv, inv.player);
    })));

    public static RegistryObject<MenuType<GrinderContainer>> GRINDER_CONTAINER = CONTAINERS.register("grinder_container", () -> IForgeMenuType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new GrinderContainer(windowId, world, pos, inv, inv.player);
    })));

    public static RegistryObject<MenuType<SmelterContainer>> SMELTER_CONTAINER = CONTAINERS.register("smelter_container", () -> IForgeMenuType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new SmelterContainer(windowId, world, pos, inv, inv.player);
    })));

    public static RegistryObject<MenuType<CrafterContainer>> CRAFTER_CONTAINER = CONTAINERS.register("crafter_container", () -> IForgeMenuType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new CrafterContainer(windowId, world, pos, inv, inv.player);
    })));

    public static RegistryObject<MenuType<BeaconContainer>> BEACON_CONTAINER = CONTAINERS.register("beacon_container", () -> IForgeMenuType.create(((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new BeaconContainer(windowId, world, pos, inv, inv.player);
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

    //Recipes
    @ObjectHolder("oredepos:chemical_plant_recipe")
    public static final RecipeSerializer<ChemicalPlantRecipe> CHEMICAL_PLANT_RECIPE_SERIALIZER = null;
    public static final RegistryObject<ChemicalPlantRecipe.ChemicalPlantRecipeType> CHEMICAL_PLANT_RECIPE_TYPE = RECIPE_TYPES.register("chemical_plant_recipe_type", ChemicalPlantRecipe.ChemicalPlantRecipeType::new);

    @ObjectHolder("oredepos:grinder_recipe")
    public static final RecipeSerializer<GrinderRecipe> GRINDER_RECIPE_SERIALIZER = null;
    public static final RegistryObject<GrinderRecipe.GrinderRecipeType> GRINDER_RECIPE_TYPE = RECIPE_TYPES.register("grinder_recipe_type", GrinderRecipe.GrinderRecipeType::new);

    @ObjectHolder("oredepos:smelter_recipe")
    public static final RecipeSerializer<SmelterRecipe> SMELTER_RECIPE_SERIALIZER = null;
    public static final RegistryObject<SmelterRecipe.SmelterRecipeType> SMELTER_RECIPE_TYPE = RECIPE_TYPES.register("smelter_recipe_type", SmelterRecipe.SmelterRecipeType::new);

    @ObjectHolder("oredepos:crafter_recipe")
    public static final RecipeSerializer<CrafterRecipe> CRAFTER_RECIPE_SERIALIZER = null;
    public static final RegistryObject<CrafterRecipe.CrafterRecipeType> CRAFTER_RECIPE_TYPE = RECIPE_TYPES.register("crafter_recipe_type", CrafterRecipe.CrafterRecipeType::new);

    //Features
    public static final RegistryObject<ODOreFeature> ORE = FEATURES.register("od_ore", () -> new ODOreFeature(OreConfiguration.CODEC));

    // Placement Modifiers
    public static final RegistryObject<PlacementModifierType<ODCountPlacement>> COUNT = PLACEMENT_MODIFIER_TYPE.register("count_placment", ODCountPlacement.ODCountPlacementType::new);


    // Other data
    List<Supplier<Block>> deposits;


    private Supplier<Block> prepareDeposit(String name, Material material, float hardness, float resistance){
        return prepareDeposit(name, material, hardness,resistance, true,true);
    }

    private Supplier<Block> prepareDeposit(String name, Material material, float hardness, float resistance, boolean hasIngot, boolean hasBlock){
        return prepareDeposit(name, material, hardness,resistance,hardness, resistance, hasIngot, hasBlock);
    }

    private Supplier<Block> prepareDeposit(String name, Material material, float hardness, float resistance, float blockHardness, float blockResistance, boolean hasIngot, boolean hasBlock){
        Supplier<Block> oreBlock = () -> new Block(BlockBehaviour.Properties.of(material)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops());
        if (hasBlock) {
            registerBlock(name + "_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(blockHardness, blockResistance)
                    .requiresCorrectToolForDrops()));
        }
        if (hasIngot) {
            ITEMS.register(name + "_ingot", () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
        }
        return registerBlock(name + "_ore", oreBlock);
    }

    private Supplier<Block> prepareDeepslateDeposit(String name, Material material, float hardness, float resistance){
        return prepareDeepslateDeposit(name, material, hardness,resistance, true,true);
    }

    private Supplier<Block> prepareDeepslateDeposit(String name, Material material, float hardness, float resistance, boolean hasChunk, boolean hasBlock){
        return prepareDeepslateDeposit(name, material, hardness,resistance,hardness, resistance, hasChunk, hasBlock);
    }

    private Supplier<Block> prepareDeepslateDeposit(String name, Material material, float hardness, float resistance, float blockHardness, float blockResistance, boolean hasChunk, boolean hasBlock){
        Supplier<Block> oreBlock = () -> new Block(BlockBehaviour.Properties.of(material)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops());
        if (hasBlock) {
            makeRawBlock(name, blockHardness, blockResistance);
        }
        if (hasChunk) {
            makeChunk(name);
        }
        return registerBlock("deepslate_" + name + "_ore", oreBlock);
    }

    private void makeRaws(String name, float blockHardness, float blockResistance){
        makeChunk(name);
        makeRawBlock(name, blockHardness, blockResistance);
    }

    private void makeChunk(String name){
        ITEMS.register("raw_" + name, () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
    }
    private void makeRawBlock(String name, float blockHardness, float blockResistance){
        registerBlock("raw_" + name + "_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                .strength(blockHardness, blockResistance)
                .requiresCorrectToolForDrops()));
    }


    private void registerAllDeposits(){
        Supplier<Block> tinOreBlock = prepareDeposit("tin", Material.STONE, 2, 5);
        Supplier<Block> deepslateTinOreBlock = prepareDeepslateDeposit("tin", Material.STONE, 2, 5);
        Supplier<Block> leadOreBlock = prepareDeposit("lead", Material.STONE, 3, 7);
        Supplier<Block> deepslateLeadOreBlock = prepareDeepslateDeposit("lead", Material.STONE, 3, 7);
        Supplier<Block> silverOreBlock = prepareDeposit("silver", Material.STONE, 3, 4);
        Supplier<Block> deepslateSilverOreBlock = prepareDeepslateDeposit("silver", Material.STONE, 3, 4);
        Supplier<Block> aluminumOreBlock = prepareDeposit("aluminum", Material.STONE, 2, 4);
        Supplier<Block> deepslateAluminumOreBlock = prepareDeepslateDeposit("aluminum", Material.STONE, 2, 4);
        Supplier<Block> uraniumOreBlock = prepareDeposit("uranium", Material.STONE, 10, 5);
        Supplier<Block> deepslateUraniumOreBlock = prepareDeepslateDeposit("uranium", Material.STONE, 10, 5);
        Supplier<Block> nickelOreBlock = prepareDeposit("nickel", Material.STONE, 3, 5);
        Supplier<Block> deepslateNickelOreBlock = prepareDeepslateDeposit("nickel", Material.STONE, 3, 5);
        Supplier<Block> zincOreBlock = prepareDeposit("zinc", Material.STONE, 2, 5);
        Supplier<Block> deepslateZincOreBlock = prepareDeepslateDeposit("zinc", Material.STONE, 2, 5);
        Supplier<Block> certusQuartzOreBlock = prepareDeposit("certus_quartz", Material.STONE, 3, 15, false, true);
        Supplier<Block> deepslateCertusQuartzOreBlock = prepareDeepslateDeposit("certus_quartz", Material.STONE, 3, 15, false, false);
        Supplier<Block> sulfurOreBlock = prepareDeposit("sulfur", Material.STONE, 2.5f, 5, false, true);
        Supplier<Block> osmiumOreBlock = prepareDeposit("osmium", Material.STONE, 2.5f, 5);
        Supplier<Block> deepslateOsmiumOreBlock = prepareDeepslateDeposit("osmium", Material.STONE, 2.5f, 5);
        Supplier<Block> arditeOreBlock = prepareDeposit("ardite", Material.STONE, 4, 5);
        makeRaws("ardite", 4, 5);
        Supplier<Block> cobaltOreBlock = prepareDeposit("cobalt", Material.STONE, 3, 7);
        makeRaws("cobalt", 3, 7);
        Supplier<Block> platinumOreBlock = prepareDeposit("platinum", Material.STONE, 4, 8);
        Supplier<Block> deepslatePlatinumOreBlock = prepareDeepslateDeposit("platinum", Material.STONE, 4, 8);

        List<DepositTemplate> depositTemplates = new LinkedList<>();
        depositTemplates.add(new DepositTemplate("minecraft", "coal_ore", OreDeposConfig.Common.coal.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "deepslate_coal_ore", OreDeposConfig.Common.coal.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "iron_ore", OreDeposConfig.Common.iron.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "deepslate_iron_ore", OreDeposConfig.Common.iron.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "gold_ore", OreDeposConfig.Common.gold.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "deepslate_gold_ore", OreDeposConfig.Common.gold.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "diamond_ore", OreDeposConfig.Common.diamond.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "deepslate_diamond_ore", OreDeposConfig.Common.diamond.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "emerald_ore", OreDeposConfig.Common.emerald.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "deepslate_emerald_ore", OreDeposConfig.Common.emerald.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "lapis_ore", OreDeposConfig.Common.lapis.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "deepslate_lapis_ore", OreDeposConfig.Common.lapis.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "redstone_ore", OreDeposConfig.Common.redstone.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "deepslate_redstone_ore", OreDeposConfig.Common.redstone.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "copper_ore", OreDeposConfig.Common.copper.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "deepslate_copper_ore", OreDeposConfig.Common.copper.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "nether_quartz_ore", OreDeposConfig.Common.nether_quartz.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "nether_gold_ore", OreDeposConfig.Common.nether_gold.factor.get()));
        depositTemplates.add(new DepositTemplate("minecraft", "ancient_debris", OreDeposConfig.Common.ancient_debris.factor.get(), 20, 1200));
        depositTemplates.add(new DepositTemplate("tin_ore", tinOreBlock, OreDeposConfig.Common.tin.factor.get()));
        depositTemplates.add(new DepositTemplate("deepslate_tin_ore", deepslateTinOreBlock, OreDeposConfig.Common.tin.factor.get()));
        depositTemplates.add(new DepositTemplate("lead_ore", leadOreBlock, OreDeposConfig.Common.lead.factor.get()));
        depositTemplates.add(new DepositTemplate("deepslate_lead_ore", deepslateLeadOreBlock, OreDeposConfig.Common.lead.factor.get()));
        depositTemplates.add(new DepositTemplate("silver_ore", silverOreBlock, OreDeposConfig.Common.silver.factor.get()));
        depositTemplates.add(new DepositTemplate("deepslate_silver_ore", deepslateSilverOreBlock, OreDeposConfig.Common.silver.factor.get()));
        depositTemplates.add(new DepositTemplate("aluminum_ore", aluminumOreBlock, OreDeposConfig.Common.aluminum.factor.get()));
        depositTemplates.add(new DepositTemplate("deepslate_aluminum_ore", deepslateAluminumOreBlock, OreDeposConfig.Common.aluminum.factor.get()));
        depositTemplates.add(new DepositTemplate("uranium_ore", uraniumOreBlock, OreDeposConfig.Common.uranium.factor.get()));
        depositTemplates.add(new DepositTemplate("deepslate_uranium_ore", deepslateUraniumOreBlock, OreDeposConfig.Common.uranium.factor.get()));
        depositTemplates.add(new DepositTemplate("nickel_ore", nickelOreBlock, OreDeposConfig.Common.nickel.factor.get()));
        depositTemplates.add(new DepositTemplate("deepslate_nickel_ore", deepslateNickelOreBlock, OreDeposConfig.Common.nickel.factor.get()));
        depositTemplates.add(new DepositTemplate("zinc_ore", zincOreBlock, OreDeposConfig.Common.zinc.factor.get()));
        depositTemplates.add(new DepositTemplate("deepslate_zinc_ore", deepslateZincOreBlock, OreDeposConfig.Common.zinc.factor.get()));
        depositTemplates.add(new DepositTemplate("certus_quartz_ore", certusQuartzOreBlock, OreDeposConfig.Common.certus_quartz.factor.get()));
        depositTemplates.add(new DepositTemplate("deepslate_certus_quartz_ore", deepslateCertusQuartzOreBlock, OreDeposConfig.Common.certus_quartz.factor.get()));
        depositTemplates.add(new DepositTemplate("sulfur_ore", sulfurOreBlock, OreDeposConfig.Common.sulfur.factor.get()));
        depositTemplates.add(new DepositTemplate("osmium_ore", osmiumOreBlock, OreDeposConfig.Common.osmium.factor.get()));
        depositTemplates.add(new DepositTemplate("deepslate_osmium_ore", deepslateOsmiumOreBlock, OreDeposConfig.Common.osmium.factor.get()));
        depositTemplates.add(new DepositTemplate("ardite_ore", arditeOreBlock, OreDeposConfig.Common.ardite.factor.get()));
        depositTemplates.add(new DepositTemplate("cobalt_ore", cobaltOreBlock, OreDeposConfig.Common.cobalt.factor.get()));
        depositTemplates.add(new DepositTemplate("platinum_ore", platinumOreBlock, OreDeposConfig.Common.platinum.factor.get()));
        depositTemplates.add(new DepositTemplate("deepslate_platinum_ore", deepslatePlatinumOreBlock, OreDeposConfig.Common.platinum.factor.get()));

        deposits = new LinkedList<>();
        for (DepositTemplate depositTemplate : depositTemplates) {
            deposits.add(registerOreDeposit(depositTemplate));
        }
    }

    private Supplier<Block> registerOreDeposit(DepositTemplate contained){
        Supplier<Block> block = () -> new OreDepositBlock(BlockBehaviour.Properties.of(Material.STONE)
                .strength(contained.hardness, contained.resistance)
                .lootFrom(contained.block)
                .requiresCorrectToolForDrops(), contained.needed, contained.factor);
        return registerBlock( contained.name + "_deposit", block);
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
        MINER = registerBlock("miner", () -> new MinerBlock(BlockBehaviour.Properties.of(Material.METAL)
                .strength(3, 10)
                .requiresCorrectToolForDrops()));
        CHEMICAL_PLANT = registerBlock("chemical_plant", () -> new ChemicalPlantBlock(BlockBehaviour.Properties.of(Material.METAL)
                .strength(3, 10)
                .requiresCorrectToolForDrops()));
        GRINDER = registerBlock("grinder", () -> new GrinderBlock(BlockBehaviour.Properties.of(Material.METAL)
                .strength(3, 10)
                .requiresCorrectToolForDrops()));
        SMELTER = registerBlock("smelter", () -> new SmelterBlock(BlockBehaviour.Properties.of(Material.METAL)
                .strength(3, 10)
                .requiresCorrectToolForDrops()));
        CRAFTER = registerBlock("crafter", () -> new CrafterBlock(BlockBehaviour.Properties.of(Material.METAL)
                .strength(3, 10)
                .requiresCorrectToolForDrops()));
        BEACON = registerBlock("beacon", () -> new BeaconBlock(BlockBehaviour.Properties.of(Material.METAL)
                .strength(3, 10)
                .requiresCorrectToolForDrops()));
    }

    private void registerItems(){
        ITEMS.register("iron_pickaxe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), Items.IRON_PICKAXE));
        ITEMS.register("diamond_pickaxe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), Items.DIAMOND_PICKAXE));
        ITEMS.register("netherite_pickaxe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), Items.NETHERITE_PICKAXE));
        ITEMS.register("iron_shovel_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), Items.IRON_SHOVEL));
        ITEMS.register("diamond_shovel_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), Items.DIAMOND_SHOVEL));
        ITEMS.register("netherite_shovel_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), Items.NETHERITE_SHOVEL));
        ITEMS.register("iron_axe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), Items.IRON_AXE));
        ITEMS.register("diamond_axe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), Items.DIAMOND_AXE));
        ITEMS.register("netherite_axe_drill_head", () -> new DrillHeadItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), Items.NETHERITE_AXE));
        ITEMS.register("wire", () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
        ITEMS.register("circuit", () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
        ITEMS.register("speed_module_1", () -> new SpeedModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.5f, 0.2f)
                .acceptAll(moduleAcceptors));
        ITEMS.register("speed_module_2", () -> new SpeedModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.6f, 0.3f)
                .acceptAll(moduleAcceptors));
        ITEMS.register("speed_module_3", () -> new SpeedModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.7f, 0.5f)
                .acceptAll(moduleAcceptors));
        ITEMS.register("efficiency_module_1", () -> new ModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), -0.3f)
                .acceptAll(moduleAcceptors));
        ITEMS.register("efficiency_module_2", () -> new ModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), -0.4f)
                .acceptAll(moduleAcceptors));
        ITEMS.register("efficiency_module_3", () -> new ModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), -0.5f)
                .acceptAll(moduleAcceptors));
        ITEMS.register("productivity_module_1", () -> new ProductivityModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.4f, -0.05f, 0.04f)
                .acceptAll(moduleAcceptors));
        ITEMS.register("productivity_module_2", () -> new ProductivityModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.6f, -0.1f, 0.06f)
                .acceptAll(moduleAcceptors));
        ITEMS.register("productivity_module_3", () -> new ProductivityModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.8f, -0.15f, 0.1f)
                .acceptAll(moduleAcceptors));
        ITEMS.register("length_module_1", () -> new DimensionModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.8f, 1, 0, 0, false)
                .accept(MinerTile.getName()));
        ITEMS.register("width_module_1", () -> new DimensionModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.8f, 0, 1, 0, false)
                .accept(MinerTile.getName()));
        ITEMS.register("depth_module_1", () -> new DimensionModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.8f, 0, 0, 1, false)
                .accept(MinerTile.getName()));
        ITEMS.register("inversion_module", () -> new DimensionModuleItem(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB), 0.2f, 0, 0, 0, true)
                .accept(MinerTile.getName()));
        ITEMS.register("certus_quartz", () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
        ITEMS.register("sulfur", () -> new Item(new Item.Properties().tab(OreDeposTab.ORE_DEPOS_TAB)));
    }

    private void registerTileEntities(){
        MINER_TILE = TILE_ENTITIES.register("miner_tile", () -> BlockEntityType.Builder.of(MinerTile::new, MINER.get()).build(null));
        CHEMICAL_PLANT_TILE = TILE_ENTITIES.register("chemical_plant_tile", () -> BlockEntityType.Builder.of(ChemicalPlantTile::new, CHEMICAL_PLANT.get()).build(null));
        GRINDER_TILE = TILE_ENTITIES.register("grinder_tile", () -> BlockEntityType.Builder.of(GrinderTile::new, GRINDER.get()).build(null));
        SMELTER_TILE = TILE_ENTITIES.register("smelter_tile", () -> BlockEntityType.Builder.of(SmelterTile::new, SMELTER.get()).build(null));
        CRAFTER_TILE = TILE_ENTITIES.register("crafter_tile", () -> BlockEntityType.Builder.of(CrafterTile::new, CRAFTER.get()).build(null));
        BEACON_TILE = TILE_ENTITIES.register("beacon_tile", () -> BlockEntityType.Builder.of(BeaconTile::new, BEACON.get()).build(null));
        ORE_DEPOSIT_TILE = TILE_ENTITIES.register("ore_deposit_tile", () -> BlockEntityType.Builder.of(OreDepositTile::new, deposits.stream().map(Supplier::get).toList().toArray(new Block[0])).build(null));
    }

    private void registerSerializers(){
        RECIPE_SERIALIZERS.register("chemical_plant_recipe", ChemicalPlantRecipe.Serializer::new);
        RECIPE_SERIALIZERS.register("grinder_recipe", GrinderRecipe.Serializer::new);
        RECIPE_SERIALIZERS.register("smelter_recipe", SmelterRecipe.Serializer::new);
        RECIPE_SERIALIZERS.register("crafter_recipe", CrafterRecipe.Serializer::new);
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
        FEATURES.register(eventBus);

        FLUIDS.register(eventBus);
        registerSerializers();
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
        PLACEMENT_MODIFIER_TYPE.register(eventBus);
    }
}
