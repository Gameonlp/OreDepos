package gameonlp.oredepos.compat;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.blocks.oredeposit.OreDepositTile;
import mcjty.theoneprobe.api.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.tags.ITag;

import javax.annotation.Nonnull;
import java.util.function.Function;

// Reference for how to implement compat in this way is from FactoriOres-Reforked
public class TOPCompat implements Function<ITheOneProbe, Void> {


    @Override
    public Void apply(@Nonnull ITheOneProbe iTheOneProbe) {
        iTheOneProbe.registerProvider(new OreDepositInfoProvider());
        return null;
    }

    public static class OreDepositInfoProvider implements IProbeInfoProvider {

        @Override
        public ResourceLocation getID() {
            return new ResourceLocation(OreDepos.MODID, "topcompat");
        }

        @Override
        public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
            BlockEntity tileEntity = world.getBlockEntity(data.getPos());
            if (tileEntity instanceof OreDepositTile tileEntityOre) {
                long amount = tileEntityOre.getAmount();

                addProbeInfoOre(probeInfo, amount, tileEntityOre.getMaxAmount());

                if (!tileEntityOre.fluidNeeded().isEmpty()) {
                    ITag<Fluid> fluid = tileEntityOre.fluidNeeded();
                    probeInfo.text(Component.translatable("tooltip." + OreDepos.MODID + ".requires_fluid")
                            .append(" ")
                            .append(new FluidStack(fluid.stream().toList().get(0), 100).getDisplayName())
                            .withStyle(ChatFormatting.GREEN));
                }
            }
        }

        private void addProbeInfoOre(IProbeInfo probeInfo, long amount, long amountMax) {
            if (amount <= 0) return;
            final IProgressStyle progressStyle = probeInfo.defaultProgressStyle()
                    .width(100)
                    .height(12)
                    .showText(true)
                    .filledColor(0xffdba560)
                    .alternateFilledColor(0xff8c633b);
            probeInfo.progress(amount, amountMax, progressStyle);
        }
    }
}