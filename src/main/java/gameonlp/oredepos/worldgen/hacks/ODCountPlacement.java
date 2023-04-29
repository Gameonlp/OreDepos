package gameonlp.oredepos.worldgen.hacks;

import com.mojang.serialization.Codec;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.config.OreConfig;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.util.Configurable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;

import java.util.Random;

public class ODCountPlacement extends RepeatingPlacement implements Configurable { // TODO might be in need of correcting
    public static final Codec<ODCountPlacement> CODEC =
            FloatProvider.codec(0, 256).fieldOf("count").xmap(ODCountPlacement::new, (p_191633_) -> p_191633_.count).codec();
    private final OreConfig config;
    private FloatProvider count;

    public ODCountPlacement(FloatProvider count){
        this.config = null;
        this.count = count;
    }
    public ODCountPlacement(OreConfig config){
        this.config = config;
        this.count = ConstantFloat.of(config.count.get().floatValue());
        OreDeposConfig.register(this);
    }

    @Override
    protected int count(Random p_191913_, BlockPos p_191914_) {
        float value = this.count.sample(p_191913_);
        if (value < 1.0){
            return value > p_191913_.nextFloat() ? 1 : 0;
        }
        return Math.round(value);
    }

    @Override
    public PlacementModifierType<?> type() {
        return RegistryManager.COUNT.get();
    }

    @Override
    public void done() {
        this.count = ConstantFloat.of(config.count.get().floatValue());
    }

    public static class ODCountPlacementType implements PlacementModifierType<ODCountPlacement> {
        @Override
        public Codec<ODCountPlacement> codec() {
            return CODEC;
        }
    }
}
