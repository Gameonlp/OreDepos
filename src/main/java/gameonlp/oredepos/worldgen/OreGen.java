package gameonlp.oredepos.worldgen;

import gameonlp.oredepos.OreDeposConfig;
import gameonlp.oredepos.RegistryManager;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.function.Supplier;

public class OreGen {
    private enum Ore {
        COAL_DEPOSIT(Lazy.of(() -> RegistryManager.COAL_ORE_DEPOSIT), OreDeposConfig.Common.coalEnable::get, 17, 1, 128,20),
        IRON_DEPOSIT(Lazy.of(() -> RegistryManager.IRON_ORE_DEPOSIT), OreDeposConfig.Common.ironEnable::get, 9, 1, 68, 20),
        REDSTONE_DEPOSIT(Lazy.of(() -> RegistryManager.REDSTONE_ORE_DEPOSIT), OreDeposConfig.Common.redstoneEnable::get, 8, 1, 16, 8),
        GOLD_DEPOSIT(Lazy.of(() -> RegistryManager.GOLD_ORE_DEPOSIT), OreDeposConfig.Common.goldEnable::get, 9, 1, 30, 5),
        LAPIS_DEPOSIT(Lazy.of(() -> RegistryManager.LAPIS_ORE_DEPOSIT), OreDeposConfig.Common.lapisEnable::get, 7, 1, 34, 2),
        DIAMOND_DEPOSIT(Lazy.of(() -> RegistryManager.DIAMOND_ORE_DEPOSIT), OreDeposConfig.Common.diamondEnable::get, 8, 1, 16, 1),
        EMERALD_DEPOSIT(Lazy.of(() -> RegistryManager.EMERALD_ORE_DEPOSIT), OreDeposConfig.Common.emeraldEnable::get, 1, 1, 33, 1),
        TIN_DEPOSIT(Lazy.of(() -> RegistryManager.TIN_ORE_DEPOSIT), OreDeposConfig.Common.tinEnable::get, 9, 1, 68, 20),
        LEAD_DEPOSIT(Lazy.of(() -> RegistryManager.LEAD_ORE_DEPOSIT), OreDeposConfig.Common.leadEnable::get, 6, 1, 35, 8),
        SILVER_DEPOSIT(Lazy.of(() -> RegistryManager.SILVER_ORE_DEPOSIT), OreDeposConfig.Common.silverEnable::get, 6, 1, 35, 8),
        ALUMINUM_DEPOSIT(Lazy.of(() -> RegistryManager.ALUMINUM_ORE_DEPOSIT), OreDeposConfig.Common.aluminumEnable::get, 6, 1, 64, 12),
        COPPER_DEPOSIT(Lazy.of(() -> RegistryManager.COPPER_ORE_DEPOSIT), OreDeposConfig.Common.copperEnable::get, 9, 1, 68, 20);

        private final Lazy<Block> block;
        private Supplier<Boolean> enabled;
        private final int veinSize;
        private final int minHeight;
        private final int maxHeight;
        private int maxCountPerChunk;

        private Ore(Lazy<Block> block, Lazy<Boolean> enabled, int veinSize, int minHeight, int maxHeight, int maxCountPerChunk){
            this.block = block;
            this.enabled = enabled;
            this.veinSize = veinSize;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.maxCountPerChunk = maxCountPerChunk;
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
            if (ore.enabled.get()) {
                OreFeatureConfig oreFeatureConfig = new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                        ore.block.get().defaultBlockState(), ore.veinSize);
                ConfiguredPlacement<?> genRange = Placement.RANGE.configured(new TopSolidRangeConfig(ore.minHeight, ore.minHeight, ore.maxHeight));
                ConfiguredFeature<?, ?> feature = register(ore, oreFeatureConfig, genRange, ore.maxCountPerChunk);
                if (event.getCategory() != Biome.Category.EXTREME_HILLS && ore == Ore.EMERALD_DEPOSIT)
                    continue;
                event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
            }
        }
    }

    private static ConfiguredFeature<?, ?> register(Ore ore, OreFeatureConfig featureConfig, ConfiguredPlacement placement, int count){
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, ore.block.get().getRegistryName(),
                Feature.ORE.configured(featureConfig).decorated(placement).squared().count(count));
    }
}
