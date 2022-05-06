package gameonlp.oredepos.compat;

import gameonlp.oredepos.OreDepos;
import gameonlp.oredepos.blocks.oredeposit.OreDepositTile;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

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
        public String getID() {
            return OreDepos.MODID;
        }

        @Override
        public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
            TileEntity tileEntity = world.getBlockEntity(data.getPos());
            if (tileEntity instanceof OreDepositTile) {
                OreDepositTile tileEntityOre = (OreDepositTile) tileEntity;
                int amount = tileEntityOre.getAmount();

                addProbeInfoOre(probeInfo, amount, tileEntityOre.getMaxAmount());

                if (tileEntityOre.fluidNeeded() != null) {
                    Fluid fluid = tileEntityOre.fluidNeeded();
                    probeInfo.text(new TranslationTextComponent("tooltip." + OreDepos.MODID + ".requires_fluid")
                            .append(" ")
                            .append(fluid.getAttributes().getDisplayName(new FluidStack(fluid, 100)))
                            .withStyle(TextFormatting.GREEN));
                }
            }
        }

        private void addProbeInfoOre(IProbeInfo probeInfo, int amount, int amountMax) {
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