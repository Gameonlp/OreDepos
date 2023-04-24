package gameonlp.oredepos.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResearchProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResearchProvider INSTANCE = new ResearchProvider();
    private LazyOptional<ResearchCapability> research = LazyOptional.of(ResearchCapabilityImpl::new);

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        research.resolve().ifPresent(r -> tag.put("research", r.serializeNBT()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        research = LazyOptional.of(() -> {
            ResearchCapabilityImpl researchCapability = new ResearchCapabilityImpl();
            researchCapability.deserializeNBT(nbt);
            return researchCapability;
        });
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CapabilityManager.get(new CapabilityToken<ResearchCapability>() {}).orEmpty(cap, research);
    }
}
