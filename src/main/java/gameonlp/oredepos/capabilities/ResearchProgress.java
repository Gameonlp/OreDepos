package gameonlp.oredepos.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class ResearchProgress implements INBTSerializable<CompoundTag> {
    public ResearchProgress merge(ResearchProgress other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
