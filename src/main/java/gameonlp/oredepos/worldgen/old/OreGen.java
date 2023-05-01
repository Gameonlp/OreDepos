package gameonlp.oredepos.worldgen.old;

import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.config.OreConfig;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.worldgen.hacks.ODCountPlacement;
import gameonlp.oredepos.worldgen.hacks.ODOreConfiguration;
import gameonlp.oredepos.worldgen.hacks.ODTrapezoidHeight;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class OreGen {

    public enum NetherOre {
        COBALT_DEPOSIT(Lazy.of(() -> RegistryManager.COBALT_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.COBALT_ORE), OreDeposConfig.Common.cobalt),
        ARDITE_DEPOSIT(Lazy.of(() -> RegistryManager.ARDITE_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.ARDITE_ORE), OreDeposConfig.Common.ardite),
        SULFUR_DEPOSIT(Lazy.of(() -> RegistryManager.SULFUR_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.SULFUR_ORE), OreDeposConfig.Common.sulfur),
        ANCIENT_DEBRIS_DEPOSIT(Lazy.of(() -> RegistryManager.ANCIENT_DEBRIS_DEPOSIT), Lazy.of(() -> RegistryManager.ANCIENT_DEBRIS), OreDeposConfig.Common.ancient_debris),
        NETHER_GOLD_DEPOSIT(Lazy.of(() -> RegistryManager.NETHER_GOLD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.NETHER_GOLD_ORE), OreDeposConfig.Common.nether_gold),
        NETHER_QUARTZ_DEPOSIT(Lazy.of(() -> RegistryManager.NETHER_QUARTZ_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.NETHER_QUARTZ_ORE), OreDeposConfig.Common.nether_quartz);

        private  OreConfig config;
        private RegistryObject<ConfiguredFeature<?, ?>> depositFeature;
        public RegistryObject<PlacedFeature> depositPlaced;
        private RegistryObject<ConfiguredFeature<?, ?>> replaceFeature;
        public RegistryObject<PlacedFeature> replacePlaced;
        private  RegistryObject<ConfiguredFeature<?, ?>> replacableFeature;
        public RegistryObject<PlacedFeature> replacablePlaced;
        private final Lazy<Block> block;
        private final Lazy<Block> replaceBlock;

        NetherOre(Lazy<Block> block, Lazy<Block> replaceBlock, OreConfig config){
            this.block = block;
            this.replaceBlock = replaceBlock;
            this.config = config;
        }

        private void registerConf(DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED) {
            this.depositFeature = CONFIGURED.register(config.getName() + "_deposit_feature", () -> new ConfiguredFeature<>(RegistryManager.ORE.get(),
                    new ODOreConfiguration(List.of(OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, block.get().defaultBlockState())), 0, config)));
            this.replaceFeature = CONFIGURED.register(config.getName() + "_deposit_replace_feature", () -> new ConfiguredFeature<>(RegistryManager.ORE.get(),
                    new ODOreConfiguration(List.of(OreConfiguration.target(new BlockMatchTest(replaceBlock.get()), block.get().defaultBlockState())), 0, config)));
            if (config.noBaseBlock) {
                this.replacableFeature = CONFIGURED.register(config.getName() + "_feature", () -> new ConfiguredFeature<>(RegistryManager.ORE.get(),
                        new ODOreConfiguration(List.of(OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, replaceBlock.get().defaultBlockState())), 0, config)));
            } else {
                replacableFeature = null;
                replacablePlaced = null;
            }
        }
        private void registerPlaced(DeferredRegister<PlacedFeature> PLACED) {
            this.depositPlaced = PLACED.register(config.getName() + "_deposit_placed",
                    () -> new PlacedFeature(depositFeature.getHolder().get(), commonOrePlacement(config, // VeinsPerChunk
                            HeightRangePlacement.of(new ODTrapezoidHeight(config)))));
            this.replacePlaced = PLACED.register(config.getName() + "_deposit_replace_placed",
                    () -> new PlacedFeature(replaceFeature.getHolder().get(), commonOrePlacement(config, // VeinsPerChunk
                            HeightRangePlacement.of(new ODTrapezoidHeight(config)))));
            if (config.noBaseBlock) {
                this.replacablePlaced = PLACED.register(config.getName() + "_placed",
                        () -> new PlacedFeature(replacableFeature.getHolder().get(), commonOrePlacement(config, // VeinsPerChunk
                                HeightRangePlacement.of(new ODTrapezoidHeight(config)))));
            } else {
                replacableFeature = null;
                replacablePlaced = null;
            }
        }
    }
    public enum Ore {
        TIN_DEPOSIT(Lazy.of(() -> RegistryManager.TIN_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.TIN_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_TIN_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_TIN_ORE), OreDeposConfig.Common.tin),
        LEAD_DEPOSIT(Lazy.of(() -> RegistryManager.LEAD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.LEAD_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_LEAD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_LEAD_ORE), OreDeposConfig.Common.lead),
        SILVER_DEPOSIT(Lazy.of(() -> RegistryManager.SILVER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.SILVER_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_SILVER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_SILVER_ORE), OreDeposConfig.Common.silver),
        ALUMINUM_DEPOSIT(Lazy.of(() -> RegistryManager.ALUMINUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.ALUMINUM_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_ALUMINUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_ALUMINUM_ORE), OreDeposConfig.Common.aluminum),
        COPPER_DEPOSIT(Lazy.of(() -> RegistryManager.COPPER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.COPPER_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_COPPER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_COPPER_ORE), OreDeposConfig.Common.copper),
        NICKEL_DEPOSIT(Lazy.of(() -> RegistryManager.NICKEL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.NICKEL_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_NICKEL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_NICKEL_ORE), OreDeposConfig.Common.nickel),
        URANIUM_DEPOSIT(Lazy.of(() -> RegistryManager.URANIUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.URANIUM_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_URANIUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_URANIUM_ORE), OreDeposConfig.Common.uranium),
        ZINC_DEPOSIT(Lazy.of(() -> RegistryManager.ZINC_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.ZINC_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_ZINC_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_ZINC_ORE), OreDeposConfig.Common.zinc),
        CERTUS_QUARTZ_DEPOSIT(Lazy.of(() -> RegistryManager.CERTUS_QUARTZ_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.CERTUS_QUARTZ_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_CERTUS_QUARTZ_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_CERTUS_QUARTZ_ORE), OreDeposConfig.Common.certus_quartz),
        OSMIUM_DEPOSIT(Lazy.of(() -> RegistryManager.OSMIUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.OSMIUM_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_OSMIUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_OSMIUM_ORE), OreDeposConfig.Common.osmium),
        PLATINUM_DEPOSIT(Lazy.of(() -> RegistryManager.PLATINUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.PLATINUM_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_PLATINUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_PLATINUM_ORE), OreDeposConfig.Common.platinum),
        COAL_DEPOSIT(Lazy.of(() -> RegistryManager.COAL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.COAL_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_COAL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_COAL_ORE), OreDeposConfig.Common.coal),
        IRON_DEPOSIT(Lazy.of(() -> RegistryManager.IRON_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.IRON_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_IRON_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_IRON_ORE), OreDeposConfig.Common.iron),
        REDSTONE_DEPOSIT(Lazy.of(() -> RegistryManager.REDSTONE_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.REDSTONE_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_REDSTONE_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_REDSTONE_ORE), OreDeposConfig.Common.redstone),
        GOLD_DEPOSIT(Lazy.of(() -> RegistryManager.GOLD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.GOLD_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_GOLD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_GOLD_ORE), OreDeposConfig.Common.gold),
        LAPIS_DEPOSIT(Lazy.of(() -> RegistryManager.LAPIS_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.LAPIS_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_LAPIS_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_LAPIS_ORE), OreDeposConfig.Common.lapis),
        DIAMOND_DEPOSIT(Lazy.of(() -> RegistryManager.DIAMOND_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DIAMOND_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_DIAMOND_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_DIAMOND_ORE), OreDeposConfig.Common.diamond),
        EMERALD_DEPOSIT(Lazy.of(() -> RegistryManager.EMERALD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.EMERALD_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_EMERALD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_EMERALD_ORE), OreDeposConfig.Common.emerald);

        private Lazy<Block> block;
        private Lazy<Block> replaceBlock;
        private Lazy<Block> deepslateBlock;
        private Lazy<Block> deepslateReplaceBlock;
        private final OreConfig config;
        private RegistryObject<ConfiguredFeature<?, ?>> depositFeature;
        public RegistryObject<PlacedFeature> depositPlaced;
        private RegistryObject<ConfiguredFeature<?, ?>> replaceFeature;
        public RegistryObject<PlacedFeature> replacePlaced;
        private RegistryObject<ConfiguredFeature<?, ?>> replacableFeature;
        public RegistryObject<PlacedFeature> replacablePlaced;

        Ore(Lazy<Block> block, Lazy<Block> replaceBlock, Lazy<Block> deepslateBlock, Lazy<Block> deepslateReplaceBlock, OreConfig config){
            this.block = block;
            this.replaceBlock = replaceBlock;
            this.deepslateBlock = deepslateBlock;
            this.deepslateReplaceBlock = deepslateReplaceBlock;
            this.config = config;
        }

        private void registerConf(DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED) {
            this.depositFeature = CONFIGURED.register(config.getName() + "_deposit_feature", () -> new ConfiguredFeature<>(RegistryManager.ORE.get(),
                    new ODOreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, block.get().defaultBlockState()),
                            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateBlock.get().defaultBlockState())), 0, config)));
            this.replaceFeature = CONFIGURED.register(config.getName() + "_deposit_replace_feature", () -> new ConfiguredFeature<>(RegistryManager.ORE.get(),
                    new ODOreConfiguration(List.of(OreConfiguration.target(new BlockMatchTest(replaceBlock.get()), block.get().defaultBlockState()),
                            OreConfiguration.target(new BlockMatchTest(deepslateReplaceBlock.get()), deepslateBlock.get().defaultBlockState())), 0, config)));
            if (config.noBaseBlock) {
                this.replacableFeature = CONFIGURED.register(config.getName() + "_feature", () -> new ConfiguredFeature<>(RegistryManager.ORE.get(),
                        new ODOreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, replaceBlock.get().defaultBlockState()),
                                OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateReplaceBlock.get().defaultBlockState())), 0, config)));
            } else {
                replacableFeature = null;
                replacablePlaced = null;
            }
        }

        private void registerPlaced(DeferredRegister<PlacedFeature> PLACED) {
            this.depositPlaced = PLACED.register(config.getName() + "_deposit_placed",
                    () -> new PlacedFeature(depositFeature.getHolder().get(), commonOrePlacement(config, // VeinsPerChunk
                            HeightRangePlacement.of(new ODTrapezoidHeight(config)))));
            this.replacePlaced = PLACED.register(config.getName() + "_deposit_replace_placed",
                    () -> new PlacedFeature(replaceFeature.getHolder().get(), commonOrePlacement(config, // VeinsPerChunk
                            HeightRangePlacement.of(new ODTrapezoidHeight(config)))));
            if (config.noBaseBlock) {
                this.replacablePlaced = PLACED.register(config.getName() + "_placed",
                        () -> new PlacedFeature(replacableFeature.getHolder().get(), commonOrePlacement(config, // VeinsPerChunk
                                HeightRangePlacement.of(new ODTrapezoidHeight(config)))));
            } else {
                replacableFeature = null;
                replacablePlaced = null;
            }
        }
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(OreConfig config, PlacementModifier p_195345_) {
        return orePlacement(new ODCountPlacement(config), p_195345_);
    }

    public static void registerConf(DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED) {
        for (Ore value : Ore.values()) {
            value.registerConf(CONFIGURED);
        }
        for (NetherOre value : NetherOre.values()) {
            value.registerConf(CONFIGURED);
        }
    }

    public static void registerPlaced(DeferredRegister<PlacedFeature> PLACED) {
        for (Ore value : Ore.values()) {
            value.registerPlaced(PLACED);
        }
        for (NetherOre value : NetherOre.values()) {
            value.registerPlaced(PLACED);
        }
    }
}
