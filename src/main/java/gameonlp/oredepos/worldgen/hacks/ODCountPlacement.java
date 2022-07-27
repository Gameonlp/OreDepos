package gameonlp.oredepos.worldgen.hacks;

import com.mojang.serialization.Codec;
import gameonlp.oredepos.RegistryManager;
import gameonlp.oredepos.config.OreConfig;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.crafting.ChemicalPlantRecipe;
import gameonlp.oredepos.util.Configurable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;

import java.util.Random;

public class ODCountPlacement extends RepeatingPlacement implements Configurable { // TODO might be in need of correcting
    public static final Codec<ODCountPlacement> CODEC =
            IntProvider.codec(0, 256).fieldOf("count").xmap(ODCountPlacement::new, (p_191633_) -> {
        return p_191633_.count;
    }).codec();
    private final OreConfig config;
    private IntProvider count;

    public ODCountPlacement(IntProvider count){
        this.config = null;
        this.count = count;
    }
    public ODCountPlacement(OreConfig config){
        this.config = config;
        this.count = ConstantInt.of(config.count.get());
        OreDeposConfig.register(this);
    }

    @Override
    protected int count(Random p_191913_, BlockPos p_191914_) {
        return this.count.sample(p_191913_);
    }

    @Override
    public PlacementModifierType<?> type() {
        return RegistryManager.COUNT.get();
    }

    @Override
    public void done() {
        this.count = ConstantInt.of(config.count.get());
    }

    public static class ODCountPlacementType implements PlacementModifierType<ODCountPlacement> {
        @Override
        public Codec<ODCountPlacement> codec() {
            return CODEC;
        }
    }
}
