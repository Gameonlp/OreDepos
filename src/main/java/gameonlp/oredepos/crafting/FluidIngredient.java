package gameonlp.oredepos.crafting;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.rmi.registry.Registry;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FluidIngredient implements Predicate<FluidStack> {
    private final @NotNull TagKey<Fluid> fluidTag;
    public static final FluidIngredient EMPTY = new FluidIngredient(FluidTags.create(Fluids.EMPTY.getRegistryName()), 0,null);
    private final int amount;
    //Users may want to only do something with nbt data, we should support that
    private final CompoundTag nbt;

    public FluidIngredient(@NotNull TagKey<Fluid> fluidTag, int amount, CompoundTag nbt) {
        this.fluidTag = fluidTag;
        this.amount = amount;
        this.nbt = nbt;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        if (fluidStack == null){
            return false;
        }
        if (!ForgeRegistries.FLUIDS.tags().getTag(fluidTag).contains(fluidStack.getFluid())){
            return false;
        }
        if (nbt != null && (!fluidStack.hasTag() || !fluidStack.getTag().equals(nbt))) {
            return false;
        }
        return fluidStack.getAmount() >= this.amount;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(fluidTag.location());
        buffer.writeInt(amount);
        buffer.writeNbt(nbt);
    }

    public static FluidIngredient fromNetwork(FriendlyByteBuf buffer) {
        return new FluidIngredient(ForgeRegistries.FLUIDS.tags().createTagKey(buffer.readResourceLocation()), buffer.readInt(), buffer.readNbt());
    }

    public @NotNull TagKey<Fluid> getFluidTag() {
        return fluidTag;
    }

    public CompoundTag getNbt() {
        return nbt;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "FluidIngredient{" +
                "fluidTag=" + fluidTag +
                ", amount=" + amount +
                ", nbt=" + nbt +
                '}';
    }
}
