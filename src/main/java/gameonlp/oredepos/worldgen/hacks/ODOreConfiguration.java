package gameonlp.oredepos.worldgen.hacks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gameonlp.oredepos.config.OreConfig;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.util.Configurable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;

public class ODOreConfiguration extends OreConfiguration implements Configurable {
    public static final Codec<ODOreConfiguration> CODEC = RecordCodecBuilder.create((p_67849_) -> {
        return p_67849_.group(Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter((p_161027_) -> {
            return p_161027_.targetStates;
        }), Codec.intRange(0, 64).fieldOf("size").forGetter((p_161025_) -> {
            return p_161025_.size;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter((p_161020_) -> {
            return p_161020_.discardChanceOnAirExposure;
        })).apply(p_67849_, ODOreConfiguration::new);
    });
    public int size;
    private OreConfig config;
    private boolean replacing;

    public ODOreConfiguration(List<TargetBlockState> targetBlockStates,  int size, float discard) {
        super(targetBlockStates, size, discard);
    }

    public ODOreConfiguration(List<TargetBlockState> p_161013_, int p_161014_, OreConfig config) {
        this(p_161013_, p_161014_, config, false);
    }

    public ODOreConfiguration(List<TargetBlockState> p_161013_, int p_161014_, OreConfig config, boolean replacing) {
        super(p_161013_, p_161014_);
        this.size = p_161014_;
        this.config = config;
        this.replacing = replacing;
        OreDeposConfig.register(this);
    }

    @Override
    public void done() {
        if (config.enabled.get() && (!replacing || config.replace.get())) {
            this.size = config.veinSize.get();
        } else {
            this.size = 0;
        }
    }
}
