package gameonlp.oredepos.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public enum Working implements StringRepresentable {
    ACTIVE,
    INACTIVE;

    @Override
    public String getSerializedName() {
        return switch (this) {
            case ACTIVE -> "active";
            case INACTIVE -> "inactive";
        };
    }

    public static final EnumProperty<Working> WORKING = EnumProperty.create("working", gameonlp.oredepos.blocks.Working.class);
}
