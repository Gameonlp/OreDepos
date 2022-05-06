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
    private enum Ore {
        TIN_DEPOSIT(Lazy.of(() -> RegistryManager.TIN_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.TIN_ORE), OreDeposConfig.Server.tin),
        LEAD_DEPOSIT(Lazy.of(() -> RegistryManager.LEAD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.LEAD_ORE), OreDeposConfig.Server.lead),
        SILVER_DEPOSIT(Lazy.of(() -> RegistryManager.SILVER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.SILVER_ORE), OreDeposConfig.Server.silver),
        ALUMINUM_DEPOSIT(Lazy.of(() -> RegistryManager.ALUMINUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.ALUMINUM_ORE), OreDeposConfig.Server.aluminum),
        COPPER_DEPOSIT(Lazy.of(() -> RegistryManager.COPPER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.COPPER_ORE), OreDeposConfig.Server.copper),
        NICKEL_DEPOSIT(Lazy.of(() -> RegistryManager.NICKEL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.NICKEL_ORE), OreDeposConfig.Server.nickel),
        URANIUM_DEPOSIT(Lazy.of(() -> RegistryManager.URANIUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.URANIUM_ORE), OreDeposConfig.Server.uranium),
        ZINC_DEPOSIT(Lazy.of(() -> RegistryManager.ZINC_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.ZINC_ORE), OreDeposConfig.Server.zinc),
        COAL_DEPOSIT(Lazy.of(() -> RegistryManager.COAL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.COAL_ORE), OreDeposConfig.Server.coal),
        IRON_DEPOSIT(Lazy.of(() -> RegistryManager.IRON_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.IRON_ORE), OreDeposConfig.Server.iron),
        REDSTONE_DEPOSIT(Lazy.of(() -> RegistryManager.REDSTONE_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.REDSTONE_ORE), OreDeposConfig.Server.redstone),
        GOLD_DEPOSIT(Lazy.of(() -> RegistryManager.GOLD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.GOLD_ORE), OreDeposConfig.Server.gold),
        LAPIS_DEPOSIT(Lazy.of(() -> RegistryManager.LAPIS_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.LAPIS_ORE), OreDeposConfig.Server.lapis),
        DIAMOND_DEPOSIT(Lazy.of(() -> RegistryManager.DIAMOND_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DIAMOND_ORE), OreDeposConfig.Server.diamond),
        EMERALD_DEPOSIT(Lazy.of(() -> RegistryManager.EMERALD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.EMERALD_ORE), OreDeposConfig.Server.emerald);

        private final Lazy<Block> block;
        private final Lazy<Block> replaceBlock;
        private final OreConfig config;
        private final Holder<ConfiguredFeature<OreConfiguration, ?>> depositFeature;
        private final Holder<PlacedFeature> depositPlaced;
        private final Holder<ConfiguredFeature<OreConfiguration, ?>> replaceFeature;
        private final Holder<PlacedFeature> replacePlaced;
        private final Holder<ConfiguredFeature<OreConfiguration, ?>> replacableFeature;
        private final Holder<PlacedFeature> replacablePlaced;

        Ore(Lazy<Block> block, Lazy<Block> replaceBlock, OreConfig config){
            this.block = block;
            this.replaceBlock = replaceBlock;
            this.config = config;
            this.depositFeature = FeatureUtils.register(block.get().getRegistryName() + "_feature", Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, block.get().defaultBlockState())), config.veinSize.get()));
            this.depositPlaced = PlacementUtils.register(block.get().getRegistryName() + "_placed",
                    depositFeature, commonOrePlacement(config.count.get(), // VeinsPerChunk
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(config.minHeight.get()), VerticalAnchor.aboveBottom(config.maxHeight.get()))));
            this.replaceFeature = FeatureUtils.register(block.get().getRegistryName() + "_replace_feature", Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(new BlockMatchTest(replaceBlock.get()), block.get().defaultBlockState())), config.veinSize.get()));
            this.replacePlaced = PlacementUtils.register(block.get().getRegistryName() + "_replace_placed",
                    replaceFeature, commonOrePlacement(config.count.get(), // VeinsPerChunk
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(config.minHeight.get()), VerticalAnchor.aboveBottom(config.maxHeight.get()))));
            if (config.isModded) {
                this.replacableFeature = FeatureUtils.register(replaceBlock.get().getRegistryName() + "_feature", Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, replaceBlock.get().defaultBlockState())), config.veinSize.get()));
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
    }

    private static void generateOre(BiomeLoadingEvent event, Ore ore, Holder<PlacedFeature> featureHolder) {
        List<Holder<PlacedFeature>> features = event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);

        if (event.getCategory() != Biome.BiomeCategory.EXTREME_HILLS && ore == Ore.EMERALD_DEPOSIT)
            return;

        features.add(featureHolder);
    }

    private static void replaceOre(BiomeLoadingEvent event, Ore ore) {
        generateOre(event, ore, ore.replacePlaced);
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }
}
