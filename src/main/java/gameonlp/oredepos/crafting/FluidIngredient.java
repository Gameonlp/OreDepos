package gameonlp.oredepos.crafting;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public class FluidIngredient implements Predicate<FluidStack> {
    private final ResourceLocation fluidTag;
    private final int amount;
    //Users may want to only do something with nbt data, we should support that
    private final CompoundNBT nbt;

    public FluidIngredient(ResourceLocation fluidTag, int amount, CompoundNBT nbt) {
        this.fluidTag = fluidTag;
        this.amount = amount;
        this.nbt = nbt;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        if (fluidStack == null){
            return false;
        }
        if (!FluidTags.getAllTags().getTag(fluidTag).contains(fluidStack.getFluid())){
            return false;
        }
        if (nbt != null && (!fluidStack.hasTag() || !fluidStack.getTag().equals(nbt))) {
            return false;
        }
        return fluidStack.getAmount() >= this.amount;
    }

    public void toNetwork(PacketBuffer buffer) {
        buffer.writeResourceLocation(fluidTag);
        buffer.writeInt(amount);
        buffer.writeNbt(nbt);
    }

    public static FluidIngredient fromNetwork(PacketBuffer buffer) {
        return new FluidIngredient(buffer.readResourceLocation(), buffer.readInt(), buffer.readNbt());
    }

    public ResourceLocation getFluidTag() {
        return fluidTag;
    }

    public CompoundNBT getNbt() {
        return nbt;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        FluidTags.getAllTags().getTag(fluidTag).getValues().forEach(System.out::println);
        return "FluidIngredient{" +
                "fluidTag=" + fluidTag +
                ", amount=" + amount +
                ", nbt=" + nbt +
                '}';
    }
}
