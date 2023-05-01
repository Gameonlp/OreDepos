package gameonlp.oredepos.worldgen.hacks;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.config.OreConfig;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.util.Configurable;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import org.slf4j.Logger;

public class ODTrapezoidHeight extends HeightProvider implements Configurable {
   public static final Codec<ODTrapezoidHeight> CODEC = RecordCodecBuilder.create((p_162005_) -> {
      return p_162005_.group(Codec.INT.optionalFieldOf("minInclusive", Integer.valueOf(0)).forGetter((p_162014_) -> {
         return p_162014_.minInclusive;
      }), Codec.INT.optionalFieldOf("maxInclusive", Integer.valueOf(0)).forGetter((p_162014_) -> {
         return p_162014_.maxInclusive;
      }), Codec.INT.optionalFieldOf("plateau", Integer.valueOf(0)).forGetter((p_162014_) -> {
         return p_162014_.plateau;
      })).apply(p_162005_, ODTrapezoidHeight::new);
   });
   private static final Logger LOGGER = LogUtils.getLogger();
   private int minInclusive;
   private int maxInclusive;
   private final int plateau;
   private OreConfig config;

   private ODTrapezoidHeight(int minInclusive, int maxInclusive, int plateau) {
      this.minInclusive = minInclusive;
      this.maxInclusive = maxInclusive;
      this.plateau = plateau;
   }

   public ODTrapezoidHeight(OreConfig config) {
      this.config = config;
      this.minInclusive = 0;
      this.maxInclusive = 0;
      this.plateau = 0;
      OreDeposConfig.register(this);
   }

   public int sample(RandomSource p_226305_, WorldGenerationContext p_226306_) {
      int i = this.minInclusive;
      int j = this.maxInclusive;
      if (i > j) {
         LOGGER.warn("Empty height range: {}", (Object)this);
         return i;
      } else {
         int k = j - i;
         if (this.plateau >= k) {
            return Mth.randomBetweenInclusive(p_226305_, i, j);
         } else {
            int l = (k - this.plateau) / 2;
            int i1 = k - l;
            return i + Mth.randomBetweenInclusive(p_226305_, 0, i1) + Mth.randomBetweenInclusive(p_226305_, 0, l);
         }
      }
   }

   public HeightProviderType<?> getType() {
      return RegistryManager.TRAPEZOID.get();
   }

   public String toString() {
      return this.plateau == 0 ? "triangle (" + this.minInclusive + "-" + this.maxInclusive + ")" : "trapezoid(" + this.plateau + ") in [" + this.minInclusive + "-" + this.maxInclusive + "]";
   }

   @Override
   public void done() {
      minInclusive = config.minHeight.get();
      maxInclusive = config.maxHeight.get();
   }
}