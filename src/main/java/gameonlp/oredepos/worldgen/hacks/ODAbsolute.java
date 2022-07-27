package gameonlp.oredepos.worldgen.hacks;

import com.mojang.serialization.Codec;
import gameonlp.oredepos.config.OreConfig;
import gameonlp.oredepos.config.OreDeposConfig;
import gameonlp.oredepos.util.Configurable;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;

public class ODAbsolute implements VerticalAnchor, Configurable {
    public static final Codec<ODAbsolute> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("absolute").xmap(ODAbsolute::new, abs -> abs.y).codec();
    private int y;
    private final OreConfig config;
    private final Direction direction;

    public enum Direction{
        UP,
        DOWN
    }

    public ODAbsolute(int y){
        this.config = null;
        this.direction = null;
        this.y = y;
    }

    public ODAbsolute(OreConfig config, Direction direction){
        this.config = config;
        this.direction = direction;
        this.y = switch (direction){
            case UP -> config.maxHeight.get();
            case DOWN -> config.minHeight.get();
        };
        OreDeposConfig.register(this);
    }

    public int resolveY(WorldGenerationContext p_158949_) {
        return this.y;
    }

    public String toString() {
        return this.y + " absolute";
    }

    @Override
    public void done() {
        this.y = switch (direction){
            case UP -> config.maxHeight.get();
            case DOWN -> config.minHeight.get();
        };
    }
}
