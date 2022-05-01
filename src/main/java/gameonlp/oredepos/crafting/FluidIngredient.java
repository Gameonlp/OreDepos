package gameonlp.oredepos.crafting;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public class FluidIngredient implements Predicate<FluidStack> {
    private final ITag<Fluid> fluidTag;
    private final int amount;
    //Users may want to only do something with nbt data, we should support that
    private final CompoundNBT nbt;

    public FluidIngredient(ITag<Fluid> fluidTag, int amount, CompoundNBT nbt) {
        this.fluidTag = fluidTag;
        this.amount = amount;
        this.nbt = nbt;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        if (fluidStack == null){
            return false;
        }
        if (!fluidTag.contains(fluidStack.getFluid())){
            return false;
        }
        if (nbt != null && !fluidStack.hasTag() || !fluidStack.getTag().equals(nbt)) {
            return false;
        }
        return fluidStack.getAmount() < this.amount;
    }

    public void toNetwork(PacketBuffer buffer) {
        buffer.writeResourceLocation(FluidTags.getAllTags().getIdOrThrow(fluidTag));
        buffer.writeInt(amount);
        buffer.writeNbt(nbt);
    }

    public static FluidIngredient fromNetwork(PacketBuffer buffer) {
        return new FluidIngredient(FluidTags.getAllTags().getTag(buffer.readResourceLocation()), buffer.readInt(), buffer.readNbt());
    }

    public int getAmount() {
        return amount;
    }
}
