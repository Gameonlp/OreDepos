package gameonlp.oredepos.worldgen.hacks;

import gameonlp.oredepos.config.OreConfig;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.util.Configurable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public class ODOreConfiguration extends OreConfiguration implements Configurable {
    public int size;
    private final OreConfig config;
    private final boolean replacing;

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
