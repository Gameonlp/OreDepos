package gameonlp.oredepos.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import gameonlp.oredepos.config.OreConfig;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class OreGen {
    private enum Ore {
        TIN_DEPOSIT(Lazy.of(() -> RegistryManager.TIN_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.TIN_ORE), OreDeposConfig.Common.tin),
        LEAD_DEPOSIT(Lazy.of(() -> RegistryManager.LEAD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.LEAD_ORE), OreDeposConfig.Common.lead),
        SILVER_DEPOSIT(Lazy.of(() -> RegistryManager.SILVER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.SILVER_ORE), OreDeposConfig.Common.silver),
        ALUMINUM_DEPOSIT(Lazy.of(() -> RegistryManager.ALUMINUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.ALUMINUM_ORE), OreDeposConfig.Common.aluminum),
        COPPER_DEPOSIT(Lazy.of(() -> RegistryManager.COPPER_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.COPPER_ORE), OreDeposConfig.Common.copper),
        URANIUM_DEPOSIT(Lazy.of(() -> RegistryManager.URANIUM_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.URANIUM_ORE), OreDeposConfig.Common.uranium),
        COAL_DEPOSIT(Lazy.of(() -> RegistryManager.COAL_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.COAL_ORE), OreDeposConfig.Common.coal),
        IRON_DEPOSIT(Lazy.of(() -> RegistryManager.IRON_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.IRON_ORE), OreDeposConfig.Common.iron),
        REDSTONE_DEPOSIT(Lazy.of(() -> RegistryManager.REDSTONE_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.REDSTONE_ORE), OreDeposConfig.Common.redstone),
        GOLD_DEPOSIT(Lazy.of(() -> RegistryManager.GOLD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.GOLD_ORE), OreDeposConfig.Common.gold),
        LAPIS_DEPOSIT(Lazy.of(() -> RegistryManager.LAPIS_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.LAPIS_ORE), OreDeposConfig.Common.lapis),
        DIAMOND_DEPOSIT(Lazy.of(() -> RegistryManager.DIAMOND_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.DIAMOND_ORE), OreDeposConfig.Common.diamond),
        EMERALD_DEPOSIT(Lazy.of(() -> RegistryManager.EMERALD_ORE_DEPOSIT), Lazy.of(() -> RegistryManager.EMERALD_ORE), OreDeposConfig.Common.emerald);

        private final Lazy<Block> block;
        private final Lazy<Block> replaceBlock;
        private final OreConfig config;

        private Ore(Lazy<Block> block, Lazy<Block> replaceBlock, OreConfig config){
            this.block = block;
            this.replaceBlock = replaceBlock;
            this.config = config;
        }

        public static Ore get(Block block){
            for (Ore ore : Ore.values()) {
                if(ore.block.equals(block)){
                    return ore;
                }
            }
            return null;
        }
    }

    public static void oreGeneration(final BiomeLoadingEvent event) {
        for (Ore ore : Ore.values()) {
            if (ore.config.enabled.get()) {
                if (ore.config.replace.get() && ore.config.isModded){
                    generateOre(event, ore.replaceBlock.get(), ore);
                    replaceOre(event, ore.block.get(), ore.replaceBlock.get(), ore);
                } else if (ore.config.replace.get()){
                    replaceOre(event, ore.block.get(), ore.replaceBlock.get(), ore);
                } else {
                    generateOre(event, ore.block.get(), ore);
                }
            }
        }
    }

    private static void generateOre(BiomeLoadingEvent event, Block block, Ore ore) {
        OreFeatureConfig oreFeatureConfig = new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                block.defaultBlockState(), ore.config.veinSize.get());
        ConfiguredPlacement<?> genRange = Placement.RANGE.configured(new TopSolidRangeConfig(ore.config.minHeight.get(), ore.config.minHeight.get(), ore.config.maxHeight.get()));
        ConfiguredFeature<?, ?> feature = register(block, oreFeatureConfig, genRange, ore.config.count.get());
        if (event.getCategory() != Biome.Category.EXTREME_HILLS && ore == Ore.EMERALD_DEPOSIT)
            return;
        event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
    }

    private static void replaceOre(BiomeLoadingEvent event, Block block, Block toReplace, Ore ore) {
        ReplaceBlockConfig replace = new ReplaceBlockConfig(toReplace.defaultBlockState(), block.defaultBlockState());
        ConfiguredPlacement<?> blocksToCheck = Placement.RANGE.configured(new TopSolidRangeConfig(ore.config.minHeight.get(), ore.config.minHeight.get(), ore.config.maxHeight.get()));
        ConfiguredFeature<?, ?> confFeature = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, block.getRegistryName(),
                RegistryManager.REPLACE_FEATURE.configured(replace).decorated(blocksToCheck).squared().count(128).count(128 / ore.config.veinSize.get()));//TODO not happy about this might make my own Placement config
        event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, confFeature);
    }

    private static ConfiguredFeature<?, ?> register(Block block, OreFeatureConfig featureConfig, ConfiguredPlacement placement, int count){
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, block.getRegistryName(),
                Feature.ORE.configured(featureConfig).decorated(placement).squared().count(count));
    }
}
