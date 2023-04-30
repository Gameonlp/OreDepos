package gameonlp.oredepos.worldgen.hacks;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class ODOreFeature extends OreFeature {
    public ODOreFeature(Codec<OreConfiguration> p_66531_) {
        super(p_66531_);
    }

    public boolean place(FeaturePlaceContext<OreConfiguration> p_160177_) {
        RandomSource random = p_160177_.random();
        BlockPos blockpos = p_160177_.origin();
        WorldGenLevel worldgenlevel = p_160177_.level();
        ODOreConfiguration oreconfiguration = (ODOreConfiguration) p_160177_.config();
        float f = random.nextFloat() * (float)Math.PI;
        float f1 = (float)oreconfiguration.size / 8.0F;
        int i = Mth.ceil(((float)oreconfiguration.size / 16.0F * 2.0F + 1.0F) / 2.0F);
        double d0 = (double)blockpos.getX() + Math.sin(f) * (double)f1;
        double d1 = (double)blockpos.getX() - Math.sin(f) * (double)f1;
        double d2 = (double)blockpos.getZ() + Math.cos(f) * (double)f1;
        double d3 = (double)blockpos.getZ() - Math.cos(f) * (double)f1;
        int j = 2;
        double d4 = blockpos.getY() + random.nextInt(3) - 2;
        double d5 = blockpos.getY() + random.nextInt(3) - 2;
        int k = blockpos.getX() - Mth.ceil(f1) - i;
        int l = blockpos.getY() - 2 - i;
        int i1 = blockpos.getZ() - Mth.ceil(f1) - i;
        int j1 = 2 * (Mth.ceil(f1) + i);
        int k1 = 2 * (2 + i);

        for(int l1 = k; l1 <= k + j1; ++l1) {
            for(int i2 = i1; i2 <= i1 + j1; ++i2) {
                if (l <= worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, l1, i2)) {
                    return this.doPlace(worldgenlevel, random, oreconfiguration, d0, d1, d2, d3, d4, d5, k, l, i1, j1, k1);
                }
            }
        }

        return false;
    }

    protected boolean doPlace(WorldGenLevel p_66533_, RandomSource p_66534_, ODOreConfiguration p_66535_, double p_66536_, double p_66537_, double p_66538_, double p_66539_, double p_66540_, double p_66541_, int p_66542_, int p_66543_, int p_66544_, int p_66545_, int p_66546_) {
        return super.doPlace(p_66533_, p_66534_, new OreConfiguration(p_66535_.targetStates, p_66535_.size, p_66535_.discardChanceOnAirExposure),p_66536_, p_66537_, p_66538_, p_66539_, p_66540_, p_66541_, p_66542_, p_66543_, p_66544_, p_66545_, p_66546_);
    }
}
