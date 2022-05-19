package gameonlp.oredepos.worldgen;

import gameonlp.oredepos.config.OreConfig;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;

import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class OreGen {
    private enum NetherOre {
        COBALT_DEPOSIT(Lazy.of(() -> RegistryManager.COBALT_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.COBALT_ORE), OreDeposConfig.Server.cobalt),
        ARDITE_DEPOSIT(Lazy.of(() -> RegistryManager.ARDITE_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.ARDITE_ORE), OreDeposConfig.Server.ardite),
        SULFUR_DEPOSIT(Lazy.of(() -> RegistryManager.SULFUR_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.SULFUR_ORE), OreDeposConfig.Server.sulfur),
        ANCIENT_DEBRIS_DEPOSIT(Lazy.of(() -> RegistryManager.ANCIENT_DEBRIS_DEPOSIT), Lazy.of(() -> RegistryManager.ANCIENT_DEBRIS), OreDeposConfig.Server.ancient_debris),
        NETHER_GOLD_DEPOSIT(Lazy.of(() -> RegistryManager.NETHER_GOLD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.NETHER_GOLD_ORE), OreDeposConfig.Server.nether_gold),
        NETHER_QUARTZ_DEPOSIT(Lazy.of(() -> RegistryManager.NETHER_QUARTZ_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.NETHER_QUARTZ_ORE), OreDeposConfig.Server.nether_quartz);

        private final OreConfig config;
        private final Holder<ConfiguredFeature<OreConfiguration,?>> depositFeature;
        private final Holder<PlacedFeature> depositPlaced;
        private final Holder<ConfiguredFeature<OreConfiguration,?>> replaceFeature;
        private final Holder<PlacedFeature> replacePlaced;
        private final Holder<ConfiguredFeature<OreConfiguration,?>> replacableFeature;
        private final Holder<PlacedFeature> replacablePlaced;

        private NetherOre(Lazy<Block> block, Lazy<Block> replaceBlock, OreConfig config){
            this.config = config;
            this.depositFeature = FeatureUtils.register(block.get().getRegistryName() + "_feature", Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, block.get().defaultBlockState())), config.veinSize.get()));
            this.depositPlaced = PlacementUtils.register(block.get().getRegistryName() + "_placed",
                    depositFeature, commonOrePlacement(config.count.get(), // VeinsPerChunk
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(config.minHeight.get()), VerticalAnchor.aboveBottom(config.maxHeight.get()))));
            this.replaceFeature = FeatureUtils.register(block.get().getRegistryName() + "_replace_feature", Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(new BlockMatchTest(replaceBlock.get()), block.get().defaultBlockState())), config.veinSize.get()));
            this.replacePlaced = PlacementUtils.register(block.get().getRegistryName() + "_replace_placed",
                    replaceFeature, commonOrePlacement(config.count.get(), // VeinsPerChunk
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(config.minHeight.get()), VerticalAnchor.aboveBottom(config.maxHeight.get()))));
            if (config.isModded) {
                this.replacableFeature = FeatureUtils.register(replaceBlock.get().getRegistryName() + "_feature", Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, replaceBlock.get().defaultBlockState())), config.veinSize.get()));
                this.replacablePlaced = PlacementUtils.register(replaceBlock.get().getRegistryName() + "_placed",
                        replacableFeature, commonOrePlacement(config.count.get(), // VeinsPerChunk
                                HeightRangePlacement.triangle(VerticalAnchor.absolute(config.minHeight.get()), VerticalAnchor.aboveBottom(config.maxHeight.get()))));
            } else {
                replacableFeature = null;
                replacablePlaced = null;
            }
        }
    }
    private enum Ore {
        TIN_DEPOSIT(Lazy.of(() -> RegistryManager.TIN_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.TIN_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_TIN_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_TIN_ORE), OreDeposConfig.Server.tin),
        LEAD_DEPOSIT(Lazy.of(() -> RegistryManager.LEAD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.LEAD_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_LEAD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_LEAD_ORE), OreDeposConfig.Server.lead),
        SILVER_DEPOSIT(Lazy.of(() -> RegistryManager.SILVER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.SILVER_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_SILVER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_SILVER_ORE), OreDeposConfig.Server.silver),
        ALUMINUM_DEPOSIT(Lazy.of(() -> RegistryManager.ALUMINUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.ALUMINUM_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_ALUMINUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_ALUMINUM_ORE), OreDeposConfig.Server.aluminum),
        COPPER_DEPOSIT(Lazy.of(() -> RegistryManager.COPPER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.COPPER_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_COPPER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_COPPER_ORE), OreDeposConfig.Server.copper),
        NICKEL_DEPOSIT(Lazy.of(() -> RegistryManager.NICKEL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.NICKEL_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_NICKEL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_NICKEL_ORE), OreDeposConfig.Server.nickel),
        URANIUM_DEPOSIT(Lazy.of(() -> RegistryManager.URANIUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.URANIUM_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_URANIUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_URANIUM_ORE), OreDeposConfig.Server.uranium),
        ZINC_DEPOSIT(Lazy.of(() -> RegistryManager.ZINC_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.ZINC_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_ZINC_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_ZINC_ORE), OreDeposConfig.Server.zinc),
        CERTUS_QUARTZ_DEPOSIT(Lazy.of(() -> RegistryManager.CERTUS_QUARTZ_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.CERTUS_QUARTZ_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_CERTUS_QUARTZ_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_CERTUS_QUARTZ_ORE), OreDeposConfig.Server.certus_quartz),
        OSMIUM_DEPOSIT(Lazy.of(() -> RegistryManager.OSMIUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.OSMIUM_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_OSMIUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_OSMIUM_ORE), OreDeposConfig.Server.osmium),
        PLATINUM_DEPOSIT(Lazy.of(() -> RegistryManager.PLATINUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.PLATINUM_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_PLATINUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_PLATINUM_ORE), OreDeposConfig.Server.platinum),
        COAL_DEPOSIT(Lazy.of(() -> RegistryManager.COAL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.COAL_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_COAL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_COAL_ORE), OreDeposConfig.Server.coal),
        IRON_DEPOSIT(Lazy.of(() -> RegistryManager.IRON_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.IRON_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_IRON_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_IRON_ORE), OreDeposConfig.Server.iron),
        REDSTONE_DEPOSIT(Lazy.of(() -> RegistryManager.REDSTONE_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.REDSTONE_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_REDSTONE_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_REDSTONE_ORE), OreDeposConfig.Server.redstone),
        GOLD_DEPOSIT(Lazy.of(() -> RegistryManager.GOLD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.GOLD_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_GOLD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_GOLD_ORE), OreDeposConfig.Server.gold),
        LAPIS_DEPOSIT(Lazy.of(() -> RegistryManager.LAPIS_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.LAPIS_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_LAPIS_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_LAPIS_ORE), OreDeposConfig.Server.lapis),
        DIAMOND_DEPOSIT(Lazy.of(() -> RegistryManager.DIAMOND_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DIAMOND_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_DIAMOND_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_DIAMOND_ORE), OreDeposConfig.Server.diamond),
        EMERALD_DEPOSIT(Lazy.of(() -> RegistryManager.EMERALD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.EMERALD_ORE), Lazy.of(() -> RegistryManager.DEEPSLATE_EMERALD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DEEPSLATE_EMERALD_ORE), OreDeposConfig.Server.emerald);

        private final OreConfig config;
        private final Holder<ConfiguredFeature<OreConfiguration, ?>> depositFeature;
        private final Holder<PlacedFeature> depositPlaced;
        private final Holder<ConfiguredFeature<OreConfiguration, ?>> replaceFeature;
        private final Holder<PlacedFeature> replacePlaced;
        private final Holder<ConfiguredFeature<OreConfiguration, ?>> replacableFeature;
        private final Holder<PlacedFeature> replacablePlaced;

        Ore(Lazy<Block> block, Lazy<Block> replaceBlock, Lazy<Block> deepslateBlock, Lazy<Block> deepslateReplaceBlock, OreConfig config){
            this.config = config;
            this.depositFeature = FeatureUtils.register(block.get().getRegistryName() + "_feature", Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, block.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateBlock.get().defaultBlockState())), config.veinSize.get()));
            this.depositPlaced = PlacementUtils.register(block.get().getRegistryName() + "_placed",
                    depositFeature, commonOrePlacement(config.count.get(), // VeinsPerChunk
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(config.minHeight.get()), VerticalAnchor.aboveBottom(config.maxHeight.get()))));
            this.replaceFeature = FeatureUtils.register(block.get().getRegistryName() + "_replace_feature", Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(new BlockMatchTest(replaceBlock.get()), block.get().defaultBlockState()), OreConfiguration.target(new BlockMatchTest(deepslateReplaceBlock.get()), deepslateBlock.get().defaultBlockState())), config.veinSize.get()));
            this.replacePlaced = PlacementUtils.register(block.get().getRegistryName() + "_replace_placed",
                    replaceFeature, commonOrePlacement(config.count.get(), // VeinsPerChunk
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(config.minHeight.get()), VerticalAnchor.aboveBottom(config.maxHeight.get()))));
            if (config.isModded) {
                this.replacableFeature = FeatureUtils.register(replaceBlock.get().getRegistryName() + "_feature", Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, replaceBlock.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateReplaceBlock.get().defaultBlockState())), config.veinSize.get()));
                this.replacablePlaced = PlacementUtils.register(replaceBlock.get().getRegistryName() + "_placed",
                        replacableFeature, commonOrePlacement(config.count.get(), // VeinsPerChunk
                                HeightRangePlacement.triangle(VerticalAnchor.absolute(config.minHeight.get()), VerticalAnchor.aboveBottom(config.maxHeight.get()))));
            } else {
                replacableFeature = null;
                replacablePlaced = null;
            }
        }
    }

    public static void oreGeneration(final BiomeLoadingEvent event) {
        for (Ore ore : Ore.values()) {
            if (ore.config.enabled.get()) {
                if (ore.config.replace.get() && ore.config.isModded){
                    generateOre(event, ore, ore.replacablePlaced);
                    replaceOre(event, ore);
                } else if (ore.config.replace.get()){
                    replaceOre(event, ore);
                } else {
                    generateOre(event, ore, ore.depositPlaced);
                }
            }
        }
        for (NetherOre ore : NetherOre.values()) {
            if (ore.config.enabled.get()) {
                if (ore.config.replace.get() && ore.config.isModded){
                    generateOre(event, ore, ore.replacablePlaced);
                    replaceOre(event, ore);
                } else if (ore.config.replace.get()){
                    replaceOre(event, ore);
                } else {
                    generateOre(event, ore, ore.depositPlaced);
                }
            }
        }
    }

    private static void generateOre(BiomeLoadingEvent event, Ore ore, Holder<PlacedFeature> featureHolder) {
        List<Holder<PlacedFeature>> features = event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);

        if (event.getCategory() != Biome.BiomeCategory.EXTREME_HILLS && ore == Ore.EMERALD_DEPOSIT)
            return;

        features.add(featureHolder);
    }
    private static void generateOre(BiomeLoadingEvent event, NetherOre ore, Holder<PlacedFeature> featureHolder) {
        List<Holder<PlacedFeature>> features = event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);
        features.add(featureHolder);
    }

    private static void replaceOre(BiomeLoadingEvent event, Ore ore) {
        generateOre(event, ore, ore.replacePlaced);
    }

    private static void replaceOre(BiomeLoadingEvent event, NetherOre ore) {
        generateOre(event, ore, ore.replacePlaced);
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }
}
